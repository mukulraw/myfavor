package in.mrfavor.mrfavorapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;



public class Login extends AppCompatActivity {
    Button loginbtn;
    EditText userid, password;
    TextView signup,forgetpass;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        loginbtn = (Button) findViewById(R.id.btn_login);
        userid = (EditText) findViewById(R.id.etLogin);
        password = (EditText) findViewById(R.id.etPassword);
        signup = (TextView) findViewById(R.id.link_signup);
TextView signup2 = (TextView) findViewById(R.id.link_signup2);

        forgetpass = (TextView) findViewById(R.id.tvForgotPassword);


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/tcb.ttf");

        loginbtn.setTypeface(custom_font);



        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(Login.this,Forgetpassword.class);
              //Intent i = new Intent(Login.this,LocationAutocomplet.class);
            startActivity(i);

            //    Intent i = new Intent(Login.this,Bookingconfirmationalert.class);
             //   i.putExtra("bookingID","#21312321312");
              //  startActivity(i);

            }
        });


        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int validation = 0;
                if (userid.getText().length() == 0) {
                    userid.setError("Please enter login id");
                    validation++;
                }

                if (password.getText().length() == 0) {
                    password.setError("Please enter password");
                    validation++;
                }

if(validation==0) {
    new SendPostRequest(userid.getText().toString(), password.getText().toString()).execute();
}

            }
        });



        GMapV2Direction gMapV2Direction = new GMapV2Direction();

        LatLng startpoint = new LatLng(28.6089863,76.9854232);

        LatLng endpoint = new LatLng(28.5502102,	77.2502537);

        Document document = gMapV2Direction.getDocument(startpoint,endpoint,"driving");

       int km = gMapV2Direction.getDistanceValue(document);



    }






    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String id,pass;
        ProgressDialog pd;
        public SendPostRequest(String i,String p) {
            this.id = i;
            this.pass = p;
        }


        protected String doInBackground(String... arg0) {

            try {
                String baseurl = getResources().getString(R.string.baseurl);
                URL url = new URL(baseurl+"/myfavour/login.php"); // here is your URL path


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id", id);
                postDataParams.put("pass",pass);
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

            if(s.contains("exists"))
            {

                String[] tmp1 = s.split("_");
                String regas = tmp1[1];

                SharedPreferences.Editor editor = getSharedPreferences("Logininfo", MODE_PRIVATE).edit();
                editor.putString("useremail",id);
                editor.putString("isLogedin","yes");


                Intent i = null;

                if(regas.contentEquals("Add Taxi/Cab"))
                {
                    i = new Intent(Login.this, DriverPanel.class);
                    editor.putString("isPassenger","no");
                }
                else {
                  i = new Intent(Login.this, Userboard.class);
                    editor.putString("isPassenger","yes");
                }
                editor.commit();
                i.putExtra("emailid",id);
                startActivity(i);
                finishAffinity();

            }
            else {

                if(s.contentEquals("otp"))
                {
                    Intent i = new Intent(Login.this,Otpverify.class);
                    i.putExtra("emailid",id);
                    startActivity(i);
                    finishAffinity();
                }
                else {

                    if(s.contains("fulldetails"))
                    {

                        String[] tmp = s.split("_");
                        String mobileno = tmp[1];


                        Intent i = new Intent(Login.this,Registerbasicdetails.class);
                        i.putExtra("emailid",id);
                        i.putExtra("mobileno",mobileno);
                        startActivity(i);
                        finishAffinity();

                    }
                    else {
                        showalert(s);
                    }
                }

            }
            if (pd != null)
            {
                pd.dismiss();
            }


        }



        private void showalert(String s) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    Login.this).create();

            // Setting Dialog Title
           // alertDialog.setTitle("System Message");

            // Setting Dialog Message
            alertDialog.setMessage(s);

            // Setting Icon to Dialog
        //    alertDialog.setIcon(R.drawable.warning);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                 //   Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
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

        while(itr.hasNext()){

            String key= itr.next();
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

}
