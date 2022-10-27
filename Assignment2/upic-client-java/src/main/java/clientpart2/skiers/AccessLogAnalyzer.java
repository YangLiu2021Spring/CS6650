package clientpart2.skiers;



import clientpart2.skiers.model.AccessLog;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AccessLogAnalyzer {
    private int totalRecords;
    private List<Long> sortedLatency;

    public AccessLogAnalyzer(Collection<AccessLog> accessLogs) {
        this.sortedLatency = accessLogs.stream()
                .filter(log -> log.getResponseCode() == 201)
                .map(log -> log.getLatency())
                .sorted()
                .collect(Collectors.toList());
        this.totalRecords = this.sortedLatency.size();
    }

    public AccessLogAnalyzer(List<AccessLog> accessLogs) {
        this.sortedLatency = accessLogs.stream()
                .filter(log -> log.getResponseCode() == 201)
                .map(log -> log.getLatency())
                .sorted()
                .collect(Collectors.toList());
        this.totalRecords = this.sortedLatency.size();
    }

    public long getTotalRecords() {
        return this.totalRecords;
    }

    public long getMeanResponseTime() {
        if (this.totalRecords == 0) {
            return 0;
        }

        long totalLatency = this.sortedLatency.stream()
                .reduce(0L, Long::sum);
        return totalLatency / this.totalRecords;
    }

    public long getMedianResponseTime() {
        if (this.totalRecords == 0) {
            return 0;
        }

        int medianIndex = this.totalRecords / 2;
        return this.sortedLatency.get(medianIndex);
    }

    public long getP99ResponseTime() {
        if (this.totalRecords == 0) {
            return 0;
        }

        int indexOfP99 = (int)Math.floor(this.totalRecords * 0.99d);
        return this.sortedLatency.get(indexOfP99);
    }

    public long getMinResponseTime() {
        if (this.totalRecords == 0) {
            return 0;
        }

        return this.sortedLatency.get(0);
    }

    public long getMaxResponseTime() {
        if (this.totalRecords == 0) {
            return 0;
        }

        int index = this.totalRecords - 1 < 0 ? 0 : this.totalRecords - 1;
        return this.sortedLatency.get(index);
    }
}

