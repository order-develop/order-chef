package com.example.chef.service.impl;

import com.example.chef.config.WXMyConfigUtil;
import com.example.chef.service.iface.OrderManagementService;
import com.example.chef.service.iface.WXPayService;
import com.example.chef.util.MD5Util;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/12 14:07
 *
 * @author DongJian
 */
@Service
public class WXPayServiceImpl implements WXPayService {

    private final Logger logger = LoggerFactory.getLogger(WXPayService.class);

    @Autowired
    MD5Util md5Util;
    @Autowired
    OrderManagementService orderManagementService;

    @Override
    public Map<String, String> dounifiedOrder(String money, String spbill_create_ip,String out_trade_no,String openid) throws Exception {
        Map<String, String> fail = new HashMap<>();
        WXMyConfigUtil config = new WXMyConfigUtil();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("nonce_str",WXPayUtil.generateNonceStr());
        String body = "订单支付";
        data.put("body", body);
        //生成订单号
        data.put("out_trade_no", out_trade_no);//11338
        data.put("total_fee", money);
        data.put("spbill_create_ip", spbill_create_ip);//ip地址
        data.put("notify_url", "http://192.168.188.3:8085/WX/notify");
        data.put("trade_type", "JSAPI");
//        data.put("attach", attach);
        data.put("openid", openid);
        data.put("sign", md5Util.getSign(data));
        StringBuffer url = new StringBuffer();
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String returnCode = resp.get("return_code");    //获取返回码
            String returnMsg = resp.get("return_msg");

            if ("SUCCESS".equals(returnCode)) {       //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
                String resultCode = (String) resp.get("result_code");
                String errCodeDes = (String) resp.get("err_code_des");
                System.out.print(errCodeDes);
                if ("SUCCESS".equals(resultCode)) {
                    //获取预支付交易回话标志
                    Map<String, String> map = new HashMap<>();
                    String prepay_id = resp.get("prepay_id");
                    String signType = "MD5";
                    map.put("prepay_id", prepay_id);
                    map.put("signType", signType);
                    String sign = md5Util.getSign(map);
                    resp.put("realsign", sign);
                    url.append("prepay_id=" + prepay_id + "&signType=" + signType + "&sign=" + sign);
                    return resp;
                } else {
                    System.out.println("logger --- 订单号错误信息：" + out_trade_no + errCodeDes);
                    logger.error("logger --- 订单号错误信息：" + errCodeDes);
                    url.append(errCodeDes);
                }
            } else {
                logger.error("logger --- 订单号错误信息：" + returnMsg);
                System.out.println("订单号错误信息：" + out_trade_no + returnMsg);
                url.append(returnMsg);
            }

        } catch (Exception e) {
            logger.error("logger --- ：" + e);
        }
        return fail;
    }

    /**
     * 支付结果通知
     *
     * @param notifyData 异步通知后的XML数据
     * @return
     */
    @Override
    public String payBack(String notifyData) {
        WXMyConfigUtil config = null;
        try {
            config = new WXMyConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        orderManagementService.updatePayStatusByOrderId(out_trade_no);
                        //处理订单逻辑
                        /**
                         *          更新数据库中支付状态。
                         *          特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功。
                         *          此处需要判断一下。后面写入库操作的时候再写
                         *
                         */

                        System.err.println(">>>>>支付成功");
                        logger.info("微信手机支付回调成功订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        logger.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                logger.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            logger.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
