package io.swagger.manager;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Helps to build a new connection when the IP is different from existing connection. So, we do not have to modify the
 * connection IP in the code and re-deploy the web application again and again.
 */
@Component
public class SkierMessageQueueConnectionManager {
    private static final String DEFAULT_HOST = "127.0.0.1";

    private static final int PORT = 5672;

    private static final String CONNECTION_FAILURE_MSG_PATTERN =
            "Failed connect to %s:%d. Please consider to update the host via URL upic/set-rmp-host/";

    private String currentHost = DEFAULT_HOST;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    public synchronized Connection getSkierMessageQueueConnection() {

        // if host is changed, then rebuild a new connection
        if (!currentHost.equals(getConnectedHost())) {
            connection = buildConnection(currentHost);
        }

        // in case connection is null
        if (Objects.isNull(connection)) {
            connection = buildConnection(currentHost);
        }

        return connection;
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
}
