package nhan.natc.laundry.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("id")
    private Long mId;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("firstname")
    private String mFirstName;

    @SerializedName("lastname")
    private String mLastName;

    @SerializedName("role_id")
    private Integer mRoleId;

    @SerializedName("avatar")
    private String mAvatar;

    public UserRequest(Long id, String email, String password, String firstName, String lastName, Integer roleId, String avatar) {
        this.mId = id;
        this.mEmail = email;
        this.mPassword = password;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mRoleId = roleId;
        this.mAvatar = avatar;
    }
}
