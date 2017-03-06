package com.stoykov.maryan.calltogmail;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IncommingCallReceiver extends BroadcastReceiver {

    static boolean ring=false;
    static boolean callReceived=false;
    String  callerPhoneNumber;
    static Context  ctx;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        // TODO: GET CREDS (read from file)
        System.out.println("### ZXMARJAN IncommingCallReceiver ONCRECEIVE");
        String time="";
        String[] allCreds;
        allCreds=new String[7];
        IncomingDetect.loadCreds(mContext);
        allCreds=IncomingDetect.GetCreds();
        ctx=mContext;

        // Get the current Phone State
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null)
            return;

        // If phone state "Rininging"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");// get phone number
           // time=bundle.getString("call_time");

            ring = true;
            // Get the Caller's Phone Number
        }

        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callReceived = true;
        }


        // If phone is Idle -------------------------------------------------------------------------------------------
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // telefonot zvonel (ring=true) i ne e podignata slus (callReceived=false) togas povikot e propusten
            if (ring == true && callReceived == false) {
                Toast.makeText(mContext, "MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm E, dd.MM.yyyy");
                time = sdf.format(new Date());
                // TODO: SEND MAIL
                //========================================
                //== SEND MAIL HERE ======================
                if (allCreds[5].equals("true")){

                    Mail sendMail=new Mail(allCreds,callerPhoneNumber+" (time: "+time+") ");
                    try {
                        sendMail.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                //========================================

                //======== SMS ===========================
                if (allCreds[6].equals("true")){
                    //TODO: SEND SMS HERE
                    SendSMS(allCreds[4],"Missed call from "+callerPhoneNumber+" on device "+allCreds[0]+" (time: "+time+").");
                   // Log.w("***********",allCreds[4]);
                }
                //========================================
            }
            ring=false;
            callReceived=false;
        }
        //------------------------------------------------------------------------------------------------------------
      }

        public static void SendSMS(String toPhone,String msg){
            System.out.println("### ZXMARJAN IncommingCallReceiver SENDSMS");

            /* ZA MARSHMALLOW I NAGORE
                        TREBA RUNTIME PERMISSIONS
                        if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = checkSelfPermission(RegistrationActivity.this, Manifest.permission.SEND_SMS);
                if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegistrationActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS);
                    return;
                }else{
                    //sendSms(mobile);
                }
            } else {
                //sendSms(mobile);
            }
            */

            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(toPhone, null, msg, null, null);
                } catch (Exception e){
                    System.out.println(e.toString());
                    }

        }

    }
