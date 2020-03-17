package nhan.natc.laundry.ui.register.activity;

import javax.inject.Inject;

import nhan.natc.laundry.BR;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseActivity;
import nhan.natc.laundry.databinding.ActivityRegisterUserBinding;
import nhan.natc.laundry.ui.register.viewmodel.RegisterUserViewModel;

public class RegisterUserActivity extends BaseActivity<ActivityRegisterUserBinding, RegisterUserViewModel> {

    @Inject
    RegisterUserViewModel registerUserViewModel;
    private ActivityRegisterUserBinding mViewBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_user;
    }

    @Override
    protected void initViews() {
        mViewBinding = getViewDataBinding();
        initListener();
        initObservation();
    }

    @Override
    public RegisterUserViewModel getViewModel() {
        return registerUserViewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewmodel;
    }

    private void initListener() {

    }

    private void initObservation() {

    }
}
