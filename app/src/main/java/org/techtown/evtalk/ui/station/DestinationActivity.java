package org.techtown.evtalk.ui.station;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.search.GpsTracker;
import org.techtown.evtalk.ui.search.SearchAdapter;
import org.techtown.evtalk.ui.search.SearchResultActivity;
import org.techtown.evtalk.user.RetrofitConnection;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationActivity extends AppCompatActivity {
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss");
    private String currentTime;
    private RetrofitConnection retrofit = new RetrofitConnection();
    public static String rText = "";

    private double stLat;
    private double stLng;

    private double curLat;
    private double curLng;
    private double dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_destination);

        TextView dest = findViewById(R.id.destText);
        dest.setText(rText);

        TextView cancel = findViewById(R.id.cancelText);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 현재 충전소 좌표 값 구하기
        stLat = StationPageActivity.station.get(StationFragment1.parsingcount).getLat();
        stLng = StationPageActivity.station.get(StationFragment1.parsingcount).getLng();

        // 현재 위치 좌표 값 구하기
        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
        curLat = gpsTracker.getLatitude();
        curLng = gpsTracker.getLongitude();

        // 현재 위치 좌표과 충전소 좌표값 계산
        dist = calDistance(stLat, stLng, curLat, curLng);

        dist = dist / 1000.0;
        dist = Math.round(dist *100)/100.0;
        Log.d("dest", Double.toString(dist));

        TextView ok = findViewById(R.id.okText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //충전소 도착지 설정 시 서버로 정보 전송...
                //distance에 거리 수정 필요...
                Date dNow = new Date(System.currentTimeMillis());
                retrofit.server.setDestination(StationPageActivity.station.get(StationFragment1.parsingcount).getStaId(), MainActivity.user.getId(), dist, dNow).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("도착지 설정 실패", "" + t.toString());
                    }
                });
                currentTime = getTime();
                Log.d("dest", getTime());
                finish();
            }
        });



    }
    // 현재 시간 구하기
    public String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    // 좌표 거리 구하기
    public double calDistance(double P1_latitude, double P1_longitude,
                              double P2_latitude, double P2_longitude) {
        if ((P1_latitude == P2_latitude) && (P1_longitude == P2_longitude)) {
            return 0;
        }

        double e10 = P1_latitude * Math.PI / 180;
        double e11 = P1_longitude * Math.PI / 180;
        double e12 = P2_latitude * Math.PI / 180;
        double e13 = P2_longitude * Math.PI / 180;

        /* 타원체 GRS80 */
        double c16 = 6356752.314140910;
        double c15 = 6378137.000000000;
        double c17 = 0.0033528107;

        double f15 = c17 + c17 * c17;
        double f16 = f15 / 2;
        double f17 = c17 * c17 / 2;
        double f18 = c17 * c17 / 8;
        double f19 = c17 * c17 / 16;

        double c18 = e13 - e11;
        double c20 = (1 - c17) * Math.tan(e10);
        double c21 = Math.atan(c20);
        double c22 = Math.sin(c21);
        double c23 = Math.cos(c21);
        double c24 = (1 - c17) * Math.tan(e12);
        double c25 = Math.atan(c24);
        double c26 = Math.sin(c25);
        double c27 = Math.cos(c25);

        double c29 = c18;
        double c31 = (c27 * Math.sin(c29) * c27 * Math.sin(c29))
                + (c23 * c26 - c22 * c27 * Math.cos(c29))
                * (c23 * c26 - c22 * c27 * Math.cos(c29));
        double c33 = (c22 * c26) + (c23 * c27 * Math.cos(c29));
        double c35 = Math.sqrt(c31) / c33;
        double c36 = Math.atan(c35);
        double c38 = 0;
        if (c31 == 0) {
            c38 = 0;
        } else {
            c38 = c23 * c27 * Math.sin(c29) / Math.sqrt(c31);
        }

        double c40 = 0;
        if ((Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))) == 0) {
            c40 = 0;
        } else {
            c40 = c33 - 2 * c22 * c26
                    / (Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38)));
        }

        double c41 = Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))
                * (c15 * c15 - c16 * c16) / (c16 * c16);
        double c43 = 1 + c41 / 16384
                * (4096 + c41 * (-768 + c41 * (320 - 175 * c41)));
        double c45 = c41 / 1024 * (256 + c41 * (-128 + c41 * (74 - 47 * c41)));
        double c47 = c45
                * Math.sqrt(c31)
                * (c40 + c45
                / 4
                * (c33 * (-1 + 2 * c40 * c40) - c45 / 6 * c40
                * (-3 + 4 * c31) * (-3 + 4 * c40 * c40)));
        double c50 = c17
                / 16
                * Math.cos(Math.asin(c38))
                * Math.cos(Math.asin(c38))
                * (4 + c17
                * (4 - 3 * Math.cos(Math.asin(c38))
                * Math.cos(Math.asin(c38))));
        double c52 = c18
                + (1 - c50)
                * c17
                * c38
                * (Math.acos(c33) + c50 * Math.sin(Math.acos(c33))
                * (c40 + c50 * c33 * (-1 + 2 * c40 * c40)));

        double c54 = c16 * c43 * (Math.atan(c35) - c47);

        // return distance in meter
        return c54;
    }

}