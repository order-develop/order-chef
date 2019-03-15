package com.example.chef.dao.mapper;

import com.example.chef.model.OrderDetail;

import java.util.List;

public interface OrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetail record);

    int updateByPrimaryKey(OrderDetail record);
//    @Select("SELECT id,user_id,food_package_id,status,used_flag,c_time,m_time,package_Price FROM order_detail  GROUP BY user_id ORDER BY c_time DESC")
//    @ResultMap("BaseResultMap")
    //查看历史订单(带筛选条件)
    List<OrderDetail> listHistoryOrder(Integer status, String startTime, String endTime);

    List<OrderDetail> listNowOrder();

    //通过套餐id查询 订餐人数
    List<OrderDetail> listOrderingCountAndUserNames(Integer foodPackageId);
    //查看我的历史订单(已支付/未支付/过期未支付)
    List<OrderDetail> listHistoryOrderByUserId(Integer userId,Integer status, String startTime, String endTime);
    //查看我的历史订单(已支付)
    List<OrderDetail> listPaidHistoryOrderByUserId(Integer userId);
    //根据订单id修改
    int updateOrderByOrderId(OrderDetail record);

    //查看当前用户的今日订单所有状态
    List<OrderDetail> listOrderTakeMealsStatus(Integer userId);
    //根据orderId获取订单
    OrderDetail getOrderByorderId(String orderId);


    //查看历史订单(带筛选条件 ,导出excel 详细信息)
    List<OrderDetail> listHistoryOrderInfo(Integer status, String startTime, String endTime);

}