package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myfavourcarpooling.easycarpooling.R;

import driver.AdapterforNewrideinfo;
import driver.Newrideinfo;

/**
 * Created by krishna on 7/9/2017.
 */
public class Fragment_driver_ride_request extends Fragment {
//    String baseurl = getResources().getString(R.string.baseurl);
    public final String url = "http://softcode.in/myfavour/newbooking.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.fragment_driver_ride_request, container, false);

        final RecyclerView requestlist = (RecyclerView) v.findViewById(R.id.rideRequestList);
        requestlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        SharedPreferences pref = getActivity().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

      String  userid = pref.getString("useremail",null);

        StringRequest request = new StringRequest(url+"?emailid="+userid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                Newrideinfo[] data = gson.fromJson(response, Newrideinfo[].class);

                 requestlist.setAdapter(new AdapterforNewrideinfo(getActivity(),data));



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        return  v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(" Mr. Favor");
    }

}
