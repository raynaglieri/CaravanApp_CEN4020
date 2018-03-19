package edu.fsu.cen4020.cen_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
    JourneyLobby.java -- Initial theory and implementation via Pair Programming by Victor and Phalguna

    Each party has a lobby, a user may join the lobby and indicate readyness, as well as view other
    user's readyness.

    Once all users are ready, the leader may launch the journey.

    The users will stay in the JourneyLobby activity, the layout will change to support the journey Map?

 */

public class JourneyLobby extends AppCompatActivity {

    // Initialize Components
    public void init()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_lobby);
        init();
    }
}
