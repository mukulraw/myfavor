package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;


import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.google.android.libraries.places.api.Places;

public class MainActivity234 extends AppCompatActivity implements PlaceSelectionListener {
    //PlaceAutocompleteFragment autocompleteFragment;
    ArrayList<DataModel_drop> dataModels;
    ListView listView;
    String userid;

    String TAG = ":adasa";

    String apiKey = "AIzaSyBc5BKE1OW-9_IV39xiyTab5H7YG21awgw";
    private static CustomAdapter_drop adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main234);

        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);

         //execute in every 50000 ms


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME , Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(this);

        autocompleteFragment.setHint("Search drop location");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail",null);





        listView=(ListView)findViewById(R.id.list);

        getFavlist();
//        dataModels= new ArrayList<>();
//
//        dataModels.add(new DataModel_drop("Apple Pie", "Android 1.0", "1","September 23, 2008"));
//        dataModels.add(new DataModel_drop("Banana Bread", "Android 1.1", "2","February 9, 2009"));
//        dataModels.add(new DataModel_drop("Cupcake", "Android 1.5", "3","April 27, 2009"));
//        dataModels.add(new DataModel_drop("Donut","Android 1.6","4","September 15, 2009"));
//
//
//        adapter= new CustomAdapter_drop(dataModels,getApplicationContext());
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                DataModel_drop dataModel= dataModels.get(position);
//
//                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//            }
//        });





        //  getFavlist();

    }

    String pick,drop;


    @Override
    public void onPlaceSelected(@NonNull Place place) {
        drop= place.getAddress().toString();

        Intent intent = getIntent();
        intent.putExtra("drop", drop);
        setResult(RESULT_OK, intent);
        //   Toast.makeText(getApplicationContext(),drop,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onError(Status status) {

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
            Toast.makeText(MainActivity234.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity234.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    void getFavlist()
    {


        String url = "http://mrfavor.in/myfavour/fav_route_droplocation.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();

                dataModels= new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.optJSONObject(i);
                        String drop_location_name=jsonObject.optString("pic_location_name");


                        dataModels.add(new DataModel_drop(drop_location_name, "", "",""));




                    }
//                    dataModels= new ArrayList<>();
//                    dataModels.add(new DataModel(pickloc, droploc, "",""));



                }



                catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter= new CustomAdapter_drop(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModel_drop dataModel= dataModels.get(position);


                       // DataModel_pick dataModel= dataModels.get(position);
                        Toast.makeText(getApplicationContext(),dataModel.getName(),Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        intent.putExtra("drop", dataModel.getName());
                        setResult(RESULT_OK, intent);
                        //    Toast.makeText(getApplicationContext(),pick,Toast.LENGTH_LONG).show();
                        finish();





//                        Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                                .setAction("No action", null).show();
                    }
                });


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
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(MyStringRequest);
    }




}
