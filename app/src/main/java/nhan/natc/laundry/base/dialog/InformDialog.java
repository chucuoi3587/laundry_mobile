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

public class InformDialog extends Dialog {

    private ImageView mIcon;
    private TextView mMessageTv;
    private Button mOkBtn;

    public InformDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.inform_dialog_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        mIcon = findViewById(R.id.icon);
        mMessageTv = findViewById(R.id.message);
        mOkBtn = findViewById(R.id.okBtn);
        CommonUtils.setBackground(mOkBtn, R.drawable.ripple_button_blue_background, R.drawable.button_blue_border_selector);
        setCancelable(false);
    }

    public static Dialog create(@NonNull Context context, String message, String buttonTitle, Integer iconRes, View.OnClickListener listener) {
        InformDialog dialog = new InformDialog(context);
        dialog.renderData(message, buttonTitle, iconRes, listener);
        return dialog;
    }

    private void renderData(String message, String buttonTitle, Integer iconRes, View.OnClickListener listener) {
        if (iconRes != null)
            mIcon.setImageResource(iconRes);
        if (message != null)
            mMessageTv.setText(message);
        if (buttonTitle != null)
            mOkBtn.setText(buttonTitle);
        mOkBtn.setOnClickListener(l -> {
            dismiss();
            if (listener != null)
                listener.onClick(l);
        });
    }

    public void setMessage(String message) {
        mMessageTv.setText(message);
    }

    public String getMessage() {
        return mMessageTv != null ? mMessageTv.getText().toString().trim() : "";
    }

    public void setButtonTitle(String title) {
        mOkBtn.setText(title);
    }

    public void setIcon(int resource) {
        if (resource > 0) {
            mIcon.setImageResource(resource);
        }
    }
}
