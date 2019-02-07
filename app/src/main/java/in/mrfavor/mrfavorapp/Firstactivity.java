package in.mrfavor.mrfavorapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myfavourcarpooling.easycarpooling.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Firstactivity extends AppCompatActivity {
    GPSTracker gps;
    LocationManager locationManager;
    String lattitude, longitude;
    private static final int REQUEST_LOCATION = 1;
    private Handler mWaitHandler = new Handler();
    boolean isRatecardFuncCalled = false;

    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstactivity);
        getSupportActionBar().hide();

        //public void getHashkey(){
            try {
                PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());

                    Log.i("Base64", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("Name not found", e.getMessage(), e);

            } catch (NoSuchAlgorithmException e) {
                Log.d("Error", e.getMessage(), e);
            }


        if (hasPermissions(this, PERMISSIONS)) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }


        //gps = new GPSTracker(Firstactivity.this);


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
                Intent i = new Intent(Firstactivity.this, Ratecard.class);
                // Intent i = new Intent(Firstactivity.this,Drawroutsonmap.class);
                startActivity(i);
            }
        });
    }


    private void getLocation() {

        if (hasPermissions(this, PERMISSIONS)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                setLatLong(lattitude,longitude);
                startApp();

                // Toast.makeText(Firstactivity.this,"Location "+ lattitude+longitude,Toast.LENGTH_LONG).show();

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setLatLong(lattitude,longitude);
                startApp();
                //  Toast.makeText(Firstactivity.this,"Location 2 "+ lattitude+longitude,Toast.LENGTH_LONG).show();

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setLatLong(lattitude,longitude);
                startApp();
                // Toast.makeText(Firstactivity.this,"Location 3 "+ lattitude+longitude,Toast.LENGTH_LONG).show();

            }else{
                startApp();
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


    void startApp()
    {
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
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {

            Log.d("permmm" , "1");

            if (hasPermissions(this , PERMISSIONS)) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                        ) {

                    Log.d("permmm" , "3");

                    Toast.makeText(getApplicationContext(), "Permissions are required for this app", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Log.d("permmm" , "4");
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }
        }

    }

}
