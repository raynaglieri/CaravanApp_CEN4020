package edu.fsu.cen4020.cen_project;

import java.util.List;

/**
 * Created by victor on 2/18/18.
 * Pair programming done by: Victor and Raymond, Phalguna and Raymond
 *
 * DB Scheme
 */

// TODO: create user entry when user is authenticated, connect with groups

public class Users {

    public String email;
    public String password;
    public int journeyCount;
    public List<String> partyKeys;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String email, String password, int journeyCount, List<String> partyKeys) {
        this.email = email;
        this.password = password;
        this.journeyCount = journeyCount;
        this.partyKeys = partyKeys;
    }

}
