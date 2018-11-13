package in.mrfavor.mrfavorapp;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.myfavourcarpooling.easycarpooling.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by krishna on 7/9/2017.
 */
public class Fragment_passenger_history extends AppCompatActivity {
    WebView web;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

         //execute in every 50000 ms
        setContentView(R.layout.fragment_passenger_history);
      //  toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        SharedPreferences pref = getSharedPreferences("Logininfo", Context.MODE_PRIVATE);
        String userid = pref.getString("useremail",null);
     //   final ProgressDialog pd = ProgressDialog.show(getApplicationContext(), "", "Loading...",true);
        web=(WebView)findViewById(R.id.webView1);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if(pd!=null && pd.isShowing())
//                {
//                    pd.dismiss();
//                }
//            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // Handle the error
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String domainname = getResources().getString(R.string.domainname);

                if (Uri.parse(url).getHost().equals("www."+domainname)) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                }
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });


        // Force links and redirects to open in the WebView instead of in a browser
        //web.setWebViewClient(new WebViewClient());

        String baseurl = getResources().getString(R.string.baseurl);

        web.loadUrl(baseurl+"/myfavour/ridehistory.php?uid="+userid);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finish();
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
            Toast.makeText(Fragment_passenger_history.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Fragment_passenger_history.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    //
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle(" Mr. Favor");
//    }

}
