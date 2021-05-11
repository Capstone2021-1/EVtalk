package org.techtown.evtalk.ui.station.parking;

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

import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.StationFragment1;
import org.techtown.evtalk.ui.station.StationPageActivity;

public class StationFragment2 extends Fragment {
    public static ParkingAdapter adapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_2, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycle_parking);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter2);


        TextView t1 = v.findViewById(R.id.textView41);
        TextView t2 = v.findViewById(R.id.textView42);
        TextView t3 = v.findViewById(R.id.textView43);
        TextView t4 = v.findViewById(R.id.textView44);
        TextView t5 = v.findViewById(R.id.textView111);
        TextView t6 = v.findViewById(R.id.textView45);
        TextView t7 = v.findViewById(R.id.textView46);
        TextView t8 = v.findViewById(R.id.textView47);
        TextView t9 = v.findViewById(R.id.textView48);
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getStaNm().equals("NULL"))                t1.setText("정보없음");            else                t1.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getStaNm());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getAddr().equals("NULL"))                t2.setText("정보없음");            else                t2.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getAddr());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getBusiNm().equals("NULL"))                t3.setText("정보없음");            else                t3.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getBusiNm());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getBusiCall().equals("NULL"))                t4.setText("정보없음");            else                t4.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getBusiCall());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getUseTime().equals("NULL"))                t5.setText("정보없음");            else                t5.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getUseTime());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getParkingFree().equals("NULL"))                t6.setText("정보없음");            else                t6.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getParkingFree());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getNote().equals("NULL"))                t7.setText("정보없음");            else                t7.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getNote());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getLimitYn().equals("NULL"))                t8.setText("정보없음");            else                t8.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getLimitYn());
        if(StationPageActivity.station.get(StationFragment1.parsingcount).getLimitDetail().equals("NULL"))                t9.setText("정보없음");            else                t9.setText(StationPageActivity.station.get(StationFragment1.parsingcount).getLimitDetail());


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
