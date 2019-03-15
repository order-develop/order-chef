package com.example.chef.util;

import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * description: TODO
 * create: 2019/2/16 13:52
 *
 * @author DongJian
 */
@Configuration
public class OrderUtil {
    public static String getRandomOrderMark(String openid) {
        String base = "0123456789";
        Random random = new Random();
        Date date = new Date();
        SimpleDateFormat fat = new SimpleDateFormat ("yyyyMMddhhmmss");
        StringBuffer sb = new StringBuffer();
        sb.append(fat.format(date));
        sb.append(openid.substring(0,5));
        int length = 32 - sb.length();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
