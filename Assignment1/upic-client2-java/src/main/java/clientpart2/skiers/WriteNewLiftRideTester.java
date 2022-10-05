package clientpart2.skiers;

import clientpart2.util.RandomUtils;
import io.swagger.client.ApiException;
import io.swagger.client.model.LiftRide;

public class WriteNewLiftRideTester {
    private static final int INITIAL_RETRIED_TIMES = 0;
    private static final int MAX_RETRY_TIMES = 5;

    private int numberOfSuccessfulRequests = 0;
    private int numberOfUnsuccessfulRequests = 0;

    public synchronized int getNumberOfTotalRequests() {
        return this.getNumberOfSuccessfulRequests() + this.getNumberOfUnsuccessfulRequests();
    }

    public synchronized int getNumberOfSuccessfulRequests() {
        return numberOfSuccessfulRequests;
    }

    public synchronized void setNumberOfSuccessfulRequests(int numberOfSuccessfulRequests) {
        this.numberOfSuccessfulRequests = numberOfSuccessfulRequests;
    }

    public synchronized void increaseNumberOfSuccessfulRequests() {
        this.numberOfSuccessfulRequests++;
    }

    public synchronized int getNumberOfUnsuccessfulRequests() {
        return numberOfUnsuccessfulRequests;
    }

    public synchronized void setNumberOfUnsuccessfulRequests(int numberOfUnsuccessfulRequests) {
        this.numberOfUnsuccessfulRequests = numberOfUnsuccessfulRequests;
    }

    public synchronized void increaseNumberOfUnsuccessfulRequests() {
        this.numberOfUnsuccessfulRequests++;
    }

    public void uploadLiftRideEventWithRandomValue() throws ApiException {
        LiftRide liftRide = new LiftRide();
        liftRide.setLiftID(RandomUtils.newPositiveInt(40));
        liftRide.setTime(RandomUtils.newPositiveInt(360));
        int resortID = RandomUtils.newPositiveInt(10);
        String seasonID = "2022";
        String dayID = "1";
        int skierID = RandomUtils.newPositiveInt(100000);

        this.uploadLiftRideEventWithRetry(
            liftRide, resortID, seasonID, dayID, skierID, INITIAL_RETRIED_TIMES, MAX_RETRY_TIMES
        );
    }

    private void uploadLiftRideEventWithRetry(
        LiftRide liftRide,
        Integer resortID,
        String seasonID,
        String dayID,
        Integer skierID,
        int retriedTimes,
        int maxRetryTimes
    ) throws ApiException {
        try {
            SkiersApiFactory.newSkiersApi().writeNewLiftRide(liftRide, resortID, seasonID, dayID, skierID);
        } catch (ApiException e) {
            retriedTimes ++;
            // {"code":0,"detailMessage":"java.net.ConnectException: Failed to connect to..."}
            if ((e.getCode() == 0 || e.getCode() >= 400 && e.getCode() < 600) && retriedTimes < maxRetryTimes) {
                uploadLiftRideEventWithRetry(liftRide, resortID, seasonID, dayID, skierID, retriedTimes, maxRetryTimes);
            }
            throw e;
        }
    }
}
