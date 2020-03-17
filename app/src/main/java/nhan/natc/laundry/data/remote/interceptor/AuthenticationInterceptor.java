package nhan.natc.laundry.data.remote.interceptor;

import android.content.Context;

import java.io.IOException;

import nhan.natc.laundry.util.CommonUtils;
import nhan.natc.laundry.util.GlobalSharePreference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthenticationInterceptor implements Interceptor{
    private Context mContext;

    public AuthenticationInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = null;
        if (original != null && !original.url().encodedPath().contains("/login")) {
            String token = GlobalSharePreference.getToken(mContext);
            if (!CommonUtils.isNullOrEmpty(token)) {
                String authenToken = "Bearer " + token;
                request = original.newBuilder()
                        .header("Authorization", authenToken)
                        .header("platform", "ANDROID")
                        .build();
            }
        }
        return request != null ? chain.proceed(request) : chain.proceed(original);
    }
}
