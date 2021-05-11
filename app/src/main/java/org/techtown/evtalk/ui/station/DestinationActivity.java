package org.techtown.evtalk.ui.station;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.evtalk.R;
import org.techtown.evtalk.user.RetrofitConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DestinationActivity extends AppCompatActivity {
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss");
    private String currentTime;
    private RetrofitConnection retrofit = new RetrofitConnection();
    public static String rText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_destination);

        TextView dest = findViewById(R.id.destText);
        dest.setText(rText);

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
                //충전소 도착지 설정 시 서버로 정보 전송...
                //distance에 거리 수정 필요...
                Date dNow = new Date(System.currentTimeMillis());
                retrofit.server.setDestination(StationPageActivity.station.get(StationFragment1.parsingcount).getStaId(), MainActivity.user.getId(), 2.5, dNow).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("도착지 설정 실패", "" + t.toString());
                    }
                });

//                if (NaviClient.instance.isKakaoNaviInstalled(context)) {
//                    Log.i(TAG, "카카오내비 앱으로 길안내 가능")
//                } else {
//                    Log.i(TAG, "카카오내비 미설치: 웹 길안내 사용 권장")
//                }

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