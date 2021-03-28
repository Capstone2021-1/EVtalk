package org.techtown.evtalk.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SlideshowFragment extends Fragment {
    private SlideshowViewModel slideshowViewModel;
    private Button btn_login_out;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        btn_login_out = (Button)root.findViewById(R.id.btn_login_out);
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
        return root;
    }
}

