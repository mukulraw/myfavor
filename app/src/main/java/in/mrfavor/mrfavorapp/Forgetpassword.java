package in.mrfavor.mrfavorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Forgetpassword extends AppCompatActivity {

    TextView tvmsg;
    EditText etemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        tvmsg = (TextView) findViewById(R.id.tvMsg);
        ActionBar ab = getSupportActionBar();

        ab.setTitle("Forgot Password");
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        etemail = (EditText) findViewById(R.id.etEmail);
        Button btn = (Button) findViewById(R.id.btn_login);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/tcb.ttf");

        btn.setTypeface(custom_font);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int validation = 0;
                if (etemail.getText().length() == 0) {
                    etemail.setError("Please enter email ID");
                    validation++;
                }


                if (validation == 0) {
                    new SendPostRequest(etemail.getText().toString()).execute();
                }

            }
        });


    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String emailid;
        ProgressDialog pd;

        public SendPostRequest(String id) {
            this.emailid = id;
        }

        protected void onPreExecute() {
            pd = new ProgressDialog(Forgetpassword.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        protected String doInBackground(String... arg0) {

            try {
                String baseurl = getResources().getString(R.string.baseurl);
                URL url = new URL(baseurl + "/myfavour/forgetpass.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", emailid);
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

            if (s.contains("ok")) {

                showalert("Your password is recoverd successfully and sent on " + etemail.getText() + ". Please check your email.");

            } else {
                showalert(s);

            }

            if (pd != null) {
                pd.dismiss();
            }


        }


        private void showalert(String s) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    Forgetpassword.this).create();

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
                    //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if(i== android.R.id.home)
        {
            finish();
            Intent ia = new Intent(Forgetpassword.this, Login.class);
            startActivity(ia);
        }

        if (i == R.id.action_settings) {
            finish();
            Intent ia = new Intent(Forgetpassword.this, LoginRegister.class);
            startActivity(ia);
        }
        return true;

    }
}
