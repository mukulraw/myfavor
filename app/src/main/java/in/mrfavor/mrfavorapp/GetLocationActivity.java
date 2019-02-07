package in.mrfavor.mrfavorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myfavourcarpooling.easycarpooling.R;

public class GetLocationActivity extends AppCompatActivity {
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);


        gps = new GPSTracker(GetLocationActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Intent intent = new Intent(getBaseContext(), MapActivity.class);
            intent.putExtra("lat", latitude);
            intent.putExtra("lng",longitude);
           // startActivity(intent);
            // \n is for new line
          // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
