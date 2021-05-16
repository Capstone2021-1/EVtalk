package org.techtown.evtalk.ui.station;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.ui.station.parking.ParkingAdapter;
import org.techtown.evtalk.ui.station.parking.StationFragment2;
import org.techtown.evtalk.ui.station.review.Review;
import org.techtown.evtalk.ui.station.review.ReviewFragment;
import org.techtown.evtalk.ui.station.review.StationFragment3;
import org.techtown.evtalk.user.RetrofitConnection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationPageActivity extends AppCompatActivity {
    private final int Fragment_1 = 1;
    private final int Fragment_2 = 2;
    private final int Fragment_3 = 3;
    private final int Fragment_review =4;
    private TextView textView;
    private Button button;
    public static List<Station> station = new ArrayList<>(); // API 호출 충전소 정보
    Station bus = null;
    private RetrofitConnection retrofit = new RetrofitConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_page);

        doparsing asdf = new doparsing();
        asdf.execute(); // 파싱..


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("EVTalk"); // 타이틀 수정

        textView = findViewById(R.id.stationname);
        textView.setText(MainActivity.mkname); // 충전소 이름 텍스트 변경

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
        FragmentView(Fragment_1); // 초기 프래그먼트




        button =  findViewById(R.id.set_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDestination gd = new GetDestination();   //같은 목적지를 향하고 있는 사용자가 있는 지 확인
                gd.execute();
//                Intent intent = new Intent(StationPageActivity.this, DestinationActivity.class);
//                startActivity(intent);
            }
        });

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

    //    public static int right = 0;
    public class doparsing extends AsyncTask<String, Void, String> {
        int right = 0;
        @Override
        protected synchronized String doInBackground(String... strings) {
            Log.i("파싱시작합니다.", "파싱파싱");

            String key_Encoding = "%2BKctK6sCnrlkUxKAGbtxsw4ZEV4x4oeLyViNSH%2FjfjNumzKpfre5WkPNLKKltku5I%2FP54TR6iUTsvYeFybHo2A%3D%3D";
            String key_Decoding = "+KctK6sCnrlkUxKAGbtxsw4ZEV4x4oeLyViNSH/jfjNumzKpfre5WkPNLKKltku5I/P54TR6iUTsvYeFybHo2A==";
            //test URL : http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?serviceKey=%2BKctK6sCnrlkUxKAGbtxsw4ZEV4x4oeLyViNSH%2FjfjNumzKpfre5WkPNLKKltku5I%2FP54TR6iUTsvYeFybHo2A%3D%3D&pageNo=1&numOfRows=9999&zcode=11

            String queryUrl = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?serviceKey="+key_Encoding+"&pageNo=1&numOfRows=9999&zcode=11";
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            int k = 0;

            try {
                xmlUrl = new URL(queryUrl);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(xmlUrl.openStream(), "UTF-8"));
                int eventType = parser.getEventType();
                Log.d("파싱 시작합니다...", "후");
                int zxcv = 0;
                int temp = -1;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            station = new ArrayList<Station>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if(parser.getName().equals("item")){ bus = new Station();}
                            else if (parser.getName().equals("statNm")) { temp = 0; }
                            else if (parser.getName().equals("statId")) { temp = 1; }
                            else if (parser.getName().equals("chgerId")) { temp = 2; }
                            else if (parser.getName().equals("chgerType")) { temp = 3; }
                            else if (parser.getName().equals("addr")) { temp = 4; }
                            else if (parser.getName().equals("lat")) { temp = 5; }
                            else if (parser.getName().equals("lng")) { temp = 6; }
                            else if (parser.getName().equals("useTime")) { temp = 7; }
                            else if (parser.getName().equals("busiId")) { temp = 8; }
                            else if (parser.getName().equals("busiNm")) { temp = 9; }
                            else if (parser.getName().equals("busiCall")) { temp = 10; }
                            else if (parser.getName().equals("stat")) { temp = 11; }
                            else if (parser.getName().equals("statUpdDt")) { temp = 12; }
                            else if (parser.getName().equals("powerType")) { temp = 13; }
                            else if (parser.getName().equals("zcode")) { temp = 14; }
                            else if (parser.getName().equals("parkingFree")) { temp = 15; }
                            else if (parser.getName().equals("note")) { temp = 16; }
                            else if (parser.getName().equals("limitYn")) { temp = 17; }
                            else if (parser.getName().equals("limitDetail")) { temp = 18; }
                            else if (parser.getName().equals("delYn")) { temp = 19; }
                            else if (parser.getName().equals("delDetail")) { temp = 20; }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("item")&&bus!=null){
                                station.add(bus);
                                k++;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (temp == 0) {
                                bus.setStaNm(parser.getText());
                                if(bus.getStaNm().equals(MainActivity.mkname))
                                    right = k;
                                temp = -1;
                            }
                            else if(temp == 1){ bus.setStaId(parser.getText()); temp = -1; }
                            else if(temp == 2){ bus.setChgerId(parser.getText()); temp = -1; }
                            else if(temp == 3){ bus.setChgerType(parser.getText()); temp = -1; }
                            else if(temp == 4){ bus.setAddr(parser.getText()); temp = -1; }
                            else if(temp == 5){ bus.setLat(Double.parseDouble(parser.getText())); temp = -1; }
                            else if(temp == 6){ bus.setLng(Double.parseDouble(parser.getText())); temp = -1; }
                            else if(temp == 7){ bus.setUseTime(parser.getText()); temp = -1; }
                            else if(temp == 8){ bus.setBusiId(parser.getText()); temp = -1; }
                            else if(temp == 9){ bus.setBusiNm(parser.getText()); temp = -1; }
                            else if(temp == 10){ bus.setBusiCall(parser.getText()); temp = -1; }
                            else if(temp == 11){ bus.setStat(parser.getText()); temp = -1; }
                            else if(temp == 12){ bus.setStatUpdDt(parser.getText()); temp = -1; }
                            else if(temp == 13){ bus.setPowerType(parser.getText()); temp = -1; }
                            else if(temp == 14){ bus.setZcode(parser.getText()); temp = -1; }
                            else if(temp == 15){ bus.setParkingFree(parser.getText()); temp = -1; }
                            else if(temp == 16){ bus.setNote(parser.getText()); temp = -1; }
                            else if(temp == 17){ bus.setLimitYn(parser.getText()); temp = -1; }
                            else if(temp == 18){ bus.setLimitDetail(parser.getText()); temp = -1; }
                            else if(temp == 19){ bus.setDelYn(parser.getText()); temp = -1; }
                            else if(temp == 20){ bus.setDelDetail(parser.getText()); temp = -1; }
                            break;
                        default: break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                Log.i("파싱실패ㅠ", "파싱실패요~");
            }

            StationFragment1.parsingcount = right;
            return null;
        }

        protected synchronized void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("파싱 잘 됐나.....확인"," = "+right +"\n" + station.get(right).getStaNm() +"\n" + station.get(right).getStaId()+"\n" + station.get(right).getChgerId()+"\n" + station.get(right).getChgerType()+"\n" + station.get(right).getAddr()+"\n" + station.get(right).getLat()+"\n" + station.get(right).getLng()+"\n" + station.get(right).getUseTime()+"\n" + station.get(right).getBusiId()+"\n" + station.get(right).getBusiNm()+"\n" + station.get(right).getBusiCall()+"\n" + station.get(right).getStat()+"\n" + station.get(right).getStatUpdDt()+"\n" + station.get(right).getPowerType()+"\n" + station.get(right).getZcode()+"\n" + station.get(right).getParkingFree()+"\n" + station.get(right).getNote()+"\n" + station.get(right).getLimitYn()+"\n" + station.get(right).getLimitDetail()+"\n" + station.get(right).getDelYn()+"\n" + station.get(right).getDelDetail() );
            textView = findViewById(R.id.stationaddr);
            textView.setText(station.get(right).getAddr()); // 충전소 주소 텍스트 변경

            //DB에서 선택된 충전소 리뷰 가져오기
            StationFragment3.reviews = new ArrayList<>();
            retrofit.server.getReviews(StationPageActivity.station.get(right).getStaId()).enqueue(new Callback<List<Review>>() {
                @Override
                public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                    List<Review> temp = response.body();
                    for(Review i : temp)
                        StationFragment3.reviews.add(i);
                }

                @Override
                public void onFailure(Call<List<Review>> call, Throwable t) {
                    Log.i("리뷰 확인 실패", "" + t.toString());
                }
            });

            Log.d("갯수임","="+Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId()) );

            StationFragment1.adapter = new StationAdapter();
            StationFragment2.adapter2 = new ParkingAdapter();
            for(int i = 0; i < Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId()); i++) { // 어댑터 추가
                StationFragment1.adapter.addItem(station.get(right + i + 1 - Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId()))); // 충전기 정보 어뎁터
                StationFragment2.adapter2.addItem(station.get(right + i + 1 - Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId()))); // 충전기 속도 어뎁터
            }

            button = findViewById(R.id.fragch_1);
            button.performClick();

//            TextView textView = findViewById(R.id.textView8);
//            textView.setText(station.get(right).getBusiNm());
        }

    }

    //같은 목적지를 향하는 사용자가 있는지 확인
    public class GetDestination extends AsyncTask<Void, Void, Void> {

        @Override
        protected synchronized Void doInBackground(Void... voids) {
            retrofit.server.getDestinationUser(station.get(StationFragment1.parsingcount).getStaId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    DestinationActivity.rText = response.body();
                    onPostExecute();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
            return null;
        }

        protected synchronized void onPostExecute() {
            super.onPostExecute(null);
            Intent intent = new Intent(StationPageActivity.this, DestinationActivity.class);
            startActivity(intent);
        }

    }
}