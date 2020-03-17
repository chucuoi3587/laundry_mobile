package nhan.natc.laundry.data.remote.service;

import java.util.List;

import io.reactivex.Observable;
import nhan.natc.laundry.data.local.Login;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.data.local.UserRole;
import nhan.natc.laundry.data.remote.model.LoginRequest;
import nhan.natc.laundry.data.remote.model.Resource;
import nhan.natc.laundry.data.remote.model.UserRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/login")
    Observable<Resource<Login>> doLogin(@Body LoginRequest request);

    @GET("user/me")
    Observable<Resource<User>> getMyInfo();

    @GET("user/all")
    Observable<Resource<List<User>>> getAllUser();

    @GET("user/{id}")
    Observable<Resource<User>> getUserById(@Path("id") long id);

    @POST("user/update")
    Observable<Resource<User>> updateUser(@Body UserRequest user);

    @GET("user/roles")
    Observable<Resource<List<UserRole>>> getAllRoles();
}
