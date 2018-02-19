package edu.fsu.cen4020.cen_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

// TODO: Integrate monitoring

public class JourneyActivity extends AppCompatActivity {

    public TextView mJourneyPartyName;
    public TextView mJourneyPartyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        //mJourneyPartyName = (TextView) findViewById(R.id.JourneyPartyName);
        //mJourneyPartyID = (TextView) findViewById(R.id.JourneyPartyID);

    }
}
