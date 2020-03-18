package nhan.natc.laundry.ui.home.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.data.remote.model.UserAllRequest;
import nhan.natc.laundry.data.remote.repository.UserRepository;

public class UserListViewModel extends BaseViewModel {

    private UserRepository mUserRepository;
    private MutableLiveData<List<User>> mListUser = new MutableLiveData<>();
    private int mPage = 0;
    private int mPerPage = 10;
    private boolean mIsLoadMore = false;
    private boolean mHasMoreRecord = false;

    @Inject
    public UserListViewModel(UserRepository userRepository) {
        this.mUserRepository = userRepository;
        fetchAllUser();
    }

    private void loadAllUser() {
        if (isLock())
            return;
        setIsLock(true);
        mPage = 0;
        mIsLoadMore = false;
        mHasMoreRecord = false;
        fetchAllUser();
    }

    public void loadMoreUser() {
        if (isLock())
            return;
        setIsLock(true);
        mPage++;
        mIsLoadMore = true;
        fetchAllUser();
    }

    private void fetchAllUser() {
        mUserRepository.getAllUser(new UserAllRequest(mPage, mPerPage))
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResource -> {
                    mListUser.postValue(listResource.data);
                    mHasMoreRecord = listResource.hasMoreRecord;
                    hideLoading();
                    setIsLock(false);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public MutableLiveData<List<User>> getListUser() {
        return mListUser;
    }

    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public boolean hasLoadMore() {
        return mHasMoreRecord;
    }
}
