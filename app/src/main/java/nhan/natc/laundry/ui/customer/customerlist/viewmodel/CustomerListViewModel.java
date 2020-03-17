package nhan.natc.laundry.ui.customer.customerlist.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.local.Customer;
import nhan.natc.laundry.data.remote.repository.CustomerRepository;

public class CustomerListViewModel extends BaseViewModel {
    private Context mContext;
    private CustomerRepository mCustomerRepository;
    private MutableLiveData<List<Customer>> mCustomers = new MutableLiveData<>();

    @Inject
    public CustomerListViewModel(Context context, CustomerRepository customerRepository) {
        this.mContext = context;
        this.mCustomerRepository = customerRepository;
        fetchCustomer();
    }

    public void fetchCustomer() {
        mCustomerRepository.getAll()
                .doOnSubscribe(this::addToDispose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mCustomers.setValue(response.data);
                }, throwable -> handleNetworkException(throwable, false));
    }

    public MutableLiveData<List<Customer>> getCustomer() {
        return mCustomers;
    }
}
