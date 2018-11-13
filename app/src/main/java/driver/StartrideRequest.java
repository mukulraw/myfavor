package driver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
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

/**
 * Created by krishna on 2/3/2018.
 */

public class StartrideRequest extends AsyncTask<String, Void, String> {
    String rideID;
    ProgressDialog pd;
    Context context;
    public Fragment_currentbooking  fragmentActivity;
    String lat,lng;




    public StartrideRequest(Fragment_currentbooking fragmentActivity, Context context, String rideID,String lat,String lng)
    {
        this.rideID = rideID;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.lat = lat;
        this.lng = lng;

    }

    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.show();
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            String baseurl = context.getResources().getString(R.string.baseurl);
            URL url = new URL(baseurl+"/myfavour/startride.php"); // here is your URL path


            JSONObject postDataParams = new JSONObject();
            postDataParams.put("rideid", rideID);
            postDataParams.put("lat", lat);
            postDataParams.put("lng", lng);

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

        if(!s.contentEquals("Done")) {
            showalert(s);
        }
        else
        {
            fragmentActivity.btnStartride.setText("Started");
            Toast.makeText(context,"Ride is started.",Toast.LENGTH_LONG).show();
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
      //  alertDialog.setTitle("System Message");

        // Setting Dialog Message
        alertDialog.setMessage(s);

        // Setting Icon to Dialog
    //    alertDialog.setIcon(R.drawable.warning);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
              //  Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
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
