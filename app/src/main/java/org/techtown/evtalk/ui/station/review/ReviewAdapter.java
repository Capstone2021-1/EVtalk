package org.techtown.evtalk.ui.station.review;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    ArrayList<Review> items = new ArrayList<Review>();

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 d일");

    // pattern: "yyyy-MM-dd hh:mm:ss"

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.review_item_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Review item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Review item) {
        items.add(item);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView review_name;
        TextView review_date;
        TextView review_contents;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 d일");

        public ViewHolder(View itemView) {
            super(itemView);

            review_name = itemView.findViewById(R.id.review_name);
            review_contents = itemView.findViewById(R.id.review_contents);
            review_date = itemView.findViewById(R.id.review_date);
        }

        public void setItem(Review item) {
            review_name.setText(item.getName());
            review_contents.setText(item.getReview());

//            review_date.setText(getTime());

//             리뷰 작성했던 시간을 서버에서 가져오기
            review_date.setText(getTime(item.getDate()));


        }

        public String getTime(Date date){
            return mFormat.format(date);
        }

        public String getTime(){
            long mNow = System.currentTimeMillis();
            Date mDate = new Date(mNow);
            return mFormat.format(mDate);
        }

    }


}
