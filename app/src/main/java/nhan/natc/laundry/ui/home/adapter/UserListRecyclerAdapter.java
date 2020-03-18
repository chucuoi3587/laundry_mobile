package nhan.natc.laundry.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nhan.natc.laundry.R;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.util.CommonUtils;

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.ViewHolder> {

    private List<User> mUsers = new ArrayList<>();
    private Context mContext;
    public interface UserListListener {
        void onItemSelected(int position);
        void onReachBottom();
    }
    private UserListListener mListener;

    public UserListRecyclerAdapter(UserListListener listener) {
        this.mListener = listener;
    }

    public void addItems(List<User> users) {
        int start = mUsers.size();
        this.mUsers.addAll(users);
        notifyItemRangeChanged(start, users.size());
    }

    public void updateItems(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public User getItem(int position) {
        if (position < mUsers.size())
            return mUsers.get(position);
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == mUsers.size() - 1)
            mListener.onReachBottom();
        holder.onBind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mEmailTv, mDisplayNameTv, mRoleTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            mDisplayNameTv = itemView.findViewById(R.id.displayName);
            mRoleTv = itemView.findViewById(R.id.roleTv);
        }

        public void onBind(User user) {
            if (user != null) {
                mEmailTv.setText(user.getEmail());
                mRoleTv.setText(user.getUserRole().getRoleDescription());
                mDisplayNameTv.setText(user.getFirstName() + " " + user.getLastName());
                switch (user.getUserRole().getId()) {
                    case 1 :
                        mRoleTv.setBackgroundResource(R.drawable.user_type_admin_bg);
                        break;
                    case 2:
                        mRoleTv.setBackgroundResource(R.drawable.user_type_manager_bg);
                        break;
                    case 3:
                        mRoleTv.setBackgroundResource(R.drawable.user_type_user_bg);
                        break;
                }
                switch (user.getUserStatus().getId()) {
                    case 1: // inactive
                        mEmailTv.setTextColor(CommonUtils.getColor(android.R.color.holo_blue_light, mContext));
                        break;
                    case 2: // active
                        mEmailTv.setTextColor(CommonUtils.getColor(android.R.color.holo_green_light, mContext));
                        break;
                    case 3: // deleted
                        mEmailTv.setTextColor(CommonUtils.getColor(android.R.color.darker_gray, mContext));
                        break;
                }
                mEmailTv.getRootView().setOnClickListener(l -> mListener.onItemSelected(getAdapterPosition()));
            }
        }
    }
}
