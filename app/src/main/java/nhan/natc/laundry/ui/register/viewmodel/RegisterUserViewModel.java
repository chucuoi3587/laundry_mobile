package nhan.natc.laundry.ui.register.viewmodel;

import android.content.Context;

import javax.inject.Inject;

import nhan.natc.laundry.base.BaseViewModel;
import nhan.natc.laundry.data.remote.repository.UserRepository;

public class RegisterUserViewModel extends BaseViewModel {

    private UserRepository mUserRepository;
    private Context mContext;

    @Inject
    public RegisterUserViewModel(Context context, UserRepository userRepository) {
        this.mContext = context;
        this.mUserRepository = userRepository;
    }
}
