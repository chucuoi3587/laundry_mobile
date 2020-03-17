package nhan.natc.laundry.ui.user.userdetail.fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.data.local.UserRole;
import nhan.natc.laundry.databinding.PopupLayoutUserRoleBinding;
import nhan.natc.laundry.ui.user.userdetail.adapter.UserRoleAdapter;
import nhan.natc.laundry.ui.user.userdetail.viewmodel.UserRoleViewModel;

public class UserRoleBottomDialog extends BottomSheetDialogFragment {

    @Inject
    UserRoleViewModel viewModel;
    private BottomSheetBehavior mBehavior;
    private PopupLayoutUserRoleBinding mViewBinding;
    private UserRoleAdapter mAdapter;
    private UserRoleDialogListener mListener;
    private int mSelectedRole;

    public UserRoleBottomDialog(UserRoleDialogListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.popup_layout_user_role, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = PopupLayoutUserRoleBinding.inflate(inflater);
        mViewBinding.setViewmodel(viewModel);
        mSelectedRole = getArguments().getInt(Constants.USER_ROLE, -1);
        mAdapter = new UserRoleAdapter(mSelectedRole);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mViewBinding.recyclerView.setAdapter(mAdapter);
        initListener();
        initObservation();
        return mViewBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        viewModel.clearDispose();
    }

    private void initListener() {
        mViewBinding.okBtn.setOnClickListener(l -> dismiss());
    }

    private void initObservation() {
        viewModel.getRoles().observe(getViewLifecycleOwner(), roles -> {
            mAdapter.addItems(roles);
        });
        viewModel.getAction().observe(getViewLifecycleOwner(), action -> {
            switch (action) {
                case ACTION_DONE:
                    mListener.onDone(mAdapter.getSelectedRole() != null && mSelectedRole != mAdapter.getSelectedRole().getId() ? mAdapter.getSelectedRole() : null);
                    dismiss();
                    break;
            }
        });
    }

    public interface UserRoleDialogListener {
        void onDone(UserRole role);
    }
}
