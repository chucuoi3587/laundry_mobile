package nhan.natc.laundry.data.local;

import com.google.gson.annotations.SerializedName;

public class Avatar {
    @SerializedName("id")
    private String mId;

    @SerializedName("fileName")
    private String mFileName;

    @SerializedName("fileType")
    private String mFileType;

    @SerializedName("data")
    private Byte[] mData;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public String getFileType() {
        return mFileType;
    }

    public void setFileType(String fileType) {
        this.mFileType = fileType;
    }

    public Byte[] getData() {
        return mData;
    }

    public void setData(Byte[] data) {
        this.mData = data;
    }
}
