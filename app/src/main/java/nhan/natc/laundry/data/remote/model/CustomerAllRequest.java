package nhan.natc.laundry.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class CustomerAllRequest {
    @SerializedName("fetch_page")
    private int mPage;

    @SerializedName("fetch_limit")
    private int mPerPage;

    public CustomerAllRequest(int page, int perPage) {
        this.mPage = page;
        this.mPerPage = perPage;
    }
}
