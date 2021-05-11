package org.techtown.evtalk.ui.station;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview1;
        TextView textView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;

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


        }
    }
}

