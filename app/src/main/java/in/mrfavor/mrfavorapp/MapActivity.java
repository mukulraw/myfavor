package in.mrfavor.mrfavorapp;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myfavourcarpooling.easycarpooling.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener,GoogleMap.OnMarkerDragListener , GoogleMap.OnCameraChangeListener{
    Marker marker;
    // Google Map
   GoogleMap googleMap;
    GoogleApiClient googleapiclient;
    Double default_long,default_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            default_lang    = extras.getDouble("lat");
            default_long  = extras.getDouble("lng");
            // and get whatever type user account id is


        }


        try {
            // Loading map
             initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void initilizeMap() {


          MapFragment mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
         mapfragment.getMapAsync(this);





    }

    private void gotoLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat,lng);

        // create marker


        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(ll,15);
        googleMap.moveCamera(cu);

    }


    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
     googleMap.setMyLocationEnabled(true);




        googleapiclient = new GoogleApiClient.Builder(this)
                           .addApi(LocationServices.API)
                          .addConnectionCallbacks(this)
                         .addOnConnectionFailedListener(this)
                        .build();

        LatLng ll = new LatLng(default_lang,default_long);
        CameraUpdate cu =  CameraUpdateFactory.newLatLngZoom(ll,15);

        googleMap.animateCamera(cu);

        MarkerOptions markeroptions=null;

                  markeroptions =  new MarkerOptions().position(ll);

                    markeroptions.title("Marker");


                   marker = googleMap.addMarker(markeroptions);

                    marker.showInfoWindow();
                    marker.setDraggable(true);






       // googleapiclient.connect();

     // gotoLocation(28.665899,77.1414751);
    }

    LocationRequest locationrequest;
    @Override
    public void onConnected(Bundle bundle) {
       // Toast.makeText(getApplicationContext(),"IN Onconnected",Toast.LENGTH_LONG).show();
       locationrequest = LocationRequest.create();
        locationrequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationrequest.setInterval(300000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleapiclient,locationrequest,this);
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
           LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
          CameraUpdate cu =  CameraUpdateFactory.newLatLngZoom(ll,15);

          googleMap.animateCamera(cu);
        }

/////
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.setTitle("Starts");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        marker.setTitle("Draging");

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // TODO Auto-generated method stub
        LatLng dragPosition = marker.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;

      //  Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
       // Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
    }
}
