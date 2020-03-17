package nhan.natc.laundry.ui.user.userdetail.viewmodel;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.FileDownload;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.data.local.UserRole;
import nhan.natc.laundry.data.remote.model.UserRequest;
import nhan.natc.laundry.data.remote.repository.FileRepository;
import nhan.natc.laundry.data.remote.repository.UserRepository;
import nhan.natc.laundry.ui.user.userdetail.activity.UserDetailViewAction;
import nhan.natc.laundry.util.CommonUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserDetailViewModel extends BaseViewModel {

    private UserRepository mUserRepository;
    private FileRepository mFileRepository;
    private Context mContext;
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    private MutableLiveData<String> mAvatarUrl = new MutableLiveData<>();
    private FileDownload mAvatar = null;
    private MutableLiveData<UserDetailViewAction> mAction = new MutableLiveData<>();

    @Inject
    public UserDetailViewModel(Context context, UserRepository userRepository, FileRepository fileRepository) {
        this.mContext = context;
        this.mUserRepository = userRepository;
        this.mFileRepository = fileRepository;
    }

    public void fetchUser(long id) {
        mUserRepository.getUserById(id)
        .doOnSubscribe(this::addToDispose)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(userResource -> mUser.setValue(userResource.data), throwable -> handleNetworkException(throwable, false));
    }

    public void updateRole(UserRole userRole) {
        User user = mUser.getValue();
        user.setUserRole(userRole);
        mUser.setValue(user);
    }

    public MutableLiveData<User> getUser() {
        return mUser;
    }

    public void doUpload(String url) {
        if (!CommonUtils.isNullOrEmpty(url)) {
            File file = new File(url);
            String mimeType = CommonUtils.getMimeType(Uri.fromFile(file), mContext);
            Log.d("NhanNATC", "=== mimeType : " + mimeType);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse(mimeType), file));
            mFileRepository.uploadFile(filePart)
                    .doOnSubscribe(this::addToDispose)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resource -> {
                        mAvatar = resource.data;
                        if (mAvatar != null)
                            mAvatarUrl.setValue(resource.data.getFileDownloadUri());
                    }, throwable -> handleNetworkException(throwable, false));
        }
    }

    public MutableLiveData<String> getAvatarUrl() {
        return mAvatarUrl;
    }

    public void doUpdateUser(String email, String firstName, String lastName) {
        mUserRepository.updateUser(new UserRequest(mUser.getValue().getId(), email, null, firstName, lastName, null, mAvatar != null? mAvatar.getFileDownloadUri() : null))
        .doOnSubscribe(this::addToDispose)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(userResource -> {
            mAction.setValue(UserDetailViewAction.ACTION_SAVE_SUCCESS);
        }, throwable -> handleNetworkException(throwable, false));
    }

    public void onSave() {
        if (isLock())
            return;
        setIsLock(true);
        mAction.setValue(UserDetailViewAction.ACTION_SAVE);
    }

    public MutableLiveData<UserDetailViewAction> getAction() {
        return mAction;
    }
}
