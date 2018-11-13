package in.mrfavor.mrfavorapp;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by krishna on 7/9/2017.
 */
public class Fragment_driver_map extends Fragment  implements OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener,GoogleMap.OnMarkerDragListener , GoogleMap.OnCameraChangeListener {
    MapView mMapView;
    Marker marker;
    private GoogleMap googleMap;
    private String longitude;
    private String latitude;
    private GoogleApiClient googleapiclient;
    private Handler handler;

    private static final int MESSAGE_ID_SAVE_CAMERA_POSITION = 1;
    private static final int MESSAGE_ID_READ_CAMERA_POSITION = 2;
    private CameraPosition lastCameraPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
       View view = inflater.inflate(R.layout.fragment_passenger_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately





        SharedPreferences prefs = getActivity().getSharedPreferences("gpslocation", 0);
        latitude  = prefs.getString("lat","28.6406381");
        longitude = prefs.getString("lng","77.1358354");





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

        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);




        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

       MarkerOptions mo =  new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").draggable(true);
        googleMap.addMarker(mo);

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));







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

    }
}
