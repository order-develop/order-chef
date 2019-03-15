package com.example.chef.service.iface;

import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/12 14:07
 *
 * @author DongJian
 */
public interface WXPayService {

    Map<String, String> dounifiedOrder(String money, String spbill_create_ip,String out_trade_no,String openid) throws Exception;

    String payBack(String notifyData);
}
