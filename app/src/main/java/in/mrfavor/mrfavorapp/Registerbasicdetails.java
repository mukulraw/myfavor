package in.mrfavor.mrfavorapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class Registerbasicdetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String emailid = "";
    String mobileno = "";
    private SimpleDateFormat dateFormatter;
    TextView tvpickdate;
    private DatePickerDialog fromDatePickerDialog;
    EditText etmobile,etemailid,etfullname;
    private Spinner spinner;
    private static final String[]paths = {"Passenger","Add Taxi/Cab"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerbasicdetails);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Registerbasicdetails.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



getSupportActionBar().setTitle("Basic Information");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);

        final EditText etfullname = (EditText) findViewById(R.id.etFullname);

        etmobile = (EditText) findViewById(R.id.etMobileno);
        etemailid = (EditText) findViewById(R.id.etEmailid);

        final RadioButton female = (RadioButton) findViewById(R.id.rbFemale);
        final RadioButton male = (RadioButton) findViewById(R.id.rbMale);





        Button btnupdate = (Button) findViewById(R.id.btnUpdate);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        int validation = 0;

                female.setError(null);

                etfullname.setError(null);
                tvpickdate.setError(null);
                etmobile.setError(null);
                etemailid.setError(null);

                if(!female.isChecked() && !male.isChecked())
                {
                    validation++;
                    female.setError("Please select gender.");
                }



                if(etfullname.getText().length()==0)
                {
                    validation++;
                    etfullname.setError("Please enter your full name.");
                }

                if(tvpickdate.getText().toString().contentEquals("Pick Date"))
                {
                    validation++;

                    tvpickdate.setError("Please pick date of birth.");
                }

                if(etmobile.getText().length()==0)
                {
                    validation++;

                    etmobile.setError("Please enter mobile number.");
                }

                if(etemailid.getText().length()==0)
                {
                    validation++;

                    etemailid.setError("Please enter email id.");
                }


                  if(validation==0)
                  {
                      String gender = null,insu = null;

                      if(male.isChecked()) gender = "male";
                      if(female.isChecked()) gender = "female";



                      String ras = spinner.getSelectedItem().toString();

                      SharedPreferences.Editor editor = getSharedPreferences("Logininfo", MODE_PRIVATE).edit();
                      editor.putString("registredas",ras);
                      editor.commit();



                      new SendPostRequest(etfullname.getText().toString(),gender,tvpickdate.getText().toString(),etmobile.getText().toString(),etemailid.getText().toString(),insu,ras).execute();
                  }


            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            emailid = bundle.getString("emailid").toString();
            mobileno = bundle.getString("mobileno").toString();
        }



        if(!emailid.isEmpty())
        {
            etemailid.setEnabled(false);
        }

        if(!mobileno.isEmpty())
        {
            etmobile.setEnabled(false);
        }


        etemailid.setText(emailid);
        etmobile.setText(mobileno);


        tvpickdate = (TextView) findViewById(R.id.tvPickdata);


        setshowdate();

        tvpickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

    }





    private void setshowdate() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvpickdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String insurance;
        String fullname,gen,dateofbirth,mobileno,emailid,registredAs;
        ProgressDialog pd;
        public SendPostRequest(String fn,String gender,String dob,String mobile,String email,String ins,String ras) {
            this.fullname = fn;
            this.gen = gender;
            this.dateofbirth = dob;
            this.mobileno = mobile;
            this.emailid = email;
            this.insurance = ins;
            this.registredAs = ras;

        }

        protected void onPreExecute() {
            pd = new ProgressDialog(Registerbasicdetails.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        protected String doInBackground(String... arg0) {

            try {
                String baseurl = getResources().getString(R.string.baseurl);
                URL url = new URL(baseurl+"/myfavour/fullregister.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("fullname",fullname);
                postDataParams.put("gender",gen);
                postDataParams.put("dob",dateofbirth);
                postDataParams.put("mobileno",mobileno);
                postDataParams.put("emailid",emailid);
                postDataParams.put("insurance",insurance);
                postDataParams.put("registredas",registredAs);

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

            Log.d("systemmsg",s.toString());

            if(s.contentEquals("done"))
            {

                Intent i = null;
                String ras = spinner.getSelectedItem().toString();
                if(ras.contentEquals("Add Taxi/Cab"))
                {
                  i = new Intent(Registerbasicdetails.this, DriverPanel.class);
                }
                else {
                  i = new Intent(Registerbasicdetails.this, Userboard.class);
                }
                    i.putExtra("emailid",emailid);
                i.putExtra("mobileno",mobileno);
                startActivity(i);
                finishAffinity();

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
                    Registerbasicdetails.this).create();

            // Setting Dialog Title
          //  alertDialog.setTitle("System Message");

            // Setting Dialog Message
            alertDialog.setMessage(s);

            // Setting Icon to Dialog
       //     alertDialog.setIcon(R.drawable.warning);

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




}
