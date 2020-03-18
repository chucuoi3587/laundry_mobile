package nhan.natc.laundry.ui.customer.customerlist.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.Customer;
import nhan.natc.laundry.data.remote.model.CustomerAllRequest;
import nhan.natc.laundry.data.remote.repository.CustomerRepository;

public class CustomerListViewModel extends BaseViewModel {
    private Context mContext;
    private CustomerRepository mCustomerRepository;
    private MutableLiveData<List<Customer>> mCustomers = new MutableLiveData<>();
    private int mPage = 0;
    private int mPerPage = 10;
    private boolean mIsLoadMore = false;
    private boolean mHasMoreRecord = false;

    @Inject
    public CustomerListViewModel(Context context, CustomerRepository customerRepository) {
        this.mContext = context;
        this.mCustomerRepository = customerRepository;
        loadCustomers();
    }

    public void loadCustomers() {
        if (isLock())
            return;
        setIsLock(true);
        mPage = 0;
        mIsLoadMore = false;
        mHasMoreRecord = false;
        fetchCustomers();
    }

    public void loadMoreCustomers() {
        if (isLock())
            return;
        setIsLock(true);
        mPage++;
        mIsLoadMore = true;
        fetchCustomers();
    }

    public void fetchCustomers() {
        mCustomerRepository.getAll(new CustomerAllRequest(mPage, mPerPage))
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mCustomers.setValue(response.data);
                    mHasMoreRecord = response.hasMoreRecord;
                    setIsLock(false);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public MutableLiveData<List<Customer>> getCustomer() {
        return mCustomers;
    }

    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public boolean hasLoadMore() {
        return mHasMoreRecord;
    }
}
