package in.mrfavor.mrfavorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myfavourcarpooling.easycarpooling.R;

public class Ratecard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratecard);
        getSupportActionBar().hide();


        Button go = (Button) findViewById(R.id.btnProcess);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Ratecard.this,LoginRegister.class);
                startActivity(i);
            }
        });
    }
}
