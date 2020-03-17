package nhan.natc.laundry.ui.user.userdetail.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.UserRole;
import nhan.natc.laundry.data.remote.repository.UserRepository;
import nhan.natc.laundry.ui.user.userdetail.activity.UserDetailViewAction;

public class UserRoleViewModel extends BaseViewModel {

    private Context mContext;
    private UserRepository mUserRepository;
    private MutableLiveData<List<UserRole>> mUserRoles = new MutableLiveData<>();
    private MutableLiveData<UserDetailViewAction> mAction = new MutableLiveData<>();

    @Inject
    public UserRoleViewModel(Context context, UserRepository userRepository) {
        this.mContext = context;
        this.mUserRepository = userRepository;
        fetchUserRole();
    }

    private void fetchUserRole() {
        mUserRepository.getAllRoles()
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mUserRoles.setValue(response.data);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public MutableLiveData<List<UserRole>> getRoles() {
        return mUserRoles;
    }

    public void onDone() {
        if (isLock())
            return;
        setIsLock(true);
        mAction.setValue(UserDetailViewAction.ACTION_DONE);
    }

    public MutableLiveData<UserDetailViewAction> getAction() {
        return mAction;
    }
}
