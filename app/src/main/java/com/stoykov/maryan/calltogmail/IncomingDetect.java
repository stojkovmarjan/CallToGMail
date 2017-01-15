package com.stoykov.maryan.calltogmail;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

public class IncomingDetect extends Service {

    private static String DeviceID; // id na uredot za vo email
    private static String SendFrom; // adresa od koja se prakjat email-ovi
    private static String Password; // password za prethodnata adresa
    private static String SendTo; // adresa sto gi prima mail-lovite
    private static String SendToSMS; //broj za sms
    private static String sendEmailCheck;
    private static String sendSMSCehck;
    private static String startAtBootCheck;
    private static Context ctx;
    private static boolean detectingActive;

    private BroadcastReceiver callReceiver=new IncommingCallReceiver();//za da registriram receiver

    public IncomingDetect() {

    }

    public static void SetCreds(String devID,String sendF,String pass,String sendT,String sendSMS,
                                String chkEmail, String chkSMS, String chkBoot){
        DeviceID=devID;
        SendFrom=sendF;
        Password=pass;
        SendTo=sendT;
        SendToSMS=sendSMS;
        sendEmailCheck=chkEmail;
        sendSMSCehck=chkSMS;
        startAtBootCheck=chkBoot;
    }

    public static void SetCreds(String [] creds){
        DeviceID=creds[0];
        SendFrom=creds[1];
        Password=creds[2];
        SendTo=creds[3];
        SendToSMS=creds[4];
        sendEmailCheck=creds[5];
        sendSMSCehck=creds[6];
        startAtBootCheck=creds[7];
    }

    public static String[] GetCreds(){
        String[] crds=new String[7];
        crds[0]=DeviceID;
        crds[1]=SendFrom;
        crds[2]=Password;
        crds[3]=SendTo;
        crds[4]=SendToSMS;
        crds[5]=sendEmailCheck;
        crds[6]=sendSMSCehck;
        crds[7]=startAtBootCheck;
        return crds;
    }

    public static boolean SaveCreds(String [] crds, Context ctxt){
        String allCreds;
        allCreds=crds[0]+":"+crds[1]+":"+crds[2]+":"+crds[3]+":"+crds[4]+":"+crds[5]+":"+crds[6]+":"+crds[7];

        return FileIO.SaveIN("crds.cfg",allCreds, ctxt);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startID){

        /** REGISTRACIJA NA BROADCAST RECEIVER ***********
         * tuka a ne u manifest, za da mozam da go pustam i gasam
         * od aplikacijata
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(callReceiver, filter); // go registriram - aktiviram
        /** ********************************************************* */
        Toast.makeText(this,"Call to mail service started",Toast.LENGTH_SHORT).show();
        detectingActive=true;
        ctx=this.getBaseContext();
        notifyRunning();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(callReceiver);//unregister receiver, go gasam
        detectingActive=false;
        cancelNotify();
        Toast.makeText(this,"Call to mail service is stopped",Toast.LENGTH_SHORT).show();
    }

    public static boolean isDetectingActive()
    {
        return detectingActive;
    }

    public void notifyRunning(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Call to email");
        mBuilder.setContentText("Missed call to email is running!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(17122112, mBuilder.build());
    }

    public void cancelNotify(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(17122112);
    }
}


