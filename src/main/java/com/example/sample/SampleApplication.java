package com.example.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication {

    private static final Logger log = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        log.info("[Step] 애플리케이션 시작");
        SpringApplication.run(SampleApplication.class, args);
        log.info("[Step] 애플리케이션 기동 완료");
    }
}
