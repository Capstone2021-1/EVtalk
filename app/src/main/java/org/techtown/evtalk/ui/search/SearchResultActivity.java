package org.techtown.evtalk.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.user.RetrofitConnection;
import org.techtown.evtalk.user.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    SearchAdapter adapter;

    public RetrofitConnection retrofit = new RetrofitConnection();

    public static List<SearchResult> results2 = new ArrayList<>(); //검색 결과 저장.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

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


//        try {
//            Thread.sleep(10000);
//        }catch (InterruptedException e){
//            System.out.println(e.getMessage());
//        }

//        for(SearchResult i : results2){
//            adapter.addItem(i);
//            Log.d("search", "adding...");
//        }
//
//        recyclerView.setAdapter(adapter);


//
//        adapter.setOnItemClickListener(new OnSearchResultClickListener() {
//            @Override
//            public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position) {
//
//            }
//        });
//
//
//        // 버튼 기능 추가해야함!
//        Button edit = findViewById(R.id.edit_btn);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//            }
//        });


    }

}