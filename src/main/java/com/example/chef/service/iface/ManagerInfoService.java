package com.example.chef.service.iface;


import com.example.chef.model.ManagerInfo;

import java.io.OutputStream;
import java.util.Map;

public interface ManagerInfoService {

    Map<String,Object> insertSelective(ManagerInfo record) throws Exception;

    Map<String, Object> updateByPrimaryKeySelective(ManagerInfo record) throws Exception;

    Map<String, Object> deleteByPrimaryKey(Integer id) throws Exception;

    Map<String, Object> selectByUserNameAndPassWord(String userName,String passWord);


    void export(String excelName, OutputStream out,Integer status,String startTime,String endTime);

    void exportNow(String excelName, OutputStream out);
}