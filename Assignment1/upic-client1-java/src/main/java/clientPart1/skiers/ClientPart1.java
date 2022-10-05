package clientPart1.skiers;

import io.swagger.client.ApiException;

import java.util.concurrent.CountDownLatch;

public class ClientPart1 {
    private static final int FIRST_ROUND_THREADS = 32;
    private static final int FIRST_ROUND_REQUESTS_PER_THREAD = 1000;
    private static final int SECOND_ROUND_THREADS = 100;
    private static final int SECOND_ROUND_REQUESTS_PER_THREAD = 10;
    private static final int EXPECTED_TOTAL_REQUESTS = 200 * 1000; // 200K

    public static void main (String[] args) throws InterruptedException {
        long t1 = System.currentTimeMillis();
        WriteNewLiftRideTester tester = new WriteNewLiftRideTester();

        CountDownLatch firstRoundCounter = new CountDownLatch(FIRST_ROUND_THREADS * FIRST_ROUND_REQUESTS_PER_THREAD);
        for (int i = 0; i < FIRST_ROUND_THREADS; i++) {
            new Thread(buildRunnable(tester, FIRST_ROUND_REQUESTS_PER_THREAD, firstRoundCounter)).start();
        }
        firstRoundCounter.await();

        while (EXPECTED_TOTAL_REQUESTS > tester.getNumberOfTotalRequests()) {
            CountDownLatch secondRoundCounter = new CountDownLatch(SECOND_ROUND_THREADS * SECOND_ROUND_REQUESTS_PER_THREAD);
            for (int i = 0; i < SECOND_ROUND_THREADS; i++) {
                new Thread(buildRunnable(tester, SECOND_ROUND_REQUESTS_PER_THREAD, secondRoundCounter)).start();
            }
            secondRoundCounter.await();
        }

        long t2 = System.currentTimeMillis();
        long timeElapsed = t2 - t1;
        System.out.println("number of successful requests sent: " + tester.getNumberOfSuccessfulRequests());
        System.out.println("number of unsuccessful requests: " + tester.getNumberOfUnsuccessfulRequests());
        System.out.println(String.format("the total run time (wall time) %d ms", timeElapsed));
        System.out.println("the total throughput in requests per second: "
            + (tester.getNumberOfTotalRequests() / ((double)timeElapsed / 1000d)));
    }

    public static Runnable buildRunnable(
        WriteNewLiftRideTester tester, int requestPerThread, CountDownLatch countDownLatch
    ) {
        return () -> {
            for (int numberOfRequests = 0; numberOfRequests < requestPerThread; numberOfRequests++) {
                try {
                    tester.uploadLiftRideEventWithRandomValue();
                    tester.increaseNumberOfSuccessfulRequests();
                } catch (ApiException e) {
                    tester.increaseNumberOfUnsuccessfulRequests();
                    //System.out.println(gson.toJson(e));
                    //e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }
        };
    }
}
