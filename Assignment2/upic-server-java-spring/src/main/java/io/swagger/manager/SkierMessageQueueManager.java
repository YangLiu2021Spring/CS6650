package io.swagger.manager;

import com.rabbitmq.client.Channel;
import io.swagger.api.ConfigurationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Helps to build a new connection when the IP is different from existing connection. So, we do not have to modify the
 * connection IP in the code and re-deploy the web application again and again.
 */
@Component
public class SkierMessageQueueManager {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationController.class);

    private final SkierMessageQueueChannelManagerFactory skierMessageQueueChannelManagerFactory;
    private final ArrayBlockingQueue<String> localQueue;

    @Autowired
    public SkierMessageQueueManager(
            SkierMessageQueueChannelManagerFactory skierMessageQueueChannelManagerFactory
    ) {
        this.skierMessageQueueChannelManagerFactory = skierMessageQueueChannelManagerFactory;
        localQueue = new ArrayBlockingQueue<>(200000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
           while(true) {
               // method take waits if necessary until an element becomes available.
               String message = localQueue.take();
               Channel channel = null;
               String queueName = null;
               try {
                   channel = skierMessageQueueChannelManagerFactory.getChannelManager().borrowObject();
                   queueName = skierMessageQueueChannelManagerFactory.getChannelConfig().getQueueName();
                   channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
               } catch (Exception e) {
                   LOG.error("Failed to publish a message", e);
               } finally {
                   try {
                       skierMessageQueueChannelManagerFactory.getChannelManager().returnObject(channel);
                   } catch (Exception e) {
                       LOG.error("Failed to return a channel", e);
                   }
               }
           }
        });
    }

    /**
     * Publish a message into the message queue
     * @param message the given message
     * @throws NullPointerException when the given message is null
     * @throws RuntimeException when fails to publish the message
     */
    public void publish(String message) {
        Objects.requireNonNull(message, "The given message is null.");

        try {
            // use method "offer" to end current publishing action sooner
            boolean success = this.localQueue.offer(message);
            if (!success) {
                throw new RuntimeException("Failed to add message " + message);
            }
        } catch (Exception e) {
            LOG.error("Failed to put message into local queue", e);
        }
    }
}
