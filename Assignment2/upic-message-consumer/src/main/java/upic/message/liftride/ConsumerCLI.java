package upic.message.liftride;

import java.time.Instant;

/**
 * A command line interface class.
 */
public class ConsumerCLI {
    public static void main(String[] args) {
        AppArg appArg = new AppArg(args);

        DataManager dataManager = new DataManager();
        DeliverCallbackProvider deliverCallbackProvider = new DeliverCallbackProvider(dataManager);
        QueueManager queueManager = new QueueManager(appArg.getHost());

        logInfo(String.format("Message consumer is started with %s. To exit press CTRL+C", appArg));
        for (int i = 0; i < appArg.getNumberOfChannels(); i++) {
            new Thread(new MessageConsumer(queueManager, deliverCallbackProvider)).start();
        }
    }

    protected static void logInfo(String message) {
        System.out.println(String.format("%s - %s", Instant.now().toString(), message));
    }

    public static class AppArg {
        private String host;
        private int numberOfChannels;

        public AppArg(String[] args) {
            this.host = args[0];
            this.numberOfChannels = Integer.parseInt(args[1]);
        }

        public String getHost() {
            return host;
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
            return String.format("{\"host\":\"%s\",\"numberOfChannels\":%d}", host, numberOfChannels);
        }
    }
}
