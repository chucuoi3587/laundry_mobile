package nhan.natc.laundry.data.remote.repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import nhan.natc.laundry.data.local.Login;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.data.local.UserRole;
import nhan.natc.laundry.data.remote.model.LoginRequest;
import nhan.natc.laundry.data.remote.model.Resource;
import nhan.natc.laundry.data.remote.model.UserRequest;
import nhan.natc.laundry.data.remote.service.UserService;

public class UserRepository {

    private UserService mUserService;

    @Inject
    public UserRepository(UserService userService) {
        mUserService = userService;
    }

    public Observable<Resource<Login>> doLogin(LoginRequest request) {
        return mUserService.doLogin(request);
    }

    public Observable<Resource<User>> getMyInfo() {
        return mUserService.getMyInfo();
    }

    public Observable<Resource<List<User>>> getAllUser() {
        return mUserService.getAllUser();
    }

    public Observable<Resource<User>> getUserById(long id) {
        return mUserService.getUserById(id);
    }

    public Observable<Resource<User>> updateUser(UserRequest user) {
        return mUserService.updateUser(user);
    }

    public Observable<Resource<List<UserRole>>> getAllRoles() {
        return mUserService.getAllRoles();
    }
}
