package nhan.natc.laundry.ui.customer.customerdetail.activity;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import javax.inject.Inject;

import nhan.natc.laundry.BR;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.base.BaseActivity;
import nhan.natc.laundry.base.customview.CustomSearchEditText;
import nhan.natc.laundry.databinding.ActivityCustomerDetailBinding;
import nhan.natc.laundry.ui.customer.customerdetail.viewmodel.CustomerDetailViewModel;
import nhan.natc.laundry.util.CommonUtils;

public class CustomerDetailActivity extends BaseActivity<ActivityCustomerDetailBinding, CustomerDetailViewModel> implements CustomSearchEditText.CustomerSearchViewListener {

    @Inject
    CustomerDetailViewModel viewModel;
    private ActivityCustomerDetailBinding mViewBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_detail;
    }

    @Override
    protected void initViews() {
        mViewBinding = getViewDataBinding();
        Toolbar toolbar = mViewBinding.mainLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.toolbarTitleTv)).setText("Customer Detail");
        toolbar.findViewById(R.id.toolbarTitleTv).setVisibility(View.VISIBLE);
        initData();
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
    public CustomerDetailViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewmodel;
    }

    private void initData() {
        long customerId = getIntent().getLongExtra(Constants.CUSTOMER_ID, -1);
        if (customerId != -1)
            viewModel.getCustomer(customerId);
    }

    private void initListener() {
        mViewBinding.nameView.setListener(this);
        mViewBinding.emailView.setListener(this);
        mViewBinding.addressView.setListener(this);
        mViewBinding.phoneView.setListener(this);
    }

    private void initObservation() {
        viewModel.getCustomer().observe(this, customer -> {
            viewModel.hideLoading();
            mViewBinding.nameView.getInputEditText().setText(customer.getName());
            mViewBinding.emailView.getInputEditText().setText(customer.getEmail());
            mViewBinding.addressView.getInputEditText().setText(customer.getAddress());
            mViewBinding.phoneView.getInputEditText().setText(customer.getPhone());
        });
        viewModel.getAction().observe(this, action -> {
            switch (action) {
                case ACTION_SAVE:
                    String name = mViewBinding.nameView.getInputEditText().getText().toString().trim();
                    String email = mViewBinding.emailView.getInputEditText().getText().toString().trim();
                    String address = mViewBinding.addressView.getInputEditText().getText().toString().trim();
                    String phone = mViewBinding.phoneView.getInputEditText().getText().toString().trim();
                    if (!name.equals(viewModel.getCustomer().getValue().getName()) ||
                        !email.equals(viewModel.getCustomer().getValue().getEmail()) ||
                        !address.equals(viewModel.getCustomer().getValue().getAddress()) ||
                        !phone.equals(viewModel.getCustomer().getValue().getPhone()))
                        viewModel.saveCustomer(name, email, address, phone);
                    else
                        viewModel.setIsLock(false);
                    break;
                case ACTION_SAVE_SUCCESS:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        });
    }

    @Override
    public void onTextChanged(Integer tag, View view, CharSequence s) {

    }

    @Override
    public boolean OnEditorAction(Integer tag, int actionId, KeyEvent event) {
        if (tag != null && actionId == EditorInfo.IME_ACTION_DONE) {
            switch (tag) {
                case R.id.nameView:
                    mViewBinding.nameView.clearFocus();
                    mViewBinding.emailView.focus();
                    break;
                case R.id.emailView:
                    mViewBinding.emailView.clearFocus();
                    mViewBinding.addressView.focus();
                    break;
                case R.id.addressView:
                    mViewBinding.addressView.clearFocus();
                    mViewBinding.phoneView.focus();
                    break;
                case R.id.phoneView:
                    CommonUtils.hideKeyboard(mViewBinding.phoneView.getInputEditText(), CustomerDetailActivity.this);
                    mViewBinding.phoneView.clearFocus();
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onEndIconClick(Integer tag, View view) {

    }

    @Override
    public void onClearText(Integer tag, View view) {
        switch (tag) {
            case R.id.nameView:
                mViewBinding.nameView.getInputEditText().setText("");
                break;
            case R.id.emailView:
                mViewBinding.emailView.getInputEditText().setText("");
                break;
            case R.id.addressView:
                mViewBinding.addressView.getInputEditText().setText("");
                break;
            case R.id.phoneView:
                mViewBinding.phoneView.getInputEditText().setText("");
                break;
        }
    }
}
