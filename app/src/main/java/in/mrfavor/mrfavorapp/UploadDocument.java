package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadDocument extends AppCompatActivity {
    String vehicle_type, vehicle_make, vehicle_model, vehicle_color, year_manufacture, licen_plate_num;
    String vehicle_picture, vehicle_permit, vehicle_registration, vehicle_insurance, vehicle_license;
    String driver_id;
    ImageView profile_vehicle_hirepermit,profile_vehicle_register,profile_vehicle_insuranse;
    Button btn_vehicle_insuranse,btn_vehicle_registration,btn_vehicle_hirepermit;
    //private int RESULT_LOAD_IMG1 = 1;
    private int RESULT_LOAD_IMG2 = 2;
    private int RESULT_LOAD_IMG3 = 3;
    private int RESULT_LOAD_IMG4 = 4;
    //private int RESULT_LOAD_IMG5 = 5;

    public static String imgDecodableString1;
    public static  String imgDecodableString2;
    public static  String imgDecodableString3;
    public static String imgDecodableString4;
    public static  String imgDecodableString5;
    String email;
    String path;
    Button driver_vehicled_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_upload_document);
        SharedPreferences pref = getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

        email= pref.getString("useremail",null);


        btn_vehicle_hirepermit=(Button) findViewById(R.id.btn_vehicle_hirepermit);
        btn_vehicle_registration=(Button)findViewById(R.id.btn_vehicle_registration);
        btn_vehicle_insuranse=(Button) findViewById(R.id.btn_vehicle_insuranse);
        profile_vehicle_hirepermit=(ImageView)findViewById(R.id.profile_vehicle_hirepermit);
        profile_vehicle_insuranse=(ImageView)findViewById(R.id.profile_vehicle_insuranse);
        profile_vehicle_register=(ImageView)findViewById(R.id.profile_vehicle_register);
        driver_vehicled_update=(Button)findViewById(R.id.driver_vehicled_update);

        driver_vehicled_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavlist();

            }
        });

        btn_vehicle_hirepermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                Intent galleryIntent2 = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                // Start the Intent
//                startActivityForResult(galleryIntent2, RESULT_LOAD_IMG2);
//                System.out.println("CLicked pick image");

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG2);

            }
        });

        btn_vehicle_insuranse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent galleryIntent3 = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                // Start the Intent
//                startActivityForResult(galleryIntent3, RESULT_LOAD_IMG3);
//                System.out.println("CLicked pick image");
//



                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG3);




            }
        });

        btn_vehicle_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent galleryIntent4 = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                // Start the Intent
//                startActivityForResult(galleryIntent4, RESULT_LOAD_IMG4);
//                System.out.println("CLicked pick image");
//
//





                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG4);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMG2) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        path = getPathFromURI(selectedImageUri);
                       // Log.i(TAG, "Image Path : " + path);
                        // Set the image in ImageView
                        profile_vehicle_hirepermit.setImageURI(selectedImageUri);

                    }
                }
            }

            else   if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMG4) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        String path = getPathFromURI1(selectedImageUri);
                         Log.i("test", "Image Path : " + path);
                        // Set the image in ImageView
                        profile_vehicle_register.setImageURI(selectedImageUri);
                    }
                }
            }

         else   if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMG3) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        String path = getPathFromURI2(selectedImageUri);
                        // Log.i(TAG, "Image Path : " + path);
                        // Set the image in ImageView
                        profile_vehicle_insuranse.setImageURI(selectedImageUri);
                    }
                }
            }


        } catch (Exception e) {
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String getPathFromURI1(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String getPathFromURI2(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    void getFavlist()
    {
        String url = "http://mrfavor.in/myfavour/driver_current_location.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //       Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_SHORT).show();
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
                MyData.put("emailid", email); //Add the data you'd like to send to the server.
                MyData.put("dl_pic",path); //Add the data you'd like to send to the server.
                MyData.put("car_pic", path); //Add the data you'd like to send to the server.
                MyData.put("car_insurance_pic", path); //Add the data you'd like to send to the server.
                MyData.put("car_rc_pic", path); //Add the data you'd like to send to the server.
                //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(MyStringRequest);




    }


}
