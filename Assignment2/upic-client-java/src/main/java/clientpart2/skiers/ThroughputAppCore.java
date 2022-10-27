package clientpart2.skiers;

import clientpart2.skiers.model.ConsumingResultRecorder;
import clientpart2.skiers.model.LiftRideEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

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
        CountDownLatch countDownLatch1 = new CountDownLatch(appArg.getFirstRoundTotalRequests());
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
            logInfo(String.format("Failed to parse arguments from input %s", GSON.toJson(args)));
            e.printStackTrace();
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
            this.totalRequests = Long.parseLong(args[0]); // should be 200K
            this.queueCapacity = Integer.parseInt(args[1]);
            this.firstRoundThreads = Integer.parseInt(args[2]); // should be 32 threads
            this.firstRoundRequestsPerThread = Integer.parseInt(args[3]); // should be 100 requests per thread
            this.secondRoundThreads = Integer.parseInt(args[4]);
            this.apiBasePath = args[5]; // i.e., http://44.227.82.44:8080/upic
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