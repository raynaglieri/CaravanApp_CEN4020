package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
    Initial implementation and skeleton by: Victor and Raymond (via Pair Programming)
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;

    public FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public String partyKey;
    public FirebaseUser currentUser;
    public Partys partyData = new Partys();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout
        setContentView(R.layout.activity_maps);

        // get passed data
        Intent extras = getIntent();
        Bundle bundleExtras = extras.getExtras();
        partyKey = bundleExtras.get("partyKey").toString();
        Log.i("MapsActivity", partyKey);

        // Get instance for auth (session)
        mAuth = FirebaseAuth.getInstance();

        // Get current user from session
        currentUser = mAuth.getCurrentUser();

        // Get our Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("partys").child(partyKey);

        // Add Journey start marker
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                partyData = snapshot.getValue(Partys.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DB Error", "DB ERROR");
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mMap = googleMap;

        LatLng marker = new LatLng(partyData.start_lat, partyData.start_long);
        LatLng marker2 = new LatLng(partyData.end_lat, partyData.end_long);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker);
        builder.include(marker2);
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 75);

        googleMap.addMarker(new MarkerOptions().position(marker)
                .title("Start Location"));
        googleMap.addMarker(new MarkerOptions().position(marker2)
                .title("Stop Location"));
        googleMap.moveCamera(cu);

    }

    // update each user's location
    // clear map
    // add all markers back (refresh)

    public void loadFollowerMarkers()
    {

    }

    public void loadLeaderMarker()
    {
        // Get lat/long of partyLeader
        // Query FB Party with PartyKey and get leader
        // Query FB user with leader name to get user (leader) lat/long
        // update marker
    }

    public void loadRequestMarkers()
    {
        // Get lat/long of leader's location
        // If leader location is within 10 mile bound of request, load
        /*
        for each request in firebase party request list
            mMap.addMarker(new MarkerOptions()
                    .position(request_lat,request_long).title("Request info here"));
                    .some image
        */
    }

    public void updateUserLocation()
    {
        // Updates Firebase for current user lat/long
        // When user location changes in Firebase, update markers
    }


}
