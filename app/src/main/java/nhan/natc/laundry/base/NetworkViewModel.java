package nhan.natc.laundry.base;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import nhan.natc.laundry.Constants;
import nhan.natc.laundry.data.remote.model.Resource;
import nhan.natc.laundry.data.remote.model.Status;

public class NetworkViewModel extends ViewModel {
    private MutableLiveData<NetworkViewAction> networkViewAction = new MutableLiveData<>();
    private String mFailedMessage;
    private boolean mIsLock = false;
    protected ObservableField<Boolean> isLoading = new ObservableField<>();

    public MutableLiveData<NetworkViewAction> getNetworkViewAction() {
        return networkViewAction;
    }

    protected NetworkViewModel() {
    }

    public ObservableField<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean _isLoading) {
        this.isLoading.set(_isLoading);
    }

    public void showLoading() {
        setIsLoading(true);
    }

    public void hideLoading() {
        setIsLoading(false);
    }

    /**
     * Default this overloaded method is applied to apis not skip error
     */
    protected <T> boolean handleNetworkException(Resource<T> Resource) {
        return handleNetworkException(Resource, false);
    }

    /**
     * Handle common network api case
     *
     * @param resource  Resource resource data
     * @param skipError Some apis need to skip error, not trigger UI action no matter what the error is - Set true
     * @return Whether error is handled or not - True if error happens
     */
    protected <T> boolean handleNetworkException(Resource<T> resource, boolean skipError) {
        if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
            // Request successfully, no errors to handle, will allow API business code to be executed
            return false;
        }
        if (skipError) {
            // Error happens, but skip them, not show any warning signs, won't allow API business code to be executed
            return true;
        }
        switch (resource.status) {
            case ERROR_CONNECT_EXCEPTION:
                networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_CONNECT_EXCEPTION);
                break;
            case ERROR_SERVER_ERROR:
                networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_SERVER_ERROR);
                break;
            case ERROR_HTTP_UNAUTHORIZED:
                // clear token, logout user
                networkViewAction.setValue(NetworkViewAction.LOGOUT);
                break;
        }
        // Error happens, already forwarded them to UI by NetworkViewAction, won't allow API business code to be executed
        return true;
    }

    protected void handleNetworkException(Throwable e, boolean skipError) {
        hideLoading();
        if (skipError)
            return;
        if (e instanceof HttpException) {
//            Log.d("NhanNATC", "=== error code : " + ((HttpException) e).code());
            mFailedMessage = "";
            try {
                JSONObject json = new JSONObject(((HttpException) e).response().errorBody().string());
                mFailedMessage = json.optString("message");
            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            switch (((HttpException) e).code()) {
                case Constants.HTTP_STATUS_NOT_FOUND:
                    networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_BAD_REQUEST);
                    break;
                case Constants.HTTP_STATUS_UNAUTHORIZE:
                    networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_UNAUTHORIZE);
                    break;
                case Constants.HTTP_SERVICE_UNAVAILABLE:
                case Constants.HTTP_SERVER_ERROR:
                    networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_SERVER_ERROR);
                    break;
                case Constants.HTTP_STATUS_FORBIDDEN_ACCESS:
                    networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_FORBIDDEN_ACCESS);
                    break;
                default:

                    break;
            }
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_CONNECT_EXCEPTION);
        } else if (e instanceof SocketTimeoutException) {
            networkViewAction.setValue(NetworkViewAction.SHOW_ERROR_NETWORK_TIMEOUT);
        }
        mIsLock = false;
    }

    public String getFailedMessage() {
        return mFailedMessage;
    }

    public void setFailedMessage(String message) {
        mFailedMessage = message;
    }

    public boolean isLock() {
        return mIsLock;
    }

    public void setIsLock(boolean mIsLock) {
        this.mIsLock = mIsLock;
    }
}
