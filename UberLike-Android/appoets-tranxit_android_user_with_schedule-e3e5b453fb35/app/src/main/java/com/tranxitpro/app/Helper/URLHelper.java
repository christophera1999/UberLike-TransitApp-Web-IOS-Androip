package com.tranxitpro.app.Helper;

public class URLHelper {
    public static final String base = "http://schedule.tranxit.co/";
//    public static final String base = "https://dev.tranxit.co/";
public static final String REDIRECT_URL = base;
    public static final String REDIRECT_SHARE_URL = "http://maps.google.com/maps?q=loc:";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.tranxitpro.app&hl=en";
    public static final int client_id = 2;
    public static final String client_secret = "yVnKClKDHPcDlqqO1V05RtDRdvtrVHfvjlfqliha";
    public static final String STRIPE_TOKEN = "pk_test_0G4SKYMm8dK6kgayCPwKWTXy";

    public static final String login = base+"oauth/token";
    public static final String register = base+"api/user/signup";
    public static final String UserProfile = base+"api/user/details";
    public static final String UseProfileUpdate = base+"api/user/update/profile";
    public static final String getUserProfileUrl = base+"api/user/details";
    public static final String GET_SERVICE_LIST_API = base+"api/user/services";
    public static final String REQUEST_STATUS_CHECK_API = base+"api/user/request/check";
    public static final String ESTIMATED_FARE_DETAILS_API = base+"api/user/estimated/fare";
    public static final String SEND_REQUEST_API = base+"api/user/send/request";
    public static final String CANCEL_REQUEST_API = base+"api/user/cancel/request";
    public static final String PAY_NOW_API = base+"api/user/payment";
    public static final String RATE_PROVIDER_API = base+"api/user/rate/provider";
    public static final String CARD_PAYMENT_LIST = base+"api/user/card";
    public static final String ADD_CARD_TO_ACCOUNT_API = base+"api/user/card";
    public static final String DELETE_CARD_FROM_ACCOUNT_API = base+"api/user/card/destory";
    public static final String GET_HISTORY_API = base+"api/user/trips";
    public static final String GET_HISTORY_DETAILS_API = base+"api/user/trip/details";
    public static final String addCardUrl = base+"api/user/add/money";
    public static final String COUPON_LIST_API = base+"api/user/promocodes";
    public static final String ADD_COUPON_API = base+"api/user/promocode/add";
    public static final String CHANGE_PASSWORD_API = base+"api/user/change/password";
    public static final String UPCOMING_TRIP_DETAILS = base+"api/user/upcoming/trip/details";
    public static final String UPCOMING_TRIPS = base+"api/user/upcoming/trips";
    public static final String GET_PROVIDERS_LIST_API = base+"api/user/show/providers";
    public static final String FORGET_PASSWORD = base + "api/user/forgot/password";
    public static final String RESET_PASSWORD = base + "api/user/reset/password";

    public static final String FACEBOOK_LOGIN = base + "api/user/auth/facebook";
    public static final String GOOGLE_LOGIN = base + "api/user/auth/google";
    public static final String LOGOUT = base + "api/user/logout";
    public static final String HELP = base + "api/user/help";
}
