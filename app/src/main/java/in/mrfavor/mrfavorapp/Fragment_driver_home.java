package in.mrfavor.mrfavorapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.myfavourcarpooling.easycarpooling.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by krishna on 7/9/2017.
 */
public class Fragment_driver_home extends Fragment implements AdapterView.OnItemSelectedListener {

    String emailid = "";
    String mobileno = "";

    TextView tvpickdate;
    private DatePickerDialog fromDatePickerDialog;
    EditText etmobile,etemailid,etfullname;
    private Spinner spinner;
    private static final String[]paths = {"Passenger", "Pool a car", "Add Taxi/Cab"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.fragment_homefragment, container, false);

        spinner = (Spinner)v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        tvpickdate = (TextView) v.findViewById(R.id.tvPickdata);


        setshowdate();

        tvpickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        return  v;
    }

    private void setshowdate() {
    final SimpleDateFormat dateFormatter = null;
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String ss = ""+newDate.YEAR;
             tvpickdate.setText(ss);

               // tvpickdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(" Mr. Favor");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
