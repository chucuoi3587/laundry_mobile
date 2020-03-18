package nhan.natc.laundry.ui.customer.customerlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nhan.natc.laundry.R;
import nhan.natc.laundry.data.local.Customer;

public class CustomerRecyclerAdapter extends RecyclerView.Adapter<CustomerRecyclerAdapter.ViewHolder> {

    private List<Customer> mCustomers = new ArrayList<>();
    public interface CustomerListener {
        void onItemClick(int position);
        void onPhoneClick(int position);
        void onReachBottom();
    }
    private CustomerListener mListener;

    public CustomerRecyclerAdapter(CustomerListener listener) {
        this.mListener = listener;
    }

    public void addItems(List<Customer> customers) {
        if (customers != null && !customers.isEmpty()) {
            int start = mCustomers.size();
            mCustomers.addAll(customers);
            notifyItemRangeChanged(start, customers.size());
        }
    }

    public void updateItems(List<Customer> customers) {
        mCustomers.clear();
        mCustomers.addAll(customers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        CustomerItemLayoutBinding viewBinding = CustomerItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == mCustomers.size() - 1)
            mListener.onReachBottom();
        holder.onBind(getItem(position));
    }

    public Customer getItem(int position) {
        if (position < mCustomers.size())
            return mCustomers.get(position);
        return null;
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        private CustomerItemLayoutBinding mViewBinding;
//
//        public ViewHolder(@NonNull CustomerItemLayoutBinding viewBinding) {
//            super(viewBinding.getRoot());
//            mViewBinding = viewBinding;
//        }
        private TextView mName, mEmail, mPhone, mAddress;
        private View mView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            mName = view.findViewById(R.id.displayNameTv);
            mEmail = view.findViewById(R.id.emailTv);
            mAddress = view.findViewById(R.id.addressTv);
            mPhone = view.findViewById(R.id.phoneTv);
        }

        public void onBind(Customer customer) {
//            mViewBinding.displayNameTv.setText(customer.getName());
//            mViewBinding.emailTv.setText(customer.getEmail());
//            mViewBinding.phoneTv.setText(customer.getPhone());
//            mViewBinding.addressTv.setText(customer.getAddress());

            mView.setOnClickListener(l -> mListener.onItemClick(getAdapterPosition()));
            mPhone.setOnClickListener(l -> mListener.onPhoneClick(getAdapterPosition()));
            mName.setText(customer.getName());
            mEmail.setText(customer.getEmail());
            mAddress.setText(customer.getAddress());
//            mPhone.setText(customer.getPhone());
        }
    }
}
