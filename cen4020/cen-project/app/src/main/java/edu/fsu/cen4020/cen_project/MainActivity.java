package edu.fsu.cen4020.cen_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    // Initial Main Activity Skeleton

    // Travel Selection Buttons

    public Button mCreateButton, mJoinButton, mVerifyButton;
    public EditText mPartyName, mPartyPassword;
    public Spinner mTravelSpinner;

    public EditText mTextBoxID, mTextBoxPass;

    public void init()
    {

        mCreateButton = (Button) findViewById(R.id.create_button);
        mJoinButton = (Button) findViewById(R.id.join_button);

        // Launchable intents go here (button launches)

        /* Road Button */
        // launches FragmentList intent to invite participants to join party
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.join_party);
                // set view items
                mTextBoxID = (EditText) findViewById(R.id.textBoxID);
                mTextBoxPass = (EditText) findViewById(R.id.textBoxPass);
                mVerifyButton = (Button) findViewById(R.id.verify_button);
                Log.i("MainActivity:", "Join Button");
            }
        }) ;

        /* Road Button */
        // launches FragmentList intent to invite participants to join party
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
                Log.i("MainActivity:", "Join Button");
            }
        }) ;

    }

    // Do verification of Party ID and Password
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
    public void createParty(View view) {
        //Maybe set content view to 'creating...'?
        Log.i("MainActivity", "Create Party button clicked.");

        // Verify that all required fields are filled
        if (mPartyName.getText().toString().isEmpty())
        {
            Log.i("MainActivity", "Party Name empty.");
        }
        if (mPartyPassword.getText().toString().isEmpty())
        {
            Log.i("MainActivity", "Party Password empty.");
        }
        if (mTravelSpinner.getSelectedItemPosition() == 0)
        {
            Log.i("MainActivity", "Selection invalid.");
        }

        // TODO: Generate a party ID for the group to be displayed
        // TODO: Create travel party on Firebase
        // TODO: Launch party monitoring screen

    }

    // Initial creation of MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize buttons and functionality in activity_main layout
        init();
    }
}
