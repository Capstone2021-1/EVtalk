package org.techtown.evtalk.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.evtalk.LoginActivity;
import org.techtown.evtalk.R;

public class GalleryFragment extends Fragment {
    private Button btn_login_out;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

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