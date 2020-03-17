package nhan.natc.laundry.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nhan.natc.laundry.ui.customer.customerdetail.activity.CustomerDetailActivity;
import nhan.natc.laundry.ui.customer.customerlist.activity.CustomerListActivity;
import nhan.natc.laundry.ui.home.activity.HomeActivity;
import nhan.natc.laundry.ui.home.fragment.UserListFragment;
import nhan.natc.laundry.ui.login.activity.LoginActivity;
import nhan.natc.laundry.ui.register.activity.RegisterUserActivity;
import nhan.natc.laundry.ui.user.userdetail.activity.UserDetailActivity;
import nhan.natc.laundry.ui.user.userdetail.fragment.UserRoleBottomDialog;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector
    abstract RegisterUserActivity contributeRegisterUserActivity();

    @ContributesAndroidInjector
    abstract HomeActivity contributeHomeActivity();

    @ContributesAndroidInjector
    abstract UserListFragment contributeUserListFragment();

    @ContributesAndroidInjector
    abstract UserDetailActivity contributeUserDetailActivity();

    @ContributesAndroidInjector
    abstract CustomerListActivity contributeCustomerListActivity();

    @ContributesAndroidInjector
    abstract CustomerDetailActivity contributeCustomerDetailActivity();

    @ContributesAndroidInjector
    abstract UserRoleBottomDialog contributeUserRoleBottomDialog();
}
