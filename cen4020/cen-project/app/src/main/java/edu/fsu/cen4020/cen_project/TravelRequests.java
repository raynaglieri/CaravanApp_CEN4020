package edu.fsu.cen4020.cen_project;

/**
 * Created by Victor Cordiano, Phalguna
 * Pair programming by Victor, Phalguna
 */

public class TravelRequests {
    public String sentBy;
    public Double latitude;
    public Double longitude;
    public String type;

    public TravelRequests() {

    }

    public TravelRequests(String sentBy, Double latitude, Double longitude, String type) {
        this.sentBy = sentBy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }
}
