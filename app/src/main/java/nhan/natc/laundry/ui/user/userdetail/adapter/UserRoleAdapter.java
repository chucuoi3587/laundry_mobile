package nhan.natc.laundry.ui.user.userdetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nhan.natc.laundry.R;
import nhan.natc.laundry.data.local.UserRole;

public class UserRoleAdapter extends RecyclerView.Adapter<UserRoleAdapter.ViewHolder> {

    private List<UserRole> mUserRoles = new ArrayList<>();
    private int mSelectedItem = -1;
    private UserRole mSelectedRole;

    public UserRoleAdapter(int selectedPosition) {
        this.mSelectedItem = selectedPosition;
    }

    public void addItems(List<UserRole> userRoles) {
        int start = mUserRoles.size();
        mUserRoles.addAll(userRoles);
        notifyItemRangeChanged(start, userRoles.size());
    }

    private void setSelectedRole(UserRole userRole) {
        if (userRole!= null && userRole.getId() != mSelectedItem) {
            mSelectedItem = userRole.getId();
            mSelectedRole = userRole;
            notifyDataSetChanged();
        }
    }

    public UserRole getSelectedRole() {
        return mSelectedRole;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_role_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }

    public UserRole getItem(int position) {
        return position < mUserRoles.size() ? mUserRoles.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mUserRoles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCheckBox;
        private TextView mTitleTv;
        private LinearLayout mMainLayout;
        public ViewHolder(View view) {
            super(view);
            mCheckBox = view.findViewById(R.id.checkBox);
            mTitleTv = view.findViewById(R.id.titleTv);
            mMainLayout = view.findViewById(R.id.mainLayout);
        }

        public void onBind(UserRole userRole) {
            mTitleTv.setText(userRole.getRoleDescription());
            if (mSelectedItem == userRole.getId()) {
                mCheckBox.setImageResource(R.drawable.checkbox_on);
            } else {
                mCheckBox.setImageResource(R.drawable.checkbox_off);
            }
            mMainLayout.setOnClickListener(l -> setSelectedRole(getItem(getAdapterPosition())));
        }
    }
}
