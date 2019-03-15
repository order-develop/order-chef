package com.example.chef.service.impl;

import com.example.chef.dao.mapper.GroupInfoMapper;
import com.example.chef.model.GroupInfo;
import com.example.chef.service.iface.GroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/18 11:28
 *
 * @author NieMingXin
 */
@Service
public class GroupInfoServiceImpl implements GroupInfoService {
    @Autowired
    GroupInfoMapper groupInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> insertSelective(GroupInfo groupInfo) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        if (groupInfo.getGroupName() != null) {
            groupInfo.setcTime(new Date());
            int result = groupInfoMapper.insertSelective(groupInfo);
            if (result > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                map.put("data", groupInfo);
                return map;
            } else {
                throw new Exception();
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteByPrimaryKey(Integer id) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        if (id != null) {
            if (groupInfoMapper.deleteByPrimaryKey(id) > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                return map;
            } else {
                throw new Exception();
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateByPrimaryKeySelective(GroupInfo groupInfo) throws Exception {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        if (groupInfo.getId() != null) {
            groupInfo.setmTime(new Date());
            if (groupInfoMapper.updateByPrimaryKeySelective(groupInfo) > 0) {
                map.put("code", 1);
                map.put("message", "操作成功");
                return map;
            } else {
                throw new Exception();
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
    }

    @Override
    public Map<String, Object> selectGroupById(Integer id) {
        //创建返回结果
        Map<String, Object> map = new HashMap<>();
        if (id != null) {
            GroupInfo groupInfo = groupInfoMapper.selectByPrimaryKey(id);
            if (groupInfo != null) {
                map.put("code", 1);
                map.put("message", "操作成功");
                map.put("data", groupInfo);
                return map;
            } else {
                map.put("code", 3);
                map.put("message", "没有数据");
                return map;
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
    }
}
