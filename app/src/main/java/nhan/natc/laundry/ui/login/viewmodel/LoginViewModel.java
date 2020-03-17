package nhan.natc.laundry.ui.login.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.remote.model.LoginRequest;
import nhan.natc.laundry.data.remote.repository.UserRepository;
import nhan.natc.laundry.ui.login.activity.LoginViewAction;
import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.GlobalSharePreference;

public class LoginViewModel extends BaseViewModel {

    private UserRepository mUserRepository;
    private Context mContext;
    MutableLiveData<LoginViewAction> mAction = new MutableLiveData<>();

    @Inject
    public LoginViewModel(Context context, UserRepository userRepository) {
        this.mContext = context;
        this.mUserRepository = userRepository;
    }

    public MutableLiveData<LoginViewAction> getAction() {
        return mAction;
    }

    public void doLogin(String email, String password) {
        mUserRepository.doLogin(new LoginRequest(email, CommonUtils.makeMd5(password)))
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resource -> {
                    String token = resource.data.getAccessToken();
                    GlobalSharePreference.storeToken(token, mContext);
                    getMyInfo();
                }, throwable -> handleNetworkException(throwable, false));
    }

    private void getMyInfo() {
        mUserRepository.getMyInfo()
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResource -> {
                    setIsLock(false);
                    GlobalSharePreference.storeUser(userResource.data, mContext);
                    mAction.setValue(LoginViewAction.ACTION_LOGIN_SUCCESS);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public void onLogin() {
        if (isLock())
            return;
        setIsLock(true);
        mAction.setValue(LoginViewAction.ACTION_LOGIN_CLICK);
    }
}
