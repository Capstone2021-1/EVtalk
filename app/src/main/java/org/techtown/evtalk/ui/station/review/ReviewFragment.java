package org.techtown.evtalk.ui.station.review;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.StationFragment1;
import org.techtown.evtalk.ui.station.StationPageActivity;
import org.techtown.evtalk.user.RetrofitConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    StationPageActivity activity;

    Review review;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String text_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancel = (Button) view.findViewById(R.id.cancelButton);
        Button save = (Button) view.findViewById(R.id.saveButton);
        EditText editText = (EditText) view.findViewById(R.id.review_text);

        activity = (StationPageActivity)getActivity();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.FragmentView(3);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_result = String.valueOf(editText.getText());     //입력한 리뷰 내용 가져오는 방법
                String time = getTime();
                review = new Review();   //리뷰 객체 생성
                review.setUser_id(MainActivity.user.getId());   //사용자 id
                review.setStat_id(StationPageActivity.station.get(StationFragment1.parsingcount).getStaId());   //충전소 id
                review.setReview(text_result);  //입력한 리뷰

                //DB에 리뷰 저장
                RetrofitConnection retrofit = new RetrofitConnection();
                retrofit.server.updateReview(review).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        //성공
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //실패
                    }
                });
                activity.FragmentView(3);
            }
        });



    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


}