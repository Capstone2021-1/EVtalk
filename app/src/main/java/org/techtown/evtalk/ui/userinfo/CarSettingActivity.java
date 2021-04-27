package org.techtown.evtalk.ui.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Car;

import java.util.ArrayList;
import java.util.Collections;

public class CarSettingActivity extends AppCompatActivity {
    TextView textView;
    static String selected_car;
    static String selected_year;
    ArrayList<String> cars_enterprise;
    //ArrayList<String> cars_name;
    ArrayList<String> cars0;  // 현대
    ArrayList<String> cars1;  // 기아
    ArrayList<String> cars2;  // 쉐보레
    ArrayList<String> cars3;  // 르노삼성
    ArrayList<String> cars_year;

    public static final int SELECT_DONE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_setting);

        cars_enterprise = new ArrayList<String>();
        //cars_name = new ArrayList<String>();
        cars0 = new ArrayList<String>();
        cars1 = new ArrayList<String>();
        cars2 = new ArrayList<String>();
        cars3 = new ArrayList<String>();
        cars_year = new ArrayList<String>();

        //--------------------------------------------------------------------------------------------------
        // 차량 제조사 ArrayList에 추가
        for (Car i : UserInfoActivity.car_list) {
            // 중복 안시키면서 추가
            if (!cars_enterprise.contains(i.getEnterprise())) {
                cars_enterprise.add(i.getEnterprise());
                Log.d("cars_enterprise", i.getEnterprise());
            }
        }

        for (Car i : UserInfoActivity.car_list) {
            // 제조사가 현대차이면서 ArrayList 중복 안시키면서 추가
            if (i.getEnterprise().equals(cars_enterprise.get(0)) && !cars0.contains(i.getVehicle())) {
                cars0.add(i.getVehicle());
                Log.d("hyundai", i.getVehicle());
            }
        }

        for (Car c : UserInfoActivity.car_list) {
            // 제조사가 기아차이면서 중복 안시키면서 추가
            if (c.getEnterprise().equals(cars_enterprise.get(1)) && !cars1.contains(c.getVehicle())) {
                cars1.add(c.getVehicle());
                Log.d("kia", c.getVehicle());
            }
        }

        for (Car c : UserInfoActivity.car_list) {
            // 제조사가 쉐보레차이면서 중복 안시키면서 추가
            if (c.getEnterprise().equals(cars_enterprise.get(2)) && !cars2.contains(c.getVehicle())) {
                cars2.add(c.getVehicle());
                Log.d("chevrolet", c.getVehicle());
            }
        }

        for (Car c : UserInfoActivity.car_list) {
            // 제조사가 르노삼성이면서 중복 안시키면서 추가
            if (c.getEnterprise().equals(cars_enterprise.get(3)) && !cars3.contains(c.getVehicle())) {
                cars3.add(c.getVehicle());
                Log.d("renault", c.getVehicle());
            }
        }

        // 차량 제조사 spinner 부분 (첫번째 spinner)
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                cars_enterprise
        );

        // 모델명 spinner 부분 (두번째 spinner)
        final Spinner spinner2 = findViewById(R.id.spinner2);

        // 현대차 adapter 만들기
        ArrayAdapter<String> adapter2_0 = new ArrayAdapter<String>(
                CarSettingActivity.this,
                android.R.layout.simple_spinner_item,
                cars0
        );
        adapter2_0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 기아 adapter 만들기
        ArrayAdapter<String> adapter2_1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                cars1
        );
        adapter2_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 쉐보레 adapter 만들기
        ArrayAdapter<String> adapter2_2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                cars2
        );
        adapter2_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 르노삼성 adapter 만들기
        ArrayAdapter<String> adapter2_3 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                cars3
        );
        adapter2_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); //

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 첫번쨰 스피너

            @Override   // 파라미터 i는 클릭한 position을 의미함
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 첫번째 스피너에 따라 두번째 스피너 선택
                switch (i) {
                    case 0: // 현대차
                        spinner2.setAdapter(adapter2_0);
                        break;
                    case 1:
                        spinner2.setAdapter(adapter2_1);
                        break;
                    case 2:
                        spinner2.setAdapter(adapter2_2);
                        break;
                    case 3:
                        spinner2.setAdapter(adapter2_3);
                        break;
                }
                //두 번째 스피너 설정
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {

                        switch (i) {
                            case 0: // 현대차
                                selected_car = cars0.get(j);
                                break;
                            case 1:
                                selected_car = cars1.get(j);
                                break;
                            case 2:
                                selected_car = cars2.get(j);
                                break;
                            case 3:
                                selected_car = cars3.get(j);
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });




        /*----------------------------------------------------------------------------------------*/
        //3번째 스피너

//        // 년도 수 전부 저장
//        for (Car i : UserInfoActivity.car_list) {
//            if (!cars_year.contains(Integer.toString(i.getYear()))) {
//                cars_year.add(Integer.toString(i.getYear()));
//                Log.d("cars_year", Integer.toString(i.getYear()));
//            }
//        }
//
//        Collections.sort(cars_year);
//        Collections.reverse(cars_year);
//
//        // 차량 년도 수 spinner 부분
//        Spinner spinner3 = findViewById(R.id.spinner3);
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_spinner_item,
//                cars_year
//        );
//
//        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner3.setAdapter(adapter3);
//
//        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_year = cars_year.get(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        // 취소 버튼
        Button edit_cancel = (Button) findViewById(R.id.edit_btn4);
        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 수정하기 버튼
        Button edit_done = (Button) findViewById(R.id.edit_btn3);
        edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("car", selected_car);  // 선택된 값 Intent로 전달
//                intent.putExtra("year", selected_year);
                setResult(SELECT_DONE, intent);
                finish();
            }
        });
    }

    // 팝업 밖 선택시 닫힘 방지
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_OUTSIDE ) {
            return false;
        }
        return true;
    }
}