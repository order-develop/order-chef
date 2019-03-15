package com.example.chef.service.iface;

import com.example.chef.model.FoodInfo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * description: 厨师端相关操作service
 * create: 2019/2/13 16:44
 *
 * @author NieMingXin
 */
public interface ChefInfoService {
   //修改菜品名称
   Map<String, Object> updateFood(Integer foodId, String foodName, BigDecimal foodPrice)throws Exception;

   //获取套餐订单数和下单用户名
   Map<String, Object>  listOrderingCountAndUserNames(Integer foodPackageId);

   Map<String, Object> addFood( FoodInfo foodInfo)throws Exception;
   //通过套餐id获取菜品名称
   Map<String, Object> listFoodsFood();

   //通过套餐id获取菜品名称
   Map<String, Object> removeFoodByFoodId(Integer foodId) throws Exception;
}
