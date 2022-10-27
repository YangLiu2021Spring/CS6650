package io.swagger.manager;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A manager class which helps to manage the message publishing for the skier message.
 */
@Component
public class SkierMessageQueueManager {
    public static final String QUEUE_NAME = "hello";

    private final SkierMessageQueueConnectionManager skierMessageQueueConnectionManager;

    @Autowired
    public SkierMessageQueueManager(SkierMessageQueueConnectionManager skierMessageQueueConnectionManager) {
        this.skierMessageQueueConnectionManager = skierMessageQueueConnectionManager;
    }

    /**
     * Publish a message into the message queue
     * @param message the given message
     * @throws NullPointerException when the given message is null
     * @throws RuntimeException when fails to publish the message
     */
    public void publish(String message) {
        Objects.requireNonNull(message, "The given message is null.");

        Connection mqConnection = skierMessageQueueConnectionManager.getSkierMessageQueueConnection();

        try {
            Channel channel = mqConnection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            channel.close();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Failed to publish message. Queue Name: %s, Message %s.", QUEUE_NAME, message), e);
        }
    }
}
