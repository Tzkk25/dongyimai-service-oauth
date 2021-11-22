package com.offcn.config;

import com.offcn.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {
    //每个人的appcode都不一样
    @Value("${sms.appcode}")
    private String appcode;

    @Value("${sms.smsSignId}")
    private String smsSignId;

    @Value("${sms.templateId}")
    private String templateId;

    String host = "https://gyytz2.market.alicloudapi.com";
    String path = "/sms/smsSendLong";
    String method = "POST";

    public HttpResponse sendSms(String mobile, String code) {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "**code**:"+code+",**minute**:5");

        querys.put("smsSignId",smsSignId);
        querys.put("templateId", templateId);// 短信验证码的模板(默认模板)

        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
