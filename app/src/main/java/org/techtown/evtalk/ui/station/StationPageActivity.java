package org.techtown.evtalk.ui.station;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.review.ReviewFragment;
import org.techtown.evtalk.ui.station.review.StationFragment3;

public class StationPageActivity extends AppCompatActivity {
    private final int Fragment_1 = 1;
    private final int Fragment_2 = 2;
    private final int Fragment_3 = 3;
    private final int Fragment_review =4;
    private TextView textView;
    private Button button;

    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("EVTalk"); // 타이틀 수정

        textView = findViewById(R.id.stationname);
        textView.setText(MainActivity.station.getStaNm()); // 충전소 이름 텍스트 변경

        textView = findViewById(R.id.stationaddr);
        textView.setText(MainActivity.station.getAddr()); // 충전소 주소 텍스트 변경

        button = findViewById(R.id.fragch_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(Fragment_1);
            }
        });
        button = findViewById(R.id.fragch_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(Fragment_2);
            }
        });
        button = findViewById(R.id.fragch_3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(Fragment_3);
            }
        });


//        StationFragment3.review_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "heelo", Toast.LENGTH_SHORT).show();
//            }
//        });
        FragmentView(Fragment_1); // 초기 프래그먼트





    }



    @Override // 뒤로가기 버튼 동작 구현
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void FragmentView(int fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1: // 첫 번째 프래그먼트
                StationFragment1 fragment1 = new StationFragment1();
                transaction.replace(R.id.stationfragment_container, fragment1);
                transaction.commit();
                break;
            case 2: // 두 번째 프래그먼트
                StationFragment2 fragment2 = new StationFragment2();
                transaction.replace(R.id.stationfragment_container, fragment2);
                transaction.commit();
                break;
            case 3: // 세 번째 프래그먼트
                StationFragment3 fragment3 = new StationFragment3();
                transaction.replace(R.id.stationfragment_container, fragment3);
                transaction.commit();
                break;

            case 4: // 리뷰 프래그먼트
                ReviewFragment fragment4 = new ReviewFragment();
                transaction.replace(R.id.stationfragment_container, fragment4);
                transaction.commit();
                break;
        }
    }

}