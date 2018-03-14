package edu.fsu.cen4020.cen_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// TODO: Integrate monitoring

public class JourneyActivity extends AppCompatActivity {

    public TextView mJourneyPartyName;
    public TextView mJourneyPartyID;
    public Spinner mPartySelectSpinner;
    public ListView mPartyFollowers;
    public List<String> activeParties;
    public String[] partyFollowers = {};
    public FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public void init()
    {
        activeParties = new ArrayList<>();
        mPartySelectSpinner = (Spinner) findViewById(R.id.partySpinner);
        mPartyFollowers = (ListView) findViewById(R.id.partyFollowers);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //mJourneyPartyName = (TextView) findViewById(R.id.JourneyPartyName);
        //mJourneyPartyID = (TextView) findViewById(R.id.JourneyPartyID);
    }

    public void getUserParties()
    {
        String userEmail = mAuth.getCurrentUser().getEmail();

        Log.i("Test", "Username: " + userEmail);

        String[] full = (userEmail.split("@"));
        String username = full[0];

        mDatabase.child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("Snapshot", snapshot.getValue().toString());

                if (snapshot.hasChild("partyKeys"))
                {
                    // Populate the party keys
                    Log.i("Test", "Has Keys");

                    for (DataSnapshot ds : snapshot.child("partyKeys").getChildren())
                    {
                        activeParties.add(ds.getValue().toString());
                    }
                }
                else
                {
                    // Populate the party keys
                    Log.i("Test", "No Keys");
                    activeParties.add("None");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, activeParties);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        mPartySelectSpinner.setAdapter(spinnerArrayAdapter);
        mPartySelectSpinner.setPrompt("Select party...");       // Create hint????
    }

    public void getPartyFollowers()
    {
        mDatabase.child("partys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
/*
                //.child(mAuth.getCurrentUser().getUid()).child("partyKeys")

                Log.i("JourneyActivity", "Getting party followers...");

                int count = 0;
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Log.i("JourneyActivity", "Snapshot #" + count);
                    if (ds.child("leader").getValue().toString().equalsIgnoreCase(mAuth.getCurrentUser().getEmail()))
                    {
                        Log.i("JourneyActivity", "Found Leader");
                        activeParties.add(ds.child("partyKey").getValue().toString());
                    }
                    count++;
                }

                //System.out.println(snapshot.getValue());
                Log.i("JourneyActivity", "Party Keys " + snapshot.getValue().toString());
                //ArrayList followers = new ArrayList<String>();
                // Result will be holded Here
                //for (DataSnapshot dsp : snapshot.getChildren()) {
                //    followers.add(String.valueOf(dsp.getValue())); //add result into array list
                //}*/
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // partyFollowers =

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, partyFollowers);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        mPartyFollowers.setAdapter(spinnerArrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        // Initialize Activity Components
        Log.i("Test", "Test1");
        init();
        getUserParties();
        getPartyFollowers();
    }
}
