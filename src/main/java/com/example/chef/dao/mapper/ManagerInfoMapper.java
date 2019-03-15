package com.example.chef.dao.mapper;

import com.example.chef.model.ManagerInfo;

public interface ManagerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManagerInfo record);

    int insertSelective(ManagerInfo record);

    ManagerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManagerInfo record);

    int updateByPrimaryKey(ManagerInfo record);

    ManagerInfo selectByUserName(String userName);
    //登录
    ManagerInfo selectByUserNameAndPassWord(String userName,String passWord);
}