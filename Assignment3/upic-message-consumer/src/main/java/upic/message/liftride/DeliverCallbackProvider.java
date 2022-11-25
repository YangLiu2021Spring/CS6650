package upic.message.liftride;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import upic.model.LiftRide;
import upic.model.ResortEvent;


public class DeliverCallbackProvider {
    private final DataManager dataManager;


    public DeliverCallbackProvider(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DeliverCallback provide(Channel channel) {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            LiftRide liftRide = LiftRide.fromMessage(message);
            ResortEvent resortEvent = ResortEvent.fromMessage(message);


            this.dataManager.save(liftRide,
                    resortEvent.getResortID(),
                    resortEvent.getSeasonID(),
                    resortEvent.getDayID(),
                    resortEvent.getSkierID()
            );

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        };


        return deliverCallback;

    }
}
