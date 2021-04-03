package org.techtown.evtalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import org.techtown.evtalk.ui.home.HomeFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_custom_login;
    private Button testbtn;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    private long p_id;
    private String p_name;
    private String p_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //레트로핏으로 서버 '/' 접속
        RetrofitConnection retrofit = new RetrofitConnection();
        retrofit.server.connect().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                    Log.d("success", "서버 접속 완료 " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failure", "" + t.toString());
            }
        });

        btn_custom_login = (Button) findViewById(R.id.btn_custom_login);
        Button testbtn = (Button) findViewById(R.id.testbtn);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        session.getCurrentSession().checkAndImplicitOpen(); // 자동 로그인

        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });


        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });
    }

    public void toMainActivity(){
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", p_id);
        intent.putExtra("name", p_name);
        intent.putExtra("image", p_image);
        startActivity(intent);
        overridePendingTransition(0,0); // 전환효과 제거
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Log.i("KAKAO_API", "사용자 아이디: " + result.getId());

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {

                                // 이메일
                                String email = kakaoAccount.getEmail();

                                if (email != null) {
                                    Log.i("KAKAO_API", "email: " + email);

                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                                } else {
                                    // 이메일 획득 불가
                                }

                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if (profile != null) {
                                    Log.i("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.i("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.i("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                    //레트로핏으로 서버 연결
                                    User user = new User(result.getId(), profile.getNickname(), profile.getThumbnailImageUrl());
                                    p_id = user.getId();
                                    p_name = user.getName();
                                    p_image = user.getProfile_image();
                                    RetrofitConnection retrofit = new RetrofitConnection();
                                    retrofit.server.getUser(user).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if(!response.isSuccessful()) {
                                                Log.d("sucess", "but not response");
                                            }
                                            Log.d("sucess", "");
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("failure", "" + t.toString());
                                        }
                                    });

                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능

                                } else {
                                    // 프로필 획득 불가
                                }

                            }
                            toMainActivity();
                        }
                    });
        }
    }



}