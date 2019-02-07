package in.mrfavor.mrfavorapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myfavourcarpooling.easycarpooling.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_droplocation extends Fragment  implements OnMapReadyCallback {

    MapView mMapView;
    Marker marker;
    private GoogleMap googleMap;
    private String longitude,orglong;
    private String latitude,orglat;
    private GoogleApiClient googleapiclient;
    TextView tvpickup;
    String locname;
    Double lat,lng;
    SharedPreferences sharedpreferences;
    MarkerOptions mo;
    public Fragment_droplocation() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_droplocation, container, false);

        locname = getArguments().getString("locname");
lat = getArguments().getDouble("latval");
        lng = getArguments().getDouble("lngval");

        sharedpreferences = this.getActivity().getSharedPreferences("pickuppoint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("p_locname", locname);
        editor.putString("p_latval",lat.toString());
        editor.putString("p_lngval",lng.toString());
        editor.commit();


        tvpickup = (TextView) view.findViewById(R.id.tvpickfrom);

        tvpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),DroplocationAutocomplet.class);

                startActivity(i);
            }
        });


        mMapView = (MapView) view.findViewById(R.id.map);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
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
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(lat,lng);
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();


        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(locname).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
