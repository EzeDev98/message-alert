package com.wm.notification.config;

import com.wm.notification.service.impl.listener.EventNotificationListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    SimpleMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);

        container.setQueueNames("event-alerts");

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(EventNotificationListener listener) {
        return new MessageListenerAdapter(listener, "handleNotificationMessage");
    }
}