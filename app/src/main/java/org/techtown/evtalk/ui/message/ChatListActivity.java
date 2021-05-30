package org.techtown.evtalk.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.databinding.ActivityChatListBinding;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatListActivity extends AppCompatActivity {

    private long user;
    private String car_number;

    private Socket mSocket;
    private ActivityChatListBinding binding;
    private ChatListAdapter adapter;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getLongExtra("user_id", -1L);
        car_number = intent.getStringExtra("car_number");

        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adapter = new ChatListAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        try {
            mSocket = IO.socket("https://www.evtalk.kr");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.emit("getList", gson.toJson(user));

        init();
    }

    private void init() {
        mSocket.on("sendRoomList", args -> {
            RoomData data = gson.fromJson(args[0].toString(), RoomData.class);
            Log.i("채팅방 확인이요", "" + data.getUsername1() + "\t" + data.getUsername2());
            Log.i("채팅방 확인이요", "" + car_number);
            ChatItem temp;
            if(data.getUsername1().equals(car_number))
                temp = new ChatItem(data.getUsername2(), data.getContent(), null, -1);
            else
                temp = new ChatItem(data.getUsername1(), data.getContent(), null, -1);
            addChatList(temp);
        });
    }

    // 리사이클러뷰에 채팅방 추가
    private void addChatList(ChatItem data) {
        runOnUiThread(() -> {
            adapter.addItem(data);
            binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
