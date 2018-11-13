package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Userboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraIdleListener {
    MapView mMapView;
    Marker marker;
    String iit1, drop11;
    private GoogleMap googleMap;
    private String longitude, orglong;
    private String latitude, orglat;
    private GoogleApiClient googleapiclient;
    LinearLayout ll1, ll2;
    double pick_lat, pick_lng, drop_lat, drop_lng;
    String lat, lng;
    int dropRequest = 102;
    int pickRequest = 101;
    TextView tvpickup;
    String locname;
    double lat_extra, lng_extra;
    List<Address> addresses;
    String iit;
    MarkerOptions mo;
    Bundle extras;
    String pick, drop;
    Toolbar toolbar;
    TextView picklocation, droplocation;
    String name, idName;
    Button confirm, confirm1;
    LinearLayout ll;
    String userid;
    PlaceAutocompleteFragment autocompleteFragment, autocompleteFragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userboard);
//        Handler handler = new Handler(Looper.getMainLooper());
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkConnection();
//                // Run your task here
//            }
//        }, 1000 );
        // end of main
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        checkConnection();
//                        //add your code here
//                    }
//                }, 1000);
//            }
//        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        mMapView = (MapView) findViewById(R.id.map);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        SharedPreferences prefs12 = getApplicationContext().getSharedPreferences("gpslocation", 0);
        orglat = latitude = prefs12.getString("lat", "28.6406381");
        orglong = longitude = prefs12.getString("lng", "77.1358354");
        ll = (LinearLayout) findViewById(R.id.content_frame);
        picklocation = (TextView) findViewById(R.id.edt1);
        droplocation = (TextView) findViewById(R.id.edt2);
        confirm = (Button) findViewById(R.id.confirm);
        confirm1 = (Button) findViewById(R.id.confirm1);
        getFavlist4();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "pick" + pick + "drop" + drop, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Drawroutsonmap.class);
                i.putExtra("pick", pick);
                i.putExtra("drop", drop);
                startActivity(i);
                finishAffinity();
            }
        });
        Intent ii = getIntent();
        pick = ii.getStringExtra("pick");
        drop = ii.getStringExtra("drop");
        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        if (prefs != null) {
            pick = prefs.getString("pick", pick);//"No name defined" is the default value.
            drop = prefs.getString("drop", drop);
            //0 is the default value.
        }
        droplocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getApplicationContext(), MainActivity234.class);
                startActivityForResult(ii, dropRequest);
            }
        });
        picklocation.setText(pick);
        droplocation.setText(drop);
        picklocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getApplicationContext(), MyActivity.class);
                startActivityForResult(ii, pickRequest);
            }
        });
        //autocompleteFragment.setText(locname);
        // autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.iclogo);
        getSupportActionBar().setTitle("Mr. Favor");

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tvusername = (TextView) header.findViewById(R.id.tvUsername);
        tvusername.setText("My Profile");
        tvusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myprofile();
                drawer.closeDrawers();
            }
        });

//      Intent   extras = getIntent();
//        if (extras != null) {
//            String value = extras.getStringExtra("pick11");
//            String drop=extras.getStringExtra("drop11");
//          //  Toast.makeText(getApplicationContext(),"pick11"+value+"drop11"+drop,Toast.LENGTH_LONG).show();
//            if (value != null || drop !=null) {
////                String fragname = extras.getString("fragment");
////                if (fragname.contentEquals("droplocation")) {
////                    droplocation();
////                } else {
//                    //confirmbooking();
//                }
//             else {
//
//                String bookingid = extras.getStringExtra("bookingid");
//                if (bookingid != null) {
//                    // history();
//                    ride();
//                } else {
//                    ride();
//                }
//            }
//            //The key argument here must match that used in the other activity
//        } else {
//            ride();
//        }

        iit = picklocation.getText().toString();


        /*final Handler handler = new Handler();
        final Timer timer = new Timer();
        final TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        checkConnection();
                    }


                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms
*/

//
//    runOnUiThread(new Runnable() {
//            public void run() {
//
//            }
//        });
    }

    private void myprofile() {
        confirm.setVisibility(View.GONE);
        picklocation.setVisibility(View.GONE);
        droplocation.setVisibility(View.GONE);
        mMapView.setVisibility(View.GONE);
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        Fragment fragment = null;
        fragment = new Fragment_passenger_home();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    private void droplocation() {
        Fragment fragment = null;
        fragment = new Fragment_droplocation();
        //replacing the fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("locname", extras.getString("locname"));
            bundle.putDouble("latval", extras.getDouble("latvalue"));
            bundle.putDouble("lngval", extras.getDouble("lngvalue"));
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    private void confirmbooking() {
        Fragment fragment = null;
        fragment = new Fragment_confirm();
        //replacing the fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("locname", extras.getString("locname"));
            bundle.putDouble("latval", extras.getDouble("latvalue"));
            bundle.putDouble("lngval", extras.getDouble("lngvalue"));
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void ratecard() {
        picklocation.setVisibility(View.GONE);
        droplocation.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        Fragment fragment = null;
        fragment = new Fragment_passenger_ratecard();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void history() {
//        mMapView.setVisibility(View.GONE);
//        ll1.setVisibility(View.GONE);
//        ll2.setVisibility(View.GONE);
//
//
//        picklocation.setVisibility(View.GONE);
//        droplocation.setVisibility(View.GONE);
//        confirm.setVisibility(View.GONE);
        Intent ii = new Intent(getApplicationContext(), Fragment_passenger_history.class);
        startActivity(ii);
//        Fragment fragment = null;
//        fragment = new Fragment_passenger_history();
//        //replacing the fragment
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
        //  }
    }

    void addFav() {
        iit1 = picklocation.getText().toString();
        drop11 = droplocation.getText().toString();
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = coder.getFromLocationName(iit1, 5);
            if (addresses == null) {
            }
            Address location = addresses.get(0);
            pick_lat = location.getLatitude();
            pick_lng = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Address> addresses1;
        try {
            addresses1 = coder.getFromLocationName(drop11, 5);
            if (addresses1 == null) {
            }
            Address location = addresses1.get(0);
            drop_lat = location.getLatitude();
            drop_lng = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), "user" + userid + drop_lat + drop_lng + pick_lat + pick_lng + iit1 + drop11, Toast.LENGTH_SHORT).show();
        String url = "http://softcode.in/myfavour/fav_route_add.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), "Response" + response, Toast.LENGTH_SHORT).show();
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("emailid", userid); //Add the data you'd like to send to the server.
                MyData.put("pic_lat", String.valueOf(pick_lat)); //Add the data you'd like to send to the server.
                MyData.put("pic_lng", String.valueOf(pick_lng)); //Add the data you'd like to send to the server.
                MyData.put("pic_location_name", iit1); //Add the data you'd like to send to the server.
                MyData.put("drop_lat", String.valueOf(drop_lat)); //Addd the data you'd like to send to the server.
                MyData.put("drop_lng", String.valueOf(drop_lng)); //Add the data you'd like to send to the server.
                MyData.put("drop_location_name", drop11); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(MyStringRequest);

    }


    private void termandconditions() {
        mMapView.setVisibility(View.GONE);
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        picklocation.setVisibility(View.GONE);
        droplocation.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        Fragment fragment = null;
        fragment = new Fragment_passenger_termconditions();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Intent ia = new Intent(Userboard.this, LoginRegister.class);
            // startActivity(ia);

            addFav();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_ride) {
            // Handle the camera action
            //  ride();
            mMapView.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            picklocation.setVisibility(View.VISIBLE);
            droplocation.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            new Userboard();
        } else if (id == R.id.nav_call) {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9599435086"));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                startActivity(callIntent);
            }
        } else if (id == R.id.nav_email) {
            final int min = 1000;
            final int max = 10000000;
            final int random = (new Random()).nextInt((max - min) + 1) + min;
            String domainname = "mrfavor.in";
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto: support@" + domainname));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ticket No. - " + random);
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        } else if (id == R.id.nav_ratecard) {
            ratecard();
        } else if (id == R.id.nav_history) {
            history();
        } else if (id == R.id.nav_termncond) {
            termandconditions();
        } else if (id == R.id.nav_share) {
            final String appPackageName = getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.addFav) {
            Intent ii = new Intent(getApplicationContext(), AddFav.class);
            startActivity(ii);
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("Logininfo", MODE_PRIVATE).edit();
            editor.putString("useremail", "");
            editor.putString("isLogedin", "");
            editor.putString("isPassenger", "");
            editor.commit();
            Intent i = new Intent(Userboard.this, LoginRegister.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
//
//        if (prefs != null) {
//             pick = prefs.getString("pick", pick);//"No name defined" is the default value.
//             drop = prefs.getString("drop", drop);
//            picklocation.setText(pick);
//            droplocation.setText(drop);//0 is the default value.
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                lat = googleMap.getCameraPosition().target.latitude + "";
                lng = googleMap.getCameraPosition().target.longitude + "";
                Geocoder geocoder;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {


                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    if (!lat.contentEquals(latitude) && !lng.contains(longitude)) {
                        updatecamera(lat, lng);
                        picklocation.setText(address);
                        pick = address;
//                    if(pick!=null)
//                    {
//                        picklocation.setText(pick);
//                    }
                    }
                    latitude = lat;
                    longitude = lng;
                    //  Toast.makeText(getActivity(),"In change OK OK "+googleMap.getCameraPosition().target.latitude,Toast.LENGTH_LONG).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        });
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                //   updatecamera(cameraPosition.target.latitude+"",cameraPosition.target.longitude+"");

            }
        });
        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mo = new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").draggable(true);
        marker = googleMap.addMarker(mo);

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void updatecamera(String latitude, String longitude) {
        String filterAddress = "";
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses =
                    geoCoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);

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
        if (marker != null) marker.remove();
        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        // tvpickup.setText(filterAddress);

        //  mo =  new MarkerOptions().position(sydney).title("Your are here").snippet(filterAddress).draggable(true);
        locname = filterAddress;
        lat_extra = Double.parseDouble(latitude);
        lng_extra = Double.parseDouble(longitude);
        mo = new MarkerOptions().position(sydney).title(filterAddress).draggable(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == dropRequest) {
                String dropLoc = data.getStringExtra("drop");
                droplocation.setText(dropLoc);
                drop = droplocation.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
                editor.putString("drop", drop);
                editor.commit();
                editor.apply();
            }
            if (requestCode == pickRequest) {
                String pickLoc = data.getStringExtra("pick");
                String pickLoc1 = data.getStringExtra("pick_loc");
                //Toast.makeText(getApplicationContext(),"kl"+pickLoc1,Toast.LENGTH_LONG).show();
                picklocation.setText(pickLoc);
                pick = picklocation.getText().toString();
                SharedPreferences.Editor editor1 = getSharedPreferences("mypref", MODE_PRIVATE).edit();
                editor1.putString("pick", pick);
                editor1.commit();
                editor1.apply();
            }
        }
    }

    //
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(),"OnDestry",Toast.LENGTH_LONG).show();
//        SharedPreferences.Editor editor1 = getSharedPreferences("mypref", MODE_PRIVATE
//        ).edit();
//        editor1.clear();
//        editor1.apply();
//        editor1.commit();
//
//    }
//
    void getFavlist4() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, "http://softcode.in/myfavour/ride_status_customer.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                if (response.equalsIgnoreCase("0")) {
                    Toast.makeText(getApplicationContext(), "No Pending Rides ", Toast.LENGTH_LONG).show();
                    confirm.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "First Complete Pending Rides ", Toast.LENGTH_LONG).show();
                    confirm.setVisibility(View.VISIBLE);
                    confirm1.setVisibility(View.GONE);
                }
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
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        MyRequestQueue.add(MyStringRequest);

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {
        if (!isOnline()) {
            Toast.makeText(Userboard.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(Userboard.this, "You are  connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
