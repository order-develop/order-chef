package com.example.chef.dao.mapper;

import com.example.chef.model.FoodInfo;

import java.util.List;

public interface FoodInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FoodInfo record);

    int insertSelective(FoodInfo record);

    FoodInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FoodInfo record);

    int updateByPrimaryKey(FoodInfo record);

    List<FoodInfo> listFoodInfoByFoodIds(String foodIds);
}