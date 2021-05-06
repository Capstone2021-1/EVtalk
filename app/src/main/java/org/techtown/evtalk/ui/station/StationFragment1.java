package org.techtown.evtalk.ui.station;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.review.Review;
import org.techtown.evtalk.ui.station.review.StationFragment3;
import org.techtown.evtalk.user.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationFragment1 extends Fragment {
    public static StationAdapter adapter;

    public static int parsingcount = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_1, container, false);
//        getReviews();   //충전소의 리뷰들 가져오기

        RecyclerView recyclerView = v.findViewById(R.id.recycle_charging);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        TextView textView = v.findViewById(R.id.textView8);
        textView.setText(MainActivity.mkbusi);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
//
//    //DB에서 해당 충전소의 리뷰가져오기
//    public void getReviews() {
//        RetrofitConnection retrofit = new RetrofitConnection();
//        retrofit.server.getReviews(StationPageActivity.station.get(parsingcount).getStaId()).enqueue(new Callback<List<Review>>() {
//            @Override
//            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
//                List<Review> temp = response.body();
//                for(Review i : temp)
//                    StationFragment3.reviews.add(i);
//            }
//
//            @Override
//            public void onFailure(Call<List<Review>> call, Throwable t) {
//                //실패
//            }
//        });
//    }
}
