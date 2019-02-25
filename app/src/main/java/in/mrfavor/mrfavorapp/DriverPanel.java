package in.mrfavor.mrfavorapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.myfavourcarpooling.easycarpooling.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import driver.Fragment_currentbooking;
import driver.Fragment_endride;
import driver.ShareDriver;

public class DriverPanel extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    MenuItem nav_request;
    private LocationManager locationManager;
    private String provider;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //execute in every 50000 ms
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_driver_panel);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        updateUI();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });   */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        nav_request = menu.findItem(R.id.nav_request);

        // set new title to the MenuItem
        nav_request.setTitle("Ride Request(0)");

        Intent serviceintetn = new Intent(DriverPanel.this, Servicefordriver.class);
        startService(serviceintetn);


        registerReceiver(broadcastReceiver, new IntentFilter(Servicefordriver.BROADCAST_ACTION));


        navigationView.setNavigationItemSelectedListener(this);


        if (SharePreferenceUtils.getInstance().getString("session").equals("1"))
        {
            String picpoint = SharePreferenceUtils.getInstance().getString("picpoint");
            String droppoint = SharePreferenceUtils.getInstance().getString("droppoint");
            String custname = SharePreferenceUtils.getInstance().getString("custname");
            String contactnumber = SharePreferenceUtils.getInstance().getString("contactnumber");
            Double piclat = SharePreferenceUtils.getInstance().getDouble("piclat");
            Double piclng = SharePreferenceUtils.getInstance().getDouble("piclng");
            Double droplat = SharePreferenceUtils.getInstance().getDouble("droplat");
            Double droplng = SharePreferenceUtils.getInstance().getDouble("droplng");
            String rideID = SharePreferenceUtils.getInstance().getString("rideID");


            final LatLng startlatlng = new LatLng(Double.parseDouble(String.valueOf(piclat)),Double.parseDouble(String.valueOf(piclng)));
            final LatLng endlatlng = new LatLng(Double.parseDouble(String.valueOf(droplat)),Double.parseDouble(String.valueOf(droplng)));

            acceptbooking(picpoint , droppoint , custname , contactnumber , startlatlng , endlatlng , rideID);


        }
        else
        {
            rideRequest();
        }





    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {

        String name = intent.getStringExtra("bookingcount");
        if (name != null) {
            nav_request.setTitle("Ride Request (" + name + ")");
        }

    }


    private void myprofile() {

        Fragment fragment = null;
        fragment = new Fragment_driver_home();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void offline() {

        Fragment fragment = null;
        fragment = new Fragment_driver_offline();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void ratecard() {

        Fragment fragment = null;
        fragment = new Fragment_driver_ratecard();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void history() {

        Fragment fragment = null;
        fragment = new Fragment_driver_history();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    public void acceptbooking(String picpoint, String droppoint, String custname, String contactnumber, LatLng picLatLng, LatLng dropLatLng, String rideID) {

//        Fragment fragment = null;
//        fragment = new Fragment_accept();
//        //replacing the fragment
//        if (fragment != null) {
        Intent bundle = new Intent(getApplicationContext(), AcceptAcivity.class);
        bundle.putExtra("picpoint", picpoint);
        bundle.putExtra("droppoint", droppoint);
        bundle.putExtra("custname", custname);
        bundle.putExtra("contactnumber", contactnumber);
        bundle.putExtra("piclat", picLatLng.latitude);
        bundle.putExtra("piclng", picLatLng.longitude);
        bundle.putExtra("droplat", dropLatLng.latitude);
        bundle.putExtra("droplng", dropLatLng.longitude);
        bundle.putExtra("rideID", rideID);
        startActivity(bundle);

        // bundle.putDouble("latval",extras.getDouble("latvalue"));
//           // bundle.putDouble("lngval",extras.getDouble("lngvalue"));
//            fragment.setArguments(bundle);
//
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }
    }


    public void currentbooking(String picpoint, String droppoint, String custname, String contactnumber, LatLng picLatLng, LatLng dropLatLng, String rideID) {

        Fragment fragment = null;
        fragment = new Fragment_currentbooking();
        //replacing the fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("picpoint", picpoint);
            bundle.putString("droppoint", droppoint);
            bundle.putString("custname", custname);
            bundle.putString("contactnumber", contactnumber);
            bundle.putDouble("piclat", picLatLng.latitude);
            bundle.putDouble("piclng", picLatLng.longitude);
            bundle.putDouble("droplat", dropLatLng.latitude);
            bundle.putDouble("droplng", dropLatLng.longitude);
            bundle.putString("rideID", rideID);

            // bundle.putDouble("latval",extras.getDouble("latvalue"));
            // bundle.putDouble("lngval",extras.getDouble("lngvalue"));
            fragment.setArguments(bundle);


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void ride() {

        Fragment fragment = null;
        fragment = new Fragment_driver_map();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
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
        if (isOnline()) {
            Toast.makeText(DriverPanel.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DriverPanel.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void rideRequest() {

        Fragment fragment = null;
        fragment = new Fragment_driver_ride_request();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    public void rideCompleted(String rideid) {

        Fragment fragment = null;
        fragment = new Fragment_endride();
        //replacing the fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("rideid", rideid);
            fragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    private void termandconditions() {

        Fragment fragment = null;
        fragment = new Fragment_driver_termconditions();
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }


    private void rating() {

        Fragment fragment = null;
        fragment = new Fragment_driver_rating();
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //  getMenuInflater().inflate(R.menu.driver_panel, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_request) {

        }

        if (id == R.id.nav_request) {
            rideRequest();
        }

        if (id == R.id.nav_report) {
            history();
            // Handle the camera action
        } else if (id == R.id.nav_onlineoffline) {
            offline();

        } else if (id == R.id.nav_rating) {

            rating();

        } else if (id == R.id.nav_callsupport) {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9599435086"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            startActivity(callIntent);

        } else if (id == R.id.nav_emailsupport) {
            final int min = 1000;
            final int max = 10000000;
            final int random = (new Random()).nextInt((max - min) + 1) + min;
            String domainname = "mrfavor.in";
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto: support@" + domainname));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Driver Ticket No. - " + random);
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));


        } else if (id == R.id.nav_ratecard) {
            ratecard();

        } else if (id == R.id.nav_termncond) {
            termandconditions();
        } else if (id == R.id.nav_logout) {

            try {
                if (AccessToken.getCurrentAccessToken() == null) {

                }else
                {
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                            .Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {

                            LoginManager.getInstance().logOut();

                        }
                    }).executeAsync();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = getSharedPreferences("Logininfo", MODE_PRIVATE).edit();

            editor.clear();
            editor.apply();

            Intent serviceintetn = new Intent(DriverPanel.this, Servicefordriver.class);
            stopService(serviceintetn);


            /*editor.putString("useremail", "");
            editor.putString("isLogedin", "");
            editor.putString("isPassenger", "");
            editor.commit();*/

            Intent i = new Intent(DriverPanel.this, LoginRegister.class);
            startActivity(i);
            finish();


        } else if (id == R.id.nav_share) {

            final String appPackageName = getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);


        }

        else if (id == R.id.upload_document) {

            uploadDocument();
        }



        else if (id == R.id.home1) {

            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new Home()).commit();
        }




        else if (id == R.id.share) {

            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new ShareDriver()).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void uploadDocument() {
        Intent ii = new Intent(getApplicationContext(), UploadDocument.class);
        startActivity(ii);
//        Fragment fragment = null;
//        fragment = new FragmentUploadDocument();
//        //replacing the fragment
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            final String lat = String.valueOf(mCurrentLocation.getLatitude());
            final String lng = String.valueOf(mCurrentLocation.getLongitude());
//            tvLocation.setText("At Time: " + mLastUpdateTime + "\n" +
//                    "Latitude: " + lat + "\n" +
//                    "Longitude: " + lng + "\n" +
//                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
//                    "Provider: " + mCurrentLocation.getProvider());
           // Toast.makeText(getApplicationContext(),lat+lng,Toast.LENGTH_LONG).show();





            String url = "http://mrfavor.in/myfavour/driver_current_location.php";
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                  //  Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_SHORT).show();
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
                    MyData.put("lat",lat); //Add the data you'd like to send to the server.
                    MyData.put("lng", lng); //Add the data you'd like to send to the server.
                   //Add the data you'd like to send to the server.
                    return MyData;
                }
            };
            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            MyRequestQueue.add(MyStringRequest);

















        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopLocationUpdates();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }















}

