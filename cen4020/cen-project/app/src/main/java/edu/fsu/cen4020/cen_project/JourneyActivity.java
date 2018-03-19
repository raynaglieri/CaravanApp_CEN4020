package edu.fsu.cen4020.cen_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    /*
        UI Buttons
     */
    public Button mQuickInviteButton, mPartySettingsButton, mLaunchJourneyButton;

    public TextView mTextPartyName;
    public TextView mJourneyPartyID;

    public Spinner mPartySelectSpinner;
    public ListView mPartyFollowers;
    public List<String> activeParties;
    public List<String> partyFollowers;
    public FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public String selectedPartyKey; // currently selected party key

    public String partyName;

    // Change: Init components, Init listeners?
    public void init()
    {
        activeParties = new ArrayList<>();
        partyFollowers = new ArrayList<>();

        mTextPartyName = (TextView) findViewById(R.id.textPartyName);
        mPartySelectSpinner = (Spinner) findViewById(R.id.partySpinner);
        mPartyFollowers = (ListView) findViewById(R.id.listPartyFollowers);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        /*
            Invite handling initialization
         */

        /*
            UI Buttons
         */

        mQuickInviteButton = (Button) findViewById(R.id.quickInviteButton);
        mPartySettingsButton = (Button) findViewById(R.id.partySettingsButton);
        mLaunchJourneyButton = (Button) findViewById(R.id.launchJourneyButton);

        /*
            UI Text Fields
         */
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
                if (snapshot.hasChild("partyKeys"))
                {
                    // Populate the party keys
                    Log.i("Test", "Has Keys");

                    // Get the party keys for the user
                    for (DataSnapshot ds : snapshot.child("partyKeys").getChildren()) {
                        activeParties.add(ds.getValue().toString());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(JourneyActivity.this,   android.R.layout.simple_spinner_item, activeParties);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    mPartySelectSpinner.setAdapter(spinnerArrayAdapter);

                    // Spinner should be successfully updated here
                    Log.i("getUserParties", "Updated Spinner");
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
    }

    /*
        Loads data from party selected from Spinner
            - Followers
            - Party Name
            - Etc.
        Sets
            - selectedPartyKey (for use with Invites)
     */
    public void getSelectedPartyData(final int position)
    {
        // Initially clear the partyFollowers list if it contains followers from a previous selection
        if (partyFollowers.size() > 0) {
            partyFollowers.clear();
        }

        // Access the Firebase DB "Partys" table
        mDatabase.child("partys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String partyKey = activeParties.get(position);

                // Iterate through the followers that belong to the selected partyKey
                // and add them to the partyFollowers list
                for (DataSnapshot ds : snapshot.child(partyKey).child("followers").getChildren())
                {
                    String user = ds.getValue().toString();
                    Log.i("Follower", "Key: " + partyKey + " User: " + user);
                    partyFollowers.add(user);
                }

                /*
                    Set data here as needed in interface
                */
                // Party Name -- TODO: Place this differently in layout
                partyName = snapshot.child(partyKey).child("partyName").getValue().toString();
                mTextPartyName.setText("Selected Party: " + partyName);
                selectedPartyKey = partyKey;

                // ListView -- Set Followers in ListView via ArrayAdapter
                ArrayAdapter<String> listViewArrayAdapter = new ArrayAdapter<String>(JourneyActivity.this, android.R.layout.simple_list_item_1, partyFollowers);
                //spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                mPartyFollowers.setAdapter(listViewArrayAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        // Initialize Activity Components
        Log.i("Test", "Test1");
        init();
        getUserParties();

        /*
            Listeners
         */

        // Spinner Item Click
        mPartySelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Toast.makeText(getApplicationContext(), "Spinner selected with pos: " + position,
                        Toast.LENGTH_LONG).show();
                getSelectedPartyData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        // Invites -- must be handled with a separate table (Invites.java)
        // TODO: Make Invites.java to handle invites via Firebase
        // TODO: Create notification manager based on invites in Table

        mQuickInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userInput = new EditText(v.getContext());
                AlertDialog inviteDialog = new AlertDialog.Builder(JourneyActivity.this).create();
                inviteDialog.setTitle("Invite Follower");
                inviteDialog.setMessage("Please enter an existing user's email address:");
                inviteDialog.setView(userInput);
                inviteDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                inviteDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SEND INVITE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //userEmail string (receiver)
                                final String userEmail = userInput.getText().toString();

                                if (userEmail.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "No email address entered. Invite not sent.",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    // Verify the user exists via Firebase
                                    mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.child(userEmail).exists()) {
                                                // User exists, invite is good
                                                Toast.makeText(getApplicationContext(), "User " + userEmail + " found. Attempting to send invite...",
                                                        Toast.LENGTH_SHORT).show();

                                                // Create and send the invite
                                                Invites invite = new Invites(selectedPartyKey, mAuth.getCurrentUser().getEmail(), userEmail);
                                                sendInvite(invite);
                                            } else {
                                                // User does not exist
                                                Toast.makeText(getApplicationContext(), "User not found. Invite not sent.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                                dialog.dismiss(); // close invite dialog
                            }
                        });
                inviteDialog.show();
            }
        }) ;

        // Pair Programming: Victor and Ray
        mLaunchJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLaunchConditions())
                {
                    // Update Party Key as Launched
                    launchPartyKey(selectedPartyKey);

                    // Launch the MapsActivity
                    // Pass Party Information via Bundle here?
                    // On launch, we need a listener that launches the MapsActivty intent on the devices of the other users...
                    Intent intent = new Intent(JourneyActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(JourneyActivity.this, "All users must be ready in order to launch.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }) ;

    }

    // Pair Programming by: Victor and Roberto
    // "Launches" the party key; i.e. denotes that the journey has begun!
    public void launchPartyKey(String key)
    {
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference().child("partys");
        dbRef.child(selectedPartyKey).child("launched").setValue(true);
    }

    // Pair Programming: Ray and Phalguna
    // Additional Guidance on APIs by: Victor
    public boolean checkLaunchConditions()
    {
        // TODO: (MAYBE) WAY TRACK CURRENTLY LOGGED IN USER'S VIA FIREBASE?
        // This requires a seperate table...?

        // All user's must be "ready" in launch screen.
        // This means that all followers must be active in the lobby

        // Get data snapshot of followers
        // Check if each follower has 'active_party' = 'selectedPartyKey'
        // AND if user is signed in

        // If so, launch

        final List<String> followers = new ArrayList<>();
        boolean result = true;

        mDatabase.child("partys").child(selectedPartyKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.child("followers").getChildren())
                {
                    Log.i("JourneyActiviy", "Launch Conditions, User: " + ds.getValue().toString());
                    followers.add(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Needs Reformulating
        // Pair Programming by: Phalguna and Roberto
        /*
        for(int i = 0; i < followers.size(); i++)
        {
            String value = mDatabase.child("users").child(followers.get(i)).child("active_party").toString();
            Log.i("JourneyActivity", value);

            if (!value.equals(selectedPartyKey))
            {
                result = false;
            }
        }
        */

        return result;
    }

    // Sends the invite to receiver
    // Pair Programming by: Victor and Phalguna
    public void sendInvite(Invites invite)
    {
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        dbRef.child(invite.getReceiver()).child("inbox").push().setValue(invite);

        Toast.makeText(getApplicationContext(), "Invite sent.",
                Toast.LENGTH_LONG).show();

    }
}
