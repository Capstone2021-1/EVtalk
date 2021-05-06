package org.techtown.evtalk.ui.station;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    public static int parsingcount;


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

        doparsing asdf = new doparsing();
        asdf.execute(); // 파싱..


    }

    public void asdf(){
        TextView textView = findViewById(R.id.stationaddr);
//        textView.setText(MainActivity.station.get(MainActivity.right).getAddr()); // 충전소 주소 텍스트 변경
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

            String queryUrl = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?serviceKey="+key_Encoding+"&pageNo=1&numOfRows=9999&zcode=11";
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            int k = 0;

            try {
                boolean flag[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}; // 21개

                xmlUrl = new URL(queryUrl);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(xmlUrl.openStream(), "UTF-8"));
                int eventType = parser.getEventType();
                Log.d("파싱 시작합니다...", "후");
                int zxcv = 0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            station = new ArrayList<Station>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if(parser.getName().equals("item")){ bus = new Station();}
                            else if (parser.getName().equals("statNm")) { flag[0] = true; }
                            else if (parser.getName().equals("statId")) { flag[1] = true; }
                            else if (parser.getName().equals("chgerId")) { flag[2] = true; }
                            else if (parser.getName().equals("chgerType")) { flag[3] = true; }
                            else if (parser.getName().equals("addr")) { flag[4] = true; }
                            else if (parser.getName().equals("lat")) { flag[5] = true; }
                            else if (parser.getName().equals("lng")) { flag[6] = true; }
                            else if (parser.getName().equals("useTime")) { flag[7] = true; }
                            else if (parser.getName().equals("busiId")) { flag[8] = true; }
                            else if (parser.getName().equals("busiNm")) { flag[9] = true; }
                            else if (parser.getName().equals("busiCall")) { flag[10] = true; }
                            else if (parser.getName().equals("stat")) { flag[11] = true; }
                            else if (parser.getName().equals("statUpdDt")) { flag[12] = true; }
                            else if (parser.getName().equals("powerType")) { flag[13] = true; }
                            else if (parser.getName().equals("zcode")) { flag[14] = true; }
                            else if (parser.getName().equals("parkingFree")) { flag[15] = true; }
                            else if (parser.getName().equals("note")) { flag[16] = true; }
                            else if (parser.getName().equals("limitYn")) { flag[17] = true; }
                            else if (parser.getName().equals("limitDetail")) { flag[18] = true; }
                            else if (parser.getName().equals("delYn")) { flag[19] = true; }
                            else if (parser.getName().equals("delDetail")) { flag[20] = true; }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("item")&&bus!=null){
                                station.add(bus);
                                k++;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (flag[0]) {
                                bus.setStaNm(parser.getText());
                                if(bus.getStaNm().equals(MainActivity.mkname))
                                    right = k;
                                flag[0] = false;
                            }
                            else if(flag[1]){ bus.setStaId(parser.getText()); flag[1] = false; }
                            else if(flag[2]){ bus.setChgerId(parser.getText()); flag[2] = false; }
                            else if(flag[3]){ bus.setChgerType(parser.getText()); flag[3] = false; }
                            else if(flag[4]){ bus.setAddr(parser.getText()); flag[4] = false; }
                            else if(flag[5]){ bus.setLat(Double.parseDouble(parser.getText())); flag[5] = false; }
                            else if(flag[6]){ bus.setLng(Double.parseDouble(parser.getText())); flag[6] = false; }
                            else if(flag[7]){ bus.setUseTime(parser.getText()); flag[7] = false; }
                            else if(flag[8]){ bus.setBusiId(parser.getText()); flag[8] = false; }
                            else if(flag[9]){ bus.setBusiNm(parser.getText()); flag[9] = false; }
                            else if(flag[10]){ bus.setBusiCall(parser.getText()); flag[10] = false; }
                            else if(flag[11]){ bus.setStat(parser.getText()); flag[11] = false; }
                            else if(flag[12]){ bus.setStatUpdDt(parser.getText()); flag[12] = false; }
                            else if(flag[13]){ bus.setPowerType(parser.getText()); flag[13] = false; }
                            else if(flag[14]){ bus.setZcode(parser.getText()); flag[14] = false; }
                            else if(flag[15]){ bus.setParkingFree(parser.getText()); flag[15] = false; }
                            else if(flag[16]){ bus.setNote(parser.getText()); flag[16] = false; }
                            else if(flag[17]){ bus.setLimitYn(parser.getText()); flag[17] = false; }
                            else if(flag[18]){ bus.setLimitDetail(parser.getText()); flag[18] = false; }
                            else if(flag[19]){ bus.setDelYn(parser.getText()); flag[19] = false; }
                            else if(flag[20]){ bus.setDelDetail(parser.getText()); flag[20] = false; }
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
            Log.d("파싱 잘 됐나.....확인"," = "+right +"\n" + station.get(right).getStaId() +"\n" + station.get(right).getStaNm()+"\n" + station.get(right).getChgerId()+"\n" + station.get(right).getChgerType()+"\n" + station.get(right).getAddr()+"\n" + station.get(right).getLat()+"\n" + station.get(right).getLng()+"\n" + station.get(right).getUseTime()+"\n" + station.get(right).getBusiId()+"\n" + station.get(right).getBusiNm()+"\n" + station.get(right).getBusiCall()+"\n" + station.get(right).getStat()+"\n" + station.get(right).getStatUpdDt()+"\n" + station.get(right).getPowerType()+"\n" + station.get(right).getZcode()+"\n" + station.get(right).getParkingFree()+"\n" + station.get(right).getNote()+"\n" + station.get(right).getLimitYn()+"\n" + station.get(right).getLimitDetail()+"\n" + station.get(right).getDelYn()+"\n" + station.get(right).getDelDetail() );
            textView = findViewById(R.id.stationaddr);
            textView.setText(station.get(right).getAddr()); // 충전소 주소 텍스트 변경

            //DB에서 선택된 충전소 리뷰 가져오기
            RetrofitConnection retrofit = new RetrofitConnection();
            retrofit.server.getReviews(StationPageActivity.station.get(right).getStaId()).enqueue(new Callback<List<Review>>() {
                @Override
                public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                    List<Review> temp = response.body();
                    for(Review i : temp)
                        StationFragment3.reviews.add(i);
                }

                @Override
                public void onFailure(Call<List<Review>> call, Throwable t) {
                    //실패
                }
            });

            Log.d("갯수임","="+Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId()) );

            StationFragment1.adapter = new StationAdapter();
            for(int i = 0; i < Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId()); i++) // 어댑터 추가
                StationFragment1.adapter.addItem(station.get(right + i + 1 - Integer.parseInt(station.get(StationFragment1.parsingcount).getChgerId())));

            button = findViewById(R.id.fragch_1);
            button.performClick();

//            TextView textView = findViewById(R.id.textView8);
//            textView.setText(station.get(right).getBusiNm());
        }

    }
}