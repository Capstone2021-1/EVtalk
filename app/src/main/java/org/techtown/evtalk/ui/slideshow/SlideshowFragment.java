package org.techtown.evtalk.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.evtalk.LoginActivity;
import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;

import java.util.regex.Pattern;

public class SlideshowFragment extends Fragment {
    private SlideshowViewModel slideshowViewModel;
    private Button btn_login_out;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        btn_login_out = (Button) root.findViewById(R.id.btn_login_out);
        btn_login_out.setOnClickListener(new View.OnClickListener() {
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


        // 상태 메시지 수정하기 버튼
        Button status_edit = (Button) root.findViewById(R.id.btn_status_edit);
        EditText status_txt = (EditText) root.findViewById(R.id.status_txt);
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
                    Toast.makeText(getContext(), "상태 메시지 수정 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //차량 번호 수정하기 버튼

        Button car_edit = (Button) root.findViewById(R.id.btn_car_edit);
        EditText car_number = (EditText) root.findViewById(R.id.car_number);
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
                    Toast.makeText(getContext(), "차량 번호 수정 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
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

}

