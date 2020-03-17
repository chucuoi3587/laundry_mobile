package nhan.natc.laundry.data.local;

import com.google.gson.annotations.SerializedName;

public class FileDownload {
    @SerializedName("fileName")
    private String mFileName;

    @SerializedName("fileDownloadUri")
    private String mFileDownloadUri;

    @SerializedName("fileType")
    private String mFileType;

    @SerializedName("size")
    private long mSize;

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public String getFileDownloadUri() {
        return mFileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.mFileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return mFileType;
    }

    public void setFileType(String fileType) {
        this.mFileType = fileType;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }
}
