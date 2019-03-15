package com.example.chef.service.iface;

import com.example.chef.model.GroupInfo;

import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/18 11:28
 *
 * @author NieMingXin
 */
public interface GroupInfoService {
    //添加管理员小组
    Map<String,Object> insertSelective(GroupInfo groupInfo) throws Exception;
    //根据id删除组
    Map<String,Object>  deleteByPrimaryKey(Integer id) throws Exception;
    //根据id修改组
    Map<String,Object>  updateByPrimaryKeySelective(GroupInfo groupInfo)throws Exception;
    //根据id查询
    Map<String,Object> selectGroupById(Integer id);
}
