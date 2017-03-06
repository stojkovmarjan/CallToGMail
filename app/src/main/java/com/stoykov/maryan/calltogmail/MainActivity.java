package com.stoykov.maryan.calltogmail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtDeviceID;       // id na uredot za vo email
    private EditText txtSendFrom;       // adresa od koja se prakjat email-ovi
    private EditText txtPassword;       // password za prethodnata adresa
    private EditText txtSendTo;         // adresa sto gi prima mail-lovite
    private EditText txtSendToSMS;

    //private Button btnTestMail;         //kopce za test na email
    private CheckBox checkStartAtBoot;  //dali da startuva servisot pri boot na uredot
    private CheckBox checkSendMail;     //send mail checkbox
    private CheckBox checkSendSMS;      //send SMS check box
    private Button btnStartService;     // kopce za start na servisot;

    /*
    private boolean sendMail;
    private boolean sendSMS;
    private boolean StartAtBoot;        // dali da startuva od boot

    private String[] allCreds;

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

        System.out.println("### ZXMARJAN START");

        //inicijalizacija na kontroli
        txtDeviceID=(EditText)findViewById(R.id.txtDeviceID);
        txtSendFrom=(EditText)findViewById(R.id.txtSendFrom);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        txtSendTo=(EditText)findViewById(R.id.txtSendTo);
        txtSendToSMS=(EditText)findViewById(R.id.txtSendSMS);
        checkStartAtBoot=(CheckBox)findViewById(R.id.checkStartAtBoot);
        checkSendMail=(CheckBox)findViewById(R.id.checkBox);
        checkSendSMS=(CheckBox)findViewById(R.id.checkBox2);
       // btnTestMail=(Button)findViewById(R.id.btnTestMail);

        btnStartService=(Button)findViewById(R.id.btnStartService);

        SharedPreferences sharedPreferences=getSharedPreferences(IncomingDetect.SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences.contains("3")&&sharedPreferences.contains("0"))
        {
            loadCreds();
        }

        if (IncomingDetect.isDetectingActive()) btnStartService.setText("STOP SERVICE");
        else btnStartService.setText("START SERVICE");
    }

   // TEST BUTTON
    public void btnTestMail_Click(View v){
        System.out.println("### ZXMARJAN TEST");
        // TODO: Testiranje na email setinzi

        String pass=txtPassword.getText().toString();
        boolean success=true;
        IncomingDetect.SetCreds(txtDeviceID.getText().toString(),
                txtSendFrom.getText().toString(),
                pass,
                txtSendTo.getText().toString(),
                txtSendToSMS.getText().toString(),
                String.valueOf(checkSendMail.isChecked()),//sendmail checkbox val
                String.valueOf(checkSendSMS.isChecked()), //send sms check val
                String.valueOf(checkStartAtBoot.isChecked()));// start at boot val

        saveCreds();

        Creds c=new Creds(  txtDeviceID.getText().toString(),
                txtSendFrom.getText().toString(),
                txtPassword.getText().toString(),
                txtSendTo.getText().toString(),
                txtSendToSMS.getText().toString(),
                String.valueOf(checkSendMail.isChecked()),//sendmail checkbox val
                String.valueOf(checkSendSMS.isChecked()), //send sms check val
                String.valueOf(checkStartAtBoot.isChecked()));// start at boot val

        if (checkSendMail.isChecked()){

            Mail m=new Mail(c.getCreds(),"**** TEST ****");

            try {
                m.send();
            } catch (Exception e) {
                success=false;
                e.printStackTrace();
            }

            if (success) Toast.makeText(this, "Test message was sent to "+txtSendTo.getText().toString(),
                                                Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Failed to send testt message to "+txtSendTo.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (checkSendSMS.isChecked()){
            IncommingCallReceiver.SendSMS(txtSendToSMS.getText().toString(),
                    "MISSED CALL TEST SMS FROM "+txtDeviceID.getText().toString());
        }
    }

    void saveCreds(){
        //System.out.println("### ZXMARJAN SAVE CREDS");
        IncomingDetect.SaveCreds(new String[]{
                        txtDeviceID.getText().toString(),
                        txtSendFrom.getText().toString(),
                        txtPassword.getText().toString(),
                        txtSendTo.getText().toString(),
                        txtSendToSMS.getText().toString(),
                        String.valueOf(checkSendMail.isChecked()),
                        String.valueOf(checkSendSMS.isChecked()),
                        String.valueOf(checkStartAtBoot.isChecked())},
                this);
    }

    void loadCreds(){
        //System.out.println("### ZXMARJAN LOAD CREDS");
        IncomingDetect.loadCreds(this);
        String [] allCreds=new String[8];

        allCreds=IncomingDetect.GetCreds();

        txtDeviceID.setText(allCreds[0]);
        txtSendFrom.setText(allCreds[1]);
        txtPassword.setText(allCreds[2]);
        txtSendTo.setText(allCreds[3]);
        txtSendToSMS.setText(allCreds[4]);

        if (allCreds[5].equals("true")) {checkSendMail.setChecked(true);} else {checkSendMail.setChecked(false);}
        if (allCreds[6].equals("true")){checkSendSMS.setChecked(true);} else {checkSendSMS.setChecked(false);}
        if (allCreds[7].equals("true")){
            checkStartAtBoot.setChecked(true);
            btnStartService.setText("STOP SERVICE");
        }
        else {checkStartAtBoot.setChecked(false);}

        //Toast.makeText(this,"Missed call to GMail settings loaded!",Toast.LENGTH_SHORT).show();
    }

    public void startService(View v){
        //System.out.println("### ZXMARJAN START SERVICE");

        String buttonState=btnStartService.getText().toString();

        if (buttonState.equals("STOP SERVICE")) {
            stopService(new Intent(getBaseContext(),IncomingDetect.class));
            //System.out.println("----------------------------ZXM1 START S");
            btnStartService.setText("START SERVICE");
        } else {
            //System.out.println("----------------------------ZXM1 STOP S");
            btnStartService.setText("STOP SERVICE");
            String pass=txtPassword.getText().toString();
            /*
            IncomingDetect.SetCreds(
                    txtDeviceID.getText().toString(),//deviceid
                    txtSendFrom.getText().toString(),//email to send from
                    pass,                            //pass for sendin email
                    txtSendTo.getText().toString(),  //email to send to
                    txtSendToSMS.getText().toString(),//phone to SMS
                    String.valueOf(checkSendMail.isChecked()),//sendmail checkbox val
                    String.valueOf(checkSendSMS.isChecked()), //send sms check val
                    String.valueOf(checkStartAtBoot.isChecked()));// start at boot val
            */
            saveCreds();// gi snima i setira creds

            startService(new Intent(getBaseContext(),IncomingDetect.class));//startuva servisot sto e aktiven celo vreme
        }
    }


    @Override
    public void finish(){

        Intent data=new Intent();
        saveCreds();;
        setResult(RESULT_OK,data);
        super.finish();
    }

}

