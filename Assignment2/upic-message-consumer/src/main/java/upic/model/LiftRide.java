package upic.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A model class represents the entity of LiftRide
 */
public class LiftRide {
    private static final Gson GSON = new GsonBuilder().create();

    private Integer time;

    private Integer liftID;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLiftID() {
        return liftID;
    }

    public void setLiftID(Integer liftID) {
        this.liftID = liftID;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    /**
     * Create a new LiftRide instance from a lift ride message
     * @param liftRideMessage this lift ride message
     */
    public static LiftRide fromMessage(String liftRideMessage) {
        return GSON.fromJson(liftRideMessage, LiftRide.class);
    }
}
