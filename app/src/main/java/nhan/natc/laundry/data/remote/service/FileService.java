package nhan.natc.laundry.data.remote.service;

import io.reactivex.Observable;
import nhan.natc.laundry.data.local.FileDownload;
import nhan.natc.laundry.data.remote.model.Resource;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {

    @Multipart
    @POST("file/upload")
    Observable<Resource<FileDownload>> doUpload(@Part MultipartBody.Part file);
}
