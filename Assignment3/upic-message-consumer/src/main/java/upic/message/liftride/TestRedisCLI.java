package upic.message.liftride;

import redis.clients.jedis.JedisPoolConfig;
import upic.model.LiftRide;
import redis.clients.jedis.JedisPool;

public class TestRedisCLI {
    public static void main(String[] args) {

        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, "34.221.126.246", 6379);
        DataManager dataManager = new DataManager(jedisPool);
        dataManager.save(LiftRide.fromMessage("{'liftID': 1, 'time': 2}"), 1, "2022", "3", 1);
        dataManager.save(LiftRide.fromMessage("{'liftID': 2, 'time': 3}"), 1, "2022", "5", 1);
        dataManager.save(LiftRide.fromMessage("{'liftID': 3, 'time': 5}"), 1, "2022", "4", 1);
        dataManager.save(LiftRide.fromMessage("{'liftID': 4, 'time': 6}"), 1, "2022", "3", 1);
        dataManager.save(LiftRide.fromMessage("{'liftID': 5, 'time': 10}"), 1, "2022", "2", 2);
        dataManager.save(LiftRide.fromMessage("{'liftID': 6, 'time': 22}"), 1, "2022", "3", 2);
        dataManager.save(LiftRide.fromMessage("{'liftID': 1, 'time': 23}"), 1, "2022", "4", 2);
    }
}
