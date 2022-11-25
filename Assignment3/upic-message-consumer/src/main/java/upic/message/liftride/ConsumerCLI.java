package upic.message.liftride;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.apache.log4j.Logger;

/**
 * A command line interface class.
 */
public class ConsumerCLI {
    static Logger log = Logger.getLogger(ConsumerCLI.class.getName());
    public static void main(String[] args) {
//        System.out.println("Your input is " + args);
        AppArg appArg = new AppArg(args);

        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, appArg.getRedisHost(), 6379);

        DataManager dataManager = new DataManager(jedisPool);
        DeliverCallbackProvider deliverCallbackProvider = new DeliverCallbackProvider(dataManager);
        QueueManager queueManager = new QueueManager(appArg.getHost());

        logInfo(String.format("Message consumer is started with %s. To exit press CTRL+C.", appArg));
        for (int i = 0; i < appArg.getNumberOfChannels(); i++) {
            new Thread(new MessageConsumer(queueManager, deliverCallbackProvider)).start();
        }
    }

    protected static void logInfo(String message) {
        log.info(message);
        //System.out.println(String.format("%s - %s", Instant.now().toString(), message));
    }

    public static class AppArg {
        private String host;

        private String redisHost;
        private int numberOfChannels;

        public AppArg(String[] args) {
            this.host = args[0];
            this.redisHost = args[1];
            this.numberOfChannels = Integer.parseInt(args[2]);
        }

        public String getHost() {
            return host;
        }

        public String getRedisHost() {
            return redisHost;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getNumberOfChannels() {
            return numberOfChannels;
        }

        public void setNumberOfChannels(int numberOfChannels) {
            this.numberOfChannels = numberOfChannels;
        }

        @Override
        public String toString() {
            return String.format("{\"host\":\"%s\",\"redisHost\":\"%s\",\"numberOfChannels\":%d}",
                    host, redisHost, numberOfChannels);
        }
    }
}
