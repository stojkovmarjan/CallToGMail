package com.stoykov.maryan.calltogmail;

/**
 * Created by marjan on 19.12.15.
 */

public class Creds {
    String _devID;
    String _emailFrom;
    String _pass;
    String _emailTo;
    String _smsPhone;
    String _checkEmail;
    String _checkSMS;
    String _checkStartAtBoot;
    public Creds(){

    }
    public Creds(String devID,
                 String emailFrom,
                 String pass,
                 String emailTo,
                 String smsPhNumber,
                 String checkEmail,
                 String checkSMS,
                 String startAtBoot){

        _devID=devID;
        _emailFrom=emailFrom;
        _pass=pass;
        _emailTo=emailTo;
        _smsPhone=smsPhNumber;
        _checkEmail=checkEmail;
        _checkSMS=checkSMS;
        _checkStartAtBoot=startAtBoot;
    }

    public void set_devID(String devID){
        _devID=devID;
    }

    public void set_emailFrom(String email){
        _emailTo=email;
    }

    public void set_Pass(String pass){
        _pass=pass;
    }

    public void set_emailTo(String to){
        _emailTo=to;
    }

    public void saveCreds(){

    }

    public void loadCreds(){

    }

    public String[] getCreds(){
        return (new String[]{
                _devID,
                _emailFrom,
                _pass,
                _emailTo,
                _smsPhone,
                _checkEmail,
                _checkSMS,
                _checkStartAtBoot});
    }

}


