package nhan.natc.laundry.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class CustomerRequest {
    @SerializedName("id")
    private long mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("phone")
    private String mPhone;

    public CustomerRequest(long id, String name, String email, String address, String phone) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
        this.mAddress = address;
        this.mPhone = phone;
    }
}
