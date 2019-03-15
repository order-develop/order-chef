package com.example.chef.controller;

import com.example.chef.model.OrderDetail;
import com.example.chef.model.UserInfo;
import com.example.chef.service.iface.OrderManagementService;
import com.example.chef.service.iface.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/13 14:09
 *
 * @author NieMingXin
 */
@RestController
@Api(value = "用户信息/订单接口")
@RequestMapping("user")
public class UserSideInfoController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    OrderManagementService orderManagementService;

    /**
     * author: NieMingXin
     * create: 2019/2/14 9:12
     * description: 用户注册
     *
     * @param userInfo userInfo
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "用户注册", notes = "添加用户", httpMethod = "POST")
    @PostMapping(value = "/register")
    public Map<String, Object> saveUserInfo(@RequestBody UserInfo userInfo) throws Exception {
        return userInfoService.insertSelective(userInfo);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/14 9:15
     * description: 查看我的历史订单
     *
     * @param userId userId
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     */
    @ApiOperation(value = "查看我的历史订单(已支付/未支付/过期未支付)", notes = "查看我的历史订单(已支付/未支付/过期未支付)", httpMethod = "GET")
    @GetMapping(value = "/userId")
    public Map<String, Object> listHistoryOrderByUserId(@RequestParam(value = "userId") Integer userId,
                                                        @RequestParam(value = "status", required = false) Integer status,
                                                        @RequestParam(value = "startTime", required = false) String startTime,
                                                        @RequestParam(value = "endTime", required = false) String endTime,
                                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return orderManagementService.listHistoryOrderByUserId(userId, status, startTime, endTime, pageNum == null ? 1 : pageNum, pageSize == null ? 15 : pageSize);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/14 16:20
     * description: 查看我的历史订单(已支付)
     *
     * @param userId   userId
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     */
    @ApiOperation(value = "查看我的历史订单(已支付)", notes = "查看我的历史订单(已支付)", httpMethod = "GET")
    @GetMapping(value = "/userId/paid")
    public Map<String, Object> listPaidHistoryOrderByUserId(@RequestParam(value = "userId") Integer userId,
                                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return orderManagementService.listPaidHistoryOrderByUserId(userId, pageNum == null ? 1 : pageNum, pageSize == null ? 15 : pageSize);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/15 13:53
     * description: 用户下订单
     *
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     */
    @ApiOperation(value = "用户下订单", notes = "用户下订单", httpMethod = "POST")
    @PostMapping(value = "/order/userId/ordering")
    public Map<String, Object> savePlaceAnOrder(@RequestBody OrderDetail orderDetail) throws Exception {
        return orderManagementService.savePlaceAnOrder(orderDetail);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/15 13:53
     * description: 修改支付状态(完成订单)
     *
     * @param orderId orderId
     * @return java.util.Map<java.lang.String , java.lang.Object>
     */
    @ApiOperation(value = "修改支付状态(完成订单)", notes = "修改支付状态(完成订单)", httpMethod = "PUT")
    @PutMapping(value = "/order/orderId")
    public Map<String, Object> updatePayStatus(@RequestParam(value = "orderId") String orderId) throws Exception {
        return orderManagementService.updatePayStatusByOrderId(orderId);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/16 12:14
     * description: 登录/用户是否注册过
     *
     * @param openId openId
     * @return java.util.Map<java.lang.String , java.lang.Object>
     */
    @ApiOperation(value = "登录", notes = "登录/用户是否注册过", httpMethod = "GET")
    @GetMapping(value = "/login/openId")
    public Map<String, Object> login(@RequestParam(value = "openId") String openId) throws Exception {
        return userInfoService.selectUserInfoByOpenId(openId);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/16 13:50
     * description: 修改取餐状态
     *
     * @param orderId orderId
     * @return java.util.Map<java.lang.String , java.lang.Object>
     */
    @ApiOperation(value = "修改订单取餐状态", notes = "修改订单取餐状态", httpMethod = "PUT")
    @PutMapping(value = "/order/orderId/meal")
    public Map<String, Object> updateTakeMealsStatus(@RequestParam(value = "orderId") String orderId) throws Exception {
        return orderManagementService.updateTakeMealsStatus(orderId);
    }

    @ApiOperation(value = "获取用户今日订单所有状态", notes = "获取用户今日订单所有状态", httpMethod = "GET")
    @GetMapping(value = "/order/userId/allStatus")
    public Map<String, Object> listOrderTakeMealsStatus(@RequestParam(value = "userId") Integer userId){
        return orderManagementService.listOrderTakeMealsStatus(userId);
    }
    /**
     * author: NieMingXin
     * create: 2019/2/24 11:07
     * description: 根据orderId获取订单价格和份数
     *
     * @param orderId orderId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @ApiOperation(value = "根据orderId获取订单价格和份数", notes = "根据orderId获取订单价格和份数", httpMethod = "GET")
    @GetMapping(value = "/order/userId/orderId")
    public Map<String, Object> getOrderByorderId(@RequestParam(value = "orderId") String orderId)  {
        return orderManagementService.getOrderByorderId(orderId);
    }

}
