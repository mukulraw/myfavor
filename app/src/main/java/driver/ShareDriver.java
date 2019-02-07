package driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.mrfavor.mrfavorapp.CustomAdapter;
import in.mrfavor.mrfavorapp.DataModel;
import in.mrfavor.mrfavorapp.Drawroutsonmap;

public class ShareDriver extends Fragment {
    Button shareoff, shareon;
String userid;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.fragment_share, container, false);
        getFavlist2();
        shareoff = (Button) v.findViewById(R.id.off);
        shareon = (Button) v.findViewById(R.id.on);

        shareon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareon.setBackgroundColor(getResources().getColor(R.color.orangeon));
                shareoff.setBackgroundColor(getResources().getColor(R.color.orangeoff));
                getFavlist();


            }
        });

shareoff.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        shareoff.setBackgroundColor(getResources().getColor(R.color.orangeon));
        shareon.setBackgroundColor(getResources().getColor(R.color.orangeoff));


        getFavlist1();
    }
});
        return v;
    }


    void getFavlist()
    {

        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail",null);

        String url = "http://softcode.in/myfavour/driver_sharetaxi_onoff.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();



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
                MyData.put("sharetaxi","Yes");
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }





    void getFavlist1()
    {

        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail",null);

        String url = "http://softcode.in/myfavour/driver_sharetaxi_onoff.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();


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
                MyData.put("sharetaxi","No");
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }




    void getFavlist2()
    {

        SharedPreferences pref = getContext().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        userid = pref.getString("useremail",null);

        String url = "http://softcode.in/myfavour/driver_get_sharetaxi_onoff.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
if(response.equalsIgnoreCase("No"))
{
    shareoff.setBackgroundColor(getResources().getColor(R.color.orangeon));
}
else if(response.equalsIgnoreCase("Yes"))
{
    shareon.setBackgroundColor(getResources().getColor(R.color.orangeon));
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
               // MyData.put("sharetaxi","No");
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }}








