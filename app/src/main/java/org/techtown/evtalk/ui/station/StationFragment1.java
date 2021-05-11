package org.techtown.evtalk.ui.station;

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

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;

public class StationFragment1 extends Fragment {
    public static StationAdapter adapter;

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

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
