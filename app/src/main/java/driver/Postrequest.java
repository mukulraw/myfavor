package driver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

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

import in.mrfavor.mrfavorapp.AcceptAcivity;

/**
 * Created by krishna on 1/23/2018.
 */

public class Postrequest extends AsyncTask<String, Void, String> {
Context context;
    ProgressDialog pd;
    String[] postsfields,postdata;
    String result;
    public AcceptAcivity accept;
   public  Postrequest(AcceptAcivity accept, Context context, String[] postsfields, String[] postdata)
   {
       this.context = context;
       this.postsfields = postsfields;
       this.postdata = postdata;
       this.accept = accept;
   }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //pd = new ProgressDialog(context);
        //pd.setMessage("Please wait...");
       // pd.show();
    }

    @Override
    protected String doInBackground(String... strings) {
              String fields,data;
               String urllink = strings[0];
        try {

            URL url = new URL(urllink); // here is your URL path


            JSONObject postDataParams = new JSONObject();

            for (int i = 0; i < postsfields.length; i++) {
                fields = postsfields[i];
                data = postdata[i];
                postDataParams.put(fields,data);

            }

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

        result = s;
        if(s.contentEquals("Done"))
        {
                 accept.afterpostrequest();
        }
        else {
            showalert(s);
        }

//        if (pd != null)
//        {
//            pd.dismiss();
//        }
    }


    public String getresult() {
         return result;
    }


    public void showalert(String s) {

        AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();

        // Setting Dialog Title
      //  alertDialog.setTitle("System Message");

        // Setting Dialog Message
        alertDialog.setMessage(s);

        // Setting Icon to Dialog
      //  alertDialog.setIcon(R.drawable.warning);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
            //    Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
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
