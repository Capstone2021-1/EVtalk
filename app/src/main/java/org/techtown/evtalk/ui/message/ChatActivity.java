package org.techtown.evtalk.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.databinding.ActivityChatBinding;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;

    private TextView textView;
    private Socket mSocket;
    private String username1;   //MainActivity 사용자
    private String username2;   //상대방
    private String roomNumber;
    private ChatAdapter adapter;
    private long lastSendTime = 0;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        username1 = intent.getStringExtra("username1");
        username2 = intent.getStringExtra("username2");
        roomNumber = intent.getStringExtra("roomNumber");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText(username2); // 타이틀 수정

        init();

        // 키보드 올라올 때 리사이클러뷰 스크롤 내리기
        final SoftKeyboardDectectorView softKeyboardDecector = new SoftKeyboardDectectorView(this);
        addContentView(softKeyboardDecector, new RelativeLayout.LayoutParams(-1, -1));

        softKeyboardDecector.setOnShownKeyboard(new SoftKeyboardDectectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() { //키보드 등장할 때
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void init() {
        try {
            mSocket = IO.socket("https://www.evtalk.kr");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        adapter = new ChatAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        TextView tv = findViewById(R.id.content_edit);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.recyclerView.setOnTouchListener(new View.OnTouchListener() { // 리사이클러 뷰 클릭 시 키보드 내리기
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                return false;
            }
        });

        binding.contentEdit.setOnClickListener(new View.OnClickListener() { // 텍스트뷰 클릭 시 키보드 올리기
            @Override
            public void onClick(View v) {
                imm.showSoftInput(tv, 0);
                binding.recyclerView.smoothScrollToPosition(adapter.getItemCount());
                layoutManager.setStackFromEnd(true);
                binding.recyclerView.setLayoutManager(layoutManager);
            }
        });

        // 메시지 전송 버튼
        binding.sendBtn.setOnClickListener(v -> sendMessage());

        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            mSocket.emit("enter", gson.toJson(new RoomData(username1, username2, roomNumber)));
        });
        mSocket.on("update", args -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            if(data.getSendTime() > lastSendTime) {
                lastSendTime = data.getSendTime();
                addChat(data);
            }
        });
    }

    // 리사이클러뷰에 채팅 추가
    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (data.getType().equals("MESSAGE")) {
                if(data.getFrom().equals(username1) || data.getFrom().equals(MainActivity.user.getName())) {    //DB에 메시지 내용 지우면 수정 필요
                    adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.RIGHT_MESSAGE));
                    binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                } else {
                    adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                    binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
    }

    private void sendMessage() {
        mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                username1,
                roomNumber,
                binding.contentEdit.getText().toString(),
                System.currentTimeMillis(),
                MainActivity.user.getId())));
        binding.contentEdit.setText("");
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    // 이전 버튼을 누를 시 방을 나가고 소켓 통신 종료
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(username1, username2, roomNumber)));
        mSocket.disconnect();
        lastSendTime = 0;
    }

    @Override // 뒤로가기 버튼 동작 구현
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(ChatActivity.this, ChatListActivity.class);
        intent.putExtra("user_id", MainActivity.user.getId());
        intent.putExtra("car_number", MainActivity.user.getCar_number());
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
