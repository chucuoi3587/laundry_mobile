package nhan.natc.laundry.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkStatusReceiver extends BroadcastReceiver {

    public ConnectivityReceiverListener connectivityReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        isConnectedOrConnecting(context);
    }

    private void isConnectedOrConnecting(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connMgr.isActiveNetworkMetered()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                switch (connMgr.getRestrictBackgroundStatus()) {
//                    case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED:
//                        // Background data usage is blocked for this app. Wherever possible,
//                        // the app should also use less data in the foreground.
//
//                    case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED:
//                        // The app is whitelisted. Wherever possible,
//                        // the app should use less data in the foreground and background.
//
//                    case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED:
//                        // Data Saver is disabled. Since the device is connected to a
//                        // metered network, the app should use less data wherever possible.
//                        DialogUtil.showInformDialog(context, context.getString(R.string.server_error), context.getString(R.string.ok_lbl), null, null);
//                        break;
//                }
//            }
//        } else
        if (context instanceof ConnectivityReceiverListener) {
            connectivityReceiverListener = (ConnectivityReceiverListener) context;
            if (connectivityReceiverListener != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            connectivityReceiverListener.onNetworkConnectionChanged(true);
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            connectivityReceiverListener.onNetworkConnectionChanged(true);
                        }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                            connectivityReceiverListener.onNetworkConnectionChanged(true);
                        } else {
                            connectivityReceiverListener.onNetworkConnectionChanged(false);
                        }
                    } else {
                        connectivityReceiverListener.onNetworkConnectionChanged(false);
                    }
                } else {
                    // getActiveNetworkInfo is deprecated in api 29
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        connectivityReceiverListener.onNetworkConnectionChanged(true);
                    } else {
                        connectivityReceiverListener.onNetworkConnectionChanged(false);
                    }
                }
            }
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isDataNetworkAvailable);
    }
}
