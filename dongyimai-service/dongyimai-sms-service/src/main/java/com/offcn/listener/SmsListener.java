package com.offcn.listener;

import com.offcn.config.SmsUtil;
import org.apache.http.HttpResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsListener {
    @Autowired
    private SmsUtil smsUtil;

    @RabbitListener(queues = "dongyimai.sms.queue")
    public void getMessage(Map<String,String> map) throws Exception {
        if (map == null) {
            System.out.println("信息为空");
            return;
        }
        String mobile = map.get("mobile");
        String code = map.get("code");
        // 发送短信
        try{
            HttpResponse httpResponse = smsUtil.sendSms(mobile, code);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode==200){
                System.out.println("发送短信成功");
            }else{
                System.out.println("发送短信失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
