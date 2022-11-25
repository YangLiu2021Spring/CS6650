package clientpart2.skiers;

import clientpart2.skiers.model.ConsumingResultRecorder;
import clientpart2.skiers.model.LiftRideEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThroughputAppCore {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected static void runTest(
            AppArg appArg,
            ConsumingResultRecorder consumingResultRecorder,
            LiftRideEventConsumer liftRideEventConsumer
    ) throws InterruptedException {
        consumingResultRecorder.start();

        // produce testing data with a single based on the requirement
        BlockingQueue<LiftRideEvent> queue = new ArrayBlockingQueue(appArg.getQueueCapacity());
        new Thread(new LiftRideEventProducer(queue, appArg.getTotalRequests())).start();

        // the first round consumers
        logInfo("Starting the first round test.");
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        for (int i = 0; i < appArg.getFirstRoundThreads(); i++) {
            new Thread(() -> {
                //logInfo("queue.peek() " + GSON.toJson(queue.peek()));
                for (int j = 0; j < appArg.getFirstRoundRequestsPerThread(); j++) {
                    liftRideEventConsumer.run(queue, countDownLatch1);
                }
            }).start();
        }
        countDownLatch1.await();
        logInfo("Finish the first round test.");

        // the second round consumers
        logInfo("Starting the second round test.");
        CountDownLatch countDownLatch2 = new CountDownLatch(
                (int)(appArg.getTotalRequests() - appArg.getFirstRoundTotalRequests()));
        for (int i = 0; i < appArg.getSecondRoundThreads(); i++) {
            new Thread(() -> {
                while (isNotLast(queue)) {
                    liftRideEventConsumer.run(queue, countDownLatch2);
                }
            }).start();
        }
        countDownLatch2.await();
        consumingResultRecorder.end();
        logInfo("Finish the second round test.");
    }

    protected static AppArg parseAppArg(String[] args) {
        try {
            AppArg appArg = new AppArg(args);
            return appArg;
        } catch (Exception e) {
            e.printStackTrace();
            logInfo(String.format(
                    "Failed to parse arguments from input %s. Please consider input 6 args likes below.\n" +
                    "~/upic-client-java/appassembler/bin/app \\\n" +
                    "http://lb-upic-web-1757023158.us-west-2.elb.amazonaws.com/upic/ 200000 200000 32 1000 250\n\n" +
                    "Meanings by order number:\n" +
                    "0 - API base patch\n" +
                    "1 - total requests will be posted to the web server\n" +
                    "2 - blocking queue capacity, which is used to turn the latency\n" +
                    "3 - number the first round threads, should be 32 by requirement from the assignment\n" +
                    "4 - number of requests send per thread of the first round, should be 1000 by requirement\n" +
                    "5 - number of the second round threads\n",
                    GSON.toJson(args)));
            throw new RuntimeException(e);
        }
    }

    protected static class AppArg {
        private String name;
        private final long totalRequests;
        private final int queueCapacity;
        private final int firstRoundThreads;
        private final int firstRoundRequestsPerThread;
        private final int secondRoundThreads;
        private final String apiBasePath;

        public AppArg(String args[]) {
            this.name = null;
            this.apiBasePath = args[0]; // i.e., http://44.227.82.44:8080/upic
            this.totalRequests = Long.parseLong(args[1]); // should be 200K
            this.queueCapacity = Integer.parseInt(args[2]); // the blocking queue capacity
            this.firstRoundThreads = Integer.parseInt(args[3]); // should be 32 threads
            this.firstRoundRequestsPerThread = Integer.parseInt(args[4]); // should be 1000 requests per thread
            this.secondRoundThreads = Integer.parseInt(args[5]);
        }

        public AppArg withName(String name) {
            this.name = name;
            return this;
        }

        public AppArg outputLog() {
            logInfo(String.format("Current appArg %s", GSON.toJson(this)));
            return this;
        }

        public String getName() {
            return name;
        }

        public long getTotalRequests() {
            return totalRequests;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public int getFirstRoundThreads() {

            return firstRoundThreads;
        }

        public int getFirstRoundRequestsPerThread() {

            return firstRoundRequestsPerThread;
        }

        public int getFirstRoundTotalRequests() {

            return this.firstRoundThreads * this.firstRoundRequestsPerThread;
        }

        public int getSecondRoundThreads() {

            return secondRoundThreads;
        }

        public String getApiBasePath() {

            return apiBasePath;
        }
    }

    protected static void logInfo(String message) {
        System.out.println(String.format("%s - %s", Instant.now().toString(), message));
    }

    protected static boolean isNotLast(BlockingQueue<LiftRideEvent> queue) {
        LiftRideEvent liftRideEvent = queue.peek();
        return liftRideEvent != null && !liftRideEvent.isLastEvent();
    }
}