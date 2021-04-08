package org.techtown.evtalk.ui.home;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        FloatingActionButton fab1 = (FloatingActionButton) root.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() { // 시간설정 - fab 클릭 시 동작
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action1", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
}

