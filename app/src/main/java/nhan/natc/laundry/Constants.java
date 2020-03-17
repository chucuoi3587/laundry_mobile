package nhan.natc.laundry;

import java.util.regex.Pattern;

public class Constants {
    public static final String URL = BuildConfig.BASE_URL;
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);

    public static int STATUS_BAR_HEIGHT = 0;
    public static int NAV_BAR_HEIGHT = 0;
    public static int SCREEN_HEIGHT = 0;

    public static final String USER_TAG_FIRST_NAME = "user_name";
    public static final String USER_TAG_LAST_NAME = "last_name";
    public static final String USER_TAG_EMAIL = "email";
    public static final String USER_TAG_ACCESS_TOKEN = "access_token";
    public static final String USER_TAG_ROLE_ID = "role_id";
    public static final String USER_TAG_ROLE_DESCRIPTION = "role_description";
    public static final String USER_TAG_AVATAR = "avatar";

    public static final String USER_ID = "user_id";
    public static final String USER_ROLE = "user_role";
    public static final String CUSTOMER_ID = "customer_id";

    public static final int HTTP_STATUS_SUCCESS = 200;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_UNAUTHORIZE = 401;
    public static final int HTTP_STATUS_FORBIDDEN_ACCESS = 403;
    public static final int HTTP_SERVER_ERROR = 500;
    public static final int HTTP_SERVICE_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;

    public static final int CAMERA_REQUEST_CODE = 1000;
    public static final int GALLERY_REQUEST_CODE = 1001;
    public static final int REQUEST_CAMERA_PERMISSION = 1002;
    public static final int REQUEST_STORAGE_PERMISSION = 1003;
    public static final int REQUEST_CUSTOMER_DETAIL = 1004;
    public static final int REQUEST_PHONE_CALL = 1005;
}
