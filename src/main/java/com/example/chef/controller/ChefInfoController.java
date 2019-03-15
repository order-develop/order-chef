package com.example.chef.controller;

import com.example.chef.model.FoodInfo;
import com.example.chef.service.iface.ChefInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * description: 厨师端
 * create: 2019/2/13 14:12
 *
 * @author NieMingXin
 */
@RestController
@Api(value = "厨师端")
@RequestMapping("chef")
public class ChefInfoController {
    @Autowired
    ChefInfoService chefInfoService;

    /**
     * author: NieMingXin
     * create: 2019/2/14 9:13
     * description: 修改套餐菜品
     *
     * @param foodId foodId
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     */
    @ApiOperation(value = "修改套餐菜品", notes = "修改套餐菜品", httpMethod = "PUT")
    @PutMapping(value = "/foodPackageId")
    public Map<String, Object> updateFood(@RequestParam(value = "foodId") Integer foodId,
                                          @RequestParam(value = "foodName") String foodName,
                                          @RequestParam(value = "foodPrice",required = false) BigDecimal foodPrice) throws Exception {
        return chefInfoService.updateFood(foodId,foodName,foodPrice);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/14 9:13
     * description: 获取套餐订单数和下单用户名
     *
     * @param foodPackageId foodPackageId
     * @return java.util.Map<java.lang.String , java.lang.Object>
     */
    @ApiOperation(value = "获取套餐订单数和下单用户名", notes = "获取套餐订单数和下单用户名", httpMethod = "GET")
    @GetMapping(value = "/foodPackageId")
    public Map<String, Object> listOrderingCountAndUserNames(@RequestParam(value = "foodPackageId") Integer foodPackageId) {
        return chefInfoService.listOrderingCountAndUserNames(foodPackageId);
    }

    /**
     * author: NieMingXin
     * create: 2019/2/19 10:27
     * description: 添加/发布菜品
     *
     * @param foodInfo foodInfo
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @ApiOperation(value = "添加/发布菜品", notes = "添加/发布菜品", httpMethod = "POST")
    @PostMapping(value = "/foodName")
    public Map<String, Object> addFood(@RequestBody FoodInfo foodInfo) throws Exception {
        return chefInfoService.addFood(foodInfo);
    }

    @ApiOperation(value = "查看菜品", notes = "查看菜品", httpMethod = "GET")
    @GetMapping(value = "/food")
    public Map<String, Object> listFoodsFood() {
        return chefInfoService.listFoodsFood();
    }


    @ApiOperation(value = "删除菜品", notes = "删除菜品", httpMethod = "DELETE")
    @DeleteMapping(value = "/{foodId}")
    public Map<String, Object> removeFoodByFoodId(@PathVariable(value = "foodId") Integer foodId) throws Exception {
        return chefInfoService.removeFoodByFoodId(foodId);
    }

}
