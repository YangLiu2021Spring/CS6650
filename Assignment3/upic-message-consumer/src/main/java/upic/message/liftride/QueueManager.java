package upic.message.liftride;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class QueueManager {
    private final static String QUEUE_NAME = "hello";

    private final static boolean AUTO_ACK = false;
    private static final int PORT = 5672;
    private final Connection connection;


    public QueueManager(String host) {

        this.connection = this.buildConnection(host);
//        this.connection = this.buildConnection("localhost");

    }

    public Channel startNewConsumeChannel(DeliverCallbackProvider deliverCallbackProvider) {
        try {
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // accept only 1 unacknowledged message
            channel.basicQos(1);

            channel.basicConsume(QUEUE_NAME, AUTO_ACK, deliverCallbackProvider.provide(channel), consumerTag -> { });

            return channel;

        } catch (Exception e) {
            throw new RuntimeException("Failed to start a new channel.", e);
        }
    }

    private Connection buildConnection(String host) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername("cs6650");
        factory.setPassword("1212213");
        factory.setVirtualHost("/");
        factory.setPort(PORT);
        factory.setHost(host);

        try {
            return factory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to connect %s:%d", host, PORT), e);
        }
    }
}
