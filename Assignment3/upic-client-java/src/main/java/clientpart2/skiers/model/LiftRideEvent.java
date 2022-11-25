package clientpart2.skiers.model;

import io.swagger.client.model.LiftRide;

public class LiftRideEvent {
    private LiftRide liftRide;
    private int resortID;
    private String seasonID;
    private String dayID;
    private int skierID;
    private boolean isLastEvent = false;

    public LiftRideEvent(LiftRide liftRide, int resortID, String seasonID, String dayID, int skierID) {
        this.liftRide = liftRide;
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
    }

    public LiftRideEvent withIsLastEvent(boolean isLastEvent) {
        this.isLastEvent = isLastEvent;
        return this;
    }

    public LiftRide getLiftRide() {
        return liftRide;
    }

    public int getResortID() {
        return resortID;
    }

    public void setResortID(int resortID) {
        this.resortID = resortID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public int getSkierID() {
        return skierID;
    }

    public boolean isLastEvent() {
        return this.isLastEvent;
    }
}