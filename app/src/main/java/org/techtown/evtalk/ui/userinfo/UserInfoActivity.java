package org.techtown.evtalk.ui.userinfo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import org.techtown.evtalk.LoginActivity;
import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.user.Car;
import org.techtown.evtalk.user.Card;
import org.techtown.evtalk.user.RetrofitConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {
    private TextView textView;
    private Bitmap img;
    private CircleImageView profile_image;  // 원형 프로필
    private Button btn_delete;
    private Button btn_logout;
    public static List<Card> membership_list = new ArrayList<>();     //회원카드 리스트
    public static List<Card> payment_list = new ArrayList<>();        //결제카드 리스트
    public static List<Car> car_list = new ArrayList<>();             //차량 리스트(enterprise, vehicle_type, image)

    private static int flag = 0; // 서버에서 로컬로 정보 한번만 받아오는 flag

    UserInfoAdapter adapter; // 멤버쉽 카드 보여주기 위한 어뎁터
    UserInfoAdapter adapter2;  // 결제 카드를 보여주기 위한 어뎁터


    final String TAG = "Profile_image";
    String name = MainActivity.user.getName();
    String car_name;     // 선택된 차량이름
    Bitmap bmImg;

    // Request Code
    final static int PICK_IMAGE = 1; //갤러리에서 사진선택
    public static final int REQUEST_CODE_CAR = 1000; //차량 선택

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        if (flag == 0) {
            GetInfo gi = new GetInfo();
            gi.execute();
            flag = 1;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("마이 페이지"); // 타이틀 수정


        // 프로필 사진 선택
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialogRadio();
            }
        });

        // 이름 설정
        TextView name_txt = (TextView) findViewById(R.id.name_txt);
        if (name != null) {
            name_txt.setText(MainActivity.user.getName());
        }

        //작성 리뷰 수 설정
        TextView review = (TextView) findViewById(R.id.totalReview);
        if(MainActivity.user.getTotal_review() != 0)
            review.setText(Integer.toString(MainActivity.user.getTotal_review()));

        // 상태 메시지 수정하기 버튼
        Button status_edit = (Button) findViewById(R.id.btn_status_edit);
        EditText status_txt = (EditText) findViewById(R.id.status_txt);

        // 서버에 설정 해둔 상태 메시지가 있다면 그걸로 설정
        if (!MainActivity.user.getMessage().equals("")) {
            status_txt.setText(MainActivity.user.getMessage());
        }
        status_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status_txt.isFocusable() == false) {
                    status_txt.setFocusableInTouchMode(true);
                    status_txt.setFocusable(true);
                    status_edit.setText("Done");
                } else {
                    status_txt.setFocusableInTouchMode(false);
                    status_txt.setFocusable(false);
                    status_edit.setText("Edit");
                    Toast.makeText(UserInfoActivity.this, "상태 메시지 수정 완료", Toast.LENGTH_SHORT).show();
                    MainActivity.user.setMessage(status_txt.getText().toString());  // 서버에 수정된 메시지 반영
                }
            }
        });

        //차량 번호 수정하기 버튼
        Button carnum_edit = (Button) findViewById(R.id.btn_carnum_edit);
        EditText car_number = (EditText) findViewById(R.id.car_number);

        // 서버에 설정 해둔 차량 번호가 있다면 그걸로 설정
        if (!MainActivity.user.getCar_number().equals("")) {
            car_number.setText(MainActivity.user.getCar_number());
        }
        car_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8) /*,new CustomInputFilter()*/});   // 필터 적용
        carnum_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!car_number.isFocusable()) {
                    car_number.setFocusableInTouchMode(true);
                    car_number.setFocusable(true);
                    carnum_edit.setText("Done");
                } else {
                    car_number.setFocusableInTouchMode(false);
                    car_number.setFocusable(false);
                    carnum_edit.setText("Edit");
                    Toast.makeText(UserInfoActivity.this, "차량 번호 수정 완료", Toast.LENGTH_SHORT).show();
                    MainActivity.user.setCar_number(car_number.getText().toString());
                }
            }
        });

        ImageView car_image = (ImageView) findViewById(R.id.car_image);
        TextView car_text = (TextView) findViewById(R.id.car_text);

        // 사용자가 설정해둔 차량 이미지로! (없으면 기본 값 쏘울 뭔가 되어 있음)
        car_image.setImageBitmap(getBitmap(MainActivity.car.getImage()));
        car_text.setText(MainActivity.car.getVehicle());  // 사용자가 설정해둔 차량 이름으로!

        Button car_edit = (Button) findViewById(R.id.btn_car_edit);

        // 차량 선택 버튼 ( 차량 선택 상세 기능은 onActivityResult에 있음!)
        car_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCarSettingActivity();
            }
        });


        // 멤버쉽 카드 정보 수정 버튼
        Button membership_edit = (Button) findViewById(R.id.btn_membership_edit);

        membership_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMembershipSettingActivity();
            }
        });

        // 멤버쉽 카드 정보 수정 액티비티 화면 구성
        RecyclerView recyclerView = findViewById(R.id.recyclerView_info_m);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserInfoAdapter();

        // 멥버쉽 카드 화면에 추가
        for (Card i : MainActivity.membership) {
            adapter.addItem(i);
        }
        recyclerView.setAdapter(adapter);


        // 결제 카드 정보 수정 버튼
        Button payment_edit = (Button) findViewById(R.id.btn_pcard_edit);

        payment_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPaymentSettingActivity();
            }
        });

        // 결제 카드 정보 수정 액티비티 화면 구성
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_info_p);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        adapter2 = new UserInfoAdapter();

        // 결제 카드 화면에 추가
        for (Card i : MainActivity.payment) {
            adapter2.addItem(i);
        }
        recyclerView2.setAdapter(adapter2);

        // 로그아웃 기능
        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        finish();
                        startActivity(intent);
                    }
                });
                Toast.makeText(UserInfoActivity.this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 회원탈퇴 기능
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //회원탈퇴 버튼 클릭 시
                new AlertDialog.Builder(UserInfoActivity.this) //탈퇴 여부를 묻는 팝업창 실행
                        .setMessage("탈퇴하시겠습니까?") //팝업창의 메세지 설정
                        .setPositiveButton("네", new DialogInterface.OnClickListener() { //"예" 버튼 클릭 시 -> 회원탈퇴 수행
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() { //회원탈퇴 실행
                                    @Override
                                    public void onFailure(ErrorResult errorResult) { //회원탈퇴 실패 시
                                        int result = errorResult.getErrorCode();

                                        if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(UserInfoActivity.this, "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(UserInfoActivity.this, "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) { //로그인 세션이 닫혀있을 시
                                        //다시 로그인해달라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(UserInfoActivity.this, "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        final Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() { //가입되지 않은 계정에서 회원탈퇴 요구 시
                                        //가입되지 않은 계정이라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(UserInfoActivity.this, "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) { //회원탈퇴에 성공하면
                                        //DB에서 회원 삭제
                                        RetrofitConnection retrofit = new RetrofitConnection();
                                        retrofit.server.deleteUser(MainActivity.user.getId()).enqueue(new Callback<Void>() {

                                            @Override
                                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                                Log.d("success", "DB 회원삭제 성공");
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Log.d("failure", "DB 회원삭제 실패");
                                            }
                                        });
                                        //"회원탈퇴에 성공했습니다."라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(UserInfoActivity.this, "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss(); //팝업 창을 닫음
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() { //"아니요" 버튼 클릭 시 -> 팝업 창을 닫음
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //팝업 창을 닫음
                            }
                        }).show();
            }
        });

    }

    public void toCarSettingActivity() {
        Intent intent = new Intent(this, CarSettingActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CAR);
        overridePendingTransition(0, 0); // 전환효과 제거
    }

    public void toMembershipSettingActivity() {
        Intent intent = new Intent(this, MembershipSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0); // 전환효과 제거
    }

    public void toPaymentSettingActivity() {
        Intent intent = new Intent(this, PaymentSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0); // 전환효과 제거

    }

    @Override // 뒤로가기 버튼 동작 구현
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //갤러리에서 이미지 불러온 후 실행 동작
        if (requestCode == PICK_IMAGE) {
            try {
                InputStream in = getApplicationContext().getContentResolver().openInputStream(data.getData());

                img = BitmapFactory.decodeStream(in);
                // 이미지 표시
                profile_image.setImageBitmap(img);
                Log.d(TAG, "갤러리 inputStream: " + data.getData());
                Log.d(TAG, "갤러리 사진decodeStream: " + img);

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 차량 선택 Intent로 값 전달 받으면 설정하기
        if (requestCode == REQUEST_CODE_CAR) {
            if (resultCode == 1001) {
                TextView car_text = (TextView) findViewById(R.id.car_text);
                ImageView car_image = (ImageView) findViewById(R.id.car_image);
//                car_name = data.getStringExtra("year");
                car_name = data.getStringExtra("car");

                car_text.setText(car_name);     // carSettingActivity에서 보낸 키 값
                String n = car_name;      // 선택한 차량 이름 배열 5~끝

                for (Car i : UserInfoActivity.car_list) {
                    if (n.equals(i.getVehicle())) {   // 차량 년도와 이름이 같다면
                        car_image.setImageBitmap(getBitmap(i.getImage()));      // 마이 페이지 차량 이미지 바꾸기

//                        // MainActivity.car (개인 차량 정보 설정)
                        RetrofitConnection retrofit = new RetrofitConnection();
                        retrofit.server.updateUserCarInfo(MainActivity.user.getId(), i).enqueue(new Callback<Car>() {
                            @Override
                            public void onResponse(Call<Car> call, Response<Car> response) {
                                MainActivity.car = response.body();
                            }

                            @Override
                            public void onFailure(Call<Car> call, Throwable t) {
                                Log.d("failure", "정보 수정 ㄴㄴ");
                            }
                        });
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // URL 비트맵 변환
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


    // 다이얼로그 보여주기
    private void photoDialogRadio() {
        final CharSequence[] PhotoModels = {"앨범에서 사진 선택", "기본사진으로 설정", "취소"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(UserInfoActivity.this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("프로필 사진 설정");
        alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) { //갤러리
                    Intent intent2 = new Intent();
                    intent2.setType("image/*");
                    intent2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent2, PICK_IMAGE);
                    Log.d(TAG, "갤러리 선택");
                    dialog.cancel();
                } else if (item == 1) { // 기본화면으로 설정
                    profile_image.setImageResource(R.drawable.jordy);
                    img = null;
                    dialog.cancel();
                } else { // 취소 버튼
                    dialog.cancel();
                }

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void onPause() {
        super.onPause();

        //수정된 정보를 DB에 저장
        RetrofitConnection retrofit = new RetrofitConnection();
        retrofit.server.updateUserInfo(MainActivity.user.getId(), MainActivity.user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("sucess", "정보 수정 완료");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failure", "정보 수정 실패");
            }
        });
    }

    //DB에서 차량, 회원카드, 결제카드 리스트 받아오기
    public class GetInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected synchronized Void doInBackground(Void... voids) {
            RetrofitConnection retrofit = new RetrofitConnection();
            try {
                Response<List<Car>> cResponse = retrofit.server.getCar_list().execute();
                List<Car> result1 = cResponse.body();
                for (Car i : result1) {
                    car_list.add(i);
                }

                Response<List<Card>> mResponse = retrofit.server.getMembership_list().execute();
                List<Card> result2 = mResponse.body();
                for (Card i : result2) {
                    membership_list.add(i);
                }

                Response<List<Card>> pResponse = retrofit.server.getPayment_list().execute();
                List<Card> result3 = pResponse.body();
                for (Card i : result3) {
                    payment_list.add(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("error", "" + e.toString());
            }
            return null;
        }
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


    // 카드 선택 액티비티에서 돌아오면 새로고침해서 정보 반영해주는 부분
    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.recyclerView_info_m);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserInfoAdapter();

        // 카드 화면에 추가
        for (Card i : MainActivity.membership) {
            adapter.addItem(i);
        }
        recyclerView.setAdapter(adapter);


        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_info_p);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        adapter2 = new UserInfoAdapter();

        // 카드 화면에 추가
        for (Card i : MainActivity.payment) {
            adapter2.addItem(i);
        }
        recyclerView2.setAdapter(adapter2);
    }
}

