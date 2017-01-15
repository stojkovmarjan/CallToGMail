package com.stoykov.maryan.calltogmail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class IncommingCallReceiver extends BroadcastReceiver {

    static boolean ring=false;
    static boolean callReceived=false;
    String  callerPhoneNumber;
    Context ctx;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        // TODO: GET CREDS (read from file)
        String[] allCreds;
        String crds=FileIO.ReadIN("crds.cfg",mContext);
        allCreds=new String[7];
        allCreds=crds.split(":");
        ctx=mContext;
        // Get the current Phone State

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

       // Toast.makeText(mContext,state, Toast.LENGTH_LONG).show();



        //callerPhoneNumber=bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        //-------------------------------------------------------
       // String cn2=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
       //Log.w("***********",cn2);

        if (state == null)
            return;

        // If phone state "Rininging"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");// get phone number
            ring = true;
            // Get the Caller's Phone Number
        }

        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callReceived = true;
        }


        // If phone is Idle -------------------------------------------------------------------------------------------
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if (ring == true && callReceived == false) {
                Toast.makeText(mContext, "It was A MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();

                // TODO: SEND MAIL
                //========================================
                //== SEND MAIL HERE ======================
                if (allCreds[5].equals("true")){

                    Mail sendMail=new Mail(allCreds,callerPhoneNumber);
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
                    SendSMS(allCreds[4],"Missed call from "+callerPhoneNumber);
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
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toPhone, null, msg, null, null);
        }

    }
