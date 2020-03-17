package nhan.natc.laundry.data.remote.service;

import java.util.List;

import io.reactivex.Observable;
import nhan.natc.laundry.data.local.Customer;
import nhan.natc.laundry.data.remote.model.CustomerRequest;
import nhan.natc.laundry.data.remote.model.Resource;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomerService {
    @GET("customer/all")
    Observable<Resource<List<Customer>>> getAll();

    @GET("customer/{customerId}")
    Observable<Resource<Customer>> getCustomer(@Path("customerId") long customerId);

    @POST("customer/update")
    Observable<Resource<Customer>> updateCustomer(@Body CustomerRequest request);
}
