package com.example.chef.dao.mapper;

import com.example.chef.model.PackageDetail;

import java.util.List;

public interface PackageDetailMapper {

    int insert(PackageDetail record);

    int insertSelective(PackageDetail record);

    int updateByPrimaryKeySelective(PackageDetail record);

    int updateByPrimaryKey(PackageDetail record);
    //通过套餐id查询菜品
    List<PackageDetail> listByFoodPackageId(Integer foodPackageId);

     int  deleteByFoodId(Integer foodId);

}