package com.offcn.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveListener {
    //监听指定队列，按顺序获取队列中的消息数据

    @RabbitListener(queues= "spring.test.queue")
    public void simple1(Object message) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("消费者接收到的消息:"+message);
    }

    //消费者1
    @RabbitListener(queues= "work_queue")
    public void work1(Object message) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("消费者1接收到的消息:"+message);
    }

    //消费者2
    @RabbitListener(queues = "work_queue")
    public void work2(Object message){
        System.out.println("消费者2接收到的消息"+message);
    }

    @RabbitListener(queues = "fanout_queue_1")
    public void recieve1(String message) {
        System.out.println("listener1:" + message);
    }

    @RabbitListener(queues = "fanout_queue_2")
    public void recieve2(String message) {
        System.out.println("listener2:" + message);
    }

    @RabbitListener(queues = "direct_queue_1")
    public void recieve3(String message) {
        System.out.println("listener1:" + message);
    }

    @RabbitListener(queues = "direct_queue_2")
    public void recieve4(String message) {
        System.out.println("listener2:" + message);
    }

    @RabbitListener(queues = "topic_queue_1")
    public void Topic1(String message) {
        System.out.println("listener1:" + message);
    }

    @RabbitListener(queues = "topic_queue_2")
    public void topic2(String message) {
        System.out.println("listener2:" + message);
    }

}
