package org.techtown.evtalk.ui.userinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Card;
import org.techtown.evtalk.user.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

       //카드 클릭 시 작동 부분
        adapter.setOnItemClickListener(new OnCardItemClickListener() {
            @Override
            public void onItemClick(CardAdapter.ViewHolder holder, View view, int position) {
                // CardAdapter에서 실행됨.
            }
        });


        // 버튼 기능 추가해야함!
        Button edit = findViewById(R.id.edit_btn2);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showToast(String data){
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        super.onPause();
        RetrofitConnection retrofit = new RetrofitConnection();
        retrofit.server.updateUserPayInfo(MainActivity.user.getId(), MainActivity.payment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("sucess", "정보 수정 완료");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failure", "정보 수정 ㄴㄴ");
            }
        });
    }
}