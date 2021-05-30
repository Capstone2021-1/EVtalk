package org.techtown.evtalk.ui.restaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.search.GpsTracker;
import org.techtown.evtalk.ui.search.SearchResultActivity;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> implements OnRestaurantClickListener {

    ArrayList<TMapPOIItem> items = new ArrayList<TMapPOIItem>();

    OnRestaurantClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycle_restaurant, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TMapPOIItem item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // 맛집 item인 TMapPOIItem 추가
    public void addItem(TMapPOIItem item) {
        items.add(item);
    }

    public void setOnItemClickListener(OnRestaurantClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        LinearLayout linearLayout = view.findViewById(R.id.restaurant_layout);
        if (listener != null) {
            listener.onItemClick(holder, view, position);


        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView restName;
        TextView restDistance;
        TextView restParking;
        TextView restTel;

        public ViewHolder(View itemView, final OnRestaurantClickListener listener) {
            super(itemView);

            restName = itemView.findViewById(R.id.rest_name);
            restDistance = itemView.findViewById(R.id.rest_dist);
            restParking = itemView.findViewById(R.id.rest_parking);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }

                }
            });
        }

        public void setItem(TMapPOIItem item) {
            restName.setText(item.getPOIName().replace(" ", "\n"));

            TMapPoint point = new TMapPoint(MainActivity.latitude, MainActivity.longitude);
            double distance = item.getDistance(point);
            int dist = (int) distance;

            restDistance.setText(Integer.toString(dist)+" m");

            if(item.parkFlag.equals("0")){
                restParking.setText("주차 불가");
            }else{
                restParking.setText("주차 가능");
            }

        }
    }

}
