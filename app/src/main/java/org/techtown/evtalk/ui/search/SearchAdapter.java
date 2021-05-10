package org.techtown.evtalk.ui.search;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
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
                Tm128 tm128 = new Tm128(items.get(position).getLngOx(), items.get(position).getLatOy()); // lng 먼저
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
        TextView distText;
        ImageView imageView;
        Drawable drawable;
        private double result_lat;
        private double result_long;
        private static double distance;
        private LatLng latLng;

        public ViewHolder(View itemView, final OnSearchResultClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            distText = itemView.findViewById(R.id.distance_text);
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

            result_lat = item.getLatOy();
            result_long = item.getLngOx();

            if(item.isChSt==1){
                drawable = imageView.getResources().getDrawable(R.drawable.ic_marker);
                imageView.setImageDrawable(drawable);

                // 거리 설정
                distance = calDistance(result_lat, result_long, SearchResultActivity.latitude, SearchResultActivity.longitude);
                distance = mToKm(distance);
                distText.setText(Double.toString(distance)+"km");
            }else if(item.isChSt==0){
                drawable = imageView.getResources().getDrawable(R.drawable.ic_baseline_near_me_24);
                imageView.setImageDrawable(drawable);

                // 거리 설정 (tm128 변환 시킨 후)
                Tm128 tm128 = new Tm128(result_long, result_lat);
                latLng = tm128.toLatLng();
                distance = calDistance(latLng.latitude, latLng.longitude, SearchResultActivity.latitude, SearchResultActivity.longitude);
                distance = mToKm(distance);
                distText.setText(Double.toString(distance)+"km");
            }

        }

        public static double getDistance(){
            return distance;
        }

        public double mToKm(double d){
            d = d / 1000.0;
            d = Math.round(d *100)/100.0;
            return d;
        }
        public double calDistance(double P1_latitude, double P1_longitude,
                               double P2_latitude, double P2_longitude) {
            if ((P1_latitude == P2_latitude) && (P1_longitude == P2_longitude)) {
                return 0;
            }

            double e10 = P1_latitude * Math.PI / 180;
            double e11 = P1_longitude * Math.PI / 180;
            double e12 = P2_latitude * Math.PI / 180;
            double e13 = P2_longitude * Math.PI / 180;

            /* 타원체 GRS80 */
            double c16 = 6356752.314140910;
            double c15 = 6378137.000000000;
            double c17 = 0.0033528107;

            double f15 = c17 + c17 * c17;
            double f16 = f15 / 2;
            double f17 = c17 * c17 / 2;
            double f18 = c17 * c17 / 8;
            double f19 = c17 * c17 / 16;

            double c18 = e13 - e11;
            double c20 = (1 - c17) * Math.tan(e10);
            double c21 = Math.atan(c20);
            double c22 = Math.sin(c21);
            double c23 = Math.cos(c21);
            double c24 = (1 - c17) * Math.tan(e12);
            double c25 = Math.atan(c24);
            double c26 = Math.sin(c25);
            double c27 = Math.cos(c25);

            double c29 = c18;
            double c31 = (c27 * Math.sin(c29) * c27 * Math.sin(c29))
                    + (c23 * c26 - c22 * c27 * Math.cos(c29))
                    * (c23 * c26 - c22 * c27 * Math.cos(c29));
            double c33 = (c22 * c26) + (c23 * c27 * Math.cos(c29));
            double c35 = Math.sqrt(c31) / c33;
            double c36 = Math.atan(c35);
            double c38 = 0;
            if (c31 == 0) {
                c38 = 0;
            } else {
                c38 = c23 * c27 * Math.sin(c29) / Math.sqrt(c31);
            }

            double c40 = 0;
            if ((Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))) == 0) {
                c40 = 0;
            } else {
                c40 = c33 - 2 * c22 * c26
                        / (Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38)));
            }

            double c41 = Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))
                    * (c15 * c15 - c16 * c16) / (c16 * c16);
            double c43 = 1 + c41 / 16384
                    * (4096 + c41 * (-768 + c41 * (320 - 175 * c41)));
            double c45 = c41 / 1024 * (256 + c41 * (-128 + c41 * (74 - 47 * c41)));
            double c47 = c45
                    * Math.sqrt(c31)
                    * (c40 + c45
                    / 4
                    * (c33 * (-1 + 2 * c40 * c40) - c45 / 6 * c40
                    * (-3 + 4 * c31) * (-3 + 4 * c40 * c40)));
            double c50 = c17
                    / 16
                    * Math.cos(Math.asin(c38))
                    * Math.cos(Math.asin(c38))
                    * (4 + c17
                    * (4 - 3 * Math.cos(Math.asin(c38))
                    * Math.cos(Math.asin(c38))));
            double c52 = c18
                    + (1 - c50)
                    * c17
                    * c38
                    * (Math.acos(c33) + c50 * Math.sin(Math.acos(c33))
                    * (c40 + c50 * c33 * (-1 + 2 * c40 * c40)));

            double c54 = c16 * c43 * (Math.atan(c35) - c47);

            // return distance in meter
            return c54;
        }


    }
}
