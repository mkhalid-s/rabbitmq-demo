package com.example.rabbitmqdemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: mshaikh4
 * Date: 07-06-2021
 * Time: 20:18
 * Year: 2021
 * Project: rabbitmq-demo
 * Package: com.example.rabbitmqdemo
 */
@Controller
public class MainController {

    final String queueName = "spring-queue";

    final String topicName = "spring-topic";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Reciever receiver;

    //Map<Character, Integer> map = new HashMap<>();


    @Bean
    Queue queue(){
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(topicName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange){
        //map.values();
        return BindingBuilder.bind(queue).to(topicExchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Reciever receiver) {
        return new MessageListenerAdapter(receiver, "recieveMsg");
    }

    @PutMapping("/msg")
    public String sendMessage(@RequestParam("value") String value) throws InterruptedException {
        System.out.println("Sending message: "+value);
        rabbitTemplate.convertAndSend(topicName, "foo.bar.baz", "Hello from RabbitMQ!  -> " + value);
        receiver.getCountDownLatch().await(1000, TimeUnit.MILLISECONDS);

        return "Sent Message";
    }
}
