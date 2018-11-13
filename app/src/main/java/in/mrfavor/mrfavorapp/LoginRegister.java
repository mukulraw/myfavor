package in.mrfavor.mrfavorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myfavourcarpooling.easycarpooling.R;

public class LoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        getSupportActionBar().hide();

        Button carpool = (Button) findViewById(R.id.button3);
        Button taxi = (Button) findViewById(R.id.button4);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/tcb.ttf");

        carpool.setTypeface(custom_font);
        taxi.setTypeface(custom_font);


// Auto Login
        SharedPreferences logininfo = getSharedPreferences("Logininfo", MODE_PRIVATE);

        String useremail = logininfo.getString("useremail", "");
        String isLogedin = logininfo.getString("isLogedin", "");

        String isPassenger = logininfo.getString("isPassenger", "");
      //  Toast.makeText(getApplicationContext(),"IS pass "+isPassenger,Toast.LENGTH_LONG).show();

        if(!useremail.isEmpty())
        {
            Intent i = null;
            if(isPassenger.contentEquals("yes"))
            {
                i = new Intent(LoginRegister.this, Userboard.class);
            }
            else
            {
                i = new Intent(LoginRegister.this, DriverPanel.class);
            }
            i.putExtra("emailid",useremail);
           startActivity(i);
            finish();
        }


        // Auto Login ends here


        carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginRegister.this,Login.class);
                startActivity(i);

            }
        });
        taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginRegister.this,Register.class);
             startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();

    }
}
