package org.techtown.evtalk.ui.userinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Car;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CarSettingActivity extends AppCompatActivity{
    TextView textView;
    static String selected_car;
    ArrayList<String> cars_name;
    public static final int SELECT_DONE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_setting);

        cars_name = new ArrayList<String>();

        for(Car i: UserInfoActivity.car_list){
            cars_name.add(Integer.toString(i.getYear())+" "+i.getVehicle());
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                cars_name
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("car_name", cars_name.get(i));
                selected_car =cars_name.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Log.d("car_name", cars_name.get(4));

        Button edit_done = (Button) findViewById(R.id.edit_btn3);
        edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("car", selected_car);
                setResult(SELECT_DONE, intent);
                finish();
            }
        });

    }

}