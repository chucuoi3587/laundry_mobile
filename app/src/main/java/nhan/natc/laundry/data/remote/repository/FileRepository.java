package nhan.natc.laundry.data.remote.repository;

import javax.inject.Inject;

import io.reactivex.Observable;
import nhan.natc.laundry.data.local.FileDownload;
import nhan.natc.laundry.data.remote.model.Resource;
import nhan.natc.laundry.data.remote.service.FileService;
import okhttp3.MultipartBody;

public class FileRepository {

    private FileService mFileService;
    @Inject
    public FileRepository(FileService fileService) {
        this.mFileService = fileService;
    }

    public Observable<Resource<FileDownload>> uploadFile(MultipartBody.Part file) {
        return mFileService.doUpload(file);
    }
}
