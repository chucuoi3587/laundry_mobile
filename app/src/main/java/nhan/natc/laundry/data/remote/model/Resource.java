package nhan.natc.laundry.data.remote.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Resource<T> {
    public final Status status;

    @SerializedName(value = "message", alternate = "error")
    public final String message;

    @SerializedName("msg")
    public String msg[];

    public final T data;

    @SerializedName("error_description")
    public final String errorDescription;

    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.errorDescription = null;
    }

    public Resource(Status status, T data, String message, String errorDescription) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.errorDescription = errorDescription;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @NonNull
    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    @NonNull
    public static <T> Resource<T> error() {
        return new Resource<>(Status.ERROR, null, null);
    }

    @NonNull
    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    @NonNull
    public static <T> Resource<T> errorConnection() {
        return new Resource<>(Status.ERROR_CONNECT_EXCEPTION, null, null);
    }

    @NonNull
    public static <T> Resource<T> errorServerError() {
        return new Resource<>(Status.ERROR_SERVER_ERROR, null, null);
    }

    @NonNull
    public static <T> Resource<T> errorHttpUnAuthorized() {
        return new Resource<>(Status.ERROR_HTTP_UNAUTHORIZED, null, null);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
