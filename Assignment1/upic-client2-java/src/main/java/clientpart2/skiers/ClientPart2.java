package clientpart2.skiers;

import clientpart2.util.FileUtils;
import io.swagger.client.ApiException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ClientPart2 {
    private static final int FIRST_ROUND_THREADS = 32;
    private static final int FIRST_ROUND_REQUESTS_PER_THREAD = 1000;
    private static final int SECOND_ROUND_THREADS = 100;
    private static final int SECOND_ROUND_REQUESTS_PER_THREAD = 10;
    private static final int EXPECTED_TOTAL_REQUESTS = 200 * 1000; // 200K
    private static final long currentMillions = System.currentTimeMillis();
    private static final String RESULT_FILE_PATH = String.format("./test_result_%s.csv", currentMillions);
    private static final String SECOND_RESULT_FILE_PATH = String.format(
            "./test_result_%s_group_by_second.csv", currentMillions);

    public static void main (String[] args) throws InterruptedException {
        FileUtils.createFileIfNotExist(RESULT_FILE_PATH);

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

        List<TestResultAnalyzer.TestResult> testResultList = TestResultAnalyzer.loadTestResult(RESULT_FILE_PATH);
        TestResultAnalyzer testResultAnalyzer = new TestResultAnalyzer(testResultList);

        System.out.println("mean response time (millisecs): " + testResultAnalyzer.getMeanResponseTime());
        System.out.println("median response time (millisecs): " + testResultAnalyzer.getMedianResponseTime());
        System.out.println("throughput = total number of requests/wall time (requests/second): "
                + (tester.getNumberOfTotalRequests() / ((double)timeElapsed / 1000d)));
        System.out.println("p99 (99th percentile) response time: " + testResultAnalyzer.getP99ResponseTime());
        System.out.println("min response time (millisecs): " + testResultAnalyzer.getMinResponseTime());
        System.out.println("max response time (millisecs): " + testResultAnalyzer.getMaxResponseTime());

        // group by time (second)
        FileUtils.createFileIfNotExist(SECOND_RESULT_FILE_PATH);
        Map<String, List<TestResultAnalyzer.TestResult>> groups = testResultList.stream()
                .collect(Collectors.groupingBy(result -> getDayHourMinSecondString(result.getTime())));
        FileUtils.appendToFile(SECOND_RESULT_FILE_PATH, "Second, Throughput, Mean, Median, P99, Min, Max\n");
        groups.keySet().stream()
                .sorted()
                .forEach(key -> {
                    List<TestResultAnalyzer.TestResult> subTestResultList = groups.get(key);
                    TestResultAnalyzer subTestResultAnalyzer = new TestResultAnalyzer(subTestResultList);
                    FileUtils.appendToFile(SECOND_RESULT_FILE_PATH, String.format("%s, %d, %d, %d, %d, %d, %d\n",
                            key,
                            subTestResultAnalyzer.getResultSize(),
                            subTestResultAnalyzer.getMeanResponseTime(),
                            subTestResultAnalyzer.getMedianResponseTime(),
                            subTestResultAnalyzer.getP99ResponseTime(),
                            subTestResultAnalyzer.getMinResponseTime(),
                            subTestResultAnalyzer.getMaxResponseTime()));
                });
    }

    public static Runnable buildRunnable(
            WriteNewLiftRideTester tester, int requestPerThread, CountDownLatch countDownLatch
    ) {
        return () -> {
            for (int numberOfRequests = 0; numberOfRequests < requestPerThread; numberOfRequests++) {
                Instant t1 = Instant.now();
                int responseCode = 201; // by default
                try {
                    tester.uploadLiftRideEventWithRandomValue();
                    tester.increaseNumberOfSuccessfulRequests();
                } catch (ApiException e) {
                    responseCode = e.getCode();
                    tester.increaseNumberOfUnsuccessfulRequests();
                    //System.out.println(gson.toJson(e));
                    //e.printStackTrace();
                } finally {
                    Instant t2 = Instant.now();
                    countDownLatch.countDown();

                    FileUtils.appendToFile(RESULT_FILE_PATH, String.format(
                            "%s, %s, %d, %d\n",
                            t1.toString(),
                            "POST",
                            t2.toEpochMilli() - t1.toEpochMilli(),
                            responseCode));
                }
            }
        };
    }

    public static String getDayHourMinSecondString(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(Instant.parse(time));
    }
}
