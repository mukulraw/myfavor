package driver;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import in.mrfavor.mrfavorapp.DriverPanel;
import com.myfavourcarpooling.easycarpooling.R;

public class AdapterforNewrideinfo extends RecyclerView.Adapter<AdapterforNewrideinfo.RideinfoViewHolder> {
Context context;
Newrideinfo[] data;

    public AdapterforNewrideinfo(Context context,Newrideinfo[] data)
    {
        this.context = context;
        this.data = data;

    }

    @Override
    public RideinfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_driver_ride_request_cardview,parent,false);
        return new RideinfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RideinfoViewHolder holder, final int position) {


                 Newrideinfo info = data[position];

                 final String picplace = info.getPickPlaceName();
        final String dropplace = info.getDropPlaceName();
        final String custname = info.getUserName();

                 holder.username.setText(custname);
                 holder.startpoint.setText(picplace);
                 holder.endpoint.setText(dropplace);

                 final String url;
                 String startlat = info.getStartLocPointLat();
                 String startng = info.getStartLocPointLng();
                 String endlat = info.getEndLocPointLat();
                 String endlng = info.getEndLocPointLng();
                 final String rideid = info.getRideid();

         final LatLng startlatlng = new LatLng(Double.parseDouble(startlat),Double.parseDouble(startng));
        final LatLng endlatlng = new LatLng(Double.parseDouble(endlat),Double.parseDouble(endlng));


        url = "http://maps.google.com/maps?saddr="+startlat+","+startng+"&daddr="+endlat+","+endlng;

                 holder.accept.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                      //   Toast.makeText(context,"Postion = "+position,Toast.LENGTH_LONG).show();
                        // Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                // Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                          //       intent.setData(Uri.parse(url));
                         //context.startActivity(intent);

                         ((DriverPanel)context).acceptbooking(picplace,dropplace,custname,"9911669023",startlatlng,endlatlng,rideid);

                     }
                 });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RideinfoViewHolder extends RecyclerView.ViewHolder{
         TextView username;
         TextView startpoint;
         TextView endpoint;
         Button accept;
        public RideinfoViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tvUsername);
            startpoint = (TextView) itemView.findViewById(R.id.tvStartride);
            endpoint = (TextView) itemView.findViewById(R.id.tvEndride);
            accept = (Button) itemView.findViewById(R.id.btn_Accept);

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/tcb.ttf");

            accept.setTypeface(custom_font);


        }
    }
}
