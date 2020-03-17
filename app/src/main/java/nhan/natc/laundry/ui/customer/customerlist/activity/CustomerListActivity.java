package nhan.natc.laundry.ui.customer.customerlist.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import nhan.natc.laundry.BR;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseActivity;
import nhan.natc.laundry.data.local.Customer;
import nhan.natc.laundry.databinding.ActivityCustomerListBinding;
import nhan.natc.laundry.ui.customer.customerdetail.activity.CustomerDetailActivity;
import nhan.natc.laundry.ui.customer.customerlist.adapter.CustomerRecyclerAdapter;
import nhan.natc.laundry.ui.customer.customerlist.viewmodel.CustomerListViewModel;
import nhan.natc.laundry.util.CommonUtils;

public class CustomerListActivity extends BaseActivity<ActivityCustomerListBinding, CustomerListViewModel> implements CustomerRecyclerAdapter.CustomerListener {

    @Inject
    CustomerListViewModel customerListViewModel;
    private ActivityCustomerListBinding mViewBinding;
    private CustomerRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_list;
    }

    @Override
    protected void initViews() {
        mViewBinding = getViewDataBinding();
        Toolbar toolbar = mViewBinding.mainLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.toolbarTitleTv)).setText("Customer Profiles");
        toolbar.findViewById(R.id.toolbarTitleTv).setVisibility(View.VISIBLE);
        mAdapter = new CustomerRecyclerAdapter(this);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewBinding.recyclerView.setAdapter(mAdapter);
        initListener();
        initObservation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public CustomerListViewModel getViewModel() {
        return customerListViewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewmodel;
    }

    private void initListener() {

    }

    private void initObservation() {
        customerListViewModel.getCustomer().observe(this, customers -> {
            mAdapter.addItems(customers);
        });
    }

    @Override
    public void onItemClick(int position) {
        if (customerListViewModel.isLock())
            return;
        customerListViewModel.setIsLock(true);
        Customer customer = mAdapter.getItem(position);
        Intent intent = new Intent(this, CustomerDetailActivity.class);
        intent.putExtra(Constants.CUSTOMER_ID, customer.getId());
        startActivityForResult(intent, Constants.REQUEST_CUSTOMER_DETAIL);
    }

    @Override
    public void onPhoneClick(int position) {
        Customer customer = mAdapter.getItem(position);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customer.getPhone()));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CUSTOMER_DETAIL:
                customerListViewModel.setIsLock(false);
                if (resultCode == RESULT_OK) {
                    customerListViewModel.fetchCustomer();
                }
                break;
        }
    }
}
