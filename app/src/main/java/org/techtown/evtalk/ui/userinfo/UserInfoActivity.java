package org.techtown.evtalk.ui.userinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.evtalk.LoginActivity;
import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.RetrofitConnection;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserInfoActivity extends AppCompatActivity {
    private TextView textView;
    Button btn_login_out;
    private Bitmap img;
    private CircleImageView profile_image;  // 원형 프로필

    final String TAG = "Profile_image";
    String name = MainActivity.user.getName();
    // Request Code
    final static int PICK_IMAGE = 1; //갤러리에서 사진선택

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView)findViewById(R.id.toolbar_title);
        ActionBar ac = getSupportActionBar();
        ac.setDisplayShowCustomEnabled(true);
        ac.setDisplayShowTitleEnabled(false);
        ac.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        textView.setText("마이 페이지"); // 타이틀 수정

        Intent intent = new Intent(this, LoginActivity.class);
        btn_login_out = (Button) findViewById(R.id.btn_login_out);
        btn_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        finish();
                        startActivity(intent);
                    }
                });
            }
        });


        // 프로필 사진 선택
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialogRadio();
            }
        });

        // 이름 설정

        TextView name_txt =(TextView) findViewById(R.id.name_txt);
        if(name!=null){
            name_txt.setText(MainActivity.user.getName());
        }

        // 상태 메시지 수정하기 버튼
        Button status_edit = (Button) findViewById(R.id.btn_status_edit);
        EditText status_txt = (EditText) findViewById(R.id.status_txt);
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
                    Toast.makeText(getApplicationContext(), "상태 메시지 수정 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //차량 번호 수정하기 버튼

        Button car_edit = (Button) findViewById(R.id.btn_car_edit);
        EditText car_number = (EditText) findViewById(R.id.car_number);
        car_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8) /*,new CustomInputFilter()*/});   // 필터 여러개 적용
        car_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (car_number.isFocusable() == false) {
                    car_number.setFocusableInTouchMode(true);
                    car_number.setFocusable(true);
                    car_edit.setText("Done");
                } else {
                    car_number.setFocusableInTouchMode(false);
                    car_number.setFocusable(false);
                    car_edit.setText("Edit");
                    Toast.makeText(getApplicationContext(), "차량 번호 수정 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override // 뒤로가기 버튼 동작 구현
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //갤러리에서 이미지 불러온 후 실행 동작
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 다이얼로그 보여주기
    private void photoDialogRadio() {
        final CharSequence[] PhotoModels = {"앨범에서 사진 선택", "기본사진으로 설정", "취소"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getApplicationContext());
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

}

//  정규식 필터 추가( 작동이 제대로 안되어서 주석 처리해둠)
//    protected static class CustomInputFilter implements InputFilter {
//
//
//        @Override
//        public CharSequence filter(CharSequence source, int start,
//                                   int end, Spanned dest, int dstart, int dend) {
//            Pattern ps = Pattern.compile("^[0-9]{2,3}$");
//
//            if (source.equals("") || ps.matcher(source).matches()) {
//                return source;
//            }
//
//            return "";
//        }
//    }

