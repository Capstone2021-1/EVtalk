package org.techtown.evtalk.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.techtown.evtalk.R;
import org.techtown.evtalk.databinding.ActivityChatListBinding;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatListActivity extends AppCompatActivity {

    private TextView textView;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("채팅방"); // 타이틀 수정


        adapter = new ChatListAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

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
            ChatItem temp;
            if(data.getUsername1().equals(car_number))
                temp = new ChatItem(data.getUsername2(), data.getContent(), null, -1, data.getRoomNumber());
            else
                temp = new ChatItem(data.getUsername1(), data.getContent(), null, -1, data.getRoomNumber());
            addChatList(temp);
        });

        adapter.setOnItemClickListener(new OnChatListClickListener() {
            @Override
            public void onItemClick(ChatListAdapter.ListViewHolder holder, View view, int position) {
                String roomNumber = adapter.items.get(position).getRoomNumber();
                String username1 = car_number;
                String username2 = adapter.items.get(position).getName();

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("roomNumber", roomNumber);
                intent.putExtra("username1", username1);
                intent.putExtra("username2", username2);
                startActivity(intent);
            }
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

    @Override // 뒤로가기 버튼 동작 구현
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            String roomNumber = adapter.items.get(position).getRoomNumber();
            adapter.items.remove(position);
            mSocket.emit("removeChat", gson.toJson(roomNumber));
            adapter.notifyItemRemoved(position);
        }
    };
}
