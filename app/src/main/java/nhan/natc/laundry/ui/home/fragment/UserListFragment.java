package nhan.natc.laundry.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import javax.inject.Inject;
import nhan.natc.laundry.BR;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseFragment;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.databinding.FragmentUserListBinding;
import nhan.natc.laundry.ui.home.activity.HomeActivity;
import nhan.natc.laundry.ui.home.adapter.UserListRecyclerAdapter;
import nhan.natc.laundry.ui.home.viewmodel.UserListViewModel;
import nhan.natc.laundry.ui.user.userdetail.activity.UserDetailActivity;

public class UserListFragment extends BaseFragment<FragmentUserListBinding, UserListViewModel, HomeActivity> implements UserListRecyclerAdapter.UserListListener {

    @Inject
    UserListViewModel userListViewModel;
    private FragmentUserListBinding mViewBinding;
    private UserListRecyclerAdapter mAdapter;

    @Override
    public boolean isPerformDependencyInjection() {
        return true;
    }

    @Override
    public UserListViewModel getViewModel() {
        return userListViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewmodel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_list;
    }

    @Override
    protected void initToolbar() {
        setHasOptionsMenu(true);
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
            case R.id.action_add_user:
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                startActivityForResult(intent, 1000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_user_header, menu);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewBinding = getViewDataBinding();
        mAdapter = new UserListRecyclerAdapter(this);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getAttachedActivity(), LinearLayoutManager.VERTICAL, false));
        mViewBinding.recyclerView.setAdapter(mAdapter);
        initObservation();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        getAttachedActivity().setHomeTitle(getString(R.string.app_name), true);
    }

    private void initObservation() {
        userListViewModel.getListUser().observe(getViewLifecycleOwner(), users -> mAdapter.addItems(users));
    }

    @Override
    public void onItemSelected(int position) {
        User user = mAdapter.getItem(position);
        if (user != null) {
            Intent intent = new Intent(getAttachedActivity(), UserDetailActivity.class);
            intent.putExtra(Constants.USER_ID, user.getId());
            startActivityForResult(intent, 1000);
        }
    }
}
