package edu.fsu.cen4020.cen_project;

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
    public String partyName;
    public String leader;
    public String followers;
    public double start_lat;
    public double start_long;
    public double end_lat;
    public double end_long;

    // TODO: Support for Google Maps for LatLng Type
    //public LatLng startLocation;
    //public LatLng endLocation;

    public Partys() {
        // Default constructor required for calls to DataSnapshot.getValue(Partys.class)
    }

    public Partys(String groupID, String groupName, String leader, String followers, double start_lat, double start_long,
                  double end_lat, double end_long) {
        this.partyKey = groupID;
        this.partyName = groupName;
        this.leader = leader;
        this.followers = followers;
        this.start_lat = start_lat;
        this.start_long = start_long;
        this.end_lat = end_lat;
        this.end_long = end_long;
    }

    // Accessors go here


}
