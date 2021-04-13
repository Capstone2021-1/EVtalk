package org.techtown.evtalk;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import com.google.android.material.navigation.NavigationView;
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
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.pedro.library.AutoPermissions;

import org.techtown.evtalk.ui.userinfo.UserInfoActivity;
import org.techtown.evtalk.user.Car;
import org.techtown.evtalk.user.Card;
import org.techtown.evtalk.user.ChargingStation;
import org.techtown.evtalk.user.RetrofitConnection;
import org.techtown.evtalk.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    public static User user;   //사용자
    public static Car car;     //사용자 차량 정보
    public static List<Card> membership = new ArrayList<>();    //사용자 회원카드 정보
    public static List<Card> payment = new ArrayList<>();
    public static List<ChargingStation> chargingStation = new ArrayList<>();;   //충전소 기본 정보
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
        if(user != null)
            getUserInfo();

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

    @Override // home 에서 뒤로가기 두번 클릭 시 종료됩니다
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

    public void startUserInfoActivity(View view){
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        startActivity(intent);
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
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
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

        // 카메라 초기 위치 설정
        LatLng initialPosition = new LatLng(37.506855, 127.066242);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);

        // 마커들 위치 정의
        markersPosition = new Vector<LatLng>();
        for (int i = 0; i < chargingStation.size(); i++) {
            markersPosition.add(new LatLng(chargingStation.get(i).getLat(),chargingStation.get(i).getLng()));
        }

        // 카메라 이동 되면 호출 되는 이벤트
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean animated) {
                freeActiveMarkers();
                // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                LatLng currentPosition = getCurrentPosition(naverMap);
                for (LatLng markerPosition: markersPosition) {
                    if (!withinSightMarker(currentPosition, markerPosition))
                        continue;
                    Marker marker = new Marker();
                    marker.setIconPerspectiveEnabled(true); // 원근감 표시
                    marker.setIcon(OverlayImage.fromResource(R.drawable.ic_baseline_location_on_24));
                    marker.setPosition(markerPosition); // 마커 위치
                    marker.setMap(naverMap);
                    activeMarkers.add(marker);
                }
            }
        });
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

        retrofit.server.getMembershipInfo(user.getId()).enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                if(response.isSuccessful()) {
                    List<Card> result = response.body();
                    for(Card i : result) {
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
                if(response.isSuccessful()) {
                    List<Card> result = response.body();
                    for(Card i : result) {
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
                if(response.body() != null) {
                    car = response.body();
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.i("failure", "차량 정보 받아오기 실패");
            }
        });
    }
}