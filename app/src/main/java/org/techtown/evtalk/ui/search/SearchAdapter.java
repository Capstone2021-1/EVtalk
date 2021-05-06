package org.techtown.evtalk.ui.search;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.map.CameraUpdate;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.userinfo.CardAdapter;
import org.techtown.evtalk.ui.userinfo.OnCardItemClickListener;
import org.techtown.evtalk.user.SearchResult;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements OnSearchResultClickListener {

    ArrayList<SearchResult> items = new ArrayList<SearchResult>();

    OnSearchResultClickListener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.search_result_layout, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        SearchResult item = items.get(position);
        viewHolder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SearchResult item) {
        items.add(item);
    }

    public void setOnItemClickListener(OnSearchResultClickListener listener) {
        this.listener = listener;
    }


    // 검색 결과 아이템 터치했을 때 작동하는 부분 ex) 검색 결과로 중앙대 클릭
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        LinearLayout linearLayout = view.findViewById(R.id.search_result_layout);
        if (listener != null) {
            listener.onItemClick(holder, view, position);

            linearLayout.setBackgroundColor(Color.LTGRAY);

            LatLng markercenter;
            if(items.get(position).isChSt==1) {
                markercenter = new LatLng(items.get(position).getLatOy(), items.get(position).getLngOx());
            }else{
                Tm128 tm128 = new Tm128(items.get(position).getLngOx(), items.get(position).getLatOy());
                markercenter = tm128.toLatLng();
            }

            CameraUpdate tempCamera;
            tempCamera = CameraUpdate.scrollTo(markercenter);
            MainActivity.getNaverMap().moveCamera(tempCamera);



        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
        Drawable drawable;

        public ViewHolder(View itemView, final OnSearchResultClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);

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

        public void setItem(SearchResult item) {
            textView.setText(item.getnOa());
            textView2.setText(item.getiOt());



            if(item.isChSt==1){
                drawable = imageView.getResources().getDrawable(R.drawable.ic_marker);
                imageView.setImageDrawable(drawable);
            }else if(item.isChSt==0){
                drawable = imageView.getResources().getDrawable(R.drawable.ic_baseline_near_me_24);
                imageView.setImageDrawable(drawable);
            }
        }

    }
}
