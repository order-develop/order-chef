package com.example.chef.dao.mapper;

import com.example.chef.model.FoodPackage;

public interface FoodPackageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FoodPackage record);

    int insertSelective(FoodPackage record);

    FoodPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FoodPackage record);

    int updateByPrimaryKey(FoodPackage record);
}