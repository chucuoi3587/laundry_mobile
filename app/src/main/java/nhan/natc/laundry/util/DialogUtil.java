package nhan.natc.laundry.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import nhan.natc.laundry.base.dialog.ConfirmDialog;
import nhan.natc.laundry.base.dialog.InformDialog;

public class DialogUtil {
    private static Dialog mDialog;
    public static void showInformDialog(Context context, String message, String buttonTitle, Integer icon, View.OnClickListener listener) {
        if (mDialog != null) {
            if (mDialog instanceof InformDialog && mDialog.isShowing()) {
                if (message.equals(((InformDialog) mDialog).getMessage()))
                    return;
            }
            mDialog.dismiss();
        }
        mDialog = InformDialog.create(context, message, buttonTitle, icon, listener);
        mDialog.show();
    }

    public static void showConfirmDialog(Context context, String message, Integer icon, String positiveBtnTitle, View.OnClickListener positiveListener,
                                         String negativeBtnTitle, View.OnClickListener negativeListener) {
        if (mDialog != null) {
            if (mDialog instanceof ConfirmDialog && mDialog.isShowing()) {
                if (message.equals(((ConfirmDialog) mDialog).getMessage()))
                    return;
            }
            mDialog.dismiss();
        }
        mDialog = ConfirmDialog.create(context, message, icon, positiveBtnTitle, positiveListener, negativeBtnTitle, negativeListener);
        mDialog.show();
    }
}
