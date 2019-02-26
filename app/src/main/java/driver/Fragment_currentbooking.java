package driver;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import in.mrfavor.mrfavorapp.AcceptAcivity;
import in.mrfavor.mrfavorapp.Cancelridebydriver;
import in.mrfavor.mrfavorapp.DataParser;
import in.mrfavor.mrfavorapp.SharePreferenceUtils;

import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class Fragment_currentbooking extends Fragment implements OnMapReadyCallback {
    // String baseurl = getResources().getString(R.string.baseurl);
    public static final String url = "http://softcode.in/myfavour/acceptbooking.php";
    String picloc, droploc, custname, contactnumber, rideID, userid, payMode, fair;
    double piclat, piclng, droplat, droplng;
    LatLng startLatlng, endLatlng;
    MapView mMapView;
    Button btnStartride, btnCanelride, btnStopride;
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.fragment_driver_currentbooking, container, false);
        picloc = getArguments().getString("picpoint");
        droploc = getArguments().getString("droppoint");
        custname = getArguments().getString("custname");
        contactnumber = getArguments().getString("contactnumber");
        payMode = getArguments().getString("payMode");
        fair = getArguments().getString("fair");
        piclat = getArguments().getDouble("piclat");
        piclng = getArguments().getDouble("piclng");
        droplat = getArguments().getDouble("droplat");
        droplng = getArguments().getDouble("droplng");
        //  Toast.makeText(getActivity(),piclat+piclng+droplat+droplng+"",Toast.LENGTH_LONG).show();

        Toast.makeText(getActivity(), "picklat" + droplat + "piclng" + droplng, Toast.LENGTH_LONG).show();
        rideID = getArguments().getString("rideID");
        startLatlng = new LatLng(piclat, piclng);
        endLatlng = new LatLng(droplat, droplng);
        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        TextView tvcustname = (TextView) v.findViewById(R.id.tvUsername);
        TextView startride = (TextView) v.findViewById(R.id.tvStartride);
        TextView endride = (TextView) v.findViewById(R.id.tvEndride);
        tvcustname.setText(custname + " (" + contactnumber + ")");
        startride.setText(picloc);
        endride.setText(droploc);
        final Button btncancel = (Button) v.findViewById(R.id.btn_canel);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/tcb.ttf");
        btncancel.setTypeface(custom_font);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharePreferenceUtils.getInstance().deletePref();

                Intent i = new Intent(getActivity(), Cancelridebydriver.class);
                i.putExtra("ride", rideID);
                startActivity(i);
            }
        });

        btnStartride = (Button) v.findViewById(R.id.btn_start);


        btnStartride.setTypeface(custom_font);

        btnStartride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btncancel.setVisibility(View.GONE);

                getlocationAndPostRequest("StartrideRequest");
            }
        });
        Button locationreached = (Button) v.findViewById(R.id.btn_alerttocustomer);
        locationreached.setTypeface(custom_font);
        locationreached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlocationAndPostRequest("LocationreachedRequest");
            }
        });
        Button btnendride = (Button) v.findViewById(R.id.btn_end);
        btnendride.setTypeface(custom_font);
        btnendride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.ride_end_layout);
                dialog.show();


                TextView amm = dialog.findViewById(R.id.textView15);
                Button fin = dialog.findViewById(R.id.textView16);


                if (payMode.equals("Wallet"))
                {
                    amm.setText("\u20B9 00");
                }
                else
                {
                    amm.setText("\u20B9 " + fair);
                }



                fin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog.dismiss();
                        SharePreferenceUtils.getInstance().deletePref();
                        getlocationAndPostRequest("endRideRequest");

                    }
                });


            }
        });
        Button nagivation = (Button) v.findViewById(R.id.btn_Navigate);
        nagivation.setTypeface(custom_font);
        nagivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + piclat + "," + piclng + "&daddr=" + droplat + "," + droplng));
                // intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        mMapView.getMapAsync(this);

        return v;
    }

    public void getlocationAndPostRequest(String requestfor) // request for LocationreachedRequest/StartrideRequest
    {
        LocationManager locationManager;
        String lattitude, longitude;
        final int REQUEST_LOCATION = 1;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        }
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setValsforrequest(lattitude, longitude, requestfor);
                // Toast.makeText(Firstactivity.this,"Location "+ lattitude+longitude,Toast.LENGTH_LONG).show();
            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setValsforrequest(lattitude, longitude, requestfor);
                //  Toast.makeText(Firstactivity.this,"Location 2 "+ lattitude+longitude,Toast.LENGTH_LONG).show();
            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                setValsforrequest(lattitude, longitude, requestfor);
                // Toast.makeText(Firstactivity.this,"Location 3 "+ lattitude+longitude,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Enable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String startridelat, enstartridelong;

    private void setValsforrequest(String lattitude, String longitude, String requestfor) {

        if (requestfor.contentEquals("LocationreachedRequest")) {
            LocationreachedRequest locationreachedRequest = new LocationreachedRequest(Fragment_currentbooking.this, getContext(), rideID, lattitude, longitude);
            locationreachedRequest.execute();
        } else if (requestfor.contentEquals("StartrideRequest")) {
            StartrideRequest startriderequest = new StartrideRequest(Fragment_currentbooking.this, getContext(), rideID, lattitude, longitude);
            startriderequest.execute();
            startridelat = lattitude;
            enstartridelong = longitude;

        } else if (requestfor.contentEquals("endRideRequest")) {
            EndrideRequest endriderequest = new EndrideRequest(Fragment_currentbooking.this, getContext(), rideID, lattitude, longitude, startridelat, enstartridelong);
            endriderequest.execute();
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Current Booking");
    }

    //    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        mMap = googleMap;
//        mMap.getUiSettings().setMapToolbarEnabled(false);
//
//
//       String url = getUrl(startLatlng, endLatlng);
//        FetchUrl FetchUrl = new FetchUrl();
//        // Start downloading json data from Google Directions API
//        Log.d("urlforfetch",url);
//      FetchUrl.execute(url);
//
//        MarkerOptions marker1 = new MarkerOptions().position(startLatlng).title(picloc);
//        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.startmarker));
//
//        MarkerOptions marker2 = new MarkerOptions().position(endLatlng).title(droploc);
//        marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.endmarker));
//
//        mMap.addMarker(marker1);
//        mMap.addMarker(marker2);
//
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        builder.include(startLatlng);
//        builder.include(endLatlng);
//        LatLngBounds bounds = builder.build();
//
//        int padding = 100; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        mMap.animateCamera(cu);
//
//    }
//
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        String url = getUrl(startLatlng, endLatlng);
        FetchUrl FetchUrl = new FetchUrl();
        // Start downloading json data from Google Directions API
        Log.d("urlforfetch", url);
        FetchUrl.execute(url);

        MarkerOptions marker1 = new MarkerOptions().position(startLatlng).title(picloc);
        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.startmarker));

        MarkerOptions marker2 = new MarkerOptions().position(endLatlng).title(droploc);
        marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.endmarker));

        mMap.addMarker(marker1);
        mMap.addMarker(marker2);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(startLatlng);
        builder.include(endLatlng);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        //int padding = 100; // offset from edges of the map in pixels
        //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String GOOGLE_API_KEY = "AIzaSyBc5BKE1OW-9_IV39xiyTab5H7YG21awgw";

        // Building the paramet
        // ers to the web service
        String parameters = str_origin + "&" + str_dest + "&key=" + GOOGLE_API_KEY;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        String distandtime;
        double totaldistance;

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;


            double distancekm = getdistance(jsonData[0]);


            try {
                jObject = new JSONObject(jsonData[0]);

                DataParser parser = new DataParser();


                // Starts parsing data
                routes = parser.parse(jObject);


            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        private double getdistance(String jsonDatum) {
            double dist = 0.0;
            String duration = "";
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject = new JSONObject(jsonDatum.toString());

                JSONArray array = jsonObject.getJSONArray("routes");

                JSONObject routes = array.getJSONObject(0);

                JSONArray legs = routes.getJSONArray("legs");

                JSONObject steps = legs.getJSONObject(0);

                JSONObject distancekm = steps.getJSONObject("distance");
                JSONObject durationobj = steps.getJSONObject("duration");

                duration = durationobj.getString("text");


                dist = Double.parseDouble(distancekm.getString("text").replaceAll("[^\\.0123456789]", ""));
                totaldistance = dist;
                distandtime = duration;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return dist;
        }


        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.BLUE);

                // distance.setText("Total Distance: "+totaldistance+" KM (Approx) Takes "+distandtime+" (Approx)");
                double amt = totaldistance * 7 + 50;
                //  fair.setText("Total Fair: "+amt+" Rupees");
                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

}
