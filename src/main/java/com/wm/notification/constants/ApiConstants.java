package com.wm.notification.constants;

public class ApiConstants {

//    public static final String USER_APP_BASE_URL = "https://users.thewealthmarket.com/api/v1/";

    public static final String USER_APP_BASE_URL = "http://localhost:8080/api/v1/";

    public static final String GET_USER_NAME = USER_APP_BASE_URL + "user/name/{wmUniqueId}";

    public static final String EXIST_UNIQUE_ID = USER_APP_BASE_URL + "user/exist/{wmUniqueId}";

    public static final String GET_WM_UNIQUE_ID = USER_APP_BASE_URL + "user/uniqueid/{id}";

    public static final String USER_IMAGE = USER_APP_BASE_URL + "user/profile-pic/{wmUniqueId}";

    public static final String REDIS_HASH_KEY = "UniqueKey:";
}