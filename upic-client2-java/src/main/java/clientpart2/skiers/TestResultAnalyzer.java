package clientpart2.skiers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestResultAnalyzer {
    private int resultSize;
    private List<Long> sortedLatency;

    public TestResultAnalyzer(List<TestResult> testResultList) {
        this.sortedLatency = testResultList.stream()
            .filter(result -> result.responseCode == 201)
            .map(result -> result.getLatency())
            .sorted()
            .collect(Collectors.toList());
        this.resultSize = this.sortedLatency.size();
    }

    public long getResultSize() {
        return this.resultSize;
    }

    public long getMeanResponseTime() {
        if (this.resultSize == 0) {
            return 0;
        }

        long totalLatency = this.sortedLatency.stream()
            .reduce(0L, Long::sum);
        return totalLatency / this.resultSize;
    }

    public long getMedianResponseTime() {
        if (this.resultSize == 0) {
            return 0;
        }

        int medianIndex = this.resultSize / 2;
        return this.sortedLatency.get(medianIndex);
    }

    public long getP99ResponseTime() {
        if (this.resultSize == 0) {
            return 0;
        }

        int indexOfP99 = (int)Math.floor(this.resultSize * 0.99d);
        return this.sortedLatency.get(indexOfP99);
    }

    public long getMinResponseTime() {
        if (this.resultSize == 0) {
            return 0;
        }

        return this.sortedLatency.get(0);
    }

    public long getMaxResponseTime() {
        if (this.resultSize == 0) {
            return 0;
        }

        int index = this.resultSize - 1 < 0 ? 0 : this.resultSize - 1;
        return this.sortedLatency.get(index);
    }

    public static List<TestResult> loadTestResult(String filePath) {
        List<TestResult> testResultList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                testResultList.add(new TestResult( line.split(", ")));

            }
            return testResultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class TestResult {
        private String time;
        private String requestType;
        private long latency;
        private int responseCode;

        public TestResult(String[] results) {
            this.time = results[0];
            this.requestType = results[1];
            this.latency = Long.parseLong(results[2]);
            this.responseCode = Integer.parseInt(results[3]);
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRequestType() {
            return requestType;
        }

        public void setRequestType(String requestType) {
            this.requestType = requestType;
        }

        public long getLatency() {
            return latency;
        }

        public void setLatency(long latency) {
            this.latency = latency;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }
    }
}
