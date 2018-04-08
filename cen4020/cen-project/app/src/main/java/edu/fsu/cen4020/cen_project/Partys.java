package edu.fsu.cen4020.cen_project;

import java.util.List;

/**
 * Created by victor on 2/18/18.
 * Pair programming done by: Victor and Raymond, Phalguna and Raymond
 * DB Scheme
 *
 */

public class Partys {

    /**
     * Used to manage Partys created by user
     *  -- Schema for Firebase DB
     *
     */

    public String partyKey; // assigned
    public String partyPassword;
    public String partyName;
    public String leader;
    public List<String> followers;
    public List<TravelRequests> requests;
    public double start_lat;
    public double start_long;
    public double end_lat;
    public double end_long;
    public boolean active;
    public boolean launched;

    // TODO: Support for Google Maps for LatLng Type
    //public LatLng startLocation;
    //public LatLng endLocation;

    public Partys() {
        // Default constructor required for calls to DataSnapshot.getValue(Partys.class)
    }

    public Partys(String groupID, String partyPassword, String groupName, String leader, List<String> followers, double start_lat, double start_long,
                  double end_lat, double end_long, boolean active, boolean launched, List<TravelRequests> requests) {
        this.partyKey = groupID;
        this.partyPassword = partyPassword;
        this.partyName = groupName;
        this.leader = leader;
        this.followers = followers;
        this.start_lat = start_lat;
        this.start_long = start_long;
        this.end_lat = end_lat;
        this.end_long = end_long;
        this.active = active;
        this.launched = launched;   // if journey has been launched or not
        this.requests = requests;
    }

    // Accessors go here


}
