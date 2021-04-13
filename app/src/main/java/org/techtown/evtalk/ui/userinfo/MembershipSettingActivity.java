package org.techtown.evtalk.ui.userinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Card;


public class MembershipSettingActivity extends AppCompatActivity {

    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_setting);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_m);

       // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter();

        // 카드 화면에 추가
        for(Card i: UserInfoActivity.membership_list){
            adapter.addItem(i);
        }
        recyclerView.setAdapter(adapter);


        // 카드 클릭 시 선택 기능 추가해야함!
        adapter.setOnItemClickListener(new OnCardItemClickListener() {
            @Override
            public void onItemClick(CardAdapter.ViewHolder holder, View view, int position) {
                Card item = adapter.getItem(position);
                showToast(item.getName()+" 선택됨");

            }
        });


        // 버튼 기능 추가해야함!
        Button edit = findViewById(R.id.edit_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), Integer.toString(UserInfoActivity.membership_list.size()), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showToast(String data){
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

}