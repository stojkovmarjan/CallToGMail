package com.stoykov.maryan.calltogmail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText txtDeviceID;       // id na uredot za vo email
    private EditText txtSendFrom;       // adresa od koja se prakjat email-ovi
    private EditText txtPassword;       // password za prethodnata adresa
    private EditText txtSendTo;         // adresa sto gi prima mail-lovite
    private EditText txtSendToSMS;

    private Button btnTestMail;         //kopce za test na email
    private CheckBox checkStartAtBoot;  //dali da startuva servisot pri boot na uredot
    private CheckBox checkSendMail;     //send mail checkbox
    private CheckBox checkSendSMS;      //send SMS check box
    private Button btnStartService;     // kopce za start na servisot;

    private boolean sendMail;
    private boolean sendSMS;
    private boolean StartAtBoot;        // dali da startuva od boot

    private String[] allCreds;
                                        /*
                                            crds[0]=DeviceID;
                                            crds[1]=SendFrom;
                                            crds[2]=Password;
                                            crds[3]=SendTo;
                                            crds[4]=SendToSMS;
                                            crds[5]=sendEmailCheck;
                                            crds[6]=sendSMSCehck;
                                            crds[7]=startAtBootCheck;
                                         */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void setBtnTestMail_Click(View v){
       // Creds c=new Creds("test","busystuff.mk","zxmarjan#1","078123456","","","","");
        Creds c=new Creds("aaaaa","busystuff.mk","zxmarjan#1","stojkovmarjan@gmail.com","0","1","","");
        Mail m=new Mail(c.getCreds(),"*******TEST*********");
        try{
            m.send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startService(View v){

    }
}
