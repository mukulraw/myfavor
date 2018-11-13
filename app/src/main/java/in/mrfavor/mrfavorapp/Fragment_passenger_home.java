package in.mrfavor.mrfavorapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myfavourcarpooling.easycarpooling.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Fragment_passenger_home extends Fragment implements AdapterView.OnItemSelectedListener {

    String emailid = "";
    String mobileno = "";
    CircleImageView imagepp;
    public  static final int RequestPermissionCode  = 1 ;
    TextView tvpickdate;
    private DatePickerDialog fromDatePickerDialog;
    EditText etmobile,etemailid,etfullname;
    private Spinner spinner;
    EditText fullname,mobileno1;
    TextView email,gen;
    String up_fullnamme,up_mobileno;
    AppCompatButton update;
    String userid;
    private static final String[]paths = {"Passenger", "Pool a car", "Add Taxi/Cab"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.fragment_homefragment, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        gen=(TextView)v.findViewById(R.id.textView91);
        email=(TextView)v.findViewById(R.id.etEmailid);
        userid = pref.getString("useremail",null);
        EnableRuntimePermission();
        imagepp=(CircleImageView) v.findViewById(R.id.profile_image);
        fullname=(EditText)v.findViewById(R.id.etFullname) ;
        mobileno1=(EditText)v.findViewById(R.id.etMobileno);
        up_fullnamme=fullname.getText().toString();
        up_mobileno=mobileno1.getText().toString();
        getProfile();
        update=(AppCompatButton)v.findViewById(R.id.btnUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate();
             //   Toast.makeText(getActivity(),up_fullnamme+up_mobileno,Toast.LENGTH_LONG).show();
            }
        });


        imagepp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 7);
            }
        });

      //
        //  getActivity().getActionBar().setTitle("tytytyt");

        spinner = (Spinner)v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        tvpickdate = (TextView) v.findViewById(R.id.tvPickdata);


        setshowdate();

        tvpickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        return  v;
    }

    private void setshowdate() {
    final SimpleDateFormat dateFormatter = null;
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String ss = ""+newDate.YEAR;
                tvpickdate.setText(ss);

               // tvpickdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(" Mr. Favor");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    Bitmap bitmap;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {

             bitmap = (Bitmap) data.getExtras().get("data");

            imagepp.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                android.Manifest.permission.CAMERA))
        {

            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                   android.Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(),"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(),"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    void profileUpdate()
    {
final String up_mobileno=mobileno1.getText().toString();
      final String   up_fullnamme=fullname.getText().toString();

     //   Toast.makeText(getActivity(),up_fullnamme+up_mobileno,Toast.LENGTH_LONG).show();


        String url = "http://softcode.in/myfavour/passenger_pic_update.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getActivity(),"Response"+response,Toast.LENGTH_SHORT).show();
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
                MyData.put("profilepic", String.valueOf(bitmap)); //Add the data you'd like to send to the server.
                MyData.put("full_name", up_fullnamme); //Add the data you'd like to send to the server.
                MyData.put("phone_number", up_mobileno); //Add the data you'd like to send to the server.

                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);





    }









    void getProfile()
    {

        String url = "http://softcode.in/myfavour/passenger_get_profile.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
try {


    JSONArray jsonArray = new JSONArray(response);

    for (int i = 0; i < jsonArray.length(); i++) {

        JSONObject jsonObject = jsonArray.optJSONObject(i);
        String profile_pic = jsonObject.optString("profile_pic");
        String gender = jsonObject.optString("gender");
        String full_name = jsonObject.optString("full_name");
        String emailId = jsonObject.optString("email");
        String ph_no = jsonObject.optString("phone_number");
        Picasso.with(getActivity()).load(profile_pic).into(imagepp);
     //   imagepp.setImageDrawable(Drawable.createFromPath(profile_pic));
        gen.setText(gender);
        fullname.setText(full_name);
        email.setText(emailId);
        mobileno1.setText(ph_no);

       // Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

    }
}catch (JSONException e)
{
    e.printStackTrace();
}
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
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(MyStringRequest);





    }

}
