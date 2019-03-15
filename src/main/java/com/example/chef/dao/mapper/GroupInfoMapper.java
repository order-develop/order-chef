package com.example.chef.dao.mapper;

import com.example.chef.model.GroupInfo;

public interface GroupInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupInfo record);

    int insertSelective(GroupInfo record);

    GroupInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupInfo record);

    int updateByPrimaryKey(GroupInfo record);
}