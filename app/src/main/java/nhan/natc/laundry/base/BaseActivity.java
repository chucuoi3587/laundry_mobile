package nhan.natc.laundry.base;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.concurrent.TimeUnit;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.service.NetworkStatusReceiver;
import nhan.natc.laundry.ui.login.activity.LoginActivity;
import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.DialogUtil;
import nhan.natc.laundry.util.GlobalSharePreference;

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity implements BaseFragment.Callback, UINetworkExceptionHandler, NetworkStatusReceiver.ConnectivityReceiverListener{

    protected String TAG = getClass().getSimpleName();

    private T mViewDataBinding;
    private V mViewModel;
    private NetworkStatusReceiver mNetworkReceiver = new NetworkStatusReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        registerNetworkCheckReceiver();
        performDataBinding();
        initViews();
        if (mViewModel != null) {
            mViewModel.onViewCreated();
        }
        initObservation();
        handleKeyboardPopup();
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    private void performDataBinding() {
        Log.d(TAG, "performDataBinding");
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        Log.d(TAG, "mViewDataBinding: " + mViewDataBinding);
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        if (mViewModel != null) {
            mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        }
        mViewDataBinding.executePendingBindings();
    }

    private void handleKeyboardPopup() {
        ViewTreeObserver vto = mViewDataBinding.getRoot().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewDataBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (Constants.SCREEN_HEIGHT > 0 && mViewDataBinding.getRoot().getHeight() < Constants.SCREEN_HEIGHT) {
                    View focusView = getCurrentFocus();
                    if (focusView != null)
                        CommonUtils.hideKeyboard(focusView, BaseActivity.this);
//                    else
//                        CommonUtils.forceHideKeyboard(BaseActivity.this);
                }
            }
        });
    }

    protected abstract void initViews();
    public abstract V getViewModel();

    public T getViewDataBinding() {
        return mViewDataBinding;
    }
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    protected abstract int getBindingVariable();

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            View decorView = getWindow().getDecorView();
//            // Hide both the navigation bar and the status bar.
//            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//            // a general rule, you should design your app to hide the status bar whenever you
//            // hide the navigation bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
////                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
    }

    @Override
    public void onConnectException() {

    }

    @Override
    public void onSocketTimeout() {

    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onUnAuthorizedError() {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isDataNetworkAvailable) {
        if (!isDataNetworkAvailable)
            DialogUtil.showInformDialog(this, getString(R.string.network_switch_off_warning), getString(R.string.ok_lbl), null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clearDispose();
        unregisterReceiver(mNetworkReceiver);
    }

    private void registerNetworkCheckReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkReceiver, intentFilter);
    }

    @Override
    public void finish() {
        CommonUtils.hideKeyboard(getCurrentFocus(), this);
        Completable.complete().delay(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(super::finish)
                .subscribe();
    }

    private void initObservation() {
        mViewModel.getNetworkViewAction().observe(this, networkViewAction -> {
            if (networkViewAction == NetworkViewAction.SHOW_ERROR_UNAUTHORIZE && !this.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName())) {
                GlobalSharePreference.clearUser(BaseActivity.this);
                DialogUtil.showInformDialog(this, getString(R.string.expired_token_warning_message), getString(R.string.ok_lbl), null, l -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finishAffinity();
                });
            }
        });
    }
}
