package org.techtown.evtalk.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.techtown.evtalk.R;

public class HomeFragment extends Fragment {

    public static String start_time;
    public static String end_time;
    public static String total_time;

    public static final int TIMECODE = 1000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        FloatingActionButton fab1 = (FloatingActionButton) root.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() { // 시간설정 - fab 클릭 시 동작
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TimeActivity.class);
                startActivityForResult(intent, TIMECODE);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) root.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() { // 충전소 설정 - fab2 클릭 시 동작
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action2", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) root.findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() { // 주변 맛집 - fab3 클릭 시 동작
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action3", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TIMECODE) {
            if (resultCode == 1001) {
                start_time = data.getStringExtra("start_time");
                end_time = data.getStringExtra("end_time");
                total_time = data.getStringExtra("total_time");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}

