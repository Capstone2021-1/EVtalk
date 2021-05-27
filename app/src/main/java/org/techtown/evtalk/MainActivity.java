package org.techtown.evtalk;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.pedro.library.AutoPermissions;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;


import org.techtown.evtalk.ui.message.StatusmessageActivity;
import org.techtown.evtalk.ui.restaurant.RestaurantActivity;
import org.techtown.evtalk.ui.search.GpsTracker;
import org.techtown.evtalk.ui.search.SearchResultActivity;
import org.techtown.evtalk.ui.station.StationPageActivity;
import org.techtown.evtalk.ui.userinfo.DrawerCardAdapter;
import org.techtown.evtalk.ui.userinfo.PaymentSettingActivity;
import org.techtown.evtalk.ui.userinfo.UserInfoActivity;
import org.techtown.evtalk.user.Car;
import org.techtown.evtalk.user.Card;
import org.techtown.evtalk.user.ChargingStation;
import org.techtown.evtalk.user.Fee;
import org.techtown.evtalk.user.RetrofitConnection;
import org.techtown.evtalk.user.SearchResult;
import org.techtown.evtalk.user.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener {
    private InfoWindow infoWindow;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    public static User user;   //사용자
    public static Car car;     //사용자 차량 정보
    public static List<Card> membership = new ArrayList<>();    //사용자 회원카드 정보
    public static List<Card> payment = new ArrayList<>();
    public static List<ChargingStation> chargingStation = new ArrayList<>();   //충전소 기본 정보
    public static List<ChargingStation> charg = new ArrayList<>();

    ;   //충전소 기본 정보
    public static List<Fee> estimated_fee = new ArrayList<>();  //예상 요금 정보
    private AppBarConfiguration mAppBarConfiguration;
    private static NaverMap naverMap;
    private static FrameLayout BSsheet;
    private BottomSheetBehavior bs;
    public int feecheck = 0;
    public List<String> feeget = new ArrayList<>();
    public RetrofitConnection retrofit = new RetrofitConnection();

    public static String start_time;
    public static String end_time;
    public static String total_time;
    public static final int TIMECODE = 1000;

    public static String search_result ="";
    public static double searchLat;
    public static double searchLng;
    private static final int SEARCH_RESULT_CODE = 2000;

    private static CameraUpdate cameraUpdate;

    public static ArrayList<TMapPOIItem> restItem = new ArrayList<>();
    public static double latitude;
    public static double longitude;

    public static NaverMap getNaverMap(){
        return naverMap;
    }
    public static CameraUpdate getCameraUpdate(){ return cameraUpdate;}
    public static ArrayList<Integer> checkboxarray = new ArrayList<>(); // 차량 회사 설정 배열

    Bitmap bmImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인한 사용자 id값 넘겨받아 객체생성
        Intent intent = getIntent();
        user = new User(intent.getExtras().getLong("id")
                , intent.getExtras().getString("name")
                , intent.getExtras().getString("image"));
        if (user != null) {
            UserInfo temp = new UserInfo();
            temp.execute();
        }

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


        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        toolbar = findViewById(R.id.toolbar);
//        NavigationUI.setupWithNavController(
//                toolbar, navController, appBarConfiguration);

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
        fab2.setOnClickListener(new View.OnClickListener() { // 충전소 카테고리 설정 - fab2 클릭 시 동작
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ChargingSettingActivity.class);
                startActivity(intent1);


                /*// 새로고침 기능 혹시 나중에 쓰일까....
                overridePendingTransition(0, 0);
                recreate();
                overridePendingTransition(0, 0);*/
            }
        });

        /* 주변 맛집 데이터 저장하기 20개만 */
        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication ("l7xx68d24409582244c887acd07632eaefcb");
        TMapPoint point = new TMapPoint(latitude, longitude);

        TMapData tmapdata = new TMapData();

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() { // 주변 맛집 - fab3 클릭 시 동작
            @Override
            public void onClick(View view) {
                makeRestList();   // 주변 맛집 리스트 만드는 부분
                try {
                    Thread.sleep(400);     // intent가 너무 빨리 실행 되어서 sleep 씀
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 잘 나오나 출력으로 확인
                for (int i = 0; i < restItem.size(); i++) {
                    TMapPOIItem item = restItem.get(i);
                    if(item.getPOIName().contains("주차장")||item.getPOIName().contains("정문")){
                        continue;
                    }
                    Log.d("맛집","POI Name: " + item.getPOIName() + "," + "Address: "
                            + item.getPOIAddress().replace("null", "")
                            + " Distance: "+ Double.toString(item.getDistance(point))
                            + " 전화 번호: "+item.telNo);
                }
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환효과 제거

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TIMECODE) {
            if (resultCode == 1001) {
                start_time = data.getStringExtra("start_time");
                end_time = data.getStringExtra("end_time");
                total_time = data.getStringExtra("total_time");
                Log.d("time", start_time);
                Log.d("time", end_time);
                Log.d("time", total_time);
            }
        }else if(requestCode == SEARCH_RESULT_CODE){   // SearchResultActivity에서 검색 결과를 누르면 MainActivity에서 정보 전달 받음
            if(resultCode == 2001) {

                Log.d("searchMainActivity", data.getStringExtra("searchLat"));
                Log.d("searchMainActivity", data.getStringExtra("searchLng"));

                searchLat = Double.parseDouble(data.getStringExtra("searchLat"));
                searchLng = Double.parseDouble(data.getStringExtra("searchLng"));

                // 마커 객체 하나 생성
                LatLng latLng = new LatLng(searchLat, searchLng);
                Marker marker = new Marker();
                marker.setPosition(latLng);

                // 좌표 값 일치하는 곳 찾아서 바텀시트 띄우기
                for(LatLng mp : markersPosition){
                    if(mp.latitude == searchLat && mp.longitude == searchLng) {
                        Log.d("searchMainActivity", "true");
                        onClick(marker);
                        break;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 맛집 리스트 생성 함수
    public void makeRestList(){
        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        TMapPoint point = new TMapPoint(latitude, longitude);

        TMapData tmapdata = new TMapData();
        tmapdata.findAroundNamePOI(point, "음식점", 1, 20,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            if(item.getPOIName().contains("주차장")||item.getPOIName().contains("정문")|| item.telNo == null){
                                continue;
                            }else {
                                restItem.add(item);
                            }
                        }
                    }
                });
    }


    // 바텀 시트 출현
    public synchronized void showbs(double lat, double lng){
        SimpleDateFormat formatType = new SimpleDateFormat("MM월 dd일 HH:mm");
        String showfee = "총 ";
        BSsheet = (FrameLayout) findViewById(R.id.bs_sheet);
        BottomSheetBehavior bs = BottomSheetBehavior.from(BSsheet);

        bs.setState(BottomSheetBehavior.STATE_EXPANDED); // 바텀 시트 출현

        TextView bs_stationname = findViewById(R.id.bs_stationname);
        bs_stationname.setText(mkname); // 충전소 이름 변경

        TextView bs_comname = findViewById(R.id.bs_comname);
        bs_comname.setText(mkbusi); // 회사이름 변경

        TextView tv1 = findViewById(R.id.textView15);
        TextView tv2 = findViewById(R.id.textView16);
        TextView tv3 = findViewById(R.id.textView17);

        if(TimeActivity.total_time == "" || TimeActivity.total_time == "0시간") { // 시간 설정 안 했을 때
            Date currentTime = Calendar.getInstance().getTime();
            start_time = new SimpleDateFormat("M월 d일 EEE HH:mm", Locale.getDefault()).format(currentTime);
            end_time = new SimpleDateFormat("M월 d일 EEE HH:mm", Locale.getDefault()).format(currentTime);

            tv1.setText(start_time + " ~ " + end_time);
            tv2.setText("충전 금액 : "+ "0" +" 원");

            showfee += "0시간 충전 시  |  0 KWh 충전  |  0 % 충전 가능";
            tv3.setText(showfee);
        }
        else{ // 시간 설정 되어있을 때
            tv1.setText(formatType.format(TimeActivity.sDate) +" ~ "+formatType.format(TimeActivity.eDate));
            tv2.setText("충전 금액 : "+ Integer.toString((int)mkfee)+" 원");

            for(int i=0;i<3;i++){
                if(i == 0){
                    showfee += Integer.toString((int)estimated_fee.get(estimated_fee.size()-i-1).getFee());
                    showfee += "시간 충전 시  |  ";
                }
                else if(i==1){
                    showfee += Integer.toString((int)estimated_fee.get(estimated_fee.size()-i-2).getFee());
                    showfee += " KWh 충전  |  ";
                }
                else if(i==2){
                    showfee += Integer.toString((int)estimated_fee.get(estimated_fee.size()-i).getFee());
                    showfee += " % 충전 가능";
                }
            }
            tv3.setText(showfee);
        }

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

    SearchView searchView;

    // 검색 쿼리 리스너
    public static List<SearchResult> results = new ArrayList<>(); //검색 결과 저장.

    private final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            search_result = s;
            if(search_result.endsWith(" ")){
                search_result = search_result.substring(0, search_result.length()-1);
            }
            hidebs();   // 검색하면 전에 열려 있던 바텀시트 닫기
            //Log.d("search", "검색 완료");

            searchView.clearFocus();

            Log.d("search", Integer.toString(results.size()));
            startSearchActivity(search_result);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            //Log.d("search", "입력중");
            return true;
        }
    };


    // 메인화면 우상단 검색 버튼
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("지역명 / 충전소 검색하기");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setIconifiedByDefault(false);

        return true;
    }

    // 화면 다른 부분 터치 시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
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

        for(int i=0 ;i<27;i++) { // 충전소 카테고리 설정 배열 초기화
            checkboxarray.add(i,1); // 초기 모두 체크 된 상태로 1 저장  // checked = 1 , unchecked = 0
        }

        // 마커들 위치 정의
        markersPosition = new Vector<LatLng>();
        for (int i = 0; i < chargingStation.size(); i++) {
            markersPosition.add(new LatLng(chargingStation.get(i).getLat(),chargingStation.get(i).getLng()));
        }


        // 카메라 이동 되면 호출 되는 이벤트 // 마커찍기
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public synchronized void onCameraChange(int reason, boolean animated) {
                feecheck = 0;
                freeActiveMarkers();
                // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                LatLng currentPosition = getCurrentPosition(naverMap);
                for (LatLng markerPosition : markersPosition) {
                    if (!withinSightMarker(currentPosition, markerPosition)) {
                        feecheck++;
                        continue;
                    }
                    if(checkboxarray.get(26) == 1){ // 출입제한 충전소 제외
                        try{
                            if(chargingStation.get(feecheck).getLimitDetail() != null && chargingStation.get(feecheck).getLimitDetail().contains("제한")) { feecheck++; continue; }
                            if(chargingStation.get(feecheck).getNote() != null &&  chargingStation.get(feecheck).getNote().contains("제한")) { feecheck++; continue; }
                        }catch (NullPointerException e){}
                    }
                    if(checkboxarray.get(0) == 0){ if(chargingStation.get(feecheck).getId().contains("CU")){ feecheck++; continue; }} // 씨어스 CU
                    if(checkboxarray.get(1) == 0){ if(chargingStation.get(feecheck).getId().contains("CV")){ feecheck++; continue; }} // 대영채비 CV
                    if(checkboxarray.get(2) == 0){ if(chargingStation.get(feecheck).getId().contains("EM")){ feecheck++; continue; }} // evmost EM
                    if(checkboxarray.get(3) == 0){ if(chargingStation.get(feecheck).getId().contains("EP")){ feecheck++; continue; }} // 이카플러그 EP
                    if(checkboxarray.get(4) == 0){ if(chargingStation.get(feecheck).getId().contains("EV")){ feecheck++; continue; }} // 에버온 EV
                    if(checkboxarray.get(5) == 0){ if(chargingStation.get(feecheck).getId().contains("EZ")){ feecheck++; continue; }} // 차지인 EZ
                    if(checkboxarray.get(6) == 0){ if(chargingStation.get(feecheck).getId().contains("GN")){ feecheck++; continue; }} // 지엔텔 GN
                    if(checkboxarray.get(7) == 0){ if(chargingStation.get(feecheck).getId().contains("GS")){ feecheck++; continue; }} // GS칼텍스 GS
                    if(checkboxarray.get(8) == 0){ if(chargingStation.get(feecheck).getId().contains("HE")){ feecheck++; continue; }} // 한국전기차충전서비스 HE
                    if(checkboxarray.get(9) == 0){ if(chargingStation.get(feecheck).getId().contains("HM")){ feecheck++; continue; }} // HUMAX EV HM
                    if(checkboxarray.get(10) == 0){ if(chargingStation.get(feecheck).getId().contains("JE")){ feecheck++; continue; }} // 제주전기자동차서비스 JE
                    if(checkboxarray.get(11) == 0){ if(chargingStation.get(feecheck).getId().contains("KE")){ feecheck++; continue; }} // 한국전기차인프라기술q KE
                    if(checkboxarray.get(12) == 0){ if(chargingStation.get(feecheck).getId().contains("KL")){ feecheck++; continue; }} // 클린일렉스 KL
                    if(checkboxarray.get(13) == 0){ if(chargingStation.get(feecheck).getId().contains("KP")){ feecheck++; continue; }} // 한국전력 KP
                    if(checkboxarray.get(14) == 0){ if(chargingStation.get(feecheck).getId().contains("KT")){ feecheck++; continue; }} // KT KT
                    if(checkboxarray.get(15) == 0){ if(chargingStation.get(feecheck).getId().contains("LH")){ feecheck++; continue; }} // LG헬로비전 LH
                    if(checkboxarray.get(16) == 0){ if(chargingStation.get(feecheck).getId().contains("ME")){ feecheck++; continue; }} // 환경부 ME
                    if(checkboxarray.get(17) == 0){ if(chargingStation.get(feecheck).getId().contains("MO")){ feecheck++; continue; }} // 매니지온 MO
                    if(checkboxarray.get(18) == 0){ if(chargingStation.get(feecheck).getId().contains("PI")){ feecheck++; continue; }} // 포스코ICT PI
                    if(checkboxarray.get(19) == 0){ if(chargingStation.get(feecheck).getId().contains("PW")){ feecheck++; continue; }} // 파워큐브 PW
                    if(checkboxarray.get(20) == 0){ if(chargingStation.get(feecheck).getId().contains("SE")){ feecheck++; continue; }} // 서울시 SE
                    if(checkboxarray.get(21) == 0){ if(chargingStation.get(feecheck).getId().contains("SF")){ feecheck++; continue; }} // 스타코프 SF
                    if(checkboxarray.get(22) == 0){ if(chargingStation.get(feecheck).getId().contains("SK")){ feecheck++; continue; }} // SK에너지 SK
                    if(checkboxarray.get(23) == 0){ if(chargingStation.get(feecheck).getId().contains("SS")){ feecheck++; continue; }} // 삼성이브이씨 SS
                    if(checkboxarray.get(24) == 0){ if(chargingStation.get(feecheck).getId().contains("ST")){ feecheck++; continue; }} // 에스트래픽 ST
                    if(checkboxarray.get(25) == 0){ if(chargingStation.get(feecheck).getId().contains("TD")){ feecheck++; continue; }} // 타디스테크놀로지 TD
                    Marker marker = new Marker();
                    marker.setIconPerspectiveEnabled(true); // 원근감 표시
                    marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker));
                    marker.setPosition(markerPosition); // 마커 위치
                    marker.setMap(naverMap);
                    marker.setOnClickListener(MainActivity.this::onClick);
                    activeMarkers.add(marker);
                    if(tagwithinSight(currentPosition, markerPosition)) {
                        infoWindow = new InfoWindow();
                        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                            @NonNull
                            @Override
                            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                                return Float.toString(chargingStation.get(feecheck).getFee());
                            }
                        });
                        infoWindow.open(marker);
                    }
                    feecheck++;
                }
            }
        });

    }

    // 마커 클릭 이벤트
    public static String mkname = "NULL";
    public static String mkbusi = "NULL";
    public static float mkfee = 0;
    Marker lastClicked = null;
    @Override
    public synchronized boolean onClick(@NonNull Overlay overlay) {
        if(overlay instanceof Marker){
            for(int i=0;i<chargingStation.size();i++) {
                if (((Marker) overlay).getPosition().latitude == chargingStation.get(i).getLat() && ((Marker) overlay).getPosition().longitude == chargingStation.get(i).getLng()) {
                    mkname = chargingStation.get(i).getName();
                    mkbusi = chargingStation.get(i).getBusiNm();
                    for(int j=0;j<estimated_fee.size();j++){
                        if(chargingStation.get(i).getId().startsWith(estimated_fee.get(j).getBusiId())) {
                            mkfee = estimated_fee.get(j).getFee();
                            break;
                        }
                    }
                    break;
                }
            }
//            infoWindow = new InfoWindow();
//            Marker marker = (Marker)overlay;
//            marker.setTag("chargingStation.get(feecheck).getFee()");
//            infoWindow.open(marker);
            // 마커 클릭 시 카메라 이동 - 이동 후 클릭된 마커 이미지 변경 안되는 오류
            /*LatLng markercenter = new LatLng(station.getLat(), station.getLng());
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(markercenter);
            naverMap.moveCamera(cameraUpdate);*/

            if (lastClicked!=null) {
                lastClicked.setIcon((OverlayImage.fromResource(R.drawable.ic_marker)));
                Log.d("marker", "purple");
            }
            lastClicked = (Marker) overlay;
            if (lastClicked!=null) {
                ((Marker) overlay).setIcon((OverlayImage.fromResource(R.drawable.ic_marker_clicked)));
                Log.d("marker", "pink");
            }

            showbs(((Marker) overlay).getPosition().latitude, ((Marker) overlay).getPosition().latitude);
            return true;
        }
        return false;
    }

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

    public boolean tagwithinSight(LatLng currentPosition, LatLng markerPosition) {
        boolean tagwithinSightMarkerLat = Math.abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT;
        boolean tagwithinSightMarkerLng = Math.abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG;
        return tagwithinSightMarkerLat && tagwithinSightMarkerLng;
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

    public Bitmap getBitmap(String imgPath) {
        Thread imgThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imgPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bmImg = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                }
            }
        };
        imgThread.start();
        try {
            imgThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return bmImg;
        }
    }

    public class UserInfo extends AsyncTask<String, Void, String> {

        @Override
        protected synchronized String doInBackground(String... strings) {
            try {

                Response<User> userResponse = retrofit.server.getUserInfo(user.getId()).execute();
                User resource = userResponse.body();
                if (resource.getCar_number() != null)
                    user.setCar_number(resource.getCar_number());
                else
                    user.setCar_number("");
                if (resource.getMessage() != null)
                    user.setMessage(resource.getMessage());
                else
                    user.setMessage("");

                Response<Car> carResponse = retrofit.server.getCarInfo(user.getId()).execute();
                car = carResponse.body();

                Response<List<Card>> membershipResponse = retrofit.server.getMembershipInfo(user.getId()).execute();
                List<Card> result = membershipResponse.body();
                for (Card i : result) {
                    if(!membership.contains(i))
                        membership.add(i);
                }

                Response<List<Card>> paymentResponse = retrofit.server.getPaymentInfo(user.getId()).execute();
                result = paymentResponse.body();
                for (Card i : result) {
                    if(!payment.contains(i))
                        payment.add(i);
                }

                Response<List<Fee>> feeResponse = retrofit.server.getChargingFee(user.getId()).execute();
                for (Fee f : feeResponse.body()) {
                    for (ChargingStation i : chargingStation) {
                        if (i.getId().contains(f.getBusiId()))
                            i.setFee(f.getFee());
                    }
                }

            } catch (IOException e) {
                Log.i("에러입니다.", "" + e.toString());
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        NavigationView navigationView = findViewById(R.id.nav_view);
        //-----------------------------drawer 설정 부분-----------------------------------------------------//
        // navigation drawer 회원이름 변경
        View headerView = navigationView.getHeaderView(0);
        TextView test = (TextView) headerView.findViewById(R.id.textView);
        if (user.getName() == null)
            test.setText("로그인 해주세요");
        else
            test.setText("" + user.getName());

        ImageView car_image = (ImageView) headerView.findViewById(R.id.nav_car_image);
        TextView car_text = (TextView) headerView.findViewById(R.id.nav_carname);

        // 사용자가 설정해둔 차량 이미지로! (없으면 기본 값 쏘울 뭔가 되어 있음)

        Log.d("Drawer", car.getVehicle());
        car_image.setImageBitmap(getBitmap(car.getImage()));
        car_text.setText(car.getVehicle());  // 사용자가 설정해둔 차량 이름으로!

        RecyclerView recyclerView = headerView.findViewById(R.id.nav_recycler_m);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DrawerCardAdapter adapter = new DrawerCardAdapter();

        // 멥버쉽 카드 화면에 추가
        for (Card i : membership) {
            adapter.addItem(i);
            Log.d("Drawer", "membership 추가");
        }
        recyclerView.setAdapter(adapter);

        // 결제 카드 정보 수정 액티비티 화면 구성
        RecyclerView recyclerView2 = headerView.findViewById(R.id.nav_recycler_p);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        DrawerCardAdapter adapter2 = new DrawerCardAdapter();

        // 결제 카드 화면에 추가
        for (Card i : payment) {
            adapter2.addItem(i);
        }
        recyclerView2.setAdapter(adapter2);

        // drawer 하단 차량검색 확인 버튼 클릭 시 StatusmessageActivity로 입력한 차량번호 전달
        TextView carnumbertext = findViewById(R.id.nav_number_search);
        Button carnumbersearch = findViewById(R.id.button);
        carnumbersearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키보드 내리기
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(carnumbertext.getWindowToken(), 0);

                String getinputtext = carnumbertext.getText().toString();
                if(getinputtext.length() <= 0){ // 입력값 없을 때
                    Snackbar.make(v, "차량 번호를 입력해 주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else{
                    StatusmessageActivity.result = null;
                    SearchPerson sp = new SearchPerson();
                    sp.execute();
                    carnumbertext.setText(null); // 텍스트 초기화
//                    Intent intent = new Intent(getApplicationContext(), StatusmessageActivity.class);
//                    intent.putExtra("inputcarnumber", getinputtext); // 입력한 차량번호
//                    startActivity(intent);
                }
                Log.d("테스트임", "이게되네");
            }
        });
        Log.d("navi", "selected");
        return super.onOptionsItemSelected(item);
    }

    //차량 번호 검색 수정 중
    public class SearchPerson extends AsyncTask<Void, Void, Void> {

        @Override
        protected synchronized Void doInBackground(Void... voids) {
            try {
                TextView carnumbertext = findViewById(R.id.nav_number_search);
                String getinputtext = carnumbertext.getText().toString();
                Response<User> response = retrofit.server.getPerson(getinputtext).execute();
                StatusmessageActivity.result = response.body();
                onPostExecute();
            } catch (IOException e) {
                Log.i("차번호 검색 오류", "" + e.toString());
            }
            return null;
        }

        protected synchronized void onPostExecute() {
            super.onPostExecute(null);
            Intent intent = new Intent(getApplicationContext(), StatusmessageActivity.class);
            startActivity(intent);
        }
    }
}