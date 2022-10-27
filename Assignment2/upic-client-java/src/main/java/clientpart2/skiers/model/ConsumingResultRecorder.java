package clientpart2.skiers.model;

public class ConsumingResultRecorder {
    private long startTime;
    private long endTime;

    private int numberOfSuccessfulRequests = 0;
    private int numberOfUnsuccessfulRequests = 0;

    public synchronized int getNumberOfSuccessfulRequests() {
        return numberOfSuccessfulRequests;
    }

    public synchronized void increaseNumberOfSuccessfulRequests() {
        this.numberOfSuccessfulRequests++;
    }

    public synchronized int getNumberOfUnsuccessfulRequests() {
        return numberOfUnsuccessfulRequests;
    }

    public synchronized void increaseNumberOfUnsuccessfulRequests() {
        this.numberOfUnsuccessfulRequests++;
    }

    public synchronized int getNumberOfTotalRequests() {
        return this.numberOfSuccessfulRequests + this.numberOfUnsuccessfulRequests;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime = System.currentTimeMillis();
    }

    public long getTotalRunTimeMillis() {
        return this.endTime - this.startTime;
    }

    public double getTotalRunTimeSeconds() {
        return (double)this.getTotalRunTimeMillis() / 1000d;
    }
}
