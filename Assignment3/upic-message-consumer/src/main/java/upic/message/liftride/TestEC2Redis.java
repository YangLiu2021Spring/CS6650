package upic.message.liftride;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import upic.model.LiftRide;
import upic.model.ResortEvent;

public class TestEC2Redis {
    public static void main(String[] args) {
        String message = "{\"liftID\":31,\"time\":19,\"resortID\":2,\"seasonID\":2022,\"dayID\":1,\"skierID\":79080 }";
        LiftRide liftRide = LiftRide.fromMessage(message);
        ResortEvent resortEvent = ResortEvent.fromMessage(message);

        if (1==1) {
            return;
        }
//        JedisPoolConfig config = new JedisPoolConfig();
//        JedisPool jedisPool = new JedisPool(config, "52.11.163.152", 6379);
//        DataManager dataManager = new DataManager(jedisPool);
//        dataManager.save(LiftRide.fromMessage("{\"liftID\":31,\"time\":19,\"resortID\":2,\"seasonID\":2022,\"dayID\":1,\"skierID\":79080 }"), -1, "-2022", "-3", -1);
    }
}
