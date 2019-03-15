package com.example.chef.controller;

import com.example.chef.config.WXMyConfigUtil;
import com.example.chef.service.iface.WXPayService;
import com.example.chef.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * description: TODO
 * create: 2019/2/12 13:59
 *
 * @author DongJian
 */
@RestController
@Api(value = "微信支付接口")
@RequestMapping("WX")
public class WXController {

    private final Logger logger = LoggerFactory.getLogger(WXController.class);

    @Autowired
    WXPayService wxPayService;
    @Autowired
    WXMyConfigUtil wxMyConfigUtil;
    @Autowired
    HttpClient httpClient;


    /**
     * author: DongJian
     * create: 2019/2/18 14:42
     * description: 获取openId
     *
     * @param code 小程序授权后获得的code
     * @return java.lang.String
     */
    @ApiOperation(value = "获取openId", notes = "获取openId", httpMethod = "GET")
    @RequestMapping(value = "/getOpenId", method = RequestMethod.GET)
    public String getOpenId(@RequestParam("code") String code) {
        if (code == null || "".equals(code)) {
            return null;
        }
        //拼接URL
        String url = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code" + "&appid=wx11e52369121a388e" + "&secret=3ae5e905b44afd583e2b1e1c9298d358" + "&js_code=" + code;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpMethod method = HttpMethod.GET;
        String json = httpClient.client(url, method, params);
        return json;
    }

    /**
     * author: DongJian
     * create: 2019/2/18 14:43
     * description: 微信统一下单接口
     *
     * @param out_trade_no 订单号
     * @param money 支付的金额
     * @param openid openid
     * @param request 请求对象
     * @return java.lang.String
     */
    @ApiOperation(value = "微信统一下单接口", notes = "微信统一下单接口", httpMethod = "POST")
    @RequestMapping(value = "/pay", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String,Object> orderPay(@RequestParam(value = "out_trade_no") String out_trade_no,
                           @RequestParam(value = "money") String money,
                           @RequestParam(value = "openid") String openid,
                           HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if (openid == null || "".equals(openid)) {
            map.put("code", -1);
            map.put("message", "缺少openid");
            return map;
        }
        if (money == null || "".equals(money)) {
            map.put("code", -1);
            map.put("message", "缺少支付金额");
            return map;
        }
        if (out_trade_no == null || "".equals(out_trade_no)) {
            map.put("code", -1);
            map.put("message", "缺少订单号");
            return map;
        }
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
//        String hehe = dateFormat.format(now);
//        String out_trade_no = hehe + "wxpay";  //777777 需要前端给的参数
//        String total_fee = "1";              //7777777  微信支付钱的单位为分
//        String user_id = "1";               //77777
//        String coupon_id = "7";               //777777
//        String spbill_create_ip = GetIPAddrUtil.getIpAddr(req);
        String spbill_create_ip = "192.168.1.1";
//        System.err.println(spbill_create_ip);
        Map<String, String> result = wxPayService.dounifiedOrder(money, spbill_create_ip,out_trade_no,openid);
        String nonce_str = (String) result.get("nonce_str");
        String prepay_id = (String) result.get("prepay_id");
        Long time = System.currentTimeMillis() / 1000;
        String timestamp = time.toString();

        //签名生成算法
//        MD5Util md5Util = new MD5Util();
        Map<String, String> mapMD5 = new HashMap<>();
        mapMD5.put("appId", wxMyConfigUtil.getAppID());
        mapMD5.put("package", "prepay_id=" + prepay_id);
        mapMD5.put("nonceStr", nonce_str);
        mapMD5.put("timeStamp", timestamp);
        mapMD5.put("signType", "MD5");
//        String sign = md5Util.getSign(map);
        String resultString = WXPayUtil.generateSignature(mapMD5, wxMyConfigUtil.getKey());
//        System.err.println(resultString);
        map.put("code", 1);
        map.put("message", "操作成功");
        map.put("data", resultString);
        return map;    //给前端app返回此字符串，再调用前端的微信sdk引起微信支付

    }

    /**
     * author: DongJian
     * create: 2019/2/18 14:44
     * description: 订单支付异步通知
     *
     * @param request 请求对象
     * @return java.lang.String
     */
    @ApiOperation(value = "手机订单支付完成后回调", notes = "手机订单支付完成后回调", httpMethod = "POST")
    @RequestMapping(value = "/notify", method = {RequestMethod.GET, RequestMethod.POST})
    public String WXPayBack(HttpServletRequest request) {
        String resXml = "";
        System.err.println("进入异步通知");
        try {
            //
            InputStream is = request.getInputStream();
            //将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            System.err.println(resXml);
            String result = wxPayService.payBack(resXml);
//            return "<xml><return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg></xml>";
            return result;
        } catch (Exception e) {
            logger.error("logger --- 手机支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }
}
