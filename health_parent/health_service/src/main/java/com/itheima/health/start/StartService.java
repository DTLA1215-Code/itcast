package com.itheima.health.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StartService {

    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:applicationContext-service.xml");
        System.in.read();
    }
}
