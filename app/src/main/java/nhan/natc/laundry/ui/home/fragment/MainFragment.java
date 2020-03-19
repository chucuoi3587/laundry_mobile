package nhan.natc.laundry.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import nhan.natc.laundry.BR;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseFragment;
import nhan.natc.laundry.databinding.FragmentHomeControlBinding;
import nhan.natc.laundry.ui.home.activity.HomeActivity;
import nhan.natc.laundry.ui.home.viewmodel.HomeControlViewModel;
import nhan.natc.laundry.ui.user.userdetail.activity.UserDetailActivity;

public class MainFragment extends BaseFragment<FragmentHomeControlBinding, HomeControlViewModel, HomeActivity> {

    @Inject
    HomeControlViewModel viewModel;

    @Override
    public boolean isPerformDependencyInjection() {
        return true;
    }

    @Override
    public HomeControlViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewmodel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_control;
    }

    @Override
    protected void initToolbar() {
        getAttachedActivity().setSupportActionBar(getAttachedActivity().getToolbar());
        getAttachedActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getAttachedActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAttachedActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
        getAttachedActivity().setHomeTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getAttachedActivity().getDrawerLayout().openDrawer(Gravity.LEFT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
