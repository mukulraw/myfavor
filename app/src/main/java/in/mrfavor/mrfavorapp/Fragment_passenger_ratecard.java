package in.mrfavor.mrfavorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myfavourcarpooling.easycarpooling.R;

/**
 * Created by krishna on 7/9/2017.
 */
public class Fragment_passenger_ratecard extends Fragment {
    WebView web;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v =  inflater.inflate(R.layout.fragment_passenger_termconditions, container, false);
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Loading...",true);

        web=(WebView)v.findViewById(R.id.webView1);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if(pd!=null && pd.isShowing())
                {
                    pd.dismiss();
                }
            }

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

        web.loadUrl(baseurl+"/myfavour/driver_ratecard.php");
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(" Mr. Favor");
    }

}
