package com.example.chef.service.impl;

import com.example.chef.dao.mapper.OrderDetailMapper;
import com.example.chef.dao.mapper.UserInfoMapper;
import com.example.chef.model.OrderDetail;
import com.example.chef.model.UserInfo;
import com.example.chef.service.iface.OrderManagementService;
import com.example.chef.util.OrderUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description: 订单表相关service
 * create: 2019/1/28 11:45
 *
 * @author NieMingXin
 */
@Service
public class OrderManagementServiceImpl implements OrderManagementService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<String, Object> listHistoryOrder(Integer pageNum, Integer pageSize, Integer status, String startTime, String endTime) {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);

        //查询所有订单
        List<OrderDetail> historyOrder = orderDetailMapper.listHistoryOrder(status, startTime, endTime);
        if (!CollectionUtils.isEmpty(historyOrder)) {
            BigDecimal packagePriceSum = new BigDecimal(0);
            //历史订单的userid不重复
            List<Integer> userIds = historyOrder.stream().map(OrderDetail::getUserId).collect(Collectors.toList());
            //历史订单总金额
            for (OrderDetail sum : historyOrder) {
                if (sum.getPackagePrice() != null) {
                    packagePriceSum = packagePriceSum.add(sum.getPackagePrice());
                }
            }
            //根据订单表id查询user表名字
            List<UserInfo> userInfos = userInfoMapper.listUserInfos(Joiner.on(",").join(userIds));
            if (userInfos != null) {
                //拼装返回数据
                Map<Long, UserInfo> userNames = new HashMap<>();
                for (UserInfo userInfo : userInfos) {
                    userNames.put(userInfo.getId(), userInfo);
                }
                for (OrderDetail history : historyOrder) {
                    //如果真实姓名为空，则取微信昵称
                    history.setUserName(userNames.get(history.getUserId().longValue()).getRealName() == null || ("").equals(userNames.get(history.getUserId().longValue()).getRealName()) ? userNames.get(history.getUserId().longValue()).getNickName() : userNames.get(history.getUserId().longValue()).getRealName());
                    //获取手机号
                    history.setTel(userNames.get(history.getUserId().longValue()).getTel());
                }
            }
            //分页后的数据
            PageInfo<OrderDetail> pageInfo = new PageInfo<>(historyOrder);
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("sum", packagePriceSum);
            map.put("data", pageInfo);
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }

    @Override
    public Map<String, Object> listNowOrder(Integer pageNum, Integer pageSize) {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        //今天的订单
        List<OrderDetail> nowOrder = orderDetailMapper.listNowOrder();
        BigDecimal packagePriceSum = new BigDecimal(0);
        //今天订单总金额
        for (OrderDetail sum : nowOrder) {
            //订单金额不为null，并且是已支付的状态
            if (sum.getPackagePrice() != null && sum.getStatus() == 1) {
                packagePriceSum = packagePriceSum.add(sum.getPackagePrice());
            }
        }
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
                //如果真实姓名为空，则取微信昵称
                now.setUserName(userNames.get(now.getUserId().longValue()).getRealName() == null || ("").equals(userNames.get(now.getUserId().longValue()).getRealName()) ? userNames.get(now.getUserId().longValue()).getNickName() : userNames.get(now.getUserId().longValue()).getRealName());
                //获取手机号
                now.setTel(userNames.get(now.getUserId().longValue()).getTel());
            }
        }
        //分页后的数据
        PageInfo<OrderDetail> pageInfo = new PageInfo<>(nowOrder);
        //如果不是空则success
        if (pageInfo.getList().size() > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("sum", packagePriceSum);
            map.put("data", pageInfo);
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updatePayStatusByOrderId(String orderId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (orderId == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setStatus(1);
        orderDetail.setmTime(new Date());
        int result = orderDetailMapper.updateOrderByOrderId(orderDetail);
        if (result > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            return map;
        } else {
            throw new Exception();
        }
    }

    @Override
    public Map<String, Object> updateTakeMealsStatus(String orderId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (orderId == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setUsedFlag(1);
        orderDetail.setmTime(new Date());
        int result = orderDetailMapper.updateOrderByOrderId(orderDetail);
        if (result > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            return map;
        } else {
            throw new Exception();
        }
    }

    @Override
    public Map<String, Object> listHistoryOrderByUserId(Integer userId, Integer status, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        //我的历史订单
        List<OrderDetail> myOrder = orderDetailMapper.listHistoryOrderByUserId(userId, status, startTime, endTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        myOrder.forEach(my -> {
            if (my.getcTime() != null) {
                my.setShortDate(formatter.format(my.getcTime()));
            }
        });
        //分页后的数据
        PageInfo<OrderDetail> pageInfo = new PageInfo<>(myOrder);
        //如果不是空则success
        if (pageInfo.getList().size() > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("data", pageInfo);
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }

    @Override
    public Map<String, Object> listPaidHistoryOrderByUserId(Integer userId, Integer pageNum, Integer pageSize) {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(pageNum, pageSize);
        //我的历史订单(已支付)
        List<OrderDetail> myOrder = orderDetailMapper.listPaidHistoryOrderByUserId(userId);
        //分页后的数据
        PageInfo<OrderDetail> pageInfo = new PageInfo<>(myOrder);
        //如果不是空则success
        if (pageInfo.getList().size() > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("data", pageInfo);
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> savePlaceAnOrder(OrderDetail orderDetail) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        //验证参数
        if (orderDetail.getUserId() != null && orderDetail.getFoodPackageId() != null && orderDetail.getPackagePrice() != null) {
            orderDetail.setUserId(orderDetail.getUserId());
            orderDetail.setFoodPackageId(orderDetail.getFoodPackageId());
            orderDetail.setcTime(new Date());
            orderDetail.setPackageNumber(orderDetail.getPackageNumber() == null ? 1 : orderDetail.getPackageNumber());
            //订单总价格   单价*份数
            BigDecimal totalPrice = orderDetail.getPackagePrice().multiply(new BigDecimal(orderDetail.getPackageNumber()));
            orderDetail.setPackagePrice(totalPrice);
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(orderDetail.getUserId().longValue());
            //生成订单号
            orderDetail.setOrderId(OrderUtil.getRandomOrderMark(userInfo.getOpenId()));
            int result = orderDetailMapper.insertSelective(orderDetail);
            if (result > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                map.put("data", orderDetail);
                BigDecimal minuteTotalPrice = totalPrice.multiply(new BigDecimal(100));
                map.put("minuteTotalPrice", minuteTotalPrice);
                map.put("openId", userInfo.getOpenId());
                return map;
            } else {
                map.put("code", -1);
                map.put("message", "系统开小差啦~ 请重试");
                throw new Exception();
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            map.put("data", orderDetail);
            return map;
        }
    }

    @Override
    public Map<String, Object> listOrderTakeMealsStatus(Integer userId) {
        //创建返回结果集
        Map<String, Object> map = new HashMap<>();
        if (userId == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        List<OrderDetail> orderDetails = orderDetailMapper.listOrderTakeMealsStatus(userId);
        if (orderDetails != null && orderDetails.size() > 0) {
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getStatus() == 1 && orderDetail.getUsedFlag() == 1) {
                    orderDetail.setIsComplete(3);
                } else if(orderDetail.getStatus() == 0){
                    orderDetail.setIsComplete(1);
                }else if(orderDetail.getStatus() == 1 && orderDetail.getUsedFlag() ==0){
                    orderDetail.setIsComplete(2);
                }
            }
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("data", orderDetails);
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }

    @Override
    public Map<String, Object> getOrderByorderId(String orderId) {
        //创建返回结果集
        Map<String, Object> map = new HashMap<>();
        if (orderId == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        OrderDetail orderDetail = orderDetailMapper.getOrderByorderId(orderId);

        if(orderDetail !=null && orderDetail.getId()!=null &&orderDetail.getUserId() !=null){
            UserInfo userInfo =userInfoMapper.selectByPrimaryKey(orderDetail.getUserId().longValue());
            orderDetail.setTel(userInfo.getTel());
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("data", orderDetail);
            return map;
        }else{
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
    }
}
