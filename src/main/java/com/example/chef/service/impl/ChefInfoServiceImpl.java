package com.example.chef.service.impl;

import com.example.chef.dao.mapper.*;
import com.example.chef.model.*;
import com.example.chef.service.iface.ChefInfoService;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description: 厨师端相关操作service
 * create: 2019/2/13 16:45
 *
 * @author NieMingXin
 */
@Service
public class ChefInfoServiceImpl implements ChefInfoService {
    @Autowired
    PackageDetailMapper packageDetailMapper;
    @Autowired
    FoodInfoMapper foodInfoMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    FoodPackageMapper foodPackageMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateFood(Integer foodId, String foodName, BigDecimal foodPrice) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        //判断参数
        if (foodId == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        FoodInfo foodInfo = new FoodInfo();
        foodInfo.setId(foodId);
        foodInfo.setFoodName(foodName);
        //如果单价为空 则不修改,有值则修改
        if (foodPrice != null) {
            foodInfo.setFoodPrice(foodPrice);
        }
        //修改菜品名称
        int result = foodInfoMapper.updateByPrimaryKeySelective(foodInfo);
        //判断是否修改成功
        if (result > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("data", foodInfo);
            return map;
        } else {
            map.put("code", -1);
            map.put("message", "系统开小差啦~ 请重试");
            throw new Exception();
        }
    }

    @Override
    public Map<String, Object> listOrderingCountAndUserNames(Integer foodPackageId) {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        //判断参数
        if (foodPackageId == null) {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
        List<OrderDetail> orderDetails = orderDetailMapper.listOrderingCountAndUserNames(foodPackageId);
        if (orderDetails == null || orderDetails.size() <= 0) {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
        List<Integer> userIds = orderDetails.stream().map(OrderDetail::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            map.put("code", 3);
            map.put("message", "没有数据");
            return map;
        }
        List<UserInfo> userInfos = userInfoMapper.listUserInfos(Joiner.on(",").join(userIds));
        if (!CollectionUtils.isEmpty(userInfos)) {
            List<String> userNameList = new ArrayList<>();
            userInfos.forEach(userInfo -> {
                userNameList.add(userInfo.getRealName() == null || "".equals(userInfo.getRealName()) ? userInfo.getNickName() : userInfo.getRealName());
            });
            map.put("today",format.format(date));
            map.put("count", userNameList.size());
            map.put("userNames", userNameList);
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
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addFood(FoodInfo foodInfo) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(foodInfo.getFoodName())) {
            map.put("code", 3);
            map.put("message", "参数错误");
            return map;
        }
        if (foodInfo.getFoodPrice() != null) {
            foodInfo.setFoodPrice(foodInfo.getFoodPrice());
        }
        foodInfo.setcTime(new Date());
        foodInfo.setmTime(new Date());
        int result = foodInfoMapper.insertSelective(foodInfo);
        PackageDetail packageDetail = new PackageDetail();
        packageDetail.setFoodId(foodInfo.getId());
        packageDetail.setcTime(new Date());
        packageDetail.setFoodPackageId(1);
        int packageDetailresult = packageDetailMapper.insertSelective(packageDetail);
        if (result > 0 && packageDetailresult > 0) {
            map.put("code", 1);
            map.put("message", "操作成功");
            map.put("foodPackageId", packageDetail.getFoodPackageId());
            map.put("data", foodInfo);
            return map;
        } else {
            map.put("code", -2);
            map.put("message", "系统开小差啦~ 请重试");
            throw new Exception();
        }
    }

    @Override
    public Map<String, Object> listFoodsFood() {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        //通过套餐id查询菜品id
        List<PackageDetail> packageDetails = packageDetailMapper.listByFoodPackageId(1);
        FoodPackage foodPackage = foodPackageMapper.selectByPrimaryKey(1);
        //套餐价格
        if (foodPackage != null && foodPackage.getPackagePrice() != null) {
            map.put("packagePrice", foodPackage.getPackagePrice());
        }
        if (!CollectionUtils.isEmpty(packageDetails) && packageDetails.size() > 0) {
            //获取菜品id
            List<Integer> foodIds = packageDetails.stream().map(PackageDetail::getFoodId).collect(Collectors.toList());
            //通过菜品id查询菜名
            List<FoodInfo> foodInfos = foodInfoMapper.listFoodInfoByFoodIds(Joiner.on(",").join(foodIds));
            //如果不为空
            if (!CollectionUtils.isEmpty(foodInfos) && foodInfos.size() > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                map.put("data", foodInfos);
                map.put("foodPackageId", 1);
                //前端要添加的字段
                map.put("copies", 1);
                return map;
            }
        }
        map.put("code", 3);
        map.put("message", "没有数据");
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> removeFoodByFoodId(Integer foodId) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        if (foodId != null) {
            int deleteFoodInfo = foodInfoMapper.deleteByPrimaryKey(foodId);
            int deletePackage = packageDetailMapper.deleteByFoodId(foodId);
            if (deleteFoodInfo > 0 && deletePackage > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                return map;
            } else {
                throw new Exception();
            }
        } else {
            map.put("code", 3);
            map.put("message", "参数错误");
            return map;
        }
    }
}
