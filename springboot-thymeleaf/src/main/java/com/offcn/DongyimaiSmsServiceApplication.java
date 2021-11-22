package com.offcn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class
)
public class DongyimaiSmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongyimaiSmsServiceApplication.class, args);
    }

}
