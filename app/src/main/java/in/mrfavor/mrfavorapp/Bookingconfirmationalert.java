package in.mrfavor.mrfavorapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myfavourcarpooling.easycarpooling.R;

public class Bookingconfirmationalert extends AppCompatActivity {
Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingconfirmationalert);

        Intent i = getIntent();
        final String bookingid = i.getStringExtra("bookingID");
        final String amount = i.getStringExtra("amount");
        Toast.makeText(getApplicationContext(),amount+bookingid,Toast.LENGTH_SHORT).show();

        TextView tvbookingID = (TextView) findViewById(R.id.tvBookingID);

        tvbookingID.setText("Booking ID: "+ bookingid);

        Button btnOK = (Button) findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   finish();
                //(new Userboard()). getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new Fragment_passenger_history()).commit();
                Intent i = new Intent(Bookingconfirmationalert.this,Fragment_passenger_history.class);
//               i.putExtra("amount",amount);
//               i.putExtra("bookingID",bookingid);
               // i.putExtra("bookingid",bookingid);
                startActivity(i);
                finish();
            }
        });

       // this.setFinishOnTouchOutside(true);

    }
}
