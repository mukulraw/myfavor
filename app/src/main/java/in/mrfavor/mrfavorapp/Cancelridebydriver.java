package in.mrfavor.mrfavorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONObject;

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

public class Cancelridebydriver extends AppCompatActivity {
    String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelridebydriver);
        checkConnection();

        Intent i = getIntent();

        final String rideid = i.getStringExtra("ride");

        EditText etreason = (EditText) findViewById(R.id.etReason);

        reason = etreason.getText().toString();

        Button btnclose = (Button) findViewById(R.id.btn_close);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/tcb.ttf");

        btnclose.setTypeface(custom_font);


        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btndone = (Button) findViewById(R.id.btn_done);

        btndone.setTypeface(custom_font);

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Postcancelrequest postcancelrequest = new Postcancelrequest(Cancelridebydriver.this,rideid,reason);
                postcancelrequest.execute();
            }
        });


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
            Toast.makeText(Cancelridebydriver.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Cancelridebydriver.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void gotoDriverpanel()
    {
        Intent i = new Intent(Cancelridebydriver.this,DriverPanel.class);
        startActivity(i);
    }

}


class Postcancelrequest extends AsyncTask<String,Void,String>
        {
            String rideID;
            String reason;
            Context context;
            ProgressDialog pd;

            public Postcancelrequest(Context context,String rideID,String reason) {
                this.context = context;
                this.rideID = rideID;
                this.reason = reason;
            }

            @Override
            protected void onPreExecute() {

                pd = new ProgressDialog(context);
                pd.setMessage("Please wait...");
                pd.show();
            }
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String baseurl = context.getResources().getString(R.string.baseurl);
                    URL url = new URL(baseurl+"/myfavour/cancelridebydriver.php"); // here is your URL path


                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("rideid", rideID);
                    postDataParams.put("reason", reason);

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

                if(s.contentEquals("Done"))
                {

                    SharedPreferences pref = context.getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

                   String  userid = pref.getString("useremail",null);

                    Intent i = new Intent(context,DriverPanel.class);
                    i.putExtra("emailid",userid);
                    context.startActivity(i);
                   // ((DriverPanel)context).rideRequest();
                }
                else
                {
                    showalert(s);
                }

                if (pd != null)
                {
                    pd.dismiss();
                }

            }



            private void showalert(String s) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        context).create();

                // Setting Dialog Title
               // alertDialog.setTitle("System Message");

                // Setting Dialog Message
                alertDialog.setMessage(s);

                // Setting Icon to Dialog
              //  alertDialog.setIcon(R.drawable.warning);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                     //   Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
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
