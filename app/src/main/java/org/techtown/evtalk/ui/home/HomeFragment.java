package org.techtown.evtalk.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.evtalk.R;

public class HomeFragment extends Fragment {

    public static String start_time;
    public static String end_time;
    public static String total_time;

    public static final int TIMECODE = 1000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }
}

