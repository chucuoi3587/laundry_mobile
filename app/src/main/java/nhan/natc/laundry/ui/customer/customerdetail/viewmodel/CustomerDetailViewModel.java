package nhan.natc.laundry.ui.customer.customerdetail.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.Customer;
import nhan.natc.laundry.data.remote.model.CustomerRequest;
import nhan.natc.laundry.data.remote.repository.CustomerRepository;
import nhan.natc.laundry.ui.customer.customerdetail.activity.CustomerDetailViewAction;

public class CustomerDetailViewModel extends BaseViewModel {

    private Context mContext;
    private CustomerRepository mCustomerRepository;
    private MutableLiveData<Customer> mCustomer = new MutableLiveData<>();
    private MutableLiveData<CustomerDetailViewAction> mAction = new MutableLiveData<>();

    @Inject
    public CustomerDetailViewModel(Context context, CustomerRepository customerRepository) {
        this.mContext = context;
        this.mCustomerRepository = customerRepository;
    }

    public void getCustomer(long customerId) {
        showLoading();
        mCustomerRepository.getCustomer(customerId)
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mCustomer.setValue(response.data);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public MutableLiveData<Customer> getCustomer() {
        return mCustomer;
    }

    public void onSave() {
        if (isLock())
            return;
        setIsLock(true);
        mAction.setValue(CustomerDetailViewAction.ACTION_SAVE);
    }

    public MutableLiveData<CustomerDetailViewAction> getAction() {
        return mAction;
    }

    public void saveCustomer(String name, String email, String address, String phone) {
        mCustomerRepository.updateCustomer(new CustomerRequest(mCustomer.getValue().getId(), name, email, address, phone))
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mAction.setValue(CustomerDetailViewAction.ACTION_SAVE_SUCCESS), throwable -> handleNetworkException(throwable, false));
    }
}
