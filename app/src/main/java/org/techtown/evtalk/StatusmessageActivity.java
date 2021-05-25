package org.techtown.evtalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatusmessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_statusmessage);

        Intent intent = getIntent();
        String receiveStr = intent.getExtras().getString("inputcarnumber"); // 전달된 차량 번호
        TextView textView = findViewById(R.id.text3);
        textView.setText(receiveStr); // 입력한 차량번호로 텍스트 설정

        // 상태 메시기 텍스트 변경 부분
        TextView textView1 = findViewById(R.id.stmessage);
        textView1.setText("그 차량의 상태 메시지~~~~~~"); // 여기서 변경 

        // 채팅하기 버튼 클릭 시 이벤트
        Button button = findViewById(R.id.chatbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 작성
            }
        });

        // 확인 버튼 클릭 시 이벤트
        TextView textView2 = findViewById(R.id.okbtn);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 종료
            }
        });
        
    }
}