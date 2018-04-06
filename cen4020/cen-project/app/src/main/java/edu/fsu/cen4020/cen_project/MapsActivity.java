package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public GoogleMap mMap;

    public boolean mLocationPermissionGranted = false;

    public FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // Construct a GeoDataClient.
    public GeoDataClient mGeoDataClient;

    // Construct a PlaceDetectionClient.
    public PlaceDetectionClient mPlaceDetectionClient;

    // Construct a FusedLocationProviderClient.
    public FusedLocationProviderClient mFusedLocationProviderClient;

    public String partyKey;
    public FirebaseUser currentUser;
    public Partys partyData = new Partys();

    public LocationRequest  locationRequest;
    public LocationCallback locationCallback;
    public GoogleApiClient googleAPIClient;

    @Override
    public void onStart(){
        super.onStart();
        googleAPIClient.connect();
    }
    @Override
    public void onStop(){
        googleAPIClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnectionSuspended(int id){

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){

    }
    @Override
    public void onConnected(Bundle bundle){

        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5*1000);

        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                Log.i("Listening", "...");
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected void onStop()
    {
        pause updates?
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout
        setContentView(R.layout.activity_maps);

        googleAPIClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                getDeviceLocation();
            };
        };


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

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // Pair Programming: Phalguna and Victor

        LatLng marker = new LatLng(partyData.start_lat, partyData.start_long);
        LatLng marker2 = new LatLng(partyData.end_lat, partyData.end_long);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker);
        builder.include(marker2);
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 75);

        mMap.addMarker(new MarkerOptions().position(marker)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title("Start Location"));  // TODO: Maybe make this the name of the place?

        mMap.addMarker(new MarkerOptions().position(marker2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title("Destination"));   // TODO: Address/Name of the place?

        //Polyline line = googleMap.addPolyline(new PolylineOptions()
        //        .add(marker,
        //                marker2)
        //        .geodesic(true));

        googleMap.moveCamera(cu);

    }

    // update each user's location
    // clear map
    // add all markers back (refresh)

    // Theory; By Victor, Ray, Phalguna

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


    // User Location Provided by Google Maps API
    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                //mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location mLastKnownLocation = task.getResult();
                            Log.i("Lat", Double.toString(mLastKnownLocation.getLatitude()));
                            Log.i("Long", Double.toString(mLastKnownLocation.getLongitude()));
                            Toast.makeText(getApplicationContext(), "Lat,Long: " + Double.toString(mLastKnownLocation.getLatitude()) + ", " + Double.toString(mLastKnownLocation.getLongitude()),
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Log.d("GET DEV LOC", "Current location is null. Using defaults.");
                            Log.e("GET DEV LOC", "Exception: %s", task.getException());
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }





}
