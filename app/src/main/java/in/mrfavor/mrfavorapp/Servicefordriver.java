package in.mrfavor.mrfavorapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.myfavourcarpooling.easycarpooling.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by krishna on 1/7/2018.
 */

public class Servicefordriver extends Service {
    @Nullable
   private final boolean isServicerunning = false;
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "in.mrfavor.servicetest";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;


    @Override
    public void onCreate() {
        super.onCreate();
      //  Toast.makeText(this, "Service is created", Toast.LENGTH_SHORT).show();
        intent = new Intent(BROADCAST_ACTION);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            new SendPostRequest().execute();
            handler.postDelayed(this, 30000); // 10 seconds
        }
    };

    private void DisplayLoggingInfo(String s) {
        Log.d(TAG, "entered DisplayLoggingInfo");

       // intent.putExtra("time", new Date().toLocaleString());
        //intent.putExtra("counter", String.valueOf(++counter));
        if(!s.isEmpty()) {
            intent.putExtra("bookingcount", s);
            sendBroadcast(intent);
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String userid;

        @Override
        protected void onPreExecute() {

            SharedPreferences pref = getSharedPreferences("Logininfo", Context.MODE_PRIVATE);

            userid = pref.getString("useremail",null);
        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                String baseurl = getResources().getString(R.string.baseurl);
                URL url = new URL(baseurl+"/myfavour/bookingalert.php?emailid="+userid); // here is your URL path


                JSONObject postDataParams = new JSONObject();

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoOutput(true);


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
           if(!s.contains("false") && !s.contains("Exception")) {
               if(s.contains("0")) {
                   DisplayLoggingInfo(s);

               }
               else
               {
                   DisplayLoggingInfo(s);
                   sendNotification();
               }
           }
        }


    }


    public void sendNotification() {


        Intent intent = new Intent(getBaseContext(),Register.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle("New Ride Request")
                        .setContentText("check request");




        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(001, mBuilder.build());

    }


}
