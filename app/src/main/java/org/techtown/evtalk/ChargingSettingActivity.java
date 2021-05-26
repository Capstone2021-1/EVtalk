package org.techtown.evtalk;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChargingSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_charging_setting);

        CheckBox ch1 = findViewById(R.id.chbox1);CheckBox ch2 = findViewById(R.id.chbox2);CheckBox ch3 = findViewById(R.id.chbox3);
        CheckBox ch4 = findViewById(R.id.chbox4);CheckBox ch5 = findViewById(R.id.chbox5);CheckBox ch6 = findViewById(R.id.chbox6);
        CheckBox ch7 = findViewById(R.id.chbox7);CheckBox ch8 = findViewById(R.id.chbox8);CheckBox ch9 = findViewById(R.id.chbox9);
        CheckBox ch10 = findViewById(R.id.chbox10);CheckBox ch11 = findViewById(R.id.chbox11);CheckBox ch12 = findViewById(R.id.chbox12);
        CheckBox ch13 = findViewById(R.id.chbox13);CheckBox ch14 = findViewById(R.id.chbox14);CheckBox ch15 = findViewById(R.id.chbox15);
        CheckBox ch16 = findViewById(R.id.chbox16);CheckBox ch17 = findViewById(R.id.chbox17);CheckBox ch18 = findViewById(R.id.chbox18);
        CheckBox ch19 = findViewById(R.id.chbox19);CheckBox ch20 = findViewById(R.id.chbox20);CheckBox ch21 = findViewById(R.id.chbox21);
        CheckBox ch22 = findViewById(R.id.chbox22);CheckBox ch23 = findViewById(R.id.chbox23);CheckBox ch24 = findViewById(R.id.chbox24);
        CheckBox ch25 = findViewById(R.id.chbox25);CheckBox ch26 = findViewById(R.id.chbox26);CheckBox ch30 = findViewById(R.id.chbox30);

        if(MainActivity.checkboxarray.get(0) == 0) ch1.setChecked(false);
        if(MainActivity.checkboxarray.get(1) == 0) ch2.setChecked(false);
        if(MainActivity.checkboxarray.get(2) == 0) ch3.setChecked(false);
        if(MainActivity.checkboxarray.get(3) == 0) ch4.setChecked(false);
        if(MainActivity.checkboxarray.get(4) == 0) ch5.setChecked(false);
        if(MainActivity.checkboxarray.get(5) == 0) ch6.setChecked(false);
        if(MainActivity.checkboxarray.get(6) == 0) ch7.setChecked(false);
        if(MainActivity.checkboxarray.get(7) == 0) ch8.setChecked(false);
        if(MainActivity.checkboxarray.get(8) == 0) ch9.setChecked(false);
        if(MainActivity.checkboxarray.get(9) == 0) ch10.setChecked(false);
        if(MainActivity.checkboxarray.get(10) == 0) ch11.setChecked(false);
        if(MainActivity.checkboxarray.get(11) == 0) ch12.setChecked(false);
        if(MainActivity.checkboxarray.get(12) == 0) ch13.setChecked(false);
        if(MainActivity.checkboxarray.get(13) == 0) ch14.setChecked(false);
        if(MainActivity.checkboxarray.get(14) == 0) ch15.setChecked(false);
        if(MainActivity.checkboxarray.get(15) == 0) ch16.setChecked(false);
        if(MainActivity.checkboxarray.get(16) == 0) ch17.setChecked(false);
        if(MainActivity.checkboxarray.get(17) == 0) ch18.setChecked(false);
        if(MainActivity.checkboxarray.get(18) == 0) ch19.setChecked(false);
        if(MainActivity.checkboxarray.get(19) == 0) ch20.setChecked(false);
        if(MainActivity.checkboxarray.get(20) == 0) ch21.setChecked(false);
        if(MainActivity.checkboxarray.get(21) == 0) ch22.setChecked(false);
        if(MainActivity.checkboxarray.get(22) == 0) ch23.setChecked(false);
        if(MainActivity.checkboxarray.get(23) == 0) ch24.setChecked(false);
        if(MainActivity.checkboxarray.get(24) == 0) ch25.setChecked(false);
        if(MainActivity.checkboxarray.get(25) == 0) ch26.setChecked(false);
        if(MainActivity.checkboxarray.get(26) == 0) ch30.setChecked(false); // 출입제한 제외

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked())                    MainActivity.checkboxarray.set(0,1);
                else                    MainActivity.checkboxarray.set(0,0);
            }
        });
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch2.isChecked())                    MainActivity.checkboxarray.set(1,1);
                else                    MainActivity.checkboxarray.set(1,0);
            }
        });
        ch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch3.isChecked())                    MainActivity.checkboxarray.set(2,1);
                else                    MainActivity.checkboxarray.set(2,0);
            }
        });
        ch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch4.isChecked())                    MainActivity.checkboxarray.set(3,1);
                else                    MainActivity.checkboxarray.set(3,0);
            }
        });
        ch5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch5.isChecked())                    MainActivity.checkboxarray.set(4,1);
                else                    MainActivity.checkboxarray.set(4,0);
            }
        });
        ch6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch6.isChecked())                    MainActivity.checkboxarray.set(5,1);
                else                    MainActivity.checkboxarray.set(5,0);
            }
        });
        ch7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch7.isChecked())                    MainActivity.checkboxarray.set(6,1);
                else                    MainActivity.checkboxarray.set(6,0);
            }
        });
        ch8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch8.isChecked())                    MainActivity.checkboxarray.set(7,1);
                else                    MainActivity.checkboxarray.set(7,0);
            }
        });
        ch9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch9.isChecked())                    MainActivity.checkboxarray.set(8,1);
                else                    MainActivity.checkboxarray.set(8,0);
            }
        });
        ch10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch10.isChecked())                    MainActivity.checkboxarray.set(9,1);
                else                    MainActivity.checkboxarray.set(9,0);
            }
        });
        ch11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch11.isChecked())                    MainActivity.checkboxarray.set(10,1);
                else                    MainActivity.checkboxarray.set(10,0);
            }
        });
        ch12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch12.isChecked())                    MainActivity.checkboxarray.set(11,1);
                else                    MainActivity.checkboxarray.set(11,0);
            }
        });
        ch13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch13.isChecked())                    MainActivity.checkboxarray.set(12,1);
                else                    MainActivity.checkboxarray.set(12,0);
            }
        });
        ch14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch14.isChecked())                    MainActivity.checkboxarray.set(13,1);
                else                    MainActivity.checkboxarray.set(13,0);
            }
        });
        ch15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch15.isChecked())                    MainActivity.checkboxarray.set(14,1);
                else                    MainActivity.checkboxarray.set(14,0);
            }
        });
        ch16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch16.isChecked())                    MainActivity.checkboxarray.set(15,1);
                else                    MainActivity.checkboxarray.set(15,0);
            }
        });
        ch17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch17.isChecked())                    MainActivity.checkboxarray.set(16,1);
                else                    MainActivity.checkboxarray.set(16,0);
            }
        });
        ch18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch18.isChecked())                    MainActivity.checkboxarray.set(17,1);
                else                    MainActivity.checkboxarray.set(17,0);
            }
        });
        ch19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch19.isChecked())                    MainActivity.checkboxarray.set(18,1);
                else                    MainActivity.checkboxarray.set(18,0);
            }
        });
        ch20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch20.isChecked())                    MainActivity.checkboxarray.set(19,1);
                else                    MainActivity.checkboxarray.set(19,0);
            }
        });
        ch21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch21.isChecked())                    MainActivity.checkboxarray.set(20,1);
                else                    MainActivity.checkboxarray.set(20,0);
            }
        });
        ch22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch22.isChecked())                    MainActivity.checkboxarray.set(21,1);
                else                    MainActivity.checkboxarray.set(21,0);
            }
        });
        ch23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch23.isChecked())                    MainActivity.checkboxarray.set(22,1);
                else                    MainActivity.checkboxarray.set(22,0);
            }
        });
        ch24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch24.isChecked())                    MainActivity.checkboxarray.set(23,1);
                else                    MainActivity.checkboxarray.set(23,0);
            }
        });
        ch25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch25.isChecked())                    MainActivity.checkboxarray.set(24,1);
                else                    MainActivity.checkboxarray.set(24,0);
            }
        });
        ch26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch26.isChecked())                    MainActivity.checkboxarray.set(25,1);
                else                    MainActivity.checkboxarray.set(25,0);
            }
        });
        ch30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch30.isChecked())                    MainActivity.checkboxarray.set(26,1);
                else                    MainActivity.checkboxarray.set(26,0);
            }
        });

        // 확인 버튼 클릭 시 이벤트
        TextView textView = findViewById(R.id.okbttn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 종료
            }
        });
    }
}