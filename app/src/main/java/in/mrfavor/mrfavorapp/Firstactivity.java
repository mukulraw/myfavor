package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myfavourcarpooling.easycarpooling.R;

public class Firstactivity extends AppCompatActivity {
    GPSTracker gps;
    LocationManager locationManager;
    String lattitude,longitude;
    private static final int REQUEST_LOCATION = 1;
    private Handler mWaitHandler = new Handler();
    boolean isRatecardFuncCalled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstactivity);
        getSupportActionBar().hide();
        // 5 sec delay
        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //The following code will execute after the 5 seconds.

                try {

                    //Go to next page i.e, start the next activity.
                    if(isRatecardFuncCalled==false) {
                        gotoratecard();
                    }

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    finish();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, 5000);  // Give a 5 seconds delay.

        // 5 sec delay












        gps = new GPSTracker(Firstactivity.this);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


        // check if GPS enabled

     /*
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Toast.makeText(Firstactivity.this,"Lat "+latitude,Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = getSharedPreferences("gpslocation", MODE_PRIVATE).edit();
            editor.putString("lat",""+latitude);
            editor.putString("lng",""+longitude);
            editor.commit();


            // startActivity(intent);
            // \n is for new line
         //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        */

      //  gps.showSettingsAlert();
        Button go = (Button) findViewById(R.id.btnGo);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(Firstactivity.this,Ratecard.class);
               // Intent i = new Intent(Firstactivity.this,Drawroutsonmap.class);
                startActivity(i);
            }
        });
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Firstactivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Firstactivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Firstactivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                setLatLong(lattitude,longitude);
                gotoratecard();

               // Toast.makeText(Firstactivity.this,"Location "+ lattitude+longitude,Toast.LENGTH_LONG).show();

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setLatLong(lattitude,longitude);
                gotoratecard();
              //  Toast.makeText(Firstactivity.this,"Location 2 "+ lattitude+longitude,Toast.LENGTH_LONG).show();

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setLatLong(lattitude,longitude);
                gotoratecard();
               // Toast.makeText(Firstactivity.this,"Location 3 "+ lattitude+longitude,Toast.LENGTH_LONG).show();

            }else{
                gotoratecard();
               // Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void gotoratecard() {
        isRatecardFuncCalled = true;
       // Intent i = new Intent(Firstactivity.this,Ratecard.class);
        Intent i = new Intent(Firstactivity.this,LoginRegister.class);
        // Intent i = new Intent(Firstactivity.this,Drawroutsonmap.class);
        startActivity(i);
        finish();
    }

    private void setLatLong(String lattitude, String longitude) {

        SharedPreferences.Editor editor = getSharedPreferences("gpslocation", MODE_PRIVATE).edit();
        editor.putString("lat",""+lattitude);
        editor.putString("lng",""+longitude);
        editor.commit();

    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
