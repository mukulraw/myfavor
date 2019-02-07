package in.mrfavor.mrfavorapp;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.myfavourcarpooling.easycarpooling.R;

import java.util.Timer;
import java.util.TimerTask;

public class GeoLocationbygooglemap extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient googleapiclient;
    Double default_long, default_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_locationbygooglemap);

        googleapiclient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleapiclient.connect();

    }

    LocationRequest locationrequest;

    @Override
    public void onConnected(Bundle bundle) {

      //  Toast.makeText(getApplicationContext(), "IN Onconnected", Toast.LENGTH_LONG).show();
        locationrequest = LocationRequest.create();
        locationrequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationrequest.setInterval(1000);

    LocationServices.FusedLocationApi.requestLocationUpdates(googleapiclient, locationrequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if(location == null)
        {
            Toast.makeText(getApplicationContext(),"Please check your GPS settings",Toast.LENGTH_LONG).show();
        }
        else
        {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            //Toast.makeText(getApplicationContext(),"Lat = "+lng,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getBaseContext(), MapActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng",lng);
           // startActivity(intent);
           // googleapiclient.disconnect();
           // finish();

        }


    }
}
