package nhan.natc.laundry.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    public LoginRequest(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
    }
}
