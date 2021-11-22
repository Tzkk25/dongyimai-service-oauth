package com.offcn.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //提供商
    @RequestMapping("/send1")
    public void send1(){
        for (int i = 1;i <= 50; i++){
            rabbitTemplate.convertAndSend("spring.test.queue", "简单队列消息"+i);
        }
    }

    @RequestMapping("/send2")
    public void send2(){
        for (int i = 1;i <= 50; i++){
            rabbitTemplate.convertAndSend("work_queue", "能者多劳队列消息"+i);
        }
    }

    @RequestMapping("/send3")
    public void send3() {
        for (int i = 1; i <= 50; i++) {
            String message = "订阅模式消息" + i;
            // 参数1：交换机名称  参数2：在fanout类型的交换机中不需要指定key   参数2：消息
            rabbitTemplate.convertAndSend("fanoutExchange", "", message);
        }
    }

    @RequestMapping("/send4")
    public void sendMessageA() {
        for (int i = 1; i <= 50; i++) {
            String message = "路由模式--routingKey=update消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("directExchange", "update", message);
        }
    }
    @RequestMapping("/send5")
    public void sendMessageB() {
        for (int i = 1; i <= 50; i++) {
            String message = "路由模式--routingKey=add消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("directExchange", "add", message);
        }
    }


    @RequestMapping("/send6")
    public void send01() {
        for (int i = 0; i < 5; i++) {
            String message = "通配符模式--routingKey=topic.keyA消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("topicExchange", "topic.keyA", message);
        }
    }
    @RequestMapping("/send7")
    public void send02(){
        for (int i = 0; i < 5; i++) {
            String message = "通配符模式--routingKey=topic.#消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("topicExchange", "topic.keyD.keyE", message);
        }
    }
}
