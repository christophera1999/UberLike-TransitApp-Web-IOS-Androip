package com.tranxitpro.provider.Helper;

/**
 * Created by jayakumar on 26/12/16.
 */

public class URLHelper {
    public static String base = "http://schedule.tranxit.co/";
    public static String HELP_URL = "http://schedule.tranxit.co";
    public static String CALL_PHONE = "1";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.tranxitpro.provider&hl=en";
    //    public static String base = "https://dev.tranxit.co/";
    public static String login = base + "api/provider/oauth/token";
    public static String register = base + "api/provider/register";
    public static String USER_PROFILE_API = base + "api/provider/profile";
    public static String UPDATE_AVAILABILITY_API = base + "api/provider/profile/available";
    public static String GET_HISTORY_API = base + "api/provider/requests/history";
    public static String GET_HISTORY_DETAILS_API = base + "api/provider/requests/history/details";
    public static String CHANGE_PASSWORD_API = base + "api/provider/profile/password";
    public static final String UPCOMING_TRIP_DETAILS = base + "api/provider/requests/upcoming/details";
    public static final String UPCOMING_TRIPS = base + "api/provider/requests/upcoming";
    public static final String CANCEL_REQUEST_API = base + "api/provider/cancel";
    public static final String TARGET_API = base + "api/provider/target";
    public static final String RESET_PASSWORD = base + "api/provider/reset/password";
    public static final String FORGET_PASSWORD = base + "api/provider/forgot/password";
    public static final String FACEBOOK_LOGIN = base + "api/provider/auth/facebook";
    public static final String GOOGLE_LOGIN = base + "api/provider/auth/google";
    public static final String LOGOUT = base + "api/provider/logout";
    public static final String SUMMARY = base + "api/provider/summary";
    public static final String HELP = base + "api/provider/help";


}
