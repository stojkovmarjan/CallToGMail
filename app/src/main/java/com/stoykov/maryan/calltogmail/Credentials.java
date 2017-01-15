package com.stoykov.maryan.calltogmail;

/**
 * Created by Marjan on 15.12.2015.
 */
public class Credentials {
    private static String uname;
    private static String password;

    public static void setCreds(String u,String p)
    {
        uname=u;
        password=p;
    }

    public static String getUname()
    {
        return uname;
    }

    public static String getPassword(){
        return password;
    }

    public static void saveCreds(){
        // TODO: Save password i uname to file
    }

    public static void loadCreds(){
        // TODO: Load password i uname from file
    }

}
