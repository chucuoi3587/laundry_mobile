package nhan.natc.laundry.data.remote.repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import nhan.natc.laundry.data.local.Customer;
import nhan.natc.laundry.data.remote.model.CustomerAllRequest;
import nhan.natc.laundry.data.remote.model.CustomerRequest;
import nhan.natc.laundry.data.remote.model.Resource;
import nhan.natc.laundry.data.remote.service.CustomerService;

public class CustomerRepository {

    private CustomerService mCustomerService;
    @Inject
    public CustomerRepository(CustomerService customerService) {
        this.mCustomerService = customerService;
    }

    public Observable<Resource<List<Customer>>> getAll(CustomerAllRequest request) {
        return mCustomerService.getAll(request);
    }

    public Observable<Resource<Customer>> getCustomer(long customerId) {
        return mCustomerService.getCustomer(customerId);
    }

    public Observable<Resource<Customer>> updateCustomer(CustomerRequest request) {
        return mCustomerService.updateCustomer(request);
    }
}
