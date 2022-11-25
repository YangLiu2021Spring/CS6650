package io.swagger.manager;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.swagger.model.SkierMessageQueueChannelConfig;
import io.swagger.model.SkierMessageQueueConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SkierMessageQueueChannelManagerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SkierMessageQueueChannelManagerFactory.class);
    private static final String CONNECTION_FAILURE_MSG_PATTERN =
            "Failed connect to %s:%d. Please consider to update the host via URL /upic/set-config";
    private SkierMessageQueueConnectionConfig connectionConfig;
    private SkierMessageQueueChannelConfig channelConfig;

    private Connection connection;

    private SkierMessageQueueChannelManager skierMessageQueueChannelManager;

    public SkierMessageQueueChannelManagerFactory() {
        // do nothing since we want to initialize connections via ConfigurationController
    }

    public synchronized void init(
            SkierMessageQueueConnectionConfig skierMessageQueueConnectionConfig,
            SkierMessageQueueChannelConfig skierMessageQueueChannelConfig
    ) {
        LOG.info("Start initializing");
        this.connectionConfig = skierMessageQueueConnectionConfig;
        this.channelConfig = skierMessageQueueChannelConfig;
        this.connection = this.buildConnection();
        this.skierMessageQueueChannelManager = new SkierMessageQueueChannelManager(connection, this.getChannelConfig());
        LOG.info("Finish initializing");
    }

    public SkierMessageQueueChannelManager getChannelManager() {
        return this.skierMessageQueueChannelManager;
    }

    public void destroy() {
        if(Objects.nonNull(this.skierMessageQueueChannelManager)) {
            skierMessageQueueChannelManager.close();
            skierMessageQueueChannelManager = null;
        }
    }

    public SkierMessageQueueConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    public SkierMessageQueueChannelConfig getChannelConfig() {
        return channelConfig;
    }

    private Connection buildConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("cs6650");
        connectionFactory.setPassword("1212213");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost(connectionConfig.getHost());
        connectionFactory.setPort(connectionConfig.getPort().intValue());

        try {
            return connectionFactory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(String.format(CONNECTION_FAILURE_MSG_PATTERN,
                    connectionConfig.getHost(), connectionConfig.getPort().intValue()), e);
        }
    }
}
