package org.techtown.evtalk.ui.station.parking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.Station;
import org.techtown.evtalk.ui.station.StationFragment1;
import org.techtown.evtalk.ui.station.StationPageActivity;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {
    ArrayList<Station> items = new ArrayList<Station>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_parking, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingAdapter.ViewHolder viewHolder, int position) {
        Station item = items.get(position);
        viewHolder.setItem(item);
    }

    public void addItem(Station item) {
        items.add(item);
    }

    @Override
    public int getItemCount() {
        return Integer.parseInt(StationPageActivity.station.get(StationFragment1.parsingcount).getChgerId()) ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView21);
            textView2 = itemView.findViewById(R.id.textView41);
        }

        public void setItem(Station item) {
            textView1.setText("충전기속도 "+item.getChgerId());
            if(item.getPowerType().equals("NULL")) { // powertype 값이 없는경우 완속인지 급속인지만 구분
                if (item.getChgerType().equals("02"))
                    textView2.setText("정보없음 | 완속 (평균 7KWh)");
                else
                    textView2.setText("정보없음 | 급속 (평균 50KWh)");
            }
            else { // powertype 값이 있는경우 그 값 표시
                textView2.setText(item.getPowerType());
            }
        }
    }
}

