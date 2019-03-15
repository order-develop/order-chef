package com.example.chef.service.iface;

import com.example.chef.model.OrderDetail;

import java.util.Date;
import java.util.Map;

/**
 * description: TODO
 * create: 2019/1/28 11:48
 *
 * @author NieMingXin
 */
public interface OrderManagementService {
    /**
     * author: NieMingXin
     * create: 2019/2/14 9:18
     * description: 查看历史订单
     *
     * @param pageNum  pageNum
     * @param pageSize  pageSize
     * @return com.github.pagehelper.PageInfo<com.example.chef.model.OrderDetail>
     */
    Map<String, Object>  listHistoryOrder(Integer pageNum, Integer pageSize,Integer status,String startTime,String endTime);
    /**
     * author: NieMingXin
     * create: 2019/2/14 9:18
     * description: 查看今日订单
     *
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return com.github.pagehelper.PageInfo<com.example.chef.model.OrderDetail>
     */
    Map<String, Object> listNowOrder(Integer pageNum, Integer pageSize);
    /**
     * author: NieMingXin
     * create: 2019/2/15 13:17
     * description: 修改支付状态
     *
     * @param orderId orderId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object>  updatePayStatusByOrderId(String orderId) throws Exception;
    /**
     * author: NieMingXin
     * create: 2019/2/15 13:17
     * description: 修改取餐状态
     *
     * @param orderId orderId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object>  updateTakeMealsStatus(String orderId) throws Exception;
    /**
     * author: NieMingXin
     * create: 2019/2/14 9:19
     * description: 查看我的历史订单(已支付/未支付/过期未支付)
     *
     * @param userId userId
     * @return com.github.pagehelper.PageInfo<com.example.chef.model.OrderDetail>
     */
    Map<String, Object> listHistoryOrderByUserId(Integer userId,Integer status, String startTime, String endTime,Integer pageNum, Integer pageSize);
    /**
     * author: NieMingXin
     * create: 2019/2/15 13:15
     * description: 查看我的历史订单(已支付)
     *
     * @param userId userId
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> listPaidHistoryOrderByUserId(Integer userId, Integer pageNum, Integer pageSize);


   /**
    * author: NieMingXin
    * create: 2019/2/15 18:22
    * description: 用户下单
    *
    * @param orderDetail orderDetail
    * @return java.util.Map<java.lang.String,java.lang.Object>
    */
    Map<String, Object> savePlaceAnOrder(OrderDetail orderDetail) throws Exception;
    /**
     * author: NieMingXin
     * create: 2019/2/22 13:41
     * description: 获取用户今日订单所有状态
     *
     * @param userId userId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> listOrderTakeMealsStatus(Integer userId);

    /**
     * author: NieMingXin
     * create: 2019/2/24 10:58
     * description: 根据orderId获取订单价格和份数
     *
     * @param orderId orderId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getOrderByorderId(String orderId);
}
