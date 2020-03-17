package nhan.natc.laundry.data.local;

import com.google.gson.annotations.SerializedName;

public class UserStatus {
    @SerializedName("id")
    private int mId;

    @SerializedName("statusName")
    private String mStatusName;

    public int getId() {
        return mId;
    }

    public String getStatusName() {
        return mStatusName;
    }
}
