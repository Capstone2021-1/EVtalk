package org.techtown.evtalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;
import com.pedro.library.AutoPermissions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    public static User user;   //사용자
    private AppBarConfiguration mAppBarConfiguration;
    private NaverMap naverMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인한 사용자 id값 넘겨받아 객체생성
        Intent intent = getIntent();
        user = new User(intent.getExtras().getLong("id")
                , intent.getExtras().getString("name")
                , intent.getExtras().getString("image"));
        getUserInfo();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home).setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


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

//        CameraPosition cameraPosition = new CameraPosition(
//                new LatLng(37.5666102, 126.9783881), // 대상 지점
//                16, // 줌 레벨
//                20, // 기울임 각도
//                180 // 베어링 각도
//        );

//        Toast.makeText(this,
//                "대상 지점 위도: " + cameraPosition.target.latitude + ", " +
//                        "대상 지점 경도: " + cameraPosition.target.longitude + ", " +
//                        "줌 레벨: " + cameraPosition.zoom + ", " +
//                        "기울임 각도: " + cameraPosition.tilt + ", " +
//                        "베어링 각도: " + cameraPosition.bearing,
//                Toast.LENGTH_SHORT).show();


        AutoPermissions.Companion.loadAllPermissions(this, 101);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frag_menu_other, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() { // AppBar에 생성된 뒤로가기 버튼을 눌렀을 때 home fragment로 이동
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
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
    }


    //메인 엑티비티 실행 시 DB에서 유저 정보 받아오기
    public void getUserInfo() {
        RetrofitConnection retrofit = new RetrofitConnection();
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
    }

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
}