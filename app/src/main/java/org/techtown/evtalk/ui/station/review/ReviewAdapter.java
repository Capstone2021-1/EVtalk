package org.techtown.evtalk.ui.station.review;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.message.ChatListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements OnReviewClickListener{

    ArrayList<Review> items = new ArrayList<Review>();

    OnReviewClickListener listener;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 d일");

    // pattern: "yyyy-MM-dd hh:mm:ss"

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.review_item_layout, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Review item = items.get(position);
        viewHolder.setItem(item);
        if(item.getUser_id() == MainActivity.user.getId()) {
            viewHolder.review_delete.setVisibility(View.VISIBLE);
            viewHolder.review_delete.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Review item) {
        items.add(item);
    }

    public void setOnItemClickListener(OnReviewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        Button review_delete = view.findViewById(R.id.review_delete);
        if(listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView review_name;
        TextView review_date;
        TextView review_contents;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 d일");
        Button review_delete;

        public ViewHolder(View itemView, final OnReviewClickListener listener) {
            super(itemView);

            review_name = itemView.findViewById(R.id.review_name);
            review_contents = itemView.findViewById(R.id.review_contents);
            review_date = itemView.findViewById(R.id.review_date);
            review_delete = itemView.findViewById(R.id.review_delete);

            review_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ReviewAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Review item) {
            review_name.setText(item.getName());
            review_contents.setText(item.getReview());
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
