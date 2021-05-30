package org.techtown.evtalk.ui.station;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder>{
    ArrayList<Station> items = new ArrayList<Station>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_station, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Station item = items.get(position);
        viewHolder.setItem(item);
    }

    public void addItem(Station item) {
        items.add(item);
    }
    // 파싱된 충전소 갯수
    @Override
    public int getItemCount() {
        return Integer.parseInt(StationPageActivity.station.get(StationFragment1.parsingcount).getChgerId()) ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview1;
        TextView textView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;

        private double stLat;
        private double stLng;

        private double curLat;
        private double curLng;


        public ViewHolder(View itemView) {
            super(itemView);
            imageview1 = itemView.findViewById(R.id.imageviewcheck);
            textView1 = itemView.findViewById(R.id.textView91);
            imageView2 = itemView.findViewById(R.id.imageView5);
            imageView3 = itemView.findViewById(R.id.imageView6);
            imageView4 = itemView.findViewById(R.id.imageView8);
            imageView5 = itemView.findViewById(R.id.imageView9);
            imageView6 = itemView.findViewById(R.id.report_button);
        }

        public void setItem(Station item) {
            if(item.getStat().equals("2")) // 충전 대기 상태
                imageview1.setImageResource(R.drawable.ic_st_wait);
            else if(item.getStat().equals("3")) // 충전중
                imageview1.setImageResource(R.drawable.ic_st_using);

            textView1.setText(item.getStatUpdDt()); // 마지막 변경 시간 표시

            if(item.getChgerType().equals("01")){ // DC차데모
                imageView3.setImageResource(R.drawable.ic_st_chademo);
            }
            else if(item.getChgerType().equals("02")) { // AC완속
                imageView5.setImageResource(R.drawable.ic_st_ac);
            }
            else if(item.getChgerType().equals("03")) { // DC차데모 + AC3상
                imageView3.setImageResource(R.drawable.ic_st_chademo);
                imageView4.setImageResource(R.drawable.ic_st_ac3);
            }
            else if(item.getChgerType().equals("04")) { // DC콤보
                imageView2.setImageResource(R.drawable.ic_st_dccombo);
            }
            else if(item.getChgerType().equals("05")) { // DC차데모 + DC콤보
                imageView3.setImageResource(R.drawable.ic_st_chademo);
                imageView2.setImageResource(R.drawable.ic_st_dccombo);
            }
            else if(item.getChgerType().equals("06")) { // DC차데모 + AC3상
                imageView3.setImageResource(R.drawable.ic_st_chademo);
                imageView4.setImageResource(R.drawable.ic_st_ac3);
            }
            else if(item.getChgerType().equals("07")) { // AC3상
                imageView4.setImageResource(R.drawable.ic_st_ac3);
            }

            /* 신고 버튼 설정하는 부분*/
            Date currentTime = Calendar.getInstance().getTime();
            String cTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(currentTime);
            String chargingTime = item.getStatUpdDt().substring(8);

            int timePass = Integer.parseInt(cTime) - Integer.parseInt(chargingTime);

            if(timePass<0){  // 만약 현재시간은 03:00이고 충전시작 시간은 23:00일 때 처럼 음수가 나오게 되는 경우 처
                timePass = Integer.parseInt(cTime)+240000-Integer.parseInt(chargingTime);
            }


            stLat = StationPageActivity.station.get(StationFragment1.parsingcount).getLat();
            stLng = StationPageActivity.station.get(StationFragment1.parsingcount).getLng();

            curLat =StationFragment1.curLat;
            curLng = StationFragment1.curLng;

            DestinationActivity destinationActivity = new DestinationActivity();
            double dist = destinationActivity.calDistance(stLat, stLng, curLat, curLng);
            dist = dist / 1000.0;
            dist = Math.round(dist *100)/100.0;

            Log.d("report", Double.toString(dist));

            // 고속 충전기, 사용중, 2시간 이상, 현재 그 충전소에 있을 경우(50m이내) 신고 버튼 활성화
            if(item.getStatUpdDt()!=null && !item.getChgerType().equals("02") && (item.getStat().equals("3"))
                    && timePass>=20000 && dist<=0.05){
                imageView6.setImageResource(R.drawable.ic_st_ill_red);
                imageView6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ReportPopUpActivity.class); // 신고 전 페이지 열기
                        view.getContext().startActivity(intent);
                    }
                });
            }

//             버튼 테스트하기 위해 만든 임시용(고속 충전기면 무조건 신고버튼 활성화 시킴)
//            if(!item.getChgerType().equals("02")){
//                imageView6.setImageResource(R.drawable.ic_st_ill_red);
//                imageView6.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(view.getContext(), ReportPopUpActivity.class); // 신고 전 페이지 열기
//                        view.getContext().startActivity(intent);
//                    }
//                });
//            }


            Log.d("report", chargingTime);
            Log.d("report", cTime);


        }
    }
}

