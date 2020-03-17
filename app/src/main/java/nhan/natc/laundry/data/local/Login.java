package nhan.natc.laundry.data.local;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("access_token")
    private String mAccessToken;

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String token) {
        this.mAccessToken = token;
    }
}
