package org.techtown.evtalk;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.pedro.library.AutoPermissions;

import org.techtown.evtalk.ui.search.SearchResultActivity;
import org.techtown.evtalk.ui.station.Station;
import org.techtown.evtalk.ui.station.StationPageActivity;
import org.techtown.evtalk.ui.userinfo.UserInfoActivity;
import org.techtown.evtalk.user.Car;
import org.techtown.evtalk.user.Card;
import org.techtown.evtalk.user.ChargingStation;
import org.techtown.evtalk.user.Fee;
import org.techtown.evtalk.user.RetrofitConnection;
import org.techtown.evtalk.user.SearchResult;
import org.techtown.evtalk.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    public static User user;   //사용자
    public static Car car;     //사용자 차량 정보
    public static List<Card> membership = new ArrayList<>();    //사용자 회원카드 정보
    public static List<Card> payment = new ArrayList<>();
    public static List<ChargingStation> chargingStation = new ArrayList<>();   //충전소 기본 정보
    public static List<Station> station = new ArrayList<>(); // API 호출 충전소 정보
    ;   //충전소 기본 정보
    private AppBarConfiguration mAppBarConfiguration;
    private static NaverMap naverMap;
    private FrameLayout BSsheet;
    private BottomSheetBehavior bs;
    //    lastTask task;
    public RetrofitConnection retrofit = new RetrofitConnection();

    public static String start_time;
    public static String end_time;
    public static String total_time;
    public static final int TIMECODE = 1000;
    public static String search_result ="";
    private static final int SEARCH_RESULT_CODE = 2000;

    private static CameraUpdate cameraUpdate;

    public static NaverMap getNaverMap(){
        return naverMap;
    }
    public static CameraUpdate getCameraUpdate(){ return cameraUpdate;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인한 사용자 id값 넘겨받아 객체생성
        Intent intent = getIntent();
        user = new User(intent.getExtras().getLong("id")
                , intent.getExtras().getString("name")
                , intent.getExtras().getString("image"));
        if (user != null)
            getUserInfo();

        // 차량 정보 초기화
        if (car == null) {
            car = new Car();
            car.setImage("https://evtalk.s3.ap-northeast-2.amazonaws.com/car_image/soulbooster.png");
            car.setVehicle("차량을 선택하세요!");
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home).setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        toolbar = findViewById(R.id.toolbar);ㄹ
//        NavigationUI.setupWithNavController(
//                toolbar, navController, appBarConfiguration);


        // navigation drawer 회원이름 변경
        View headerView = navigationView.getHeaderView(0);
        TextView test = (TextView) headerView.findViewById(R.id.textView);
        if (user.getName() == null)
            test.setText("로그인 해주세요");
        else
            test.setText("" + user.getName());

        // 지도 객체 만들기
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            Log.d("Map", "Make mapFragement");
            NaverMapOptions options = new NaverMapOptions().locationButtonEnabled(false);
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        //지도 객체 만들기

        // FusedLocationSource 생성하고 Navermap에 지정
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        BSsheet = (FrameLayout) findViewById(R.id.bs_sheet);
        BottomSheetBehavior bs = BottomSheetBehavior.from(BSsheet); // 바텀 시트 동작을 위한 Behavior
        hidebs();

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() { // 시간설정 - fab 클릭 시 동작
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimeActivity.class);
                startActivityForResult(intent, TIMECODE);
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() { // 충전소 설정 - fab2 클릭 시 동작
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "충전소 설정 페이지는 아직...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() { // 주변 맛집 - fab3 클릭 시 동작
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "주변 맛집은 나중에...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // 바텀 시트 취소 버튼 동작
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                hidebs();
            }
        });
        // 바텀 시트 클릭 시 충전소 페이지 호출
        BSsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {
//                        doparsing();
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        Intent intent = new Intent(getApplicationContext(), StationPageActivity.class);
                        startActivity(intent);
                    }
                }).start();

            }
        });
//        doparsing(); // 앱 구동시 파싱 한번 테스트
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TIMECODE) {
            if (resultCode == 1001) {
                start_time = data.getStringExtra("start_time");
                end_time = data.getStringExtra("end_time");
                total_time = data.getStringExtra("total_time");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 바텀 시트 출현
    public synchronized void showbs(double lat, double lng){
        BSsheet = (FrameLayout) findViewById(R.id.bs_sheet);
        BottomSheetBehavior bs = BottomSheetBehavior.from(BSsheet);

        bs.setState(BottomSheetBehavior.STATE_EXPANDED); // 바텀 시트 출현

        TextView bs_stationname = findViewById(R.id.bs_stationname);
        bs_stationname.setText(mkname); // 충전소 이름 변경

        TextView bs_comname = findViewById(R.id.bs_comname);
        bs_comname.setText(mkbusi); // 회사이름 변경


        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.hide(); // 플로팅 버튼 숨기기
        fab2.hide();
        fab3.hide();
    }
    // 바텀 시트 숨기기
    public void hidebs(){
        BSsheet = (FrameLayout) findViewById(R.id.bs_sheet);
        BottomSheetBehavior bs = BottomSheetBehavior.from(BSsheet);
        bs.setState(BottomSheetBehavior.STATE_HIDDEN);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.show();
        fab2.show();
        fab3.show();
    }

    // home 에서 뒤로가기 두번 클릭 시 종료
    @Override
    public void onBackPressed() {
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후 2초가 지났으면 Toast Show
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로가기\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후 2초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this); // 앱 종료
            toast.cancel(); // 현재 표시된 Toast 취소
        }
    }

    // 마이페이지 이동
    public void startUserInfoActivity(View view) {
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    //Log.d("Current Location", "locationSource deactivated");
    //Log.d("Current Location", "locationSource activated");


    // 검색 쿼리 리스너
    public static List<SearchResult> results = new ArrayList<>(); //검색 결과 저장.

    private final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            search_result = s;
            if(search_result.endsWith(" ")){
                search_result = search_result.substring(0, search_result.length()-1);
            }
            Log.d("search", "검색 완료");

            Log.d("search", Integer.toString(results.size()));
            startSearchActivity(search_result);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            Log.d("search", "입력중");
            return true;
        }
    };


    // 추가적으로 키보드 내려가게 하는 기능 필요
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("지역명 / 충전소 검색하기");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setIconifiedByDefault(false);
//        searchView.clearFocus();

        return true;
    }

    public void startSearchActivity(String s) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        startActivityForResult(intent, SEARCH_RESULT_CODE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public synchronized void onMapReady(@NonNull NaverMap naverMap) {
        Log.d("onMapRead", "onMapReady Method Begin");

        // FusedLocationSource 생성하고 Navermap에 지정
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);


        // 지도 옵션 주기
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true); // 지도 건물 모양 보이게
        naverMap.setBuildingHeight(0.5f); // 지도가 기울어지면 건물 입체적으로 표시
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false); // 실시간 교통 상황


        // UI 설정 하기
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setScaleBarEnabled(true);  // 지도 스케일 표시
        LocationButtonView locationButtonView = findViewById(R.id.location); // 현재 위치 버튼 설정
        locationButtonView.setMap(naverMap); // 현재 위치 버튼 설정

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 현재 위치 표시
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        // 카메라 초기 위치 설정
        LatLng initialPosition = new LatLng(37.506855, 127.066242);
        cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);

        // 마커들 위치 정의
        markersPosition = new Vector<LatLng>();
        for (int i = 0; i < chargingStation.size(); i++) {
            markersPosition.add(new LatLng(chargingStation.get(i).getLat(),chargingStation.get(i).getLng()));
        }

        // 카메라 이동 되면 호출 되는 이벤트
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public synchronized void onCameraChange(int reason, boolean animated) {
                freeActiveMarkers();
                // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                LatLng currentPosition = getCurrentPosition(naverMap);
                for (LatLng markerPosition : markersPosition) {
                    if (!withinSightMarker(currentPosition, markerPosition))
                        continue;
                    Marker marker = new Marker();
                    marker.setIconPerspectiveEnabled(true); // 원근감 표시
                    marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker));
                    marker.setPosition(markerPosition); // 마커 위치
                    marker.setMap(naverMap);
                    marker.setOnClickListener(MainActivity.this::onClick);
                    activeMarkers.add(marker);
                }
            }
        });
    }

    // 마커 클릭 이벤트
    public static String mkname = "NULL";
    public static String mkbusi = "NULL";
    Marker lastClicked = null;
    @Override
    public synchronized boolean onClick(@NonNull Overlay overlay) {
        if(overlay instanceof Marker){
            for(int i=0;i<chargingStation.size();i++){
                if(((Marker) overlay).getPosition().latitude == chargingStation.get(i).getLat() && ((Marker) overlay).getPosition().longitude == chargingStation.get(i).getLng()) {
                    mkname = chargingStation.get(i).getName();
                    mkbusi = chargingStation.get(i).getBusiNm();
                    break;
                }
            }
//            station.setLat(((Marker) overlay).getPosition().latitude);
//            station.setLng(((Marker) overlay).getPosition().longitude);


            if (lastClicked!=null) {
                lastClicked.setIcon((OverlayImage.fromResource(R.drawable.ic_marker)));
                Log.d("marker", "purple");
            }
            lastClicked = (Marker) overlay;
            if (lastClicked!=null) {
                ((Marker) overlay).setIcon((OverlayImage.fromResource(R.drawable.ic_marker_clicked)));
                Log.d("marker", "pink");
            }

            // 마커 클릭 시 카메라 이동 - 이동 후 클릭된 마커 이미지 변경 안되는 오류
//            LatLng markercenter = new LatLng(station.getLat(), station.getLng());
//            cameraUpdate = CameraUpdate.scrollTo(markercenter);
//            naverMap.moveCamera(cameraUpdate);


            showbs(((Marker) overlay).getPosition().latitude, ((Marker) overlay).getPosition().latitude);
            return true;
        }
        return false;
    }

    public static int right = 0;
    /*public synchronized void doparsing() {
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
            parser = factory.newPullParser();
            parser.setInput(xmlUrl.openStream(), "utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("statNm")) { flag[0] = true; }
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
                        break;
                    case XmlPullParser.TEXT:
                        if (flag[0]) {
                            station.get(k).setStaNm(parser.getText());
                            if(station.get(k).getStaNm() == mkname)
                                right = k;
                        }
                        else if(flag[1]){ station.get(k).setStaId(parser.getText()); flag[1] = false; }
                        else if(flag[2]){ station.get(k).setChgerId(parser.getText()); flag[2] = false; }
                        else if(flag[3]){ station.get(k).setChgerType(parser.getText()); flag[3] = false; }
                        else if(flag[4]){ station.get(k).setAddr(parser.getText()); flag[4] = false; }
                        else if(flag[5]){ station.get(k).setLat(Double.parseDouble(parser.getText())); flag[5] = false; }
                        else if(flag[6]){ station.get(k).setLng(Double.parseDouble(parser.getText())); flag[6] = false; }
                        else if(flag[7]){ station.get(k).setUseTime(parser.getText()); flag[7] = false; }
                        else if(flag[8]){ station.get(k).setBusiId(parser.getText()); flag[8] = false; }
                        else if(flag[9]){ station.get(k).setBusiNm(parser.getText()); flag[9] = false; }
                        else if(flag[10]){ station.get(k).setBusiCall(parser.getText()); flag[10] = false; }
                        else if(flag[11]){ station.get(k).setStat(parser.getText()); flag[11] = false; }
                        else if(flag[12]){ station.get(k).setStatUpdDt(parser.getText()); flag[12] = false; }
                        else if(flag[13]){ station.get(k).setPowerType(parser.getText()); flag[13] = false; }
                        else if(flag[14]){ station.get(k).setZcode(parser.getText()); flag[14] = false; }
                        else if(flag[15]){ station.get(k).setParkingFree(parser.getText()); flag[15] = false; }
                        else if(flag[16]){ station.get(k).setNote(parser.getText()); flag[16] = false; }
                        else if(flag[17]){ station.get(k).setLimitYn(parser.getText()); flag[17] = false; }
                        else if(flag[18]){ station.get(k).setLimitDetail(parser.getText()); flag[18] = false; }
                        else if(flag[19]){ station.get(k).setDelYn(parser.getText()); flag[19] = false; }
                        else if(flag[20]){ station.get(k++).setDelDetail(parser.getText()); flag[20] = false; }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
//            station.setBusiNm("이게 나오면 안되는데...");
        }
    }*/

    // 마커 정보 저장시킬 변수들 선언
    private Vector<LatLng> markersPosition;
    private Vector<Marker> activeMarkers;

    // 현재 카메라가 보고있는 위치
    public LatLng getCurrentPosition(NaverMap naverMap) {
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        return new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    // 선택한 마커의 위치가 가시거리(카메라가 보고있는 위치 반경 3km 내)에 있는지 확인
    public final static double REFERANCE_LAT = 1 / 109.958489129649955;
    public final static double REFERANCE_LNG = 1 / 88.74;
    public final static double REFERANCE_LAT_X3 = 3 / 109.958489129649955;
    public final static double REFERANCE_LNG_X3 = 3 / 88.74;

    public boolean withinSightMarker(LatLng currentPosition, LatLng markerPosition) {
        boolean withinSightMarkerLat = Math.abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT_X3;
        boolean withinSightMarkerLng = Math.abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG_X3;
        return withinSightMarkerLat && withinSightMarkerLng;
    }

    // 지도상에 표시되고있는 마커들 지도에서 삭제
    private void freeActiveMarkers() {
        if (activeMarkers == null) {
            activeMarkers = new Vector<Marker>();
            return;
        }
        for (Marker activeMarker: activeMarkers) {
            activeMarker.setMap(null);
        }
        activeMarkers = new Vector<Marker>();
    }

    //메인 엑티비티 실행 시 DB에서 유저 정보 받아오기
    public void getUserInfo() {
        retrofit.server.getUserInfo(user.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User resource = response.body();
                if (resource.getCar_number() != null)
                    user.setCar_number(resource.getCar_number());
                else
                    user.setCar_number("");
                if (resource.getMessage() != null)
                    user.setMessage(resource.getMessage());
                else
                    user.setMessage("");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", "사용자 정보 받아오기 실패");
            }
        });

        retrofit.server.getMembershipInfo(user.getId()).enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                if (response.isSuccessful()) {
                    List<Card> result = response.body();
                    for (Card i : result) {
                        membership.add(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Log.i("failure", "회원카드 받아오기 실패");
            }
        });

        retrofit.server.getPaymentInfo(user.getId()).enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                if (response.isSuccessful()) {
                    List<Card> result = response.body();
                    for (Card i : result) {
                        payment.add(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Log.i("failure", "결제카드 받아오기 실패");
            }
        });

        retrofit.server.getCarInfo(user.getId()).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.body() != null) {
                    car = response.body();
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.i("failure", "차량 정보 받아오기 실패");
            }
        });

        retrofit.server.getChargingFee(user.getId()).enqueue(new Callback<List<Fee>>() {
            @Override
            public void onResponse(Call<List<Fee>> call, Response<List<Fee>> response) {
                for (Fee f : response.body()) {
                    for (ChargingStation i : chargingStation) {
                        if (i.getId().contains(f.getBusiId()))
                            i.setFee(f.getFee());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Fee>> call, Throwable t) {
                Log.i("오류", "" + t);
            }
        });
    }
}