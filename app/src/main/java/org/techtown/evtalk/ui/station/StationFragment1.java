package org.techtown.evtalk.ui.station;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.search.GpsTracker;

public class StationFragment1 extends Fragment {
    public static StationAdapter adapter;

    StationPageActivity activity;
    public static double curLat;
    public static double curLng;

    public static ImageView report;

    public static int parsingcount = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_station_1, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycle_charging);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        TextView textView = v.findViewById(R.id.textView8);
        textView.setText(MainActivity.mkbusi);


        // 현재 위치 좌표 값 구하기  (StationAdapter에서는 getContext()를 사용할 수 없기 때문에 여기서 구함)
        GpsTracker gpsTracker = new GpsTracker(getContext());
        curLat = gpsTracker.getLatitude();
        curLng = gpsTracker.getLongitude();



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
