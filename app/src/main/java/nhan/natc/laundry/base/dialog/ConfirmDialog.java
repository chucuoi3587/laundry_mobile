package nhan.natc.laundry.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import nhan.natc.laundry.R;
import nhan.natc.laundry.util.CommonUtils;

public class ConfirmDialog extends Dialog {

    private TextView mMessageTv;
    private Button mPossitiveBtn, mNegativeBtn;
    private ImageView mIconImgv;

    public ConfirmDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.confirm_dialog_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        mIconImgv = findViewById(R.id.icon);
        mMessageTv = findViewById(R.id.message);
        mPossitiveBtn = findViewById(R.id.okBtn);
        mNegativeBtn = findViewById(R.id.cancelBtn);
        CommonUtils.setBackground(mPossitiveBtn, R.drawable.ripple_button_blue_background, R.drawable.button_blue_border_selector);
        CommonUtils.setBackground(mNegativeBtn, R.drawable.ripple_button_transparent_background, R.drawable.button_transparent_border_selector);
        setCancelable(false);
    }

    public static Dialog create(@NonNull Context context, String message, Integer iconRes, String positiveBtnTitle,
                                View.OnClickListener positiveListener, String negativeBtnTitle, View.OnClickListener negativeListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.renderData(message, iconRes, positiveBtnTitle, positiveListener, negativeBtnTitle, negativeListener);
        return dialog;
    }

    private void renderData(String message, Integer iconRes, String positiveBtnTitle,
                            View.OnClickListener positiveListener, String negativeBtnTitle, View.OnClickListener negativeListener) {
        if (iconRes != null)
            mIconImgv.setImageResource(iconRes);
        if (message != null)
            mMessageTv.setText(message);
        if (positiveBtnTitle != null)
            mPossitiveBtn.setText(positiveBtnTitle);
        mPossitiveBtn.setOnClickListener(l -> {
            dismiss();
            if (positiveListener != null)
                positiveListener.onClick(l);
        });
        if (negativeBtnTitle != null)
            mNegativeBtn.setText(negativeBtnTitle);
        mNegativeBtn.setOnClickListener(l -> {
            dismiss();
            if (negativeListener != null)
                negativeListener.onClick(l);
        });
    }

    public String getMessage() {
        return mMessageTv != null ? mMessageTv.getText().toString().trim() : "";
    }
}
