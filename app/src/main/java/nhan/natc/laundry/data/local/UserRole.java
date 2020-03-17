package nhan.natc.laundry.data.local;

import com.google.gson.annotations.SerializedName;

public class UserRole {
    @SerializedName("id")
    private int mId;

    @SerializedName("description")
    private String mRoleDescription;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getRoleDescription() {
        return mRoleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.mRoleDescription = roleDescription;
    }

    public UserRole(int id, String roleDescription) {
        this.mId = id;
        this.mRoleDescription = roleDescription;
    }
}
