package clientpart2.skiers;


import clientpart2.skiers.model.LiftRideEvent;
import clientpart2.util.RandomUtils;
import io.swagger.client.model.LiftRide;

import java.util.concurrent.BlockingQueue;

public class LiftRideEventProducer implements Runnable {
    private static final String SEASON_ID = "2022";
    private static final String DAY_ID = "1";

    private final BlockingQueue<LiftRideEvent> queue;
    private final long numberOfMaxEvents;

    public LiftRideEventProducer(BlockingQueue<LiftRideEvent> queue, long numberOfMaxEvents) {
        this.queue = queue;
        this.numberOfMaxEvents = numberOfMaxEvents;
    };

    @Override
    public void run() {
        try {
            for (long i = 0; i < numberOfMaxEvents; i++) {
                queue.put(this.createRandomEvent());
            }

            // put an event with the last event flag in order to stop the consumers
            queue.put(this.createRandomEvent().withIsLastEvent(true));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private LiftRideEvent createRandomEvent() {
        LiftRide liftRide = new LiftRide();
        liftRide.setLiftID(RandomUtils.newPositiveInt(40));
        liftRide.setTime(RandomUtils.newPositiveInt(360));
        int resortID = RandomUtils.newPositiveInt(10);
        int skierID = RandomUtils.newPositiveInt(100000);

        return new LiftRideEvent(liftRide, resortID, SEASON_ID, DAY_ID, skierID);
    }
}

