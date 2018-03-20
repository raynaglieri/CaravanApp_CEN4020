package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/*
    JourneyLobby.java -- Initial theory and implementation via Pair Programming by Victor and Phalguna

    Each party has a lobby, a user may join the lobby and indicate readyness, as well as view other
    user's readyness.

    Once all users are ready, the leader may launch the journey.

    The users will stay in the JourneyLobby activity, the layout will change to support the journey Map?

 */

public class JourneyLobby extends AppCompatActivity {

    // Intent Bundle Capture
    public String username;
    public String partyKey;

    public TextView mPartyName;

    // Initialize Components
    public void init()
    {
        mPartyName = (TextView) findViewById(R.id.lobbyPartyName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_lobby);

        // get passed data
        Intent extras = getIntent();
        Bundle bundleExtras = extras.getExtras();

        // Do we need this?
        username = bundleExtras.get("user").toString();
        partyKey = bundleExtras.get("partyKey").toString();

        Log.i("JourneyLobby", username + " --> " + partyKey);
        init();

        // Set partyname here with supplied key
        

    }
}
