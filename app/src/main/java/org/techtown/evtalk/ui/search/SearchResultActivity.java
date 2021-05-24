package org.techtown.evtalk.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.user.RetrofitConnection;
import org.techtown.evtalk.user.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity implements LocationListener {

    SearchAdapter adapter;
    TextView textView;
    public static List<SearchResult> results2 = new ArrayList<>(); //검색 결과 저장.

    public RetrofitConnection retrofit = new RetrofitConnection();

    // 현재 위치 변수들
    public static String provider;
    public static double longitude;
    public static double latitude;
    public static double altitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;


    public static final int SEARCHRESULTCODE = 2001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("\""+MainActivity.search_result+"\" 검색 결과"); // 타이틀 수정



        RecyclerView recyclerView = findViewById(R.id.recyclerView_search_result);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SearchAdapter();

        Log.d("search", MainActivity.search_result);

        retrofit.server.searchChSt(MainActivity.search_result).enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                List<SearchResult> temp = response.body();
                for (SearchResult i : temp){
                    results2.add(i);
                    adapter.addItem(i);
                    Log.d("search", "success");
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                Log.i("error", "" + t.toString());
            }
        });

//
        adapter.setOnItemClickListener(new OnSearchResultClickListener() {
            @Override
            public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position) {

                double lat = adapter.items.get(position).getLatOy();
                double lng = adapter.items.get(position).getLngOx();

                Log.d("searchResultActivity", Double.toString(lat));
                Log.d("searchResultActivity", Double.toString(lng));

                // 클릭 한 값의 lat, lng를 MainActivity로 전달해주기
                Intent intent = new Intent();
                intent.putExtra("searchLat", Double.toString(lat));
                intent.putExtra("searchLng", Double.toString(lng));
                setResult(SEARCHRESULTCODE, intent);
                finish();
            }
        });

        // 현재 위치 좌표값 가져오기
        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
        longitude = gpsTracker.getLongitude();
        latitude = gpsTracker.getLatitude();

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
    public void onLocationChanged(@NonNull Location location) {

    }

}