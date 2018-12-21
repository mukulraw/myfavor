package in.mrfavor.mrfavorapp;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
public class Drawroutsonmap extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String amount;
    ArrayList<LatLng> MarkerPoints;
    TextView distance, fair;
    String userid, km, cartype, totaltimetaken;
    double totalkm;
    String plat, plng, pname, dlat, dlng, dname;
    int selectedcar;
    ImageView car1, car2;
    String pick, drop;
    double pick_lat, pick_lng, drop_lat, drop_lng;
    ImageView ivCar;

    ImageView check1 , check2 , check3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler();
         //execute in every 50000 ms
        setContentView(R.layout.activity_drawroutsonmap);
        final Button confirm = (Button) findViewById(R.id.btn_confirm);
        final Button schedule = (Button) findViewById(R.id.btn_schedule);
        final Button confirm1 = (Button) findViewById(R.id.btn_confirm1);
        final Button schedule1 = (Button) findViewById(R.id.btn_schedule1);
        ivCar=(ImageView)findViewById(R.id.ivCar);

        check1 = (ImageView) findViewById(R.id.check1);
        check2 = (ImageView) findViewById(R.id.check2);
        check3 = (ImageView) findViewById(R.id.check3);

        ivCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.VISIBLE);
                schedule.setVisibility(View.VISIBLE);
                confirm1.setVisibility(View.GONE);
                schedule1.setVisibility(View.GONE);


                check1.setVisibility(View.VISIBLE);
                check2.setVisibility(View.GONE);
                check3.setVisibility(View.GONE);

                getFavlist1();
                //calFair("Sharing");

              //  Toast.makeText(getApplicationContext(),"Sharing Feature will be Available very soon",Toast.LENGTH_LONG).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent ii = getIntent();
        pick = ii.getStringExtra("pick");
        drop = ii.getStringExtra("drop");
        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = coder.getFromLocationName(pick, 5);
            if (addresses == null) {
            }
            Address location = addresses.get(0);
            pick_lat = location.getLatitude();
            pick_lng = location.getLongitude();
        }   catch (IOException e) {
            e.printStackTrace();
        }
        List<Address> addresses1;
        try {
        addresses1 = coder.getFromLocationName(drop, 5);
        if (addresses1 == null) {
            }
            Address location = addresses1.get(0);
            drop_lat = location.getLatitude();
            drop_lng = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle(" Mr. Favor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.resize_iclogo);
        SharedPreferences pref = getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        userid = pref.getString("useremail", null);
        cartype = "Micro";
        car1 = (ImageView) findViewById(R.id.ivCar1);
        car2 = (ImageView) findViewById(R.id.ivCar2);
        car1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calFair("Micro");
          confirm.setVisibility(View.VISIBLE);
          schedule.setVisibility(View.VISIBLE);
          confirm1.setVisibility(View.GONE);
          schedule1.setVisibility(View.GONE);

                check2.setVisibility(View.VISIBLE);
                check1.setVisibility(View.GONE);
                check3.setVisibility(View.GONE);

            }
        });
        car2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.VISIBLE);
                schedule.setVisibility(View.VISIBLE);
                confirm1.setVisibility(View.GONE);
                schedule1.setVisibility(View.GONE);
                calFair("Sedan");

                check3.setVisibility(View.VISIBLE);
                check2.setVisibility(View.GONE);
                check1.setVisibility(View.GONE);

            }
        });
        //getActionBar().show();
        distance = (TextView) findViewById(R.id.tvDistance);
        fair = (TextView) findViewById(R.id.tvFair);

        confirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Toast.makeText(getApplicationContext(),"first select car type",Toast.LENGTH_LONG).show();
            }
        });
        schedule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"first select car type",Toast.LENGTH_LONG).show();

            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final Calendar currentDate = Calendar.getInstance();
            final Calendar date = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(Drawroutsonmap.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(Drawroutsonmap.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                //datetime.setText(new SimpleDateFormat("dd-MMM-yyyy h:mm a").format(date.getTime()));
                            }
                        },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
                datePickerDialog.show();
                Toast.makeText(getApplicationContext(),"schedule",Toast.LENGTH_LONG).show();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km = totalkm + "";
                new SendPostRequest(userid, ""+pick_lat, ""+pick_lng, ""+pick, ""+drop_lat, ""+drop_lng, drop, km).execute();
                //  new SendPostRequest("1","2","3","4","5","6","7","8").execute();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){
            Toast.makeText(Drawroutsonmap.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Drawroutsonmap.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady (GoogleMap googleMap){
        SharedPreferences pref = getSharedPreferences("pickuppoint", Context.MODE_PRIVATE);
//            String pLocname = pref.getString("p_locname", "Not Selected");
//            double pLatval = Double.parseDouble(pref.getString("p_latval", "0"));
//            double pLngval = Double.parseDouble(pref.getString("p_lngval", "0"));
        plat = String.valueOf(pick_lat);
        plng = String.valueOf(pick_lng);
        pname = pick;
        Bundle extras;
        extras = getIntent().getExtras();
        String locname = extras.getString("locname");
        double latval = extras.getDouble("latvalue");
        double lngval = extras.getDouble("lngvalue");
        dlat = String.valueOf(drop_lat);
        dlng = String.valueOf(drop_lng);
        dname = drop;
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(pick_lat, pick_lng);
        LatLng dest = new LatLng(drop_lat, drop_lng);
        String url = getUrl(sydney, dest);
        FetchUrl FetchUrl = new FetchUrl();
        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        MarkerOptions marker1 = new MarkerOptions().position(sydney).title(pick);
        MarkerOptions marker2 = new MarkerOptions().position(dest).title(drop);
        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.startmarker));
        marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.endmarker));
        mMap.addMarker(marker1);
        mMap.addMarker(marker2);
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(sydney);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.userboard, menu);
        return true;
    }
    public double CalculationByDistanceByradius(LatLng StartP, LatLng EndP) {
        int Radius = 6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }
    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        String GOOGLE_API_KEY = "AIzaSyBA95_DKOdwSP3OG6SLvdtT0o0jO1w5bpM";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&key=" + GOOGLE_API_KEY;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.d("Routeurl", url);
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

            Log.d("Totaldistancekm", distancekm + "");

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

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
                totaltimetaken = duration;

                Log.d("totalduration", duration);

                Log.i("Distance", distance.toString());
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
                totalkm = totaldistance;
                distance.setText("Total Distance: " + totaldistance + " KM (Approx) Takes " + distandtime + " (Approx)");
                double amt = totaldistance * 7 + 50;
                //  fair.setText("Total Fair: "+amt+" Rupees");
                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                calFair("Micro");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String insurance;
        String userid, picklat, picklng, picklocname, droplat, droplng, droplocname, distancekm;
        ProgressDialog pd;

        public SendPostRequest(String p1, String p2, String p3, String p4, String p5, String p6, String p7, String p8) {
            this.userid = p1;
            this.picklat = p2;
            this.picklng = p3;
            this.picklocname = p4;
            this.droplat = p5;
            this.droplng = p6;
            this.droplocname = p7;
            this.distancekm = p8;

        }

        protected void onPreExecute() {
            pd = new ProgressDialog(Drawroutsonmap.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        protected String doInBackground(String... arg0) {

            try {
                String baseurl = getResources().getString(R.string.baseurl);
                URL url = new URL(baseurl + "/myfavour/confirmbooking.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", userid);
                postDataParams.put("picklat", picklat);
                postDataParams.put("picklng", picklng);
                postDataParams.put("picklocname", picklocname);
                postDataParams.put("droplat", droplat);
                postDataParams.put("droplng", droplng);
                postDataParams.put("droplocname", droplocname);
                postDataParams.put("distance", distancekm);
                postDataParams.put("cartype", cartype);

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("systemmsg", s.toString());
            if (s.contentEquals("error")) {
                showalert("Some error occurred please try again.");


            } else {
                 Toast.makeText(Drawroutsonmap.this,"Your booking is confirmed.",Toast.LENGTH_LONG).show();
                Intent i = new Intent(Drawroutsonmap.this, Bookingconfirmationalert.class);
                i.putExtra("bookingID", "#" + s);
                i.putExtra("amount",amount);
                startActivity(i);
                finishAffinity();
            }
            if (pd != null) {
                pd.dismiss();
            }
        }

        private void showalert(String s) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    Drawroutsonmap.this).create();

            // Setting Dialog Title
            //  alertDialog.setTitle("System Message");

            // Setting Dialog Message
            alertDialog.setMessage(s);

            // Setting Icon to Dialog
            //   alertDialog.setIcon(R.drawable.warning);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed

                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

double totalfair,gst;
    private void calFair(String typeofcar) {

        int hours, minutes;
        int totalminutes;

        cartype = typeofcar;

try
{
    if (totaltimetaken.contains("hour")) {
        String[] tmp = totaltimetaken.split("hour");
        hours = Integer.parseInt(tmp[0].replaceAll("[^0-9]", ""));
        minutes = Integer.parseInt(tmp[1].replaceAll("[^0-9]", ""));
        totalminutes = hours * 60 + minutes;
    } else {
        minutes = Integer.parseInt(totaltimetaken.replaceAll("[^0-9]", ""));
        totalminutes = minutes;
    }

    if (typeofcar.contentEquals("Micro")) {
        totalfair = totalkm * 5.7 + 40 + totalminutes * 1.5;


    } else {// for sedan car
        totalfair = totalkm * 10 + 50 + totalminutes * 1.75;
    }
    gst = totalfair * 5 / 100;
    totalfair = totalfair + gst;
    totalfair = Math.round(totalfair);
    fair.setText("Total Fair: " + totalfair + " Rupees");
    amount= String.valueOf(totalfair);
}
catch (NullPointerException e)
{
    e.printStackTrace();
}


        // Toast.makeText(Drawroutsonmap.this,"Total KM "+totalkm+" and "+cartype+" Time "+totalminutes,Toast.LENGTH_LONG).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();

    }
    void getFavlist1()
    {

        SharedPreferences pref = getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail",null);

        String url = "http://softcode.in/myfavour/sharetaxi_status.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                if(response.equalsIgnoreCase("0"))
                {
                    Toast.makeText(getApplicationContext(),"Sharing Taxi not available for this route . kindly try after some time",Toast.LENGTH_LONG).show();
                }
                else {

                }


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
                MyData.put("emailid",userid);
                MyData.put("picup_lat",plat);
                MyData.put("picup_lng",plng);
                MyData.put("drop_lat",dlat);
                MyData.put("drop_lng",dlng);

                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        MyRequestQueue.add(MyStringRequest);
    }
}





