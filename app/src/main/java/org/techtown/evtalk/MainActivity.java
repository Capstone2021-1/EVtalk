package org.techtown.evtalk;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
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

import org.techtown.evtalk.ui.message.ChatListActivity;
import org.techtown.evtalk.ui.message.StatusmessageActivity;
import org.techtown.evtalk.ui.restaurant.RestaurantActivity;
import org.techtown.evtalk.ui.search.GpsTracker;
import org.techtown.evtalk.ui.search.SearchResultActivity;
import org.techtown.evtalk.ui.station.StationPageActivity;
import org.techtown.evtalk.ui.userinfo.DrawerCardAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener {
    private InfoWindow infoWindow;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    public static User user;   //?????????
    public static Car car;     //????????? ?????? ??????
    public static List<Card> membership = new ArrayList<>();    //????????? ???????????? ??????
    public static List<Card> payment = new ArrayList<>();
    public static List<ChargingStation> chargingStation = new ArrayList<>();   //????????? ?????? ??????
    public static List<ChargingStation> charg = new ArrayList<>();

    ;   //????????? ?????? ??????
    public static List<Fee> estimated_fee = new ArrayList<>();  //?????? ?????? ??????
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
    public static ArrayList<Integer> checkboxarray = new ArrayList<>(); // ?????? ?????? ?????? ??????
    public static ArrayList<Integer> checkboxvelocityarray = new ArrayList<>(); // ?????? ?????? ?????? ??????

    Marker lastClicked = null;
    Bitmap bmImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //???????????? ????????? id??? ???????????? ????????????
        Intent intent = getIntent();
        user = new User(intent.getExtras().getLong("id")
                , intent.getExtras().getString("name")
                , intent.getExtras().getString("image"));
        if (user != null) {
            UserInfo temp = new UserInfo();
            temp.execute();
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("token", token);
                        retrofit.server.setUserToken(user.getId(), token).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                //??????
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.i("fail", t.toString());
                            }
                        });
                    }
                });

        // ?????? ?????? ?????????
        if (car == null) {
            car = new Car();
            car.setImage("https://evtalk.s3.ap-northeast-2.amazonaws.com/car_image/soulbooster.png");
            car.setVehicle("????????? ???????????????!");
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

        // ?????? ?????? ?????????
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            Log.d("Map", "Make mapFragement");
            NaverMapOptions options = new NaverMapOptions().locationButtonEnabled(false);
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        //?????? ?????? ?????????

        // FusedLocationSource ???????????? Navermap??? ??????
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        BSsheet = (FrameLayout) findViewById(R.id.bs_sheet);
        BottomSheetBehavior bs = BottomSheetBehavior.from(BSsheet); // ?????? ?????? ????????? ?????? Behavior
        hidebs();

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() { // ???????????? - fab ?????? ??? ??????
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimeActivity.class);
                startActivityForResult(intent, TIMECODE);
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() { // ????????? ???????????? ?????? - fab2 ?????? ??? ??????
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ChargingSettingActivity.class);
                startActivity(intent1);
            }
        });

        /* ?????? ?????? ????????? ???????????? 20?????? */
        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication ("l7xx68d24409582244c887acd07632eaefcb");
        TMapPoint point = new TMapPoint(latitude, longitude);

        TMapData tmapdata = new TMapData();

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() { // ?????? ?????? - fab3 ?????? ??? ??????
            @Override
            public void onClick(View view) {
                makeRestList();   // ?????? ?????? ????????? ????????? ??????
                try {
                    Thread.sleep(400);     // intent??? ?????? ?????? ?????? ????????? sleep ???
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // ??? ????????? ???????????? ??????
                for (int i = 0; i < restItem.size(); i++) {
                    TMapPOIItem item = restItem.get(i);
                    if(item.getPOIName().contains("?????????")||item.getPOIName().contains("??????")){
                        continue;
                    }
                    Log.d("??????","POI Name: " + item.getPOIName() + "," + "Address: "
                            + item.getPOIAddress().replace("null", "")
                            + " Distance: "+ Double.toString(item.getDistance(point))
                            + " ?????? ??????: "+item.telNo);
                }
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // ???????????? ??????

            }
        });

        // ?????? ?????? ?????? ?????? ??????
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                hidebs();
            }
        });
        // ?????? ?????? ?????? ??? ????????? ????????? ??????
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
        }else if(requestCode == SEARCH_RESULT_CODE){   // SearchResultActivity?????? ?????? ????????? ????????? MainActivity?????? ?????? ?????? ??????
            if(resultCode == 2001) {

                Log.d("searchMainActivity", data.getStringExtra("searchLat"));
                Log.d("searchMainActivity", data.getStringExtra("searchLng"));

                searchLat = Double.parseDouble(data.getStringExtra("searchLat"));
                searchLng = Double.parseDouble(data.getStringExtra("searchLng"));

                // ?????? ?????? ?????? ??????
                LatLng latLng = new LatLng(searchLat, searchLng);
                Marker marker = new Marker();
                marker.setPosition(latLng);

                // ?????? ??? ???????????? ??? ????????? ???????????? ?????????
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

    // ?????? ????????? ?????? ??????
    public void makeRestList(){
//        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
//        latitude = gpsTracker.getLatitude();
//        longitude = gpsTracker.getLongitude();
        LatLng curLatLng = getCurrentPosition(naverMap);
        latitude = curLatLng.latitude;
        longitude = curLatLng.longitude;

        TMapPoint point = new TMapPoint(latitude, longitude);

        TMapData tmapdata = new TMapData();
        tmapdata.findAroundNamePOI(point, "?????????", 3, 20,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            if(item.getPOIName().contains("?????????")||item.getPOIName().contains("??????")|| item.telNo == null){
                                continue;
                            }else {
                                restItem.add(item);
                            }
                        }
                    }
                });

        tmapdata.findAroundNamePOI(point, "??????", 3, 20,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            if(item.getPOIName().contains("?????????")||item.getPOIName().contains("??????")|| item.telNo == null){
                                continue;
                            }else {
                                restItem.add(item);
                            }
                        }
                    }
                });
    }


    // ?????? ?????? ??????
    public synchronized void showbs(double lat, double lng){
        SimpleDateFormat formatType = new SimpleDateFormat("MM??? dd??? HH:mm");
        String showfee = "??? ";
        BSsheet = (FrameLayout) findViewById(R.id.bs_sheet);
        BottomSheetBehavior bs = BottomSheetBehavior.from(BSsheet);

        bs.setState(BottomSheetBehavior.STATE_EXPANDED); // ?????? ?????? ??????

        TextView bs_stationname = findViewById(R.id.bs_stationname);
        bs_stationname.setText(mkname); // ????????? ?????? ??????

        TextView bs_comname = findViewById(R.id.bs_comname);
        bs_comname.setText(mkbusi); // ???????????? ??????

        TextView tv1 = findViewById(R.id.textView15);
        TextView tv2 = findViewById(R.id.textView16);
        TextView tv3 = findViewById(R.id.textView17);

        if(TimeActivity.total_time == "" || TimeActivity.total_time == "0??????") { // ?????? ?????? ??? ?????? ???
            Date currentTime = Calendar.getInstance().getTime();
            start_time = new SimpleDateFormat("M??? d??? EEE HH:mm", Locale.getDefault()).format(currentTime);
            end_time = new SimpleDateFormat("M??? d??? EEE HH:mm", Locale.getDefault()).format(currentTime);

            tv1.setText(start_time + " ~ " + end_time);
            tv2.setText("?????? ?????? : "+ "0" +" ???");

            showfee += "0?????? ?????? ???  |  0 KWh ??????  |  0 % ?????? ??????";
            tv3.setText(showfee);
        }
        else{ // ?????? ?????? ???????????? ???
            tv1.setText(formatType.format(TimeActivity.sDate) +" ~ "+formatType.format(TimeActivity.eDate));
            tv2.setText("?????? ?????? : "+ Integer.toString((int)mkfee)+" ???");

            for(int i=0;i<3;i++){
                if(i == 0){
                    showfee += Integer.toString((int)estimated_fee.get(estimated_fee.size()-i-1).getFee());
                    showfee += "?????? ?????? ???  |  ";
                }
                else if(i==1){
                    showfee += Integer.toString((int)estimated_fee.get(estimated_fee.size()-i-2).getFee());
                    showfee += " KWh ??????  |  ";
                }
                else if(i==2){
                    showfee += Integer.toString((int)estimated_fee.get(estimated_fee.size()-i).getFee());
                    showfee += " % ?????? ??????";
                }
            }
            tv3.setText(showfee);
        }

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.hide(); // ????????? ?????? ?????????
        fab2.hide();
        fab3.hide();
    }
    // ?????? ?????? ?????????
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
        if(lastClicked != null) {
            lastClicked.setIcon((OverlayImage.fromResource(R.drawable.ic_marker)));
            lastClicked = null;
        }
    }

    // home ?????? ???????????? ?????? ?????? ??? ??????
    @Override
    public void onBackPressed() {
        // ??????????????? ???????????? ????????? ????????? ????????? 2?????? ?????? ??????????????? ?????? ??? 2?????? ???????????? Toast Show
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'????????????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // ??????????????? ???????????? ????????? ????????? ????????? 2?????? ?????? ??????????????? ?????? ??? 2?????? ????????? ???????????? ??????
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this); // ??? ??????
            toast.cancel(); // ?????? ????????? Toast ??????
        }
    }

    // ???????????????????????? ??????
    public void startChatListActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
        intent.putExtra("user_id", user.getId());
        intent.putExtra("car_number", user.getCar_number());
        startActivity(intent);
    }

    // ??????????????? ??????
    public void startUserInfoActivity(View view) {
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // ?????? ?????????
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

    // ?????? ?????? ?????????
    public static List<SearchResult> results = new ArrayList<>(); //?????? ?????? ??????.

    private final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            search_result = s;
            if(search_result.endsWith(" ")){
                search_result = search_result.substring(0, search_result.length()-1);
            }
            hidebs();   // ???????????? ?????? ?????? ?????? ???????????? ??????
            //Log.d("search", "?????? ??????");

            searchView.clearFocus();

            Log.d("search", Integer.toString(results.size()));
            startSearchActivity(search_result);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            //Log.d("search", "?????????");
            return true;
        }
    };


    // ???????????? ????????? ?????? ??????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("????????? / ????????? ????????????");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setIconifiedByDefault(false);

        return true;
    }

    // ?????? ?????? ?????? ?????? ??? ????????? ?????????
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

        // FusedLocationSource ???????????? Navermap??? ??????
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);


        // ?????? ?????? ??????
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true); // ?????? ?????? ?????? ?????????
        naverMap.setBuildingHeight(0.5f); // ????????? ??????????????? ?????? ??????????????? ??????
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false); // ????????? ?????? ??????


        // UI ?????? ??????
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setScaleBarEnabled(true);  // ?????? ????????? ??????
        LocationButtonView locationButtonView = findViewById(R.id.location); // ?????? ?????? ?????? ??????
        locationButtonView.setMap(naverMap); // ?????? ?????? ?????? ??????

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // ?????? ?????? ??????
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        // ????????? ?????? ?????? ??????
        LatLng initialPosition = new LatLng(37.506855, 127.066242);
        cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);

        for(int i=0 ;i<3;i++) { // ????????? ???????????? ?????? ?????? ?????????
            checkboxvelocityarray.add(i,0); // ?????? 50KWh, 100KWh ??? ?????? ?????? ??????
            if(i == 0) checkboxvelocityarray.add(i,1); // ?????? ?????? 7KWh ????????? ?????????
        }

        for(int i=0 ;i<31;i++) { // ????????? ???????????? ?????? ?????? ?????????
            checkboxarray.add(i,1); // ?????? ?????? ?????? ??? ????????? 1 ??????  // checked = 1 , unchecked = 0
        }

        // ????????? ?????? ??????
        markersPosition = new Vector<LatLng>();
        for (int i = 0; i < chargingStation.size(); i++) {
            markersPosition.add(new LatLng(chargingStation.get(i).getLat(),chargingStation.get(i).getLng()));
        }


        // ????????? ?????? ?????? ?????? ?????? ????????? // ????????????
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public synchronized void onCameraChange(int reason, boolean animated) {
                feecheck = 0;
                freeActiveMarkers();
                // ????????? ?????????????????? ???????????? ????????????????????? ?????? ??????
                LatLng currentPosition = getCurrentPosition(naverMap);
                for (LatLng markerPosition : markersPosition) {
                    if (!withinSightMarker(currentPosition, markerPosition)) {
                        feecheck++;
                        continue;
                    }
                    if(checkboxarray.get(26) == 1){ // ???????????? ????????? ??????
                        try{
                            if(chargingStation.get(feecheck).getLimitDetail() != null && chargingStation.get(feecheck).getLimitDetail().contains("??????")) { feecheck++; continue; }
                            if(chargingStation.get(feecheck).getNote() != null &&  chargingStation.get(feecheck).getNote().contains("??????")) { feecheck++; continue; }
                        }catch (NullPointerException e){}
                    }
                    if(checkboxarray.get(27) == 0){ // DC?????? ??????
                        if(chargingStation.get(feecheck).getchgerType().equals("04") || chargingStation.get(feecheck).getchgerType().equals("05")) { feecheck++; continue; } }
                    if(checkboxarray.get(28) == 0){ // DC????????? ??????
                        if(chargingStation.get(feecheck).getchgerType().equals("01") || chargingStation.get(feecheck).getchgerType().equals("03") || chargingStation.get(feecheck).getchgerType().equals("05") || chargingStation.get(feecheck).getchgerType().equals("06")) { feecheck++; continue; } }
                    if(checkboxarray.get(29) == 0){ // AC3??? ??????
                        if(chargingStation.get(feecheck).getchgerType().equals("03") || chargingStation.get(feecheck).getchgerType().equals("06") || chargingStation.get(feecheck).getchgerType().equals("07")) { feecheck++; continue; } }
                    if(checkboxarray.get(30) == 0){ // ?????? ??????
                        if(chargingStation.get(feecheck).getchgerType().equals("02")) { feecheck++; continue; } }
                    if(checkboxarray.get(0) == 0){ if(chargingStation.get(feecheck).getId().contains("CU")){ feecheck++; continue; }} // ????????? CU
                    if(checkboxarray.get(1) == 0){ if(chargingStation.get(feecheck).getId().contains("CV")){ feecheck++; continue; }} // ???????????? CV
                    if(checkboxarray.get(2) == 0){ if(chargingStation.get(feecheck).getId().contains("EM")){ feecheck++; continue; }} // evmost EM
                    if(checkboxarray.get(3) == 0){ if(chargingStation.get(feecheck).getId().contains("EP")){ feecheck++; continue; }} // ??????????????? EP
                    if(checkboxarray.get(4) == 0){ if(chargingStation.get(feecheck).getId().contains("EV")){ feecheck++; continue; }} // ????????? EV
                    if(checkboxarray.get(5) == 0){ if(chargingStation.get(feecheck).getId().contains("EZ")){ feecheck++; continue; }} // ????????? EZ
                    if(checkboxarray.get(6) == 0){ if(chargingStation.get(feecheck).getId().contains("GN")){ feecheck++; continue; }} // ????????? GN
                    if(checkboxarray.get(7) == 0){ if(chargingStation.get(feecheck).getId().contains("GS")){ feecheck++; continue; }} // GS????????? GS
                    if(checkboxarray.get(8) == 0){ if(chargingStation.get(feecheck).getId().contains("HE")){ feecheck++; continue; }} // ?????????????????????????????? HE
                    if(checkboxarray.get(9) == 0){ if(chargingStation.get(feecheck).getId().contains("HM")){ feecheck++; continue; }} // HUMAX EV HM
                    if(checkboxarray.get(10) == 0){ if(chargingStation.get(feecheck).getId().contains("JE")){ feecheck++; continue; }} // ?????????????????????????????? JE
                    if(checkboxarray.get(11) == 0){ if(chargingStation.get(feecheck).getId().contains("KE")){ feecheck++; continue; }} // ??????????????????????????????q KE
                    if(checkboxarray.get(12) == 0){ if(chargingStation.get(feecheck).getId().contains("KL")){ feecheck++; continue; }} // ??????????????? KL
                    if(checkboxarray.get(13) == 0){ if(chargingStation.get(feecheck).getId().contains("KP")){ feecheck++; continue; }} // ???????????? KP
                    if(checkboxarray.get(14) == 0){ if(chargingStation.get(feecheck).getId().contains("KT")){ feecheck++; continue; }} // KT KT
                    if(checkboxarray.get(15) == 0){ if(chargingStation.get(feecheck).getId().contains("LH")){ feecheck++; continue; }} // LG???????????? LH
                    if(checkboxarray.get(16) == 0){ if(chargingStation.get(feecheck).getId().contains("ME")){ feecheck++; continue; }} // ????????? ME
                    if(checkboxarray.get(17) == 0){ if(chargingStation.get(feecheck).getId().contains("MO")){ feecheck++; continue; }} // ???????????? MO
                    if(checkboxarray.get(18) == 0){ if(chargingStation.get(feecheck).getId().contains("PI")){ feecheck++; continue; }} // ?????????ICT PI
                    if(checkboxarray.get(19) == 0){ if(chargingStation.get(feecheck).getId().contains("PW")){ feecheck++; continue; }} // ???????????? PW
                    if(checkboxarray.get(20) == 0){ if(chargingStation.get(feecheck).getId().contains("SE")){ feecheck++; continue; }} // ????????? SE
                    if(checkboxarray.get(21) == 0){ if(chargingStation.get(feecheck).getId().contains("SF")){ feecheck++; continue; }} // ???????????? SF
                    if(checkboxarray.get(22) == 0){ if(chargingStation.get(feecheck).getId().contains("SK")){ feecheck++; continue; }} // SK????????? SK
                    if(checkboxarray.get(23) == 0){ if(chargingStation.get(feecheck).getId().contains("SS")){ feecheck++; continue; }} // ?????????????????? SS
                    if(checkboxarray.get(24) == 0){ if(chargingStation.get(feecheck).getId().contains("ST")){ feecheck++; continue; }} // ??????????????? ST
                    if(checkboxarray.get(25) == 0){ if(chargingStation.get(feecheck).getId().contains("TD")){ feecheck++; continue; }} // ???????????????????????? TD
                    Marker marker = new Marker();
                    marker.setIconPerspectiveEnabled(true); // ????????? ??????
                    marker.setPosition(markerPosition); // ?????? ??????
                    if(lastClicked != null){
                        if (marker.getPosition().equals(lastClicked.getPosition())) {
                            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker_clicked));
                            lastClicked = marker;
                        }else
                            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker));
                    }else{
                        marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker));
                    }
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
        // ?????? ?????? ?????????
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                if(lastClicked != null){
                    lastClicked.setIcon((OverlayImage.fromResource(R.drawable.ic_marker)));
                    lastClicked = null;
                    hidebs();
                }
            }
        });
    }

    // ?????? ?????? ?????????
    public static String mkname = "NULL";
    public static String mkbusi = "NULL";
    public static float mkfee = 0;
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

            if(lastClicked != null){
                lastClicked.setIcon((OverlayImage.fromResource(R.drawable.ic_marker)));
                lastClicked = (Marker) overlay;
                ((Marker) overlay).setIcon((OverlayImage.fromResource(R.drawable.ic_marker_clicked)));
            }else{
                ((Marker) overlay).setIcon((OverlayImage.fromResource(R.drawable.ic_marker_clicked)));
                lastClicked = (Marker) overlay;
            }

            showbs(((Marker) overlay).getPosition().latitude, ((Marker) overlay).getPosition().latitude);
            return true;
        }
        return false;
    }

    // ?????? ?????? ???????????? ????????? ??????
    private Vector<LatLng> markersPosition;
    private Vector<Marker> activeMarkers;

    // ?????? ???????????? ???????????? ??????
    public LatLng getCurrentPosition(NaverMap naverMap) {
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        return new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    // ????????? ????????? ????????? ????????????(???????????? ???????????? ?????? ?????? 3km ???)??? ????????? ??????
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

    // ???????????? ?????????????????? ????????? ???????????? ??????
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

                Response<User> rResponse = retrofit.server.getUserReview(MainActivity.user.getId()).execute();
                user.setTotal_review(rResponse.body().getTotal_review());

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
                Log.i("???????????????.", "" + e.toString());
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        NavigationView navigationView = findViewById(R.id.nav_view);
        //-----------------------------drawer ?????? ??????-----------------------------------------------------//
        // navigation drawer ???????????? ??????
        View headerView = navigationView.getHeaderView(0);
        TextView test = (TextView) headerView.findViewById(R.id.textView);
        if (user.getName() == null)
            test.setText("????????? ????????????");
        else
            test.setText("" + user.getName());

        ImageView car_image = (ImageView) headerView.findViewById(R.id.nav_car_image);
        TextView car_text = (TextView) headerView.findViewById(R.id.nav_carname);

        // ???????????? ???????????? ?????? ????????????! (????????? ?????? ??? ?????? ?????? ?????? ??????)

        Log.d("Drawer", car.getVehicle());
        car_image.setImageBitmap(getBitmap(car.getImage()));
        car_text.setText(car.getVehicle());  // ???????????? ???????????? ?????? ????????????!

        RecyclerView recyclerView = headerView.findViewById(R.id.nav_recycler_m);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DrawerCardAdapter adapter = new DrawerCardAdapter();

        // ????????? ?????? ????????? ??????
        for (Card i : membership) {
            adapter.addItem(i);
            Log.d("Drawer", "membership ??????");
        }
        recyclerView.setAdapter(adapter);

        // ?????? ?????? ?????? ?????? ???????????? ?????? ??????
        RecyclerView recyclerView2 = headerView.findViewById(R.id.nav_recycler_p);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        DrawerCardAdapter adapter2 = new DrawerCardAdapter();

        // ?????? ?????? ????????? ??????
        for (Card i : payment) {
            adapter2.addItem(i);
        }
        recyclerView2.setAdapter(adapter2);

        // drawer ?????? ???????????? ?????? ?????? ?????? ??? StatusmessageActivity??? ????????? ???????????? ??????
        TextView carnumbertext = findViewById(R.id.nav_number_search);
        Button carnumbersearch = findViewById(R.id.button);
        carnumbersearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ????????? ?????????
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(carnumbertext.getWindowToken(), 0);

                String getinputtext = carnumbertext.getText().toString();
                if(getinputtext.length() <= 0){ // ????????? ?????? ???
                    Snackbar.make(v, "?????? ????????? ????????? ?????????.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else{
                    StatusmessageActivity.result = null;
                    SearchPerson sp = new SearchPerson();
                    sp.execute();
                    carnumbertext.setText(null); // ????????? ?????????
//                    Intent intent = new Intent(getApplicationContext(), StatusmessageActivity.class);
//                    intent.putExtra("inputcarnumber", getinputtext); // ????????? ????????????
//                    startActivity(intent);
                }
                Log.d("????????????", "????????????");
            }
        });
        Log.d("navi", "selected");
        return super.onOptionsItemSelected(item);
    }

    //?????? ?????? ?????? ?????? ???
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
                Log.i("????????? ?????? ??????", "" + e.toString());
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