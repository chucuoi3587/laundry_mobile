package nhan.natc.laundry.ui.home.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.data.remote.repository.UserRepository;

public class UserListViewModel extends BaseViewModel {

    private UserRepository mUserRepository;
    private MutableLiveData<List<User>> mListUser = new MutableLiveData<>();

    @Inject
    public UserListViewModel(UserRepository userRepository) {
        this.mUserRepository = userRepository;
        fetchAllUser();
    }

    private void fetchAllUser() {
        mUserRepository.getAllUser()
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResource -> {
                    mListUser.postValue(listResource.data);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public MutableLiveData<List<User>> getListUser() {
        return mListUser;
    }
}
