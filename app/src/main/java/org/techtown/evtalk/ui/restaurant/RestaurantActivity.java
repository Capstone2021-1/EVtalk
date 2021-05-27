package org.techtown.evtalk.ui.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.skt.Tmap.TMapPOIItem;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.search.GpsTracker;

public class RestaurantActivity extends AppCompatActivity {
    RestaurantAdapter adapter;
    TextView textView;

    public static String searchQuery="";
    public static final int SEARCHQ= 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Toolbar toolbar = findViewById(R.id.toolbar_restaurant);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("맛집 리스트"); // 타이틀 수정

        RecyclerView recyclerView = findViewById(R.id.recyclerView_restaurant);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RestaurantAdapter();

        for(TMapPOIItem i: MainActivity.restItem){
            adapter.addItem(i);
        }
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnRestaurantClickListener() {
            @Override
            public void onItemClick(RestaurantAdapter.ViewHolder holder, View view, int position) {
                searchQuery = adapter.items.get(position).getPOIAddress().replace("null", "")
                        +" " + adapter.items.get(position).getPOIName();
                Intent intent = new Intent(RestaurantActivity.this, WebViewActivity.class);
//                intent.putExtra("searchQ", searchQuery);
//                setResult(SEARCHQ, intent);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환효과 제거
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.restItem.clear();
    }
}