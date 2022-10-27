package io.swagger.configuration;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkierMessageQueueConfiguration {
    public static final String QUEUE_NAME = "hello";

    @Bean
    public Connection getSkierMessageQueueConnection() {
        String host = "54.202.105.9";
        int port = 5672;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("cs6650");
            factory.setPassword("1212213");
            factory.setVirtualHost("/");
            factory.setPort(port);
            factory.setHost(host);
            return factory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to connect to channel %s:%d", host, port), e);
        }
    }
}
