package upic.message.liftride;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import upic.model.LiftRide;

public class DeliverCallbackProvider {
    private final DataManager dataManager;

    public DeliverCallbackProvider(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DeliverCallback provide(Channel channel) {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            LiftRide liftRide = LiftRide.fromMessage(message);
            this.dataManager.save(liftRide);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
    }
}
