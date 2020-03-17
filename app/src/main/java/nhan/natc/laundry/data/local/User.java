package nhan.natc.laundry.data.local;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private long mId;

    @SerializedName("userStatus")
    private UserStatus mUserStatus;

    @SerializedName("role")
    private UserRole mUserRole;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("firstname")
    private String mFirstName;

    @SerializedName("lastname")
    private String mLastName;

    @SerializedName("avatar")
    private String mAvatar;

    public long getId() {
        return mId;
    }

    public UserStatus getUserStatus() {
        return mUserStatus;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public void setUserRole(UserRole role) {
        this.mUserRole = role;
    }

    public UserRole getUserRole() {
        return mUserRole;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }
}
