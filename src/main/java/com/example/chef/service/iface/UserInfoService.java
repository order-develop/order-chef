package com.example.chef.service.iface;

import com.example.chef.model.UserInfo;

import java.util.Map;

/**
 * description: 用户表相关service
 * create: 2019/2/13 10:20
 *
 * @author NieMingXin
 */
public interface UserInfoService {
    Map<String, Object>  insertSelective(UserInfo record) throws Exception;

    Map<String, Object>  selectUserInfoByOpenId(String openId);
}
