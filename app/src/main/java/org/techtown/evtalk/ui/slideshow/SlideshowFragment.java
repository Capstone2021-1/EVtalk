package org.techtown.evtalk.ui.slideshow;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.evtalk.LoginActivity;
import org.techtown.evtalk.MainActivity;
import org.techtown.evtalk.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class SlideshowFragment extends Fragment {
    private SlideshowViewModel slideshowViewModel;
    Button btn_login_out;
    ImageView profile_pic;
    private Bitmap img;
    private CircleImageView mPhotoCircleImageView;  // 원형 프로필
    private String mCurrentPhotoPath;           // 카메라로 찍은 사진 저장할 루트경로
    private String mTmpDownloadImageUri; //Shared에서 받아올떄 String형이라 임시로 받아오는데 사용

    // Request Code
    final static int PICK_IMAGE = 1; //갤러리에서 사진선택
    final static int CAPTURE_IMAGE = 2;  //카메라로찍은 사진선택

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


        // 프로필 사진 선택
        profile_pic = (ImageView) root.findViewById(R.id.profile_image);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Profile", "권한 설정 완료");
                        photoDialogRadio();
                    } else {
                        Log.d("Profile", "권한 설정 요청");
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Profile", "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Profile", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(in);
                in.close();
                // 이미지 표시
                mPhotoCircleImageView.setImageBitmap(img);
                Log.d("Profile", "갤러리 inputStream: " + data.getData());
                Log.d("Profile", "갤러리 사진decodeStream: " + img);

                mTmpDownloadImageUri = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    File file = new File(mCurrentPhotoPath);
                    InputStream in = getContext().getContentResolver().openInputStream(Uri.fromFile(file));
                    img = BitmapFactory.decodeStream(in);
                    mPhotoCircleImageView.setImageBitmap(img);
                    in.close();

                    mTmpDownloadImageUri = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void photoDialogRadio() {
        final CharSequence[] PhotoModels = {"갤러리에서 가져오기", "카메라로 촬영 후 가져오기", "기본사진으로 하기"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("프로필사진 설정");
        alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getContext(), PhotoModels[item] + "가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                if (item == 0) { //갤러리
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_IMAGE);
                } else if (item == 1) { //카메라찍은 사진가져오기
                    takePictureFromCameraIntent();
                } else { //기본화면으로하기
                    mPhotoCircleImageView.setImageResource(R.drawable.jordy);
                    img = null;
                    mTmpDownloadImageUri = null;
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }


    //카메라로 촬영한 이미지를파일로 저장해주는 함수
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //카메라 인텐트실행 함수
    private void takePictureFromCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.mtjin.studdytogether.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
            }
        }
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

