package nhan.natc.laundry.ui.login.activity;

import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import javax.inject.Inject;
import nhan.natc.laundry.BR;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseActivity;
import nhan.natc.laundry.databinding.ActivityLoginBinding;
import nhan.natc.laundry.ui.home.activity.HomeActivity;
import nhan.natc.laundry.ui.login.viewmodel.LoginViewModel;
import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.DialogUtil;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    @Inject
    LoginViewModel loginViewModel;
    private ActivityLoginBinding mViewBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        mViewBinding = getViewDataBinding();
        CommonUtils.setBackground(mViewBinding.loginBtn, R.drawable.ripple_button_blue_background, R.drawable.button_blue_border_selector);
        initListener();
        initObservation();
    }

    @Override
    public LoginViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewmodel;
    }

    private void initListener() {
        mViewBinding.passwordView.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                if (mViewBinding.passwordView.validatePassword()) {
                    mViewBinding.loginBtn.requestFocus();
                    CommonUtils.hideKeyboard(LoginActivity.this.getCurrentFocus(), LoginActivity.this.getApplicationContext());
                    mViewBinding.usernameView.onError(false, "",false);
                    if (mViewBinding.usernameView.validateEmail() && mViewBinding.passwordView.validatePassword()) {
                        loginViewModel.setIsLock(true);
                        loginViewModel.doLogin(mViewBinding.usernameView.getTextValue(), mViewBinding.passwordView.getTextValue());
                    }
                }
                return true;
            }
            return false;
        });
        mViewBinding.usernameView.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_NEXT){
                if (mViewBinding.usernameView.validateEmail()) {
                    mViewBinding.passwordView.focus();
                    mViewBinding.usernameView.onError(false, "",false);
                }
                return true;
            }
            return false;
        });
    }

    private void initObservation() {
        loginViewModel.getAction().observe(this, action -> {
            switch (action) {
                case ACTION_LOGIN_CLICK:
                    if (mViewBinding.usernameView.validateEmail() && mViewBinding.passwordView.validatePassword()) {
                        loginViewModel.doLogin(mViewBinding.usernameView.getTextValue(), mViewBinding.passwordView.getTextValue());
                    } else {
                        loginViewModel.setIsLock(false);
                    }
                    break;
                case ACTION_LOGIN_SUCCESS:
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        });
        loginViewModel.getNetworkViewAction().observe(this, networkViewAction -> {
            switch (networkViewAction) {
                case SHOW_ERROR_FORBIDDEN_ACCESS:
                case SHOW_ERROR_UNAUTHORIZE:
                    DialogUtil.showInformDialog(LoginActivity.this, !CommonUtils.isNullOrEmpty(loginViewModel.getFailedMessage()) ? loginViewModel.getFailedMessage() : getString(R.string.login_failed), getString(R.string.ok_lbl), null, l -> loginViewModel.setIsLock(false));
                    break;
                case SHOW_ERROR_SERVER_ERROR:
                    DialogUtil.showInformDialog(LoginActivity.this, !CommonUtils.isNullOrEmpty(loginViewModel.getFailedMessage()) ? loginViewModel.getFailedMessage() : getString(R.string.server_error), getString(R.string.ok_lbl), null, l -> loginViewModel.setIsLock(false));
                    break;
                case SHOW_ERROR_NETWORK_TIMEOUT:
                case SHOW_ERROR_CONNECT_EXCEPTION:
                    DialogUtil.showInformDialog(LoginActivity.this, !CommonUtils.isNullOrEmpty(loginViewModel.getFailedMessage()) ? loginViewModel.getFailedMessage() : getString(R.string.error_internet_connection_warning), getString(R.string.ok_lbl), null,  l -> loginViewModel.setIsLock(false));
                    break;
            }
        });
    }

}
