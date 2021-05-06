package org.techtown.evtalk.ui.station.review;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.StationFragment1;
import org.techtown.evtalk.ui.station.StationFragment2;
import org.techtown.evtalk.ui.station.StationPageActivity;

import java.util.ArrayList;
import java.util.List;

public class StationFragment3 extends Fragment {

    StationPageActivity activity;
    public static List<Review> reviews = new ArrayList<>();

    public static Button review_button;
    TextView review_count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_station_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ReviewAdapter adapter = new ReviewAdapter();

        for (Review i : reviews) {
            adapter.addItem(i);
            Log.d("review", "setting review");
        }
        recyclerView.setAdapter(adapter);

        review_count = (TextView) view.findViewById(R.id.review_count);
        review_count.setText(Integer.toString(reviews.size()));


        review_button = (Button) view.findViewById(R.id.write_review_button);
        activity = (StationPageActivity) getActivity();
        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.FragmentView(4);
            }
        });

    }
}
