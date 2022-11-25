package upic.message.liftride;

import org.apache.log4j.Logger;
import upic.model.LiftRide;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class DataManager {
    static Logger log = Logger.getLogger(DataManager.class.getName());
    private JedisPool jedisPool;
    public DataManager(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    public synchronized void save(LiftRide liftRide, int resortID, String seasonID, String dayID, int skierID) {

        Jedis jedis = null;
        try {
            if (null != jedisPool){

                jedis = jedisPool.getResource();
                try {
                    jedis.auth("redis2022");
                } catch (Exception e) {
                    log.error(e);
                }
            }
            String key = "Resort" + resortID + "Season" + seasonID + "Day" + dayID + "Skier" + skierID;
            String value = String.valueOf(liftRide.getLiftID());

            log.info("Saving key: " + key + ", value: " + value);
            jedis.lpush(key, value);
            log.info("Saved successfully");
        } catch (Exception e) {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
            log.error(e);
            throw e;
        } finally {

            if (jedis != null) {
                jedis.close();

            }
        }
    }

}
