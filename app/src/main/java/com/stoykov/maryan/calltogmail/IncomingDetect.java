package com.stoykov.maryan.calltogmail;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

    protected static final String SHARED_PREFS="calltogmail_shared";//protected za da mozam bez getter da ja citam

    public IncomingDetect() {
        //System.out.println("### ZXMARJAN IncomingDetect EMTY CONSTR");
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
        //System.out.println("### ZXMARJAN IncomingDetect CONSTR 1");
    }

    public static void SetCreds(String [] creds){
        //System.out.println("### ZXMARJAN IncomingDetect CONSTR 2");
        DeviceID=creds[0];
        SendFrom=creds[1];
        Password=creds[2];
        SendTo=creds[3];
        SendToSMS=creds[4];
        sendEmailCheck=creds[5];
        sendSMSCehck=creds[6];
        startAtBootCheck=creds[7];
    }

    public static void loadCreds(Context ctxt){
        //System.out.println("### ZXMARJAN IncomingDetect LOAD CREDS");
        try {
            SharedPreferences sharedPreferences=ctxt.getSharedPreferences(SHARED_PREFS,ctxt.MODE_PRIVATE);

                SetCreds(sharedPreferences.getString("0",""),
                        sharedPreferences.getString("1",""),
                        sharedPreferences.getString("2",""),
                        sharedPreferences.getString("3",""),
                        sharedPreferences.getString("4",""),
                        sharedPreferences.getString("5",""),
                        sharedPreferences.getString("6",""),
                        sharedPreferences.getString("7",""));

        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
    public static String[] GetCreds(){
        //System.out.println("### ZXMARJAN IncomingDetect GET CREDS");
        String[] crds=new String[8];
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
        //System.out.println("### ZXMARJAN IncomingDetect SAVE CREDS");
        boolean success=true;
        SetCreds(crds);
        try {
            SharedPreferences sharedPreferences=ctxt.getSharedPreferences(SHARED_PREFS,ctxt.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            for (int f=0;f<8;f++){
                editor.putString(String.valueOf(f),crds[f]);
                }
                editor.commit();
                editor.apply();

            } catch (Exception e){
                success=false;
                System.out.println(e.toString());
            }

        return success;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // System.out.println("### ZXMARJAN IncomingDetect IBinder");
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        //System.out.println("### ZXMARJAN IncomingDetect ONSTARTCOMMAND");
        /** REGISTRACIJA NA BROADCAST RECEIVER ***********
         * tuka a ne u manifest, za da mozam da go pustam i gasam
         * od aplikacijata
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(callReceiver, filter); // go registriram - aktiviram
        /** ********************************************************* */
        Toast.makeText(this,"Call to GMail service started",Toast.LENGTH_SHORT).show();
        //detectingActive=true;
        ctx=this.getBaseContext();
        detectingActive=true;
        notifyRunning();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //System.out.println("### ZXMARJAN IncomingDetect ONDESTROY");
        Context ctxt=getBaseContext();
        unregisterReceiver(callReceiver);//unregister receiver, go gasam

        //detectingActive=false;

        //====================== StartAtBoot CHECKBOX FALSE =================
       // SharedPreferences sharedPreferences=ctxt.getSharedPreferences(SHARED_PREFS,ctxt.MODE_PRIVATE);
        //SharedPreferences.Editor editor=sharedPreferences.edit();
            //editor.putString("7","false");
        //editor.commit();
        //editor.apply();
        detectingActive=false;
        cancelNotify();
        Toast.makeText(this,"Call to mail service is stopped",Toast.LENGTH_SHORT).show();
    }

    public static boolean isDetectingActive() {
        //System.out.println("### ZXMARJAN IncomingDetect isDetectingActive");
        return detectingActive;
    }

    public void notifyRunning(){
        //System.out.println("### ZXMARJAN IncomingDetect NotifyRun");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Call to GMail");
        mBuilder.setContentText("Missed call to GMail is running!");

        Intent resultIntent = new Intent(this, Main2Activity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Main2Activity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(17122112, mBuilder.build());
    }

    public void cancelNotify(){
        //System.out.println("### ZXMARJAN IncomingDetect CANCEL NOTIFY");
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(17122112);
    }
}


