package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Initial Main Activity Skeleton
    public static FirebaseUser currentUser = null;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference mDatabase;
    public DatabaseReference dbRef;

    // Travel Selection Buttons
    public Button mCreateButton, mJoinButton, mVerifyButton;
    public Button mExistUser, mRegisterUser, mSkipButton;
    public EditText mPartyName, mPartyPassword;
    public Spinner mTravelSpinner;
    public Button mLoginButton;
    public Button mLogoutButton;
    public Button mMyPartiesButton;
    public Button mMyLobbiesButton;
    public boolean loggedIN = false;

    public int PLACE_PICKER_REQUEST;

    public LatLng startLocation = null;
    public LatLng stopLocation = null;

    public TextView mViewPartyName, mViewPartyPassword, mViewTravelType, mViewStartLocation, mViewDestination;
    public TextView mViewWelcomeUser;

    public EditText mTextBoxID, mTextBoxPass;

    /*
        Google Maps API Location Selection Buttons (Create Party)
     */

    public Button mStartLocationButton, mStopLocationButton;

    /*
        Followed Lobbies Components

     */
    public ListView mExpandableListView;
    public Button mEnterButton;
    public String selected_lobby;

    public void init()
    {
        // Get reference of Firebase Database for reads/writes
        selected_lobby = "";

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("partys");

        mViewWelcomeUser = (TextView) findViewById(R.id.welcome_User);
        mCreateButton = (Button) findViewById(R.id.create_button);
        mJoinButton = (Button) findViewById(R.id.join_button);

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mMyPartiesButton = (Button) findViewById(R.id.myPartiesButton);
        mMyLobbiesButton = (Button) findViewById(R.id.myLobbiesButton);

        mMyLobbiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.followed_lobbies);

                mExpandableListView = (ListView) findViewById(R.id.expandableListView);
                mEnterButton = (Button) findViewById(R.id.enterButton);

                // Access the Firebase DB "Partys" table
                mDatabase.child("partys").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // ListView -- Set Followers in ListView via ArrayAdapter
                        List<String> lobbyKeys = new ArrayList<>();

                        String user = firebaseAuth.getCurrentUser().getEmail();

                        Log.i("MainActivity", "HERE");
                        Log.i("MainActivity", snapshot.getValue().toString());

                        // Iterate through the followers that belong to the selected partyKey
                        // and add them to the partyFollowers list
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            for (DataSnapshot dsf : ds.child("followers").getChildren())
                            {
                                Log.i("MainActivity", dsf.getValue().toString());
                                if (dsf.getValue().toString().equals(user))
                                {
                                    String name_key = ds.child("partyName").getValue().toString() + "\n" + ds.child("partyKey").getValue().toString();
                                    lobbyKeys.add(name_key);
                                }
                            }
                        }
                        ArrayAdapter<String> listViewArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, lobbyKeys);
                        mExpandableListView.setAdapter(listViewArrayAdapter);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                mExpandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id)
                    {
                        Toast.makeText(getApplicationContext(), "Short click",
                                Toast.LENGTH_LONG).show();
                    }
                });

                // Pair Programming by: Victor and Ray
                mExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
                    {
                        Toast.makeText(getApplicationContext(), "Lobby selected.",
                                Toast.LENGTH_LONG).show();

                        String selection = arg0.getItemAtPosition(pos).toString();

                        String[] full = (selection.split("\n"));
                        String key = full[1];

                        selected_lobby = key;

                        // Reset background color
                        for (int i = 0; i < arg0.getCount(); i++)
                        {
                            arg0.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                        arg0.getChildAt(pos).setBackgroundColor(Color.rgb(176, 242, 125));

                        return true;
                    }
                });

                // Pair Programming by Victor and Phalguna
                mEnterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!selected_lobby.isEmpty())
                        {
                            Log.i("MainActivity", "Is Selected");

                            // Update Selected Lobby to User's 'active_lobby'
                            String username = getUsernameFromEmail(firebaseAuth.getCurrentUser().getEmail());
                            DatabaseReference dbRef;
                            dbRef = FirebaseDatabase.getInstance().getReference().child("users");
                            dbRef.child(username).child("active_party").setValue(selected_lobby);

                            // TODO: Launch lobby activity here with corresponding party key
                            Intent intent = new Intent(MainActivity.this, JourneyLobby.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("partyKey", selected_lobby);
                            bundle.putString("user", username);
                            intent.putExtras(bundle);

                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please select a lobby (long click).",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // Pair Programming: Victor and Roberto
        // Maintenence for signing user out of application
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                currentUser = null;
                // call on create?
                setContentView(R.layout.welcome_screen);
            }
        }) ;

        mMyPartiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        }) ;

        String welcomeMsg = "\nWelcome, " + currentUser.getEmail() + "!";
        mViewWelcomeUser.setText(welcomeMsg);

        // Launchable intents go here (button launches)
        // Launches FragmentList intent to invite participants to join party
        // Initial Pair Programming by: Phalguna and Raymond
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.join_party);
                // set view items
                mTextBoxID = (EditText) findViewById(R.id.textBoxID);
                mTextBoxPass = (EditText) findViewById(R.id.textBoxPass);
                mVerifyButton = (Button) findViewById(R.id.verify_button);
                Log.i("MainActivity:", "Join Button");

                // Verify Button
                // Pair Programming: Victor and Phalguna
                mVerifyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String joinPartyKey = mTextBoxID.getText().toString();
                        final String joinPass = mTextBoxPass.getText().toString();
                        Log.i("MainActivity:", "Verify Button");
                        Log.i("MainActivity:", "ID: " + joinPartyKey + " PASS: " + joinPass);

                        dbRef = FirebaseDatabase.getInstance().getReference();
                        dbRef.child("partys").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.child(joinPartyKey).exists()) {
                                    if (snapshot.child(joinPartyKey).child("partyPassword").getValue().toString().equals(joinPass))
                                    {
                                        List<String> newFollowers = new ArrayList<>();
                                        // Add user as follower to party
                                        for (DataSnapshot ds : snapshot.child(joinPartyKey).child("followers").getChildren())
                                        {
                                            newFollowers.add(ds.getValue().toString());
                                        }

                                        if (newFollowers.contains(firebaseAuth.getCurrentUser().getEmail()))
                                        {
                                            Toast.makeText(getApplicationContext(), "You are already a member of this party.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            newFollowers.add(firebaseAuth.getCurrentUser().getEmail());

                                            dbRef.child("partys").child(joinPartyKey).child("followers").setValue(newFollowers);
                                            Toast.makeText(getApplicationContext(), "You have joined the party successfully.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Invalid party password.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    // Party does not exist
                                    Toast.makeText(getApplicationContext(), "Party key not found.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                }) ;

            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                //startActivity(intent);
                setContentView(R.layout.create_party);
                // set view items
                mPartyName = (EditText) findViewById(R.id.partyName);
                mPartyPassword = (EditText) findViewById(R.id.partyPassword);
                mTravelSpinner = (Spinner) findViewById(R.id.travelSpinner);

                mStartLocationButton = (Button) findViewById(R.id.startLocationButton);
                mStopLocationButton = (Button) findViewById(R.id.stopLocationButton);

                mViewPartyName = (TextView) findViewById(R.id.viewPartyName);
                mViewPartyPassword = (TextView) findViewById(R.id.viewPartyPassword);
                mViewTravelType = (TextView) findViewById(R.id.viewTravelType);
                mViewStartLocation = (TextView) findViewById(R.id.viewStartLocation);
                mViewDestination = (TextView) findViewById(R.id.viewDestination);

                /*
                    Listeners
                 */

                mStartLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Implement Google Maps Place Picker API
                        Toast.makeText(MainActivity.this, "Start Location", Toast.LENGTH_SHORT).show();
                        try {

                            PLACE_PICKER_REQUEST = 1;

                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            // Set LatLng Bounds here?

                            startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesNotAvailableException ex1) {
                            Toast.makeText(MainActivity.this, "Please update your Google Play Services", Toast.LENGTH_SHORT).show();
                        } catch (GooglePlayServicesRepairableException rp1) {
                            Toast.makeText(MainActivity.this, "Please repair your Google Play Services", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) ;

                mStopLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Implement Google Maps Place Picker API
                        Toast.makeText(MainActivity.this, "Stop Location", Toast.LENGTH_SHORT).show();
                        try {

                            PLACE_PICKER_REQUEST = 2;

                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            // Set LatLng Bounds here?

                            startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesNotAvailableException ex1) {
                            Toast.makeText(MainActivity.this, "Please update your Google Play Services", Toast.LENGTH_SHORT).show();
                        } catch (GooglePlayServicesRepairableException rp1) {
                            Toast.makeText(MainActivity.this, "Please repair your Google Play Services", Toast.LENGTH_SHORT).show();
                        }


                    }
                }) ;

                Log.i("MainActivity:", "Join Button");
            }
        }) ;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, MainActivity.this);
                // String toastMsg = String.format("Place: %s", place.getName());
                // Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_LONG).show();
                if (PLACE_PICKER_REQUEST == 1)
                {
                    startLocation = place.getLatLng();
                    Toast.makeText(getApplicationContext(), "Start Location Updated",
                            Toast.LENGTH_LONG).show();
                }
                if (PLACE_PICKER_REQUEST == 2)
                {
                    stopLocation = place.getLatLng();
                    Toast.makeText(getApplicationContext(), "Stop Location Updated",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Helper function to more effectively parse email
    // Pair Programming by Phalguna and Roberto
    public String getUsernameFromEmail(String email)
    {
        // Get Firebase username for insert
        String[] full = (email.split("@"));
        String username = full[0];
        return username;
    }

    // Do verification of Party ID and Password
    // Pait Programming by Raymond and Roberto
    public void navigateVerify(View view) {
        setContentView(R.layout.activity_main);
        if (mTextBoxID.getText().toString().isEmpty())
        {

        }
        if (mTextBoxPass.getText().toString().isEmpty())
        {

        }
        init();
        Log.i("MainActivity", "Verify button clicked.");
    }

    // Do verification of Party ID and Password
    public void navigateMain(View view) {
        setContentView(R.layout.activity_main);
        init();
        Log.i("MainActivity", "Main button clicked.");
    }

    // Do verification of Party ID and Password
    public void goLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // SKIP BUTTON
    // For testing purposes -- created by victor and phalguna
    public void goSkip(View view) {
        setContentView(R.layout.activity_main);
        init();
    }

    /*
        Create Party functionality

        This is where a user can create a Party
            -- Party ID, Password, Travel Type, Start Location, Destination, Make Active Checkbox
            -- Followers are to be invited in the corresponding screen

        TODO: Potentially make this a seperate class?
        TODO: Integrate Geolocation and Google Maps
        TODO: Google Maps -- Select Start Location Button, Select Destination Button
        TODO: Make Active Checkbox on creation screen to denote if they want the party to be active
            -- IF A PARTY IS MADE 'ACTIVE' ALL OTHER PARTIES ARE DEACTIVATED FOR THE USER
            -- ONLY ONE PARTY MAY BE ACTIVE AT A TIME
     */
    public void createParty(View view) {
        //Maybe set content view to 'creating...'?
        Log.i("MainActivity", "Create Party button clicked.");

        boolean check = true;
        mViewPartyName.setTextColor(Color.BLACK);
        mViewPartyPassword.setTextColor(Color.BLACK);
        mViewTravelType.setTextColor(Color.BLACK);

        // Verify that all required fields are filled
        if (mPartyName.getText().toString().isEmpty())
        {
            Log.i("MainActivity", "Party Name empty.");
            mViewPartyName.setTextColor(Color.RED);
            check = false;
        }
        if (mPartyPassword.getText().toString().isEmpty())
        {
            Log.i("MainActivity", "Party Password empty.");
            mViewPartyPassword.setTextColor(Color.RED);
            check = false;
        }
        if (mTravelSpinner.getSelectedItemPosition() == 0)
        {
            Log.i("MainActivity", "Selection invalid.");
            mViewTravelType.setTextColor(Color.RED);
            check = false;
        }
        if (startLocation == null || stopLocation == null)
        {
            Log.i("MainActivity", "Please select a start/stop location.");
            check = false;
        }

        /*
            The party is fully created here
         */
        if (check) {
            Toast.makeText(getApplicationContext(), "Success: creating party...",
                    Toast.LENGTH_LONG).show();


            FirebaseDatabase db = FirebaseDatabase.getInstance();
            // Generate a Key?
            String key = db.getReference("partys").push().getKey();

            // created by phalguna and ray
	            //Party Id will be created from creater userid and groupname
		        //will be done through database

            // Create travel party on Firebase

            /**
             * Vals:
             *      private String partyKey; // assigned
                    private String partyName;
                    private String leader;
                    private List<String> followers;
                    private double start_lat;
                    private double start_long;
                    private double end_lat;
                    private double end_long;
             */

            FirebaseUser user = firebaseAuth.getCurrentUser();

            String partyKey = key; // set party key here from phalguna and rays generated key
            String partyPassword = mPartyPassword.getText().toString();
            String partyName = mPartyName.getText().toString();
            String leader = user.getEmail().toString();
            List<String> followers = new ArrayList<>();
            // for testing purposes
            //followers.add("testUser1");
            //followers.add("testUser2");
            double start_lat = startLocation.latitude;
            double start_long = startLocation.longitude;
            double end_long = stopLocation.longitude;
            double end_lat = stopLocation.latitude;
            boolean active = true;
            boolean launched = false;
            List<TravelRequests> requests = new ArrayList<>();

            Partys party = new Partys(partyKey, partyPassword, partyName, leader, followers, start_lat, start_long, end_lat, end_long, active, launched, requests);

            // create the database entry in firebase
            dbRef.child(partyKey).setValue(party);


            // Get firebase username for insert
            String userEmail = firebaseAuth.getCurrentUser().getEmail();

            Log.i("Test", "Username: " + userEmail);

            String[] full = (userEmail.split("@"));
            String username = full[0];
            Log.i("Tag", "Username: " + username);

            String name_key = partyName + " : " + key;

            // insert partyKey into user's account
            dbRef = FirebaseDatabase.getInstance().getReference().child("users");
            dbRef.child(username).child("partyKeys").push().setValue(key);

            Toast.makeText(getApplicationContext(), "Party creation successful!",
                    Toast.LENGTH_LONG).show();

            setContentView(R.layout.activity_main);
            Intent intent = new Intent(MainActivity.this, JourneyActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Error: Please fix the invalid fields.",
                    Toast.LENGTH_LONG).show();
        }

    }

    // Initial creation of MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currentUser==null)  {
            setContentView(R.layout.welcome_screen);
            mLoginButton = (Button) findViewById(R.id.loginButton);
            mExistUser = (Button) findViewById(R.id.existUser);
            mRegisterUser = (Button) findViewById(R.id.registerUser);
            mSkipButton = (Button) findViewById(R.id.skipButton);
        }
        else {
            setContentView(R.layout.activity_main);
            // Initialize buttons and functionality in activity_main layout
            init();
        }
    }
}
