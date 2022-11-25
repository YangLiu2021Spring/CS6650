package io.swagger.manager;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.swagger.model.SkierMessageQueueChannelConfig;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class SkierMessageQueueChannelManager {
    private static final Logger LOG = LoggerFactory.getLogger(SkierMessageQueueChannelManager.class);
    private Connection connection;
    private final SkierMessageQueueChannelConfig channelConfig;
    private GenericObjectPool<Channel> objectPool;

    public SkierMessageQueueChannelManager(
            Connection connection,
            SkierMessageQueueChannelConfig skierMessageQueueChannelConfig
    ) {
        this.connection = connection;
        this.channelConfig = skierMessageQueueChannelConfig;

        GenericObjectPoolConfig<Channel> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxTotal(this.channelConfig.getMaxTotal());
        genericObjectPoolConfig.setMaxIdle(this.channelConfig.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(this.channelConfig.getMinIdle());

        this.objectPool = new GenericObjectPool<>(
                new ChannelPooledObjectFactory(connection, this.channelConfig),
                genericObjectPoolConfig);

        LOG.info("Start pre-loading channels with minIdle number {}", this.channelConfig.getMinIdle());
        for (int i = 0; i < this.channelConfig.getMinIdle(); i++) {
            try {
                this.objectPool.addObject();
            } catch (Exception e) {
                LOG.error("Failed to add a channel", e);
                throw new RuntimeException("Failed to add a channel", e);
            }
        }
        LOG.info("Finish pre-loading channels");
    }

    public Channel borrowObject() {
        try {
            return this.objectPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to borrow a channel", e);
        }
    }

    public void returnObject(Channel channel) {
        this.objectPool.returnObject(channel);
    }

    public boolean isOpen() {
        return Objects.isNull(this.connection) ? false : this.connection.isOpen();
    }

    public void close() {
        if (Objects.nonNull(this.objectPool)) {
            this.objectPool.close();
            this.objectPool = null;
        }

        if (this.isOpen()) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (IOException e) {
                LOG.warn("Failed to close a connection", e);
            }
        }
    }

    public static class ChannelPooledObjectFactory extends BasePooledObjectFactory<Channel> {
        private Connection connection;
        private SkierMessageQueueChannelConfig channelConfig;

        public ChannelPooledObjectFactory(
                Connection connection,
                SkierMessageQueueChannelConfig channelConfig
        ) {
            this.connection = connection;
            this.channelConfig = channelConfig;
        }

        @Override
        public Channel create() {
            try {
                Channel channel = this.connection.createChannel();
                channel.queueDeclare(this.channelConfig.getQueueName(), false, false, false, null);
                return channel;
            } catch (Exception e) {
                throw new RuntimeException(String.format(
                        "Failed to create a RMQ channel. Queue Name: %s.", this.channelConfig.getQueueName()), e);
            }
        }

        /**
         * Use the default PooledObject implementation.
         */
        @Override
        public PooledObject<Channel> wrap(Channel obj) {
            return new DefaultPooledObject<>(obj);
        }

        @Override
        public void destroyObject(final PooledObject<Channel> p) throws Exception  {
            p.getObject().close();
        }
    }
}
