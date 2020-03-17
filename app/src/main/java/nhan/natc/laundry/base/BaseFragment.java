package nhan.natc.laundry.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.ui.login.activity.LoginActivity;
import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.DialogUtil;
import nhan.natc.laundry.util.GlobalSharePreference;

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel,
        R extends BaseActivity> extends Fragment {
    private R mActivity;
    private T mViewDataBinding;
    private V mViewModel;
    private View mRootView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public abstract boolean isPerformDependencyInjection();

    public abstract V getViewModel();

    public abstract int getBindingVariable();

    @LayoutRes
    public abstract int getLayoutId();

    protected abstract void initToolbar();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isPerformDependencyInjection()) {
            performDependencyInjection();
        }
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = getViewModel();
        if (mViewModel != null) {
            mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        }
        mViewDataBinding.executePendingBindings();
        initToolbar();
        initView(savedInstanceState);
        initData(savedInstanceState);
        if (mViewModel != null) {
            mViewModel.onViewCreated();
        }
        initObservation();
        handleKeyboardPopup();
    }

    private void handleKeyboardPopup() {
        ViewTreeObserver vto = mViewDataBinding.getRoot().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewDataBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (Constants.SCREEN_HEIGHT > 0 && mViewDataBinding.getRoot().getHeight() < Constants.SCREEN_HEIGHT) {
                    View focusView = mActivity.getCurrentFocus();
                    if (focusView != null)
                        CommonUtils.hideKeyboard(focusView, mActivity);
//                    else
//                        CommonUtils.forceHideKeyboard(mActivity);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (R) context;
        mActivity.onFragmentAttached();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        mCompositeDisposable.dispose();
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public R getAttachedActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void back() {

    }

    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    private void initObservation() {
        mViewModel.getNetworkViewAction().observe(this, networkViewAction -> {
            if (networkViewAction == NetworkViewAction.SHOW_ERROR_UNAUTHORIZE && !this.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName())) {
                GlobalSharePreference.clearUser(getAttachedActivity());
                DialogUtil.showInformDialog(getActivity(), getString(nhan.natc.laundry.R.string.expired_token_warning_message), getString(nhan.natc.laundry.R.string.ok_lbl), null, l -> {
                    Intent intent = new Intent(getAttachedActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getAttachedActivity().finishAffinity();
                });
            }
        });
    }
}
