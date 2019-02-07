package driver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myfavourcarpooling.easycarpooling.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krishna on 2/5/2018.
 */

public class Fragment_endride extends Fragment {
        String rideid;
        String distance1;
        TextView price;
        public Fragment_endride() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_ridecompleted, container, false);
        distance1 = getArguments().getString("distance");
        TextView distance=(TextView)v.findViewById(R.id.distance);
         price=(TextView)v.findViewById(R.id.price);
        distance.setText("Total distance covered : "+distance1);
        rideid = getArguments().getString("rideid");
        payment();


       // Toast.makeText(getContext(),"Ride ID "+rideid,Toast.LENGTH_LONG).show();

        return v;


    }
    void payment()
    {
        String url = "http://softcode.in/myfavour/cal_fair.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                price.setText("Total Price "+response);
             //   Toast.makeText(getActivity(),"response"+response,Toast.LENGTH_LONG).show();


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
                MyData.put("rideid", rideid);
                MyData.put("km", distance1);

                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Ride completed");
    }
}
