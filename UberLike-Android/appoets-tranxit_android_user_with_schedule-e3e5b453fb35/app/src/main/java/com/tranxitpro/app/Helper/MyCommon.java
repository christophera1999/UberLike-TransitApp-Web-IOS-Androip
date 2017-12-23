package com.tranxitpro.app.Helper;


public class MyCommon {
    private static MyCommon ourInstance = new MyCommon();
    public String gcmKey = "";
    public String hidePassKey = "";
    public Boolean active = false;
    public Boolean mapFrag = false;
    public Boolean mainActivity = false;
    public int requestCancel;

    public static MyCommon getInstance() {
        return ourInstance;
    }

    private MyCommon() {
    }
}
