package com.stoykov.maryan.calltogmail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class BootReceiver extends BroadcastReceiver {

    private String[] allCreds;
    private Context ctx;
    public BootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        //Toast.makeText(context,"Boot RECEIVED",Toast.LENGTH_SHORT).show();
        ctx=context;
        //TODO: Citaj sets pa ako treba i creds
       boolean fexists =false;
        SharedPreferences sharedPreferences=context.getSharedPreferences(IncomingDetect.SHARED_PREFS,context.MODE_PRIVATE);
        System.out.println("### ZXMARJAN BootRec onRECEIVE");
        if (sharedPreferences.contains("3")&&sharedPreferences.contains("0")) fexists=true;

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)&&fexists) {
            loadCreds();
            System.out.println("ZXMARJAN 77777777777777777777 "+allCreds[7]);
            if (allCreds[7].equals("true")){//ako poslednoto vo crds fajlot e true start na servisot
                /**
                 Vistinski nacin za intent, ako ne e taka na android 5.0 pagja app ********************
                 */
                Intent serviceIntent = new Intent(context,IncomingDetect.class);
                context.startService(serviceIntent);
            }

        }
    }
//==================================================================
    void loadCreds(){
        System.out.println("### ZXMARJAN BootRec loadcred");
        allCreds=new String[8];

        IncomingDetect.loadCreds(ctx);

        allCreds=IncomingDetect.GetCreds();

    }
//==================================================================
}
