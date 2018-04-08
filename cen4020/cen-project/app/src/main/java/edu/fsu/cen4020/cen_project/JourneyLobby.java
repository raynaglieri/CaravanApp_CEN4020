package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
    JourneyLobby.java -- Initial theory and implementation via Pair Programming by Victor and Phalguna (initial), Victor and Raymond (additions)

    Each party has a lobby, a user may join the lobby and indicate readyness, as well as view other
    user's readyness.

    Once all users are ready, the leader may launch the journey.

    The users will stay in the JourneyLobby activity, the layout will change to support the journey Map?

 */

public class JourneyLobby extends AppCompatActivity {

    // Intent Bundle Capture
    public String username;
    public String partyKey;

    public Partys party = new Partys();
    public DatabaseReference mDatabase;

    public TextView mPartyName, mJourneyStatus;
    public Button mGoButton;
    public ImageView mLaunchImage;

    public ListView mFollowerList;

    // Initialize Components
    public void init()
    {
        mPartyName = (TextView) findViewById(R.id.lobbyPartyName);
        mFollowerList = (ListView) findViewById(R.id.followerList);
        mJourneyStatus = (TextView) findViewById(R.id.journeyStatus);
        mGoButton = (Button) findViewById(R.id.goButton);
        mLaunchImage = (ImageView) findViewById(R.id.launchImage);
        mGoButton.setEnabled(false);
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
        mPartyName.setText("Lobby Key: " + partyKey);

        // Get our Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("partys").child(partyKey);

        // Load party Data
        // Pair programming by Victor and Raymond
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                party = snapshot.getValue(Partys.class);

                ArrayAdapter<String> listViewArrayAdapter = new ArrayAdapter<String>(JourneyLobby.this, android.R.layout.simple_list_item_1, party.followers);
                mFollowerList.setAdapter(listViewArrayAdapter);

                if (party.launched)
                {
                    mJourneyStatus.setTextColor(Color.GREEN);
                    mJourneyStatus.setText("\nThis journey has LAUNCHED!");
                    mLaunchImage.setImageResource(R.drawable.launched);
                    mGoButton.setEnabled(true);
                }
                else
                {
                    mJourneyStatus.setTextColor(Color.RED);
                    mJourneyStatus.setText("\nThis journey has NOT LAUNCHED!\nParty leader (" + party.leader + ") has not launched yet.");
                    mLaunchImage.setImageResource(R.drawable.notlaunched);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DB Error", "DB ERROR");
            }
        });

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch the MapsActivity for this user
                Intent intent = new Intent(JourneyLobby.this, MapsActivity.class);

                // Pass party information into Intent w/ Bundle here
                Bundle bundle = new Bundle();
                bundle.putString("partyKey", partyKey);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }
}
