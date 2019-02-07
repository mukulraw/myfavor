package in.mrfavor.mrfavorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myfavourcarpooling.easycarpooling.R;

public class SplachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Button carpool = (Button) findViewById(R.id.button);
        Button taxi = (Button) findViewById(R.id.button2);

        carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SplachActivity.this,GeoLocationbygooglemap.class);
                startActivity(i);

            }
        });


        taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplachActivity.this,MapActivity.class);
                startActivity(i);
            }
        });


    }
}
