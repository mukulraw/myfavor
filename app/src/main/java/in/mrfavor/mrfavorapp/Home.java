package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.myfavourcarpooling.easycarpooling.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends Fragment implements PlaceSelectionListener
{
    String edit;
    Button enable, disable;
    String userid;
    double lat, lng;
    GoogleMap map;
    //PlaceAutocompleteFragment autocompleteFragment;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    String state, country,subLocality;
    EditText  ed;
    List<Address> addresses;

    String TAG = ":adasa";

    String apiKey = "AIzaSyBc5BKE1OW-9_IV39xiyTab5H7YG21awgw";


    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.home, container, false);
        Places.initialize(getContext().getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(getContext());

        //execute in every 50000 ms


        if (!Places.isInitialized()) {
            Places.initialize(getContext().getApplicationContext(), apiKey);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(this);

        autocompleteFragment.setHint("Search drop off location");

        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
//        SupportMapFragment supportMapFragment;
//        if (Build.VERSION.SDK_INT < 21) {
//            supportMapFragment = (SupportMapFragment) getActivity()
//                    .getSupportFragmentManager().findFragmentById(R.id.map);
//        } else {
//            supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
//        }
//        supportMapFragment.getMapAsync(this);
        userid = pref.getString("useremail", null);
        getFavlist2();

     //   ed = (EditText) v.findViewById(R.id.homeedit);

        //edit = ed.getText().toString();
        enable = (Button) v.findViewById(R.id.enabled);
        disable = (Button) v.findViewById(R.id.disable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavlist();
                getFavlist4();
            }
        });
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavlist1();

            }
        });
        return v;
    }
        void getFavlist() {
        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        String url = "http://mrfavor.in/myfavour/driver_gotohome.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                enable.setBackgroundColor(getResources().getColor(R.color.orangeon));
                disable.setBackgroundColor(getResources().getColor(R.color.orangeoff));


            }
//                    dataModels= new ArrayList<>();
//                    dataModels.add(new DataModel(pickloc, droploc, "",""));


        }
                , new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("emailid", userid);
                MyData.put("gotohome", "Yes");
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }
        void getFavlist1() {
        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        String url = "http://mrfavor.in/myfavour/driver_gotohome.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                disable.setBackgroundColor(getResources().getColor(R.color.orangeon));
                enable.setBackgroundColor(getResources().getColor(R.color.orangeoff));

//                Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
            }
//                    dataModels= new ArrayList<>();
//                    dataModels.add(new DataModel(pickloc, droploc, "",""));
        }


                , new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("emailid", userid);
                MyData.put("gotohome", "No");
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }
    void getFavlist2() {

        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail", null);

        String url = "http://mrfavor.in/myfavour/dirver_getstatus_gotohome.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Yes")) {
                    disable.setBackgroundColor(getResources().getColor(R.color.orangeoff));
                    enable.setBackgroundColor(getResources().getColor(R.color.orangeon));

                } else {
                    enable.setBackgroundColor(getResources().getColor(R.color.orangeoff));
                    disable.setBackgroundColor(getResources().getColor(R.color.orangeon));

                }

                //   Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();


            }

//                    dataModels= new ArrayList<>();
//                    dataModels.add(new DataModel(pickloc, droploc, "",""));


        }


                , new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("emailid", userid);
                //  MyData.put("gotohome","No");
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }


    void getFavlist4() {
        if (edit != null && !edit.isEmpty()) {
            try {
                Geocoder coder = new Geocoder(getActivity());
                List<Address> addressList = coder.getFromLocationName(edit, 1);
                if (addressList != null && addressList.size() > 0) {
                    lat = addressList.get(0).getLatitude();
                    lng = addressList.get(0).getLongitude();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } // end catch
        } // end if
        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail", null);

        String url = "http://mrfavor.in/myfavour/driver_set_homelocation.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getActivity(),"helllo"+response,Toast.LENGTH_LONG).show();
            }
//                    dataModels= new ArrayList<>();
//                    dataModels.add(new DataModel(pickloc, droploc, "",""));
        }
                , new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("emailid", userid);
                MyData.put("home_lat ", String.valueOf(lat));
                MyData.put("home_lng", String.valueOf(lng));
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        edit= place.getAddress().toString();
    }

    @Override
    public void onError(@NonNull Status status) {

    }
//
//    private GoogleMap mMap;
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setZoomGesturesEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        //Initialize Google Play Services
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient();
//                mMap.setMyLocationEnabled(true);
//            }
//        } else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
//
//
////        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
////            @Override
////            public void onCameraIdle() {
////               lat = mMap.getCameraPosition().target.latitude+"";
////               lng = mMap.getCameraPosition().target.longitude+"";
////                Geocoder geocoder;
////
////                geocoder = new Geocoder(getActivity(), Locale.getDefault());
////
////                try {
////                    addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
////                String city = addresses.get(0).getLocality();
////                String state = addresses.get(0).getAdminArea();
////                String country = addresses.get(0).getCountryName();
////                String postalCode = addresses.get(0).getPostalCode();
////                String knownName = addresses.get(0).getFeatureName();
////                if(!lat.contentEquals(latitude) && !lng.contains(longitude)) {
////                    updatecamera(lat, lng);
////
////
////                    picklocation.setText(address);
////                    pick=address;
////
//////                    if(pick!=null)
//////                    {
//////                        picklocation.setText(pick);
//////                    }
////
////                }
////                latitude = lat;
////                longitude = lng;
////
////                //  Toast.makeText(getActivity(),"In change OK OK "+googleMap.getCameraPosition().target.latitude,Toast.LENGTH_LONG).show();
////            }
////        });
//
//    }
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//    @Override
//    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    mLocationRequest, this);
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
////Showing Current Location Marker on Map
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        LocationManager locationManager = (LocationManager)
//              getActivity().  getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getBestProvider(new Criteria(), true);
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location locations = locationManager.getLastKnownLocation(provider);
//        List<String> providerList = locationManager.getAllProviders();
//        if (null != locations && null != providerList && providerList.size() > 0) {
//            double longitude = locations.getLongitude();
//            double latitude = locations.getLatitude();
//            Geocoder geocoder = new Geocoder(getActivity(),
//                    Locale.getDefault());
//            try {
//                List<Address> listAddresses = geocoder.getFromLocation(latitude,
//                        longitude, 1);
//                if (null != listAddresses && listAddresses.size() > 0) {
//                    state = listAddresses.get(0).getAdminArea();
//                    country = listAddresses.get(0).getCountryName();
//                     subLocality = listAddresses.get(0).getSubLocality();
//                    autocompleteFragment.setText(subLocality+" "+state+" "+country);
//                    markerOptions.title("" + latLng + "," + subLocality + "," + state
//                            + "," + country);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
////        mCurrLocationMarker = mMap.addMarker(markerOptions);
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
////        if (mGoogleApiClient != null) {
////            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
////                    this);
////        }
//    }
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//    }
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            } else {
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(getActivity(),
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                     //   mMap.setMyLocationEnabled(true);
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "permission denied",
//                            Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
  //  }


    /*@Override
    public void onPlaceSelected(Place place) {
        edit= place.getAddress().toString();

    }

    @Override
    public void onError(Status status) {

    }*/
}
