package com.example.rabbitmqdemo;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ IDEA.
 * User: mshaikh4
 * Date: 07-06-2021
 * Time: 20:13
 * Year: 2021
 * Project: rabbitmq-demo
 * Package: com.example.rabbitmqdemo
 */
@Component
public class Reciever {

    private CountDownLatch countDownLatch = new CountDownLatch(1);


    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void recieveMsg(String message){
        System.out.println("Recieved Message: ---> "+ message + "  <--- ");
        countDownLatch.countDown();
    }
}
