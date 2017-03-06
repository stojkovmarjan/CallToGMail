package com.stoykov.maryan.calltogmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Main2Activity extends AppCompatActivity {

    TextView lblDeviceID;
    TextView lblSendFrom;
    TextView lblSendTo;
    TextView lblSendSMSTo;
    String [] creds;

    ToggleButton toggleStatus;
    CheckBox checkSendMail;
    CheckBox checkSendSMS;
    String checkStartAtBoot;

    final int REQUEST_CODE=1712;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toggleStatus=(ToggleButton)findViewById(R.id.toggleStatus);
        lblDeviceID=(TextView)findViewById(R.id.lblDeviceID);
        lblSendFrom=(TextView)findViewById(R.id.lblSendFrom);
        lblSendTo=(TextView)findViewById(R.id.lblSendTo);
        lblSendSMSTo=(TextView)findViewById(R.id.lblSendSMSTo);
        checkSendMail=(CheckBox)findViewById(R.id.checkSendGMail);
        checkSendSMS=(CheckBox)findViewById(R.id.checkSendSMS);
        //checkStartAtBoot=(CheckBox)findViewById(R.id.checkStartAtBoot);

        GetServiceStatus();

        if ((creds[1].equals("")) && (creds[1].equals("")) && creds[2].equals("") && creds[3].equals("")&&creds[4].equals("")){
            //ako nema nisto setirano odi na settings direktno
            Intent i=new Intent(this,MainActivity.class);
            startActivityForResult(i,REQUEST_CODE);
        }

    }

    /**
     * za togle buttonot, da go startuva/gase servisot
     * @param v
     */
    public void toggle_click(View v){

        if (toggleStatus.isChecked()){// ako se vkluce od kopceto

            startService(new Intent(getBaseContext(),IncomingDetect.class));// service on

            // proveri checkMail i checkSMS, edno od dvete mora da bide vkluceno
            if ((!checkSendSMS.isChecked())&&(!checkSendMail.isChecked())){
                Toast.makeText(this,"\"Send from gmail\" or \"Send SMS to\" shuld be enabled!",Toast.LENGTH_LONG).show();
                toggleStatus.setChecked(false);//service off
            } else
                {
                // ako e vkluceno za prakjanje mail a ne se setirani adresite
                    if ((checkSendMail.isChecked()) && ((creds[1].equals(""))||(creds[3].equals("")))){
                        Toast.makeText(this,"Enter both \"Send from\" and \"Send to\" e-mails!",Toast.LENGTH_LONG).show();
                        toggleStatus.setChecked(false);//service off
                    }
                 // ako e vkluceno za prakjanje SMS a ne e vnesen broj sto prima
                    if ((checkSendSMS.isChecked()) &&  ((creds[4].equals("")))){
                        Toast.makeText(this,"Enter SMS receiving phone number!",Toast.LENGTH_LONG).show();
                        toggleStatus.setChecked(false);//service off
                    }
                }


        }

        //ako se iskluce
        if (!toggleStatus.isChecked()) {
            stopService(new Intent(getBaseContext(),IncomingDetect.class));
            //Toast.makeText(this,"Service stopped",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * za checkBoxovite da gi fatam na promena i da ja snimam vo setinzite
     * @param v
     */
    public void checkOn_Click(View v){

        creds[5]=String.valueOf(checkSendMail.isChecked());

        creds[6]=String.valueOf(checkSendSMS.isChecked());

        creds[7]=checkStartAtBoot;

        IncomingDetect.SaveCreds(creds,this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: 16.1.2017  : Povikaj settings ...VIDI
            // Preku metod sto ima Intent sto ke vrate rezultat
            Intent i=new Intent(this,MainActivity.class);
            startActivityForResult(i,REQUEST_CODE);
            //startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // za da znam deka vrednostite se podeseni vo setup
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode==RESULT_OK && requestCode==REQUEST_CODE){
            GetServiceStatus();
        }
    }

    /**
     * pri start i pri vrakjanje os settings
     * gi zima vrednostite i gi prikazuva
     */
    void GetServiceStatus(){

        toggleStatus.setChecked(IncomingDetect.isDetectingActive());

        IncomingDetect.loadCreds(this);

        creds=IncomingDetect.GetCreds();

        lblDeviceID.setText(creds[0]);
        lblSendFrom.setText(creds[1]+"@gmail.com");
        lblSendTo.setText(creds[3]);
        lblSendSMSTo.setText(creds[4]);

        if (creds[5].equals("true")){
            checkSendMail.setChecked(true);
        } else checkSendMail.setChecked(false);

        if (creds[6].equals("true")){
            checkSendSMS.setChecked(true );
        } else checkSendSMS.setChecked(false );

        checkStartAtBoot=creds[7];

    }

}
