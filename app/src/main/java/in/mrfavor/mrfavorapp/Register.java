package in.mrfavor.mrfavorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity {
    Button register;
    EditText mobileno,loginid,password,age;
    TextView signin,signin2;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.btn_register);
        getSupportActionBar().hide();

        mobileno = (EditText) findViewById(R.id.etMobileno);
        loginid = (EditText) findViewById(R.id.etLoginid);
        password = (EditText) findViewById(R.id.etPassword);
        age =(EditText) findViewById(R.id.etAge);

        if (getIntent().getStringExtra("email") != null)
        {
            loginid.setText(getIntent().getStringExtra("email"));
        }

        if (getIntent().getStringExtra("pass") != null)
        {
            password.setText(getIntent().getStringExtra("pass"));
            password.setClickable(false);
            password.setFocusable(false);
            password.setEnabled(false);
        }

       signin = (TextView) findViewById(R.id.link_signin);

        signin2 = (TextView) findViewById(R.id.link_signin2);

        String EMAIL = "email";

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                getUserProfile(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });

        signin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int validation = 0;


                if(mobileno.length()==0)
                {
                    validation++;
                    mobileno.setError("Please enter mobile number.");
                }

                if(loginid.length()==0)
                {
                    validation++;
                    loginid.setError("Please enter email id.");
                }

                if(password.length()==0)
                {
                    validation++;
                    password.setError("Please enter password.");
                }
                if (age.getText().length() > 0 && Integer.parseInt(age.getText().toString()) < 18)
                {
                    validation++;
                    age.setError("Age should be more than 18");
                    Toast.makeText(Register.this, "Your are below 18", Toast.LENGTH_SHORT).show();
                }

                if(validation==0) {
                    new SendPostRequest("", loginid.getText().toString(),mobileno.getText().toString(),password.getText().toString()).execute();
                }

            }
        });


    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String fullname,loginid,mobileno,password;
        ProgressDialog pd;
        public SendPostRequest(String fn,String lid,String mn,String pass) {
            this.fullname = fn;
            this.loginid = lid;
            this.mobileno = mn;
            this.password = pass;
        }

        protected void onPreExecute() {
            pd = new ProgressDialog(Register.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        protected String doInBackground(String... arg0) {

            try {
                String baseurl = getResources().getString(R.string.baseurl);
                URL url = new URL(baseurl+"/myfavour/register2.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("fullname",fullname);
                postDataParams.put("loginid",loginid);
                postDataParams.put("mobileno",mobileno);
                postDataParams.put("pass",password);

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
if(s.contentEquals("otp"))
{
    Intent i = new Intent(Register.this,Otpverify.class);
    i.putExtra("emailid",loginid);
    i.putExtra("mobileno",mobileno);
    startActivity(i);

}
            else {
    showalert(s);

}
            if (pd != null)
            {
                pd.dismiss();
            }
        }

        private void showalert(String s) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    Register.this).create();

            // Setting Dialog Title
          //  alertDialog.setTitle("System Message");

            // Setting Dialog Message
            alertDialog.setMessage(s);

            // Setting Icon to Dialog
        //    alertDialog.setIcon(R.drawable.warning);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                //    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //You can fetch user info like this…
                            //object.getJSONObject(“picture”).
                            //String email = getJSONObject("data").getString("url");
                            //object.getString(“name”);
                            String ema = object.getString("email");
                            String iid = object.getString("id");

                            Log.d("fb" , ema + "  " + iid);

                            new SendPostRequest2(ema, iid).execute();

                            //object.getString(“id”));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public class SendPostRequest2 extends AsyncTask<String, Void, String> {
        String id,pass;
        ProgressDialog pd;
        public SendPostRequest2(String i,String p) {
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
                    i = new Intent(Register.this, DriverPanel.class);
                    editor.putString("isPassenger","no");
                }
                else {
                    i = new Intent(Register.this, Userboard.class);
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
                    Intent i = new Intent(Register.this,Otpverify.class);
                    i.putExtra("emailid",id);
                    startActivity(i);
                    finishAffinity();
                }
                else {

                    if(s.contains("fulldetails"))
                    {

                        String[] tmp = s.split("_");
                        String mobileno = tmp[1];


                        Intent i = new Intent(Register.this,Registerbasicdetails.class);
                        i.putExtra("emailid",id);
                        i.putExtra("mobileno",mobileno);
                        startActivity(i);
                        finishAffinity();

                    }
                    else {


                            loginid.setText(id);

                            password.setText(pass);
                            password.setClickable(false);
                            password.setFocusable(false);
                            password.setEnabled(false);


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
                    Register.this).create();

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

}
