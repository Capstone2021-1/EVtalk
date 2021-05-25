package org.techtown.evtalk.ui.station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;

public class ReportPopUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report_pop_up);


        TextView cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView report = findViewById(R.id.report_msg);

        // 신고하기 버튼 누르면 메시지 앱으로
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( Intent.ACTION_SENDTO );
                intent.putExtra("sms_body", StationPageActivity.station.get(StationFragment1.parsingcount).getAddr()+"에 위치한 "+
                        MainActivity.mkname+ " 충전소에 불법 주차된 차량이 있습니다." +
                        "많은 사람이 불편을 겪고 있으니 과태료 부과 및 빠른 조치 취해주세요.");
                intent.setData( Uri.parse( "smsto:112") );
                startActivity(intent);

                finish();
            }
        });



    }
}