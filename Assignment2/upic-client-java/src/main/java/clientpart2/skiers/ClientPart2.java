package clientpart2.skiers;

import clientpart2.skiers.model.AccessLog;
import clientpart2.skiers.model.ConsumingResultRecorder;
import clientpart2.util.FileUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class ClientPart2 extends ThroughputAppCore {
    private static final String GROUPED_LATENCY_FILE = String.format(
            "./test_result_%s_group_by_second.csv", System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        // prepare
        AppArg appArg = parseAppArg(args).withName("CS6650 Fall 2022 Assignment 1 - Part 2").outputLog();
        Queue<AccessLog> accessLogQueue = new ConcurrentLinkedDeque<>();
        ConsumingResultRecorder consumingResultRecorder = new ConsumingResultRecorder();
        LiftRideEventConsumer liftRideEventConsumer = new LiftRideEventConsumer(
                consumingResultRecorder, new SkiersApiFactory(appArg.getApiBasePath()), accessLogQueue
        );

        // run
        runTest(appArg, consumingResultRecorder, liftRideEventConsumer);

        // analysis latency
        AccessLogAnalyzer accessLogAnalyzer = new AccessLogAnalyzer(accessLogQueue);

        System.out.println("mean response time (millisecs): " + accessLogAnalyzer.getMeanResponseTime());
        System.out.println("median response time (millisecs): " + accessLogAnalyzer.getMedianResponseTime());
        System.out.println("throughput = total number of requests/wall time (requests/second): "
                + (consumingResultRecorder.getNumberOfTotalRequests() / consumingResultRecorder.getTotalRunTimeSeconds()));
        System.out.println("p99 (99th percentile) response time: " + accessLogAnalyzer.getP99ResponseTime());
        System.out.println("min response time (millisecs): " + accessLogAnalyzer.getMinResponseTime());
        System.out.println("max response time (millisecs): " + accessLogAnalyzer.getMaxResponseTime());

        // group by time (second)
        FileUtils.createFileIfNotExist(GROUPED_LATENCY_FILE);
        Map<String, List<AccessLog>> groups = accessLogQueue.stream()
                .collect(Collectors.groupingBy(result -> getDayHourMinSecondString(result.getTime())));
        FileUtils.appendToFile(GROUPED_LATENCY_FILE, "Second, Throughput, Mean, Median, P99, Min, Max\n");
        groups.keySet().stream()
                .sorted()
                .forEach(key -> {
                    List<AccessLog> subAccessLogList = groups.get(key);
                    AccessLogAnalyzer subAccessLogAnalyzer = new AccessLogAnalyzer(subAccessLogList);
                    FileUtils.appendToFile(GROUPED_LATENCY_FILE, String.format("%s, %d, %d, %d, %d, %d, %d\n",
                            key,
                            subAccessLogAnalyzer.getTotalRecords(),
                            subAccessLogAnalyzer.getMeanResponseTime(),
                            subAccessLogAnalyzer.getMedianResponseTime(),
                            subAccessLogAnalyzer.getP99ResponseTime(),
                            subAccessLogAnalyzer.getMinResponseTime(),
                            subAccessLogAnalyzer.getMaxResponseTime()));
                });;

        exit(0);
    }

    public static String getDayHourMinSecondString(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(Instant.parse(time));
    }
}
