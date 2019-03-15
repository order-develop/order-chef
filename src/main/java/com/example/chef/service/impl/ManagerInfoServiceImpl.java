package com.example.chef.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.chef.controller.WXController;
import com.example.chef.dao.mapper.ManagerInfoMapper;
import com.example.chef.dao.mapper.OrderDetailMapper;
import com.example.chef.dao.mapper.UserInfoMapper;
import com.example.chef.model.ManagerInfo;
import com.example.chef.model.OrderDetail;
import com.example.chef.model.UserInfo;
import com.example.chef.service.iface.ManagerInfoService;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description: TODO
 * create: 2019/2/15 10:24
 *
 * @author NieMingXin
 */
@Service
public class ManagerInfoServiceImpl implements ManagerInfoService {
    private final Logger logger = LoggerFactory.getLogger(ManagerInfoServiceImpl.class);

    @Autowired
    ManagerInfoMapper managerInfoMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    UserInfoMapper userInfoMapper;


    @Override
    public Map<String, Object> insertSelective(ManagerInfo record) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(record.getUserName()) && StringUtils.isEmpty(record.getPassword()) && record.getGroupId() != null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        } else {
            ManagerInfo managerInfo = managerInfoMapper.selectByUserName(record.getUserName());
            if (managerInfo != null && !StringUtils.isEmpty(managerInfo.getUserName()) && managerInfo.getUserName().equals(record.getUserName())) {
                map.put("code", -99);
                map.put("message", "用户名已存在");
                return map;
            }
            if (record.getPassword().length() < 6 || record.getPassword().length() > 12) {
                map.put("code", -88);
                map.put("message", "密码位数不符合规则");
                return map;
            }
            record.setcTime(new Date());
            int result = managerInfoMapper.insertSelective(record);
            if (result > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                map.put("data", record);
                return map;
            } else {
                map.put("code", -1);
                map.put("message", "系统开小差啦~ 请重试");
                throw new Exception();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateByPrimaryKeySelective(ManagerInfo record) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (record.getId() == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        record.setmTime(new Date());
        if (!StringUtils.isEmpty(record.getPassword())) {
            ManagerInfo managerInfo = managerInfoMapper.selectByPrimaryKey(record.getId());
            if (record.getPassword().length() < 6 || record.getPassword().length() > 12) {
                map.put("code", -88);
                map.put("message", "密码位数不符合规则");
                return map;
            }
            if (record.getPassword().equals(managerInfo.getPassword())) {
                map.put("code", -99);
                map.put("message", "新密码与旧密码相同");
                return map;
            } else {
                int result = managerInfoMapper.updateByPrimaryKeySelective(record);
                if (result > 0) {
                    map.put("code", 1);
                    map.put("message", "操作成功");
                    return map;
                } else {
                    throw new Exception();
                }
            }
        } else {
            int result = managerInfoMapper.updateByPrimaryKeySelective(record);
            if (result > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                return map;
            } else {
                throw new Exception();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteByPrimaryKey(Integer id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (id != null) {
            int result = managerInfoMapper.deleteByPrimaryKey(id);
            if (result == 1) {
                map.put("code", 1);
                map.put("message", "操作成功");
                return map;
            } else {
                map.put("code", -1);
                map.put("message", "系统开小差啦~ 请重试");
                throw new Exception();
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
    }

    @Override
    public Map<String, Object> selectByUserNameAndPassWord(String userName, String passWord) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(userName) && StringUtils.isEmpty(passWord)) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        ManagerInfo managerInfo = managerInfoMapper.selectByUserNameAndPassWord(userName, passWord);
        if (managerInfo != null && managerInfo.getId() != null) {
            map.put("code", 1);
            map.put("message", "操作成功");
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }

    @Override
    public void export(String excelName, OutputStream out,Integer status,String startTime,String endTime) {
        List<OrderDetail> historyOrder = orderDetailMapper.listHistoryOrderInfo(status,startTime,endTime);
        if (!CollectionUtils.isEmpty(historyOrder)) {
            //历史订单的userid不重复
            Set<Integer> userIds = historyOrder.stream().map(OrderDetail::getUserId).collect(Collectors.toSet());
            //根据订单表id查询user表名字
            List<UserInfo> userInfos = userInfoMapper.listUserInfos(Joiner.on(",").join(userIds));
            if (userInfos != null) {
                //拼装返回数据
                Map<Long, UserInfo> userNames = new HashMap<>();
                for (UserInfo userInfo : userInfos) {
                    userNames.put(userInfo.getId(), userInfo);
                }
                for (OrderDetail history : historyOrder) {
                    switch (history.getStatus()){
                        case 0:
                            history.setStatusInfo("未支付");
                            break;
                        case 1:
                            history.setStatusInfo("已支付");
                            break;
                        case 2:
                            history.setStatusInfo("过期未支付");
                            break;
                        default:history.setStatusInfo("");
                    }
                    //如果真实姓名为空，则取微信昵称
                    history.setUserName(userNames.get(history.getUserId().longValue()).getRealName() == null || ("").equals(userNames.get(history.getUserId().longValue()).getRealName()) ? userNames.get(history.getUserId().longValue()).getNickName() : userNames.get(history.getUserId().longValue()).getRealName());
                    //获取手机号
                    history.setTel(userNames.get(history.getUserId().longValue()).getTel());
                }
            }
        }
        try {
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //生成一个表格
            HSSFSheet sheet = wb.createSheet(excelName);
            // 第三步，在sheet中添加表头第0行
            HSSFRow row = sheet.createRow(0);

            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style1 = wb.createCellStyle();
            // 设置字体
            HSSFFont font = wb.createFont();
            //设置字体大小
            font.setFontHeightInPoints((short) 14);
            //字体加粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            //设置字体名字
            font.setFontName("宋体");
            //设置样式;
            HSSFCellStyle style = wb.createCellStyle();
            style.setTopBorderColor(HSSFColor.BLACK.index);
            //在样式用应用设置的字体;
            style.setFont(font);
            //设置自动换行;
            style.setWrapText(false);
            //设置水平对齐的样式为居中对齐;
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //设置垂直对齐的样式为居中对齐;
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            HSSFCell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("历史订单记录表");
            row.setHeight((short) (20 * 30));
            this.insertData(wb, sheet, row, historyOrder, out);
        } catch (Exception e) {
        }
    }

    @Override
    public void exportNow(String excelName, OutputStream out) {
        //今天的订单
        List<OrderDetail> nowOrder = orderDetailMapper.listNowOrder();
        BigDecimal packagePriceSum = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(nowOrder)) {
            List<Integer> userIds = nowOrder.stream().map(OrderDetail::getUserId).collect(Collectors.toList());
            //根据订单表id查询user表名字
            List<UserInfo> userInfos = userInfoMapper.listUserInfos(Joiner.on(",").join(userIds));
            //拼装返回数据
            Map<Long, UserInfo> userNames = new HashMap<>();
            for (UserInfo userInfo : userInfos) {
                userNames.put(userInfo.getId(), userInfo);
            }
            for (OrderDetail now : nowOrder) {
                switch (now.getStatus()){
                    case 0:
                        now.setStatusInfo("未支付");
                        break;
                    case 1:
                        now.setStatusInfo("已支付");
                        break;
                    case 2:
                        now.setStatusInfo("过期未支付");
                        break;
                        default:now.setStatusInfo("");
                }
                //如果真实姓名为空，则取微信昵称
                now.setUserName(userNames.get(now.getUserId().longValue()).getRealName() == null || ("").equals(userNames.get(now.getUserId().longValue()).getRealName()) ? userNames.get(now.getUserId().longValue()).getNickName() : userNames.get(now.getUserId().longValue()).getRealName());
                //获取手机号
                now.setTel(userNames.get(now.getUserId().longValue()).getTel());
            }
        }
        try {
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //生成一个表格
            HSSFSheet sheet = wb.createSheet(excelName);
            // 第三步，在sheet中添加表头第0行
            HSSFRow row = sheet.createRow(0);

            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style1 = wb.createCellStyle();
            // 设置字体
            HSSFFont font = wb.createFont();
            //设置字体大小
            font.setFontHeightInPoints((short) 14);
            //字体加粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            //设置字体名字
            font.setFontName("宋体");
            //设置样式;
            HSSFCellStyle style = wb.createCellStyle();
            style.setTopBorderColor(HSSFColor.BLACK.index);
            //在样式用应用设置的字体;
            style.setFont(font);
            //设置自动换行;
            style.setWrapText(false);
            //设置水平对齐的样式为居中对齐;
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //设置垂直对齐的样式为居中对齐;
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//                style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//                HSSFFont font =  wb.createFont();
//                font.setFontName("宋体");
//                font.setFontHeightInPoints((short)16);
            HSSFCell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("今日订单记录表");
            row.setHeight((short) (20 * 30));
            this.insertData(wb, sheet, row, nowOrder, out);

        } catch (Exception e) {

        }
    }

    /**
     * 导入数据到表格中
     *
     * @param wb           execl文件
     * @param sheet        表格
     * @param row          表格行
     * @param orderDetails 要导出的数据
     * @param out          输出流
     */
    private void insertData(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row, List<OrderDetail> orderDetails, OutputStream out) {
        try {
            row = sheet.createRow(1);
            String[] title = new String[]{"用户名", "手机号", "下单时间","支付状态", "最后操作时间"};
            for (int i = 0; i < title.length; i++) {
                row.createCell(i).setCellValue(title[i]);
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < orderDetails.size(); i++) {
                row = sheet.createRow(i + 2);
                OrderDetail data = orderDetails.get(i);
                row.createCell(0).setCellValue(data.getUserName());
                row.createCell(1).setCellValue(data.getTel());
                row.createCell(2).setCellValue(format.format(data.getcTime()));
                row.createCell(3).setCellValue(data.getStatusInfo());
                if (data.getmTime() != null) {
                    row.createCell(4).setCellValue(format.format(data.getmTime()));
                }
//                BigDecimal je = data.getJe();
//                if (je != null) {
//                    row.createCell(2).setCellValue(je.doubleValue());
//                }
            }
            sheet.autoSizeColumn((short)0); //调整第一列宽度
            sheet.autoSizeColumn((short)1); //调整第二列宽度
            sheet.autoSizeColumn((short)2); //调整第三列宽度
            sheet.autoSizeColumn((short)3); //调整第四列宽度
            sheet.autoSizeColumn((short)4); //调整第五列宽度
            //合并单元格，前面2位代表开头结尾行，后面2位代表开头结尾列
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, title.length - 1);
            sheet.addMergedRegion(region);
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
