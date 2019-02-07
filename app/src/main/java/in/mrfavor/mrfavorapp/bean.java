package in.mrfavor.mrfavorapp;

import android.app.Application;
import android.util.Log;

public class bean extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        FontsOverride.setDefaultFont(this, "MONOSPACE", "tcb.ttf");


    }

}
