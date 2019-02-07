package in.mrfavor.mrfavorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myfavourcarpooling.easycarpooling.R;

public class Terms extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        final ProgressDialog pd = ProgressDialog.show(Terms.this, "", "Loading...",true);

        Log.d("webvieww" , "called");

        web=(WebView)findViewById(R.id.webView1);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                Log.d("webvieww" , url);

                if(pd!=null && pd.isShowing())
                {
                    pd.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // Handle the error
                Log.d("webvieww" , description);
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

        web.loadUrl(baseurl+"/myfavour/passenger_term_condition.php");

    }
}
