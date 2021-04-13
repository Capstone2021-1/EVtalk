package org.techtown.evtalk.ui.userinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Card;

public class PaymentSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_setting);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_p);

        // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        CardAdapter adapter = new CardAdapter("payment");

        for(Card i: UserInfoActivity.payment_list){
            adapter.addItem(i);
        }


        recyclerView.setAdapter(adapter);

        // 카드 클릭 시 선택 기능 추가해야함!
        adapter.setOnItemClickListener(new OnCardItemClickListener() {
            @Override
            public void onItemClick(CardAdapter.ViewHolder holder, View view, int position) {
                //Card item = adapter.getItem(position);
                //showToast("아이템 선택됨: "+item.getName());
            }
        });


        // 버튼 기능 추가해야함!
        Button edit = findViewById(R.id.edit_btn2);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), Integer.toString(UserInfoActivity.payment_list.size()), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void showToast(String data){
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}