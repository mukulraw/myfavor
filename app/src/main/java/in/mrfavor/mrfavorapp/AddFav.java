package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFav extends AppCompatActivity {

    ArrayList<DataModel> dataModels;
    ListView listView;
    String userid;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.iclogo);
        getSupportActionBar().setTitle("          Mr. Favor");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail",null);
        listView=(ListView)findViewById(R.id.list);


        getFavlist();
















    }
    String pickloc,droploc;


    void getFavlist()
    {


        String url = "http://mrfavor.in/myfavour/fav_route_list.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataModels= new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.optJSONObject(i);
                          pickloc=jsonObject.optString("pic_location_name");
                          droploc=jsonObject.optString("drop_location_name");

                        dataModels.add(new DataModel(pickloc, droploc, "",""));




                    }
//                    dataModels= new ArrayList<>();
//                    dataModels.add(new DataModel(pickloc, droploc, "",""));



                }



                     catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter= new CustomAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModel dataModel= dataModels.get(position);
                        //Toast.makeText(getApplicationContext(),dataModel.getName()+dataModel.getType(),Toast.LENGTH_LONG).show();
                        Intent ii=new Intent(getApplicationContext(),Drawroutsonmap.class);
                        ii.putExtra("pick",dataModel.getName());
                        ii.putExtra("drop",dataModel.getType());
                        startActivity(ii);
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





}
