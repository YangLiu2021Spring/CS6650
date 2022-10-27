package io.swagger.manager;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Helps to build a new connection when the IP is different from existing connection. So, we do not have to modify the
 * connection IP in the code and re-deploy the web application again and again.
 */
@Component
public class SkierMessageQueueManager {
    private static final String QUEUE_NAME = "hello";

    private static final String DEFAULT_HOST = "127.0.0.1";

    private static final int PORT = 5672;

    private static final String CONNECTION_FAILURE_MSG_PATTERN =
            "Failed connect to %s:%d. Please consider to update the host via URL upic/set-rmp-host/";

    private String currentHost = DEFAULT_HOST;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    private ObjectPool<Channel> channelPool;

    /**
     * Publish a message into the message queue
     * @param message the given message
     * @throws NullPointerException when the given message is null
     * @throws RuntimeException when fails to publish the message
     */
    public void publish(String message) {
        Objects.requireNonNull(message, "The given message is null.");

        // if host is changed, then rebuild a new channel pool using the new host connection
        if (Objects.isNull(channelPool) || this.isHostChanged()) {
            GenericObjectPool genericObjectPool =
                    new GenericObjectPool<Channel>(new ChannelFactory(this.getConnection()));
            genericObjectPool.setMinIdle(1000);
            genericObjectPool.setMaxIdle(4000);
            channelPool = genericObjectPool;
        }

        Channel channel = null;
        try {
            channel = channelPool.borrowObject();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Failed to publish message. Queue Name: %s, Message %s.", QUEUE_NAME, message), e);
        } finally {
            try {
                if (Objects.nonNull(channel)) {
                    channelPool.returnObject(channel);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format(
                        "Failed to return channel to the pool. Queue Name: %s, Message %s.", QUEUE_NAME, message), e);
            }
        }
    }

    private synchronized Connection getConnection() {

        // if host is changed, then rebuild a new connection
        if (this.isHostChanged()) {
            connection = buildConnection(currentHost);
        }

        // in case connection is null
        if (Objects.isNull(connection)) {
            connection = buildConnection(currentHost);
        }

        return connection;
    }

    private boolean isHostChanged() {
        return !currentHost.equals(getConnectedHost());
    }

    public String getConnectedHost() {
        return Objects.isNull(connectionFactory) ? null : connectionFactory.getHost();
    }

    public void setCurrentHost(String currentHost) {
        this.currentHost = currentHost;
    }

    private Connection buildConnection(String host) {
        try {
            connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername("cs6650");
            connectionFactory.setPassword("1212213");
            connectionFactory.setVirtualHost("/");
            connectionFactory.setPort(PORT);
            connectionFactory.setHost(host);
            return connectionFactory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(String.format(CONNECTION_FAILURE_MSG_PATTERN, host, PORT), e);
        }
    }

    public static class ChannelFactory extends BasePooledObjectFactory<Channel> {
        private Connection connection;

        public ChannelFactory(Connection connection) {
            this.connection = connection;
        }

        @Override
        public Channel create() {
            try {
                Channel channel = this.connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                return this.connection.createChannel();
            } catch (Exception e) {
                throw new RuntimeException(String.format(
                        "Failed to create a RMQ channel. Queue Name: %s.", QUEUE_NAME), e);
            }
        }

        /**
         * Use the default PooledObject implementation.
         */
        @Override
        public PooledObject<Channel> wrap(Channel channel) {
            return new DefaultPooledObject<Channel>(channel);
        }

        /**
         * When an object is returned to the pool, do nothing since we do not change the channel.
         */
        @Override
        public void passivateObject(PooledObject<Channel> pooledObject) {
            // do nothing
        }
    }
}
