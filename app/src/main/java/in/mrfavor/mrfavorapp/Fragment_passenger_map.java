package in.mrfavor.mrfavorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myfavourcarpooling.easycarpooling.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by krishna on 7/9/2017.
 */
public class Fragment_passenger_map extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraIdleListener {
    MapView mMapView;
    Marker marker;
    private GoogleMap googleMap;
    private String longitude, orglong;
    private String latitude, orglat;
    private GoogleApiClient googleapiclient;
    TextView tvpickup;
    String locname;
    double lat_extra, lng_extra;

    MarkerOptions mo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_passenger_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);


        tvpickup = (TextView) view.findViewById(R.id.tvpickfrom);


        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/tcb.ttf");

        tvpickup.setTypeface(custom_font);

        tvpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewLocationautocomplet.class);
                i.putExtra("locname", locname);
                i.putExtra("lat", lat_extra);
                i.putExtra("lng", lng_extra);
                startActivity(i);
            }
        });


        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        SharedPreferences prefs = getActivity().getSharedPreferences("gpslocation", 0);
        orglat = latitude = prefs.getString("lat", "28.6406381");
        orglong = longitude = prefs.getString("lng", "77.1358354");

       // Toast.makeText(getContext(), orglat, Toast.LENGTH_LONG).show();


        mMapView.getMapAsync(this);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(" Mr. Favor");
    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;


        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            googleMap.setMyLocationEnabled(true);
          // return;
        }


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //Using position get Value from arraylist
              //  Toast.makeText(getActivity(),"Position ",Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);


        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                String lat = googleMap.getCameraPosition().target.latitude+"";
                String lng = googleMap.getCameraPosition().target.longitude+"";
                if(!lat.contentEquals(latitude) && !lng.contains(longitude)) {
                    updatecamera(lat, lng);
                }
                latitude = lat;
                longitude = lng;

                //  Toast.makeText(getActivity(),"In change OK OK "+googleMap.getCameraPosition().target.latitude,Toast.LENGTH_LONG).show();
            }
        });

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

              //   updatecamera(cameraPosition.target.latitude+"",cameraPosition.target.longitude+"");

            }
        });


        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));



       mo =  new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").draggable(true);
      marker=  googleMap.addMarker(mo);

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



    }


    public void updatecamera(String latitude,String longitude)
    {


        String filterAddress = "";
        Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses =
                    geoCoder.getFromLocation(Double.parseDouble(latitude),Double.parseDouble(longitude), 1);

            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                    filterAddress += addresses.get(0).getAddressLine(i) + " ";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }



        //googleMap.clear();

       if(marker!=null) marker.remove();
        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));


       // tvpickup.setText(filterAddress);

      //  mo =  new MarkerOptions().position(sydney).title("Your are here").snippet(filterAddress).draggable(true);
                 locname = filterAddress;
                 lat_extra = Double.parseDouble(latitude);
                 lng_extra = Double.parseDouble(longitude);

        mo =  new MarkerOptions().position(sydney).title(filterAddress).draggable(true);
       marker = googleMap.addMarker(mo);
        marker.showInfoWindow();

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        markeranimation(marker);

    }

    private void markeranimation(Marker marker) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(getActivity(),"Location change",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraIdle() {
      //  Toast.makeText(getActivity(), "The camera has stopped moving.",
             //   Toast.LENGTH_SHORT).show();

    }



}
