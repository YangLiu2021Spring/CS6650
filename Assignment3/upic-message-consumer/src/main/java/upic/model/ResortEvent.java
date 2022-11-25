package upic.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ResortEvent {
    private static final Gson GSON = new GsonBuilder().create();

    private int resortID;

    private String seasonID;

    private String dayID;

    private int skierID;


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

    public void setDayID(String dayID) {
        this.dayID = dayID;
    }

    public int getSkierID() {
        return skierID;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public static ResortEvent fromMessage(String resortEventMessage) {
        return GSON.fromJson(resortEventMessage, ResortEvent.class);
    }
}
