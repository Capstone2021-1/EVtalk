package org.techtown.evtalk.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import org.techtown.evtalk.LoginActivity;
import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;
import org.techtown.evtalk.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {
    private Button btn_t1;
    private Button btn_t2;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        btn_t1 = (Button)root.findViewById(R.id.btn_t1);
        btn_t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        getActivity().finish();
                        startActivity(intent);
                    }
                });
            }
        });

        btn_t2 = (Button)root.findViewById(R.id.btn_t2);
        btn_t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //회원탈퇴 버튼 클릭 시
                new AlertDialog.Builder(getActivity()) //탈퇴 여부를 묻는 팝업창 실행
                        .setMessage("탈퇴하시겠습니까?") //팝업창의 메세지 설정
                        .setPositiveButton("네", new DialogInterface.OnClickListener() { //"예" 버튼 클릭 시 -> 회원탈퇴 수행
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() { //회원탈퇴 실행
                                    @Override
                                    public void onFailure(ErrorResult errorResult) { //회원탈퇴 실패 시
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getActivity(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) { //로그인 세션이 닫혀있을 시
                                        //다시 로그인해달라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(getActivity(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() { //가입되지 않은 계정에서 회원탈퇴 요구 시
                                        //가입되지 않은 계정이라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(getActivity(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) { //회원탈퇴에 성공하면
                                        //DB에서 회원 삭제
                                        RetrofitConnection retrofit = new RetrofitConnection();
                                        retrofit.server.deleteUser(MainActivity.user.getId()).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                Log.d("success", "DB 회원삭제 성공");
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Log.d("failure", "DB 회원삭제 실패");
                                            }
                                        });
                                        //"회원탈퇴에 성공했습니다."라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(getActivity(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
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

        return root;
    }
}