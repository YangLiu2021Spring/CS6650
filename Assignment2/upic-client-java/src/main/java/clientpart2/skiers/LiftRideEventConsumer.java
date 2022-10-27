package clientpart2.skiers;


import clientpart2.skiers.model.AccessLog;
import clientpart2.skiers.model.ConsumingResultRecorder;
import clientpart2.skiers.model.LiftRideEvent;
import io.swagger.client.ApiException;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class LiftRideEventConsumer {
    private static final int INITIAL_RETRIED_TIMES = 0;
    private static final int MAX_RETRY_TIMES = 5;

    private final ConsumingResultRecorder consumingResultRecorder;
    private final SkiersApiFactory skiersApiFactory;
    private final Queue<AccessLog> accessLogQueue;

    public LiftRideEventConsumer(
            ConsumingResultRecorder consumingResultRecorder,
            SkiersApiFactory skiersApiFactory,
            Queue<AccessLog> accessLogQueue
    ) {
        this.consumingResultRecorder = consumingResultRecorder;
        this.skiersApiFactory = skiersApiFactory;
        this.accessLogQueue = accessLogQueue;
    }

    public void run(BlockingQueue<LiftRideEvent> queue, CountDownLatch countDownLatch) {
        Instant t1 = Instant.now();
        int responseCode = 201; // by default
        try {
            uploadLiftRideEventWithRetry(queue.take(), INITIAL_RETRIED_TIMES, MAX_RETRY_TIMES);
            this.consumingResultRecorder.increaseNumberOfSuccessfulRequests();
        } catch (ApiException e) {
            responseCode = e.getCode();
            this.consumingResultRecorder.increaseNumberOfUnsuccessfulRequests();
            throw new RuntimeException("Unexpected error", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected error", e);
        } finally {
            Instant t2 = Instant.now();
            countDownLatch.countDown();

            if (accessLogQueue != null) {
                AccessLog accessLog = new AccessLog();
                accessLog.setTime(t1.toString());
                accessLog.setRequestType("POST");
                accessLog.setLatency(t2.toEpochMilli() - t1.toEpochMilli());
                accessLog.setResponseCode(responseCode);
                this.accessLogQueue.add(accessLog);
            }
        }
    }

    private void uploadLiftRideEventWithRetry(
            LiftRideEvent event,
            int retriedTimes,
            int maxRetryTimes
    ) throws ApiException {
        try {
            skiersApiFactory.newSkiersApi().writeNewLiftRide(
                    event.getLiftRide(), event.getResortID(), event.getSeasonID(), event.getDayID(), event.getSkierID());
        } catch (ApiException e) {
            retriedTimes ++;
            // {"code":0,"detailMessage":"java.net.ConnectException: Failed to connect to..."}
            if ((e.getCode() == 0 || e.getCode() >= 400 && e.getCode() < 600) && retriedTimes < maxRetryTimes) {
                uploadLiftRideEventWithRetry(event, retriedTimes, maxRetryTimes);
            }
            throw e;
        }
    }
}
