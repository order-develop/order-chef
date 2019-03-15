package com.example.chef.config;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * description: TODO
 * create: 2019/2/12 13:47
 *
 * @author DongJian
 */
@Configuration
public class WXMyConfigUtil implements WXPayConfig {
    private byte[] certData;

    public WXMyConfigUtil() throws Exception {
//        String certPath = "D:\\谷歌下载目录\\BaiduNetdisk_5.7.2.exe";//从微信商户平台下载的安全证书存放的目录
//
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    @Override
    public String getAppID() {
        return "wx426b3015555a46be";
    }

    //parnerid
    @Override
    public String getMchID() {
        return "1900009851";
    }

    @Override
    public String getKey() {
        return "8934e7d15453e97507ef794cf7b0519d";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
