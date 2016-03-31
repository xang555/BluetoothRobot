package com.robot.xang.robot;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class soundControl extends Activity {
    private BluetoothDevice device=null;
       private int CODE_SEECH=1;
    private Handler mHandler=null;
    private serviceBluetooth serviceB=null;
    //======================variable==========================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_control);
        overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        Intent intent=getIntent();
        parcelable parcel=(parcelable)intent.getParcelableExtra(MainActivity.KEYSOCKET);
        device=parcel.device;


    }//end oncreate

    @Override
    protected void onStart() {
        super.onStart();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == serviceBluetooth.CONNECTED) {
                    Toast.makeText(soundControl.this,"Connect to Robot",Toast.LENGTH_SHORT).show();
                } else if (msg.what == serviceBluetooth.STOPCONNECT) {
                    Toast.makeText(soundControl.this,"unnable to Connect Robot",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });//end handle



    }//end start

    public void msound(View view){

        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"please speech to controll\n" +
                "Forward=reobot forward\n" +
                "Down=robot down\n" +
                "right=Robot right\n" +
                "left=robot left");
        startActivityForResult(intent,CODE_SEECH);

    }//end msound click

public void connect(View view){
    serviceB = new serviceBluetooth(device, mHandler);
    serviceB.start();

}//end connect

public void stop(View view){
   // Toast.makeText(soundControl.this, "stop", Toast.LENGTH_SHORT).show();
    sendcommand("s",serviceB);

}//end stop

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==CODE_SEECH && resultCode==RESULT_OK){
            ArrayList<String> list=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (String command:list){

              if (command.startsWith("f") ||command.startsWith("F")){
                        //forwatr
                  sendcommand("u",serviceB);
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          sendcommand("s",serviceB);
                      }
                  },5000);
                return;
              }else if (command.startsWith("r")|| command.startsWith("R")){
                 //Toast.makeText(soundControl.this, "right", Toast.LENGTH_SHORT).show();
                  //rigth
                  sendcommand("r",serviceB);
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          sendcommand("s",serviceB);
                      }
                  },500);

                 return;
              }else if (command.startsWith("d")|| command.startsWith("D")){
                 // Toast.makeText(soundControl.this, "down", Toast.LENGTH_SHORT).show();
                  //left
                  sendcommand("d",serviceB);
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          sendcommand("s",serviceB);
                      }
                  },5000);
                  return;
              }else if (command.startsWith("l")||command.startsWith("L")){

                 // Toast.makeText(soundControl.this, "left", Toast.LENGTH_SHORT).show();
                  //left
                  sendcommand("l",serviceB);
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          sendcommand("s",serviceB);
                      }
                  },500);
                      return;
              }else if (command.startsWith("s")||command.startsWith("S")){

                sendcommand("s",serviceB);
                 return;
              }//end if



            }//end for



        }//end if


        super.onActivityResult(requestCode, resultCode, data);
    }//end onactivityresult

     @Override
    protected void onDestroy() {
        if (serviceB!=null){
            serviceB.cancle();
        }//end if

        super.onDestroy();
    }//end ondestroy

    @Override
    public void onBackPressed() {
               super.onBackPressed();
        overridePendingTransition(R.anim.entertransition,R.anim.entertransition);
    }

    //================================send command==============================================================
    private void sendcommand(String Command,serviceBluetooth serviceblue){
        try{
            managementsocket mag=new managementsocket(Command,serviceblue.getSocket());
            mag.start();
        }catch (Exception e){
            Toast.makeText(soundControl.this, "can not connect bluetooth", Toast.LENGTH_SHORT).show();
        }

    }//end function sendcommand


}//end class soundControl
