package nhan.natc.laundry.ui.home.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import nhan.natc.laundry.BR;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseActivity;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.databinding.ActivityHomeBinding;
import nhan.natc.laundry.ui.customer.customerlist.activity.CustomerListActivity;
import nhan.natc.laundry.ui.home.fragment.MainFragment;
import nhan.natc.laundry.ui.home.fragment.UserListFragment;
import nhan.natc.laundry.ui.home.viewmodel.HomeViewModel;
import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.GlobalSharePreference;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements NavigationView.OnNavigationItemSelectedListener{
    @Inject
    HomeViewModel homeViewModel;
    private ActivityHomeBinding mViewBinding;
    private Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        mViewBinding = getViewDataBinding();
//        mViewBinding.slideMenu.initMenu(user.getUserRole().getId());
        mViewBinding.DrawerLayout.setStatusBarBackgroundColor(CommonUtils.getColor(R.color.colorDarkBlue, this));
        mToolbar = mViewBinding.DrawerLayout.findViewById(R.id.toolbar);
        setUpUserInfo();
        mViewBinding.navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public HomeViewModel getViewModel() {
        return homeViewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewmodel;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return mViewBinding.DrawerLayout;
    }

    public void setHomeTitle(String title) {
        ((TextView) mToolbar.findViewById(R.id.toolbarTitleTv)).setText(title);
        mToolbar.findViewById(R.id.toolbarTitleTv).setVisibility(View.VISIBLE);
    }

    private void setUpUserInfo() {
        User user = GlobalSharePreference.getUser(this);
        TextView roleTv = mViewBinding.navigationView.getHeaderView(0).findViewById(R.id.role);
        roleTv.setText(user.getUserRole().getRoleDescription());
        switch (user.getUserRole().getId()) {
            case 1 :
                roleTv.setBackgroundResource(R.drawable.user_type_admin_bg);
                break;
            case 2:
                roleTv.setBackgroundResource(R.drawable.user_type_manager_bg);
                break;
            case 3:
                roleTv.setBackgroundResource(R.drawable.user_type_user_bg);
                break;
        }
        if (!CommonUtils.isNullOrEmpty(user.getAvatar())) {
            CircleImageView imgvAvatar = mViewBinding.navigationView.getHeaderView(0).findViewById(R.id.avatar);
            Glide.with(this).load(Constants.URL + "/file/download/" + user.getAvatar()).into(imgvAvatar);
        }
        ((TextView) mViewBinding.navigationView.getHeaderView(0).findViewById(R.id.displayName)).setText(user.getFirstName() + " " + user.getLastName());
        loadFragment(user.getUserRole().getId());
    }

    private void loadFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new UserListFragment();
                break;
            case 2:
                fragment = new MainFragment();
                break;
            case 3:

                break;
        }
        Fragment f = fragmentManager.findFragmentById(R.id.flContent);
        if (f != null) {
            fragmentManager.beginTransaction().remove(f).commit();
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().add(R.id.flContent, fragment, fragment.getClass().getSimpleName()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_customer_profiles:
                intent = new Intent(this, CustomerListActivity.class);
                startActivity(intent);
                break;
        }
        mViewBinding.DrawerLayout.closeDrawer(Gravity.LEFT);
        return false;
    }
}
