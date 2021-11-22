package com.offcn.util;


import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class SmsUtil {

    String host = "http://dingxin.market.alicloudapi.com";
    String path = "/dx/sendSms";
    String method = "POST";

    @Value("${sms.appcode}")
    String appcode ;

    @Value("${sms.tpl_id}")
    String tpl_id;

    /**
     *
     * @param mobile 手机号
     * @param code    验证码
     * @return
     */
    public HttpResponse sendSms(String mobile, String code) {

        //封装请求头参数
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);

        //请求参数
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile",mobile);
        querys.put("param", "code:"+code);
        querys.put("tpl_id", tpl_id);//模板id

        //3、请求体
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
