package com.example.chef.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.chef.dao.mapper.UserInfoMapper;
import com.example.chef.model.UserInfo;
import com.example.chef.service.iface.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 用户表相关service
 * create: 2019/2/13 10:21
 *
 * @author NieMingXin
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> insertSelective(UserInfo userInfo) throws Exception {
        Map<String, Object> map = new HashMap<>();

        //验证参数
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getOpenId())
                && !StringUtils.isEmpty(userInfo.getRealName())
                && !StringUtils.isEmpty(userInfo.getTel())
                && userInfo.getRoleId() != null
                && !StringUtils.isEmpty(userInfo.getNickName())) {
            UserInfo user = userInfoMapper.selectUserInfoByOpenId(userInfo.getOpenId());
            if (user != null && user.getOpenId() != null) {
                map.put("code", -99);
                map.put("message", "用户已存在");
                return map;
            } else {
                userInfo.setcTime(new Date());
                int result = userInfoMapper.insertSelective(userInfo);
                //判断插入是否成功
                if (result > 0) {
                    map.put("code", 1);
                    map.put("message", "操作成功");
                    map.put("data", userInfo);
                    return map;
                } else {
                    map.put("code", -1);
                    map.put("message", "系统开小差啦~ 请重试");
                    throw new Exception();
                }
            }
        } else {
            map.put("code", -2);
            map.put("message", "参数错误");
            return map;
        }
    }

    @Override
    public Map<String, Object> selectUserInfoByOpenId(String openId) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByOpenId(openId);
        Map<String, Object> map = new HashMap<>();
        if (userInfo != null && userInfo.getId() != null) {
            map.put("code", 1);
            map.put("message", "用户已存在");
            map.put("data", userInfo);
            return map;
        } else {
            map.put("code", 3);
            map.put("message", "可以注册");
            return map;
        }
    }
}
