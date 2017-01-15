package com.stoykov.maryan.calltogmail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


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
        boolean fexists =FileIO.FileExists("crds.cfg",context);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)&&fexists) {
            loadCreds();
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
        String crds=FileIO.ReadIN("crds.cfg",ctx);
        allCreds=new String[7];
        allCreds=crds.split(":");

        IncomingDetect.SetCreds(allCreds[0],//deviceid
                                allCreds[1],//email to send from
                                allCreds[2],                            //pass for sendin email
                                allCreds[3],  //email to send to
                                allCreds[4],//phone to SMS
                                allCreds[5],//sendmail checkbox val
                                allCreds[6], //send sms check val
                                allCreds[7]);// start at boot val

    }
//==================================================================
}
