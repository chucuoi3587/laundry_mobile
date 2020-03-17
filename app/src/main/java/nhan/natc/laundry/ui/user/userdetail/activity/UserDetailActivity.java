package nhan.natc.laundry.ui.user.userdetail.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import nhan.natc.laundry.BR;
import nhan.natc.laundry.BuildConfig;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseActivity;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.databinding.ActivityUserDetailBinding;
import nhan.natc.laundry.ui.user.userdetail.fragment.UserRoleBottomDialog;
import nhan.natc.laundry.ui.user.userdetail.viewmodel.UserDetailViewModel;
import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.DialogUtil;

public class UserDetailActivity extends BaseActivity<ActivityUserDetailBinding, UserDetailViewModel> {

    @Inject
    UserDetailViewModel userDetailViewModel;
    private ActivityUserDetailBinding mViewBinding;
    private String cameraFilePath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initViews() {
        mViewBinding = getViewDataBinding();
        Toolbar toolbar = mViewBinding.mainLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.toolbarTitleTv)).setText("User Information");
        toolbar.findViewById(R.id.toolbarTitleTv).setVisibility(View.VISIBLE);
        initData();
        initListener();
        initObservation();
    }

    @Override
    public UserDetailViewModel getViewModel() {
        return userDetailViewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewmodel;
    }

    private void initData() {
        long id = getIntent().getLongExtra(Constants.USER_ID, -1);
        if (id != -1) {
            userDetailViewModel.fetchUser(id);
            mViewBinding.passwordView.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mViewBinding.firstNameView.getLayoutParams();
            params.topToBottom = R.id.emailView;
            mViewBinding.firstNameView.requestLayout();
        }
    }

    private void initListener() {
        mViewBinding.roleView.setInputEnable(false);
        mViewBinding.roleView.getInputEditText().setOnClickListener(l -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            UserRoleBottomDialog dialogFragment = new UserRoleBottomDialog(userRole -> {
                if (userRole != null) {
                    userDetailViewModel.updateRole(userRole);
                }
            });
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.USER_ROLE, userDetailViewModel.getUser().getValue() != null ? userDetailViewModel.getUser().getValue().getUserRole().getId() : -1);
            dialogFragment.setArguments(bundle);
            dialogFragment.show(ft, "dialog");
        });
        mViewBinding.avatar.setOnClickListener(l ->
            DialogUtil.showConfirmDialog(this, "Select avatar from source", null, "CAMERA", v -> {
                if (checkCameraPermission())
                    captureFromCamera();
                },"GALLERY", v -> {
                if (checkStoragePermission())
                    pickFromGallery();
            })
        );
    }

    private void initObservation() {
        userDetailViewModel.getUser().observe(this, user -> {
            mViewBinding.emailView.getInputEditText().setText(user.getEmail());
            mViewBinding.firstNameView.getInputEditText().setText(user.getFirstName());
            mViewBinding.lastNameView.getInputEditText().setText(user.getLastName());
            mViewBinding.roleView.getInputEditText().setText(user.getUserRole().getRoleDescription());
            if (user.getAvatar() != null)
                Glide.with(this)
                    .load(Constants.URL + "/file/download/" + user.getAvatar())
                    .into(mViewBinding.avatar);
        });
        userDetailViewModel.getAvatarUrl().observe(this, url -> {
            if (!CommonUtils.isNullOrEmpty(url))
                Glide.with(this)
                        .load(Constants.URL + "/file/download/" + url)
                        .into(mViewBinding.avatar);
        });
        userDetailViewModel.getAction().observe(this, action -> {
            switch (action) {
                case ACTION_SAVE:
                    User user = userDetailViewModel.getUser().getValue();
                    String email = mViewBinding.emailView.getInputEditText().getText().toString().trim();
                    String firstName = mViewBinding.firstNameView.getInputEditText().getText().toString().trim();
                    String lastName = mViewBinding.lastNameView.getInputEditText().getText().toString().trim();
                    if (user != null && (!user.getEmail().equals(email) || !user.getFirstName().equals(firstName) || !user.getLastName().equals(lastName)
                    || !CommonUtils.isNullOrEmpty(userDetailViewModel.getAvatarUrl().getValue())))
                        userDetailViewModel.doUpdateUser(email, firstName, lastName);
                    else
                        userDetailViewModel.setIsLock(false);
                    break;
                case ACTION_SAVE_SUCCESS:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default ic_location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = /*"file://" +*/ image.getAbsolutePath();
        return image;
    }

    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_REQUEST_CODE:
                    userDetailViewModel.doUpload(cameraFilePath);
                    break;
                case Constants.GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    userDetailViewModel.doUpload(imgDecodableString);
                    break;
            }
        }
    }

    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!CommonUtils.hasPermission(Manifest.permission.CAMERA, this)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA_PERMISSION);
                return false;
            }
        }
        return true;
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!CommonUtils.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) || !CommonUtils.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CAMERA_PERMISSION);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CAMERA_PERMISSION:
                if (CommonUtils.hasPermission(Manifest.permission.CAMERA, this)) {
                    captureFromCamera();
                }
                break;
            case Constants.REQUEST_STORAGE_PERMISSION:
                if (CommonUtils.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && CommonUtils.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                    pickFromGallery();
                }
                break;
        }
    }
}
