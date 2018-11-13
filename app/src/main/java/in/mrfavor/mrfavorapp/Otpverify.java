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

public class Otpverify extends AppCompatActivity {
    Button otpverify, resendotp;
    EditText etotp;
    String mobileno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        otpverify = (Button) findViewById(R.id.btnverifyotp);
        resendotp = (Button) findViewById(R.id.btnRegenerateOTP);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/tcb.ttf");
        otpverify.setTypeface(custom_font);
        resendotp.setTypeface(custom_font);
        etotp = (EditText) findViewById(R.id.etOTP);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String emailid = "";

        if (bundle != null) {
            emailid = bundle.getString("emailid").toString();
            try {
                mobileno = bundle.getString("mobileno").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        final String baseurl = getResources().getString(R.string.baseurl);
        final String finalEmailid = emailid;
        otpverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int validation = 0;
                if (etotp.length() == 0) {
                    validation++;
                    etotp.setError("Please enter full name.");
                }

                if (validation == 0) {
                    new SendPostRequest(finalEmailid, etotp.getText().toString()).execute(baseurl + "/myfavour/verifyotp.php");
                }
            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPostRequest(finalEmailid, "0").execute(baseurl + "/myfavour/resendotp.php");
            }
        });

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String emailid, otp;
        ProgressDialog pd;

        public SendPostRequest(String em, String o) {
            this.emailid = em;
            this.otp = o;

        }

        protected void onPreExecute() {
            pd = new ProgressDialog(Otpverify.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        protected String doInBackground(String... arg0) {
            try {
                String weburl = arg0[0];
                URL url = new URL(weburl); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("emailid", emailid);
                postDataParams.put("otp", otp);


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
            if (s.contentEquals("verified")) {
                SharedPreferences.Editor editor = getSharedPreferences("Logininfo", MODE_PRIVATE).edit();
                editor.putString("useremail", emailid);
                editor.putString("isLogedin", "yes");
                editor.commit();
                Intent i = new Intent(Otpverify.this, Registerbasicdetails.class);
                i.putExtra("emailid", emailid);
                i.putExtra("mobileno", mobileno);
                startActivity(i);
                finishAffinity();

            } else {
                showalert(s);

            }
            if (pd != null) {
                pd.dismiss();
            }
        }

        private void showalert(String s) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    Otpverify.this).create();
            // Setting Dialog Title
            // alertDialog.setTitle("System Message");
            if (s.contentEquals("otp")) {
                // Setting Dialog Message
                alertDialog.setMessage("OTP sent successfully.");
                // Setting Icon to Dialog
                // alertDialog.setIcon(R.drawable.warning);
            } else {
                // Setting Dialog Message
                alertDialog.setMessage(s);

                // Setting Icon to Dialog
                //   alertDialog.setIcon(R.drawable.warning);
            }

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
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
}