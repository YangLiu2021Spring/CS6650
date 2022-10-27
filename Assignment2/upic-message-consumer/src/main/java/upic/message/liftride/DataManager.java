package upic.message.liftride;

import upic.model.LiftRide;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DataManager {
    private static final Map<Integer, LiftRide> STORE = new ConcurrentHashMap();

    public synchronized void save(LiftRide liftRide) {
        STORE.put(liftRide.getLiftID(), liftRide);
    }
}
