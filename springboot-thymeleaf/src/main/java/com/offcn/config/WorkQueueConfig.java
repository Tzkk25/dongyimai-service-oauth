package com.offcn.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkQueueConfig {
    /**
     * 队列名.
     */
    private final String work = "work_queue";

    @Bean
    public Queue workQueue() {
        return new Queue(work);
    }
}
