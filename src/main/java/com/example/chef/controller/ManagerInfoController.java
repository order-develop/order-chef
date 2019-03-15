package com.example.chef.controller;

import com.example.chef.model.GroupInfo;
import com.example.chef.model.ManagerInfo;
import com.example.chef.service.iface.GroupInfoService;
import com.example.chef.service.iface.ManagerInfoService;
import com.example.chef.service.iface.OrderManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/15 10:21
 *
 * @author NieMingXin
 */
@RestController
@Api(value = "后台管理接口")
@RequestMapping("manager")
public class ManagerInfoController {
    @Autowired
    ManagerInfoService managerInfoService;
    @Autowired
    OrderManagementService orderManagementService;
    @Autowired
    GroupInfoService groupInfoService;

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:21
     * description: 登录后台管理
     *
     * @param userName userName
     * @param passWord passWord
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "登录后台", notes = "登录后台", httpMethod = "POST")
    @PostMapping(value = "/login")
    public Map<String, Object> selectByUserNameAndPassWord(@RequestParam(value = "userName") String userName, @RequestParam(value = "passWord") String passWord) {
        return managerInfoService.selectByUserNameAndPassWord(userName, passWord);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:21
     * description: 根据id删除管理员
     *
     * @param id id
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "根据id删除管理员", notes = "根据id删除管理员", httpMethod = "DELETE")
    @DeleteMapping(value = "/{id}")
    public Map<String, Object> removeManagerById(@PathVariable(value = "id") Integer id) throws Exception {
        return managerInfoService.deleteByPrimaryKey(id);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:21
     * description: 添加管理员
     *
     * @param managerInfo managerInfo
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "添加管理员", notes = "添加管理员", httpMethod = "POST")
    @PostMapping(value = "/register/manager")
    public Map<String, Object> insertManager(@RequestBody ManagerInfo managerInfo) throws Exception {
        return managerInfoService.insertSelective(managerInfo);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:21
     * description: managerInfo
     *
     * @param managerInfo managerInfo
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "修改管理员", notes = "修改管理员", httpMethod = "PUT")
    @PutMapping(value = "/update/manager")
    public Map<String, Object> updateManagerById(@RequestBody ManagerInfo managerInfo) throws Exception {
        return managerInfoService.updateByPrimaryKeySelective(managerInfo);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:21
     * description: 根据组id查询GroupInfo
     *
     * @param id id
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "根据组id查询GroupInfo", notes = "根据组id查询GroupInfo", httpMethod = "GET")
    @GetMapping(value = "/group/id")
    public Map<String, Object> selectGroupById(@RequestParam(value = "id") Integer id) {
        return groupInfoService.selectGroupById(id);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:22
     * description: 根据id删除组
     *
     * @param id id
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "根据id删除组", notes = "根据id删除组", httpMethod = "DELETE")
    @DeleteMapping(value = "/group/{id}")
    public Map<String, Object> removeGroupById(@PathVariable(value = "id") Integer id) throws Exception {
        return groupInfoService.deleteByPrimaryKey(id);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:22
     * description: 添加组
     *
     * @param groupInfo groupInfo
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "添加组", notes = "添加组", httpMethod = "POST")
    @PostMapping(value = "/register/manager/group")
    public Map<String, Object> insertGroup(@RequestBody GroupInfo groupInfo) throws Exception {
        return groupInfoService.insertSelective(groupInfo);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/18 13:22
     * description: 修改组昵称
     *
     * @param groupInfo groupInfo
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "修改组昵称", notes = "修改组昵称", httpMethod = "PUT")
    @PutMapping(value = "/update/manager/group")
    public Map<String, Object> updateGroupById(@RequestBody GroupInfo groupInfo) throws Exception {
        return groupInfoService.updateByPrimaryKeySelective(groupInfo);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/14 9:13
     * description: 分页查看历史订单
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return com.github.pagehelper.PageInfo<com.example.chef.model.OrderDetail>
     */
    @ApiOperation(value = "分页查看历史订单", notes = "分页查看历史订单,分页信息不传，则为第一页，一页15条", httpMethod = "GET")
    @GetMapping(value = "/order/history")
    public Map<String, Object> listHistoryOrder(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @RequestParam(value = "status", required = false) Integer status,
                                                @RequestParam(value = "startTime", required = false) String startTime,
                                                @RequestParam(value = "endTime", required = false) String endTime) {
        return orderManagementService.listHistoryOrder(pageNum == null ? 1 : pageNum, pageSize == null ? 15 : pageSize, status, startTime, endTime);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/14 9:13
     * description: 分页查看今日订单
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return com.github.pagehelper.PageInfo<com.example.chef.model.OrderDetail>
     */
    @ApiOperation(value = "分页查看今日订单", notes = "分页查看今日订单,分页信息不传，则为第一页，一页15条", httpMethod = "GET")
    @GetMapping(value = "/order/now")
    public Map<String, Object> listNowOrder(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return orderManagementService.listNowOrder(pageNum == null ? 1 : pageNum, pageSize == null ? 15 : pageSize);
    }

    @ApiOperation(value = "历史订单导出Excel", notes = "历史订单导出Excel", httpMethod = "GET")
    @GetMapping("/export/history")
    public void exportHistory(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "startTime", required = false) String startTime,
                              @RequestParam(value = "endTime", required = false) String endTime) {
        Map<String, Object> map = new HashMap<>();
        response.setContentType("octets/stream");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String date = df.format(new Date());
        String excelName = "历史订单 " + date;
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        if (agent.contains("firefox")) {
            try {
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes(), "ISO8859-1"));
                OutputStream out = response.getOutputStream();
                managerInfoService.export(excelName, out, status, startTime, endTime);
            } catch (Exception e) {
            }
        } else {
            try {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1") + ".xls");
                OutputStream out = response.getOutputStream();
                managerInfoService.export(excelName, out, status, startTime, endTime);
            } catch (Exception e) {
            }
        }
    }

    @ApiOperation(value = "今日订单导出Excel", notes = "今日订单导出Excel", httpMethod = "GET")
    @GetMapping("/export/now")
    public void exportNow(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("octets/stream");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String date = df.format(new Date());
        String excelName = "今日订单 " + date;
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        if (agent.contains("firefox")) {
            try {
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes(), "ISO8859-1"));
                OutputStream out = response.getOutputStream();
                managerInfoService.exportNow(excelName, out);
            } catch (Exception e) {
            }
        } else {
            try {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1") + ".xls");
                OutputStream out = response.getOutputStream();
                managerInfoService.exportNow(excelName, out);
            } catch (Exception e) {
            }
        }
    }
}
