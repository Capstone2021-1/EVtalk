package org.techtown.evtalk.ui.station;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.techtown.evtalk.R;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DestinationActivity extends AppCompatActivity {
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss");
    private String currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_destination);

        TextView dest = findViewById(R.id.destText);

        /*
        동일한 목적지로 설정한 정보가 있다면 다르게 처리( 00시 00분에 몇 키로 떨어진 곳에서 현재 충전소를 목적지로 설정함)

         */

        if(true){
            dest.setText("00시 00분에 몇 키로 떨어진 곳에서 현재 충전소를 목적지로");
        }
        TextView cancel = findViewById(R.id.cancelText);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView ok = findViewById(R.id.okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTime = getTime();
                Log.d("dest", getTime());
                finish();
            }
        });



    }


    // 현재 시간 구하기
    public String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}