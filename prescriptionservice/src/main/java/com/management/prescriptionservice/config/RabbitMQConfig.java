package com.management.prescriptionservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.prescription.exchange}")
    private String prescriptionExchange;

    @Value("${rabbitmq.prescription.queue}")
    private String prescriptionQueue;

    @Value("${rabbitmq.prescription.routing-key}")
    private String prescriptionRoutingKey;

    @Bean
    public Queue prescriptionQueue() {
        return QueueBuilder.durable(prescriptionQueue)
                .withArgument("x-dead-letter-exchange", prescriptionExchange + ".dlx")
                .withArgument("x-dead-letter-routing-key", prescriptionRoutingKey + ".dlq")
                .build();
    }

    @Bean
    public DirectExchange prescriptionExchange() {
        return new DirectExchange(prescriptionExchange);
    }

    @Bean
    public Binding prescriptionBinding() {
        return BindingBuilder
                .bind(prescriptionQueue())
                .to(prescriptionExchange())
                .with(prescriptionRoutingKey);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(prescriptionQueue + ".dlq");
    }


    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(prescriptionExchange + ".dlx");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(prescriptionRoutingKey + ".dlq");
    }
}
