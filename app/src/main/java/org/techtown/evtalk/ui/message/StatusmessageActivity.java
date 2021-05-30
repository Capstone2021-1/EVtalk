package org.techtown.evtalk.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.user.User;

public class StatusmessageActivity extends AppCompatActivity {

    public static User result;      //차량 검색 결과 저장되어있음.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_statusmessage);
        Intent intent = getIntent();
//        String receiveStr = intent.getExtras().getString("inputcarnumber"); // 전달된 차량 번호
        TextView textView = findViewById(R.id.text3);
        textView.setText(result.getCar_number()); // 입력한 차량번호로 텍스트 설정 -> result의 차량번호로 텍스트 설정

        // 상태 메시기 텍스트 변경 부분
        TextView textView1 = findViewById(R.id.stmessage);
        if(result.getId() != -1)
            textView1.setText(result.getMessage()); // 여기서 변경
        else
            textView1.setText("회원이 존재하지 않습니다.");

        // 채팅하기 버튼 클릭 시 이벤트
        Button button = findViewById(R.id.chatbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomNumber = "";
                if(result.getId() < MainActivity.user.getId())
                    roomNumber += result.getId() + "" + MainActivity.user.getId();
                else roomNumber += MainActivity.user.getId() + "" + result.getId();
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("username1", MainActivity.user.getCar_number());
                intent.putExtra("username2", result.getCar_number());
                intent.putExtra("roomNumber", roomNumber);
                startActivity(intent);
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