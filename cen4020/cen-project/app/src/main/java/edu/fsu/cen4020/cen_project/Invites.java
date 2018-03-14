package edu.fsu.cen4020.cen_project;

/**
 * Created by Victor and Ray on 3/14/18.
 * Invite scheme for Firebase Invite Handling
 */

public class Invites {
    public String partyKey;
    public String receiver;     // Who is being invited (follower)
    public String sender;       // Who is sending the invite (leader)

    public Invites() {

    }

    public Invites(String partyKey, String sender, String receiver) {
        this.partyKey = partyKey;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getReceiver()
    {
        return this.receiver;
    }

    // Accessors
}
