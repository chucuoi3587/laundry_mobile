package nhan.natc.laundry.base;

public interface UINetworkExceptionHandler {
    /**
     * Exception when the network connection on mobile is turned off
     */
    void onConnectException();

    /**
     * Exception when the time making a connection to server is over
     */
    void onSocketTimeout();

    /**
     * Exception when none of above case happen
     */
    void onServerError();

    /**
     * Exception when UnAuthorized
     */
    void onUnAuthorizedError();
}
