package com.robot.xang.robot;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class sensorControl extends ActionBarActivity {
    private Button up;
    private Button down;
    private Button rigth;
    private Button left;
    private Button startsensor;
    private Button sdown;
    private Sensor sensor;
    private Button start;
    private SensorManager manager;
    private SensorEventListener slistener;
    private serviceBluetooth serviceBlue=null;
    private BluetoothDevice device;
    private Handler mHandler;
    private boolean checkl=false;
    private boolean checkr=false;
    private boolean checku=false;
    private boolean checkd=false;
    //=========================variable==================================//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_control);
        overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        up=(Button)findViewById(R.id.sup);
        down=(Button)findViewById(R.id.SDown);
        rigth=(Button)findViewById(R.id.srigth);
        left=(Button)findViewById(R.id.sleft);
        startsensor=(Button)findViewById(R.id.Break);
        sdown =(Button)findViewById(R.id.sensorDown);
        start=(Button)findViewById(R.id.starts);

       manager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }//end create

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        parcelable parcel=(parcelable)intent.getParcelableExtra(MainActivity.KEYSOCKET);
        device=parcel.device;
        slistener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    float dx=event.values[0];//values dx
                    float dy=event.values[1]; //values
                    float dz=event.values[2];
                    if (dx>=4){ //trun left
                        if (!checkl) { //check send command
                            left.setBackgroundColor(Color.GREEN);
                            rigth.setBackgroundColor(Color.WHITE);
                            up.setBackgroundColor(Color.WHITE);
                            down.setBackgroundColor(Color.WHITE);
                            sendcommand("l", serviceBlue);
                            checkl=true;
                            checkr=false;
                            checku=false;
                            checkd=false;
                        }//end if

                    }else if (dx<=-4){ //trun rigth
                        if (!checkr){
                            rigth.setBackgroundColor(Color.GREEN);
                            left.setBackgroundColor(Color.WHITE);
                            up.setBackgroundColor(Color.WHITE);
                            down.setBackgroundColor(Color.WHITE);
                            sendcommand("r",serviceBlue);
                            checkr=true;
                            checku=false;
                            checkl=false;
                            checkd=false;
                        }//end if

                    }else if ((dx>-1 || dx<1) && dz>7){ //forward
                        if (!checku){
                            up.setBackgroundColor(Color.GREEN);
                            rigth.setBackgroundColor(Color.WHITE);
                            left.setBackgroundColor(Color.WHITE);
                            down.setBackgroundColor(Color.WHITE);
                            sendcommand("u",serviceBlue);
                            checkr=false;
                            checku=true;
                            checkl=false;
                            checkd=false;
                        }else if ((dy>=4 && dx>=-1) && dz>=5){ //back
                            up.setBackgroundColor(Color.WHITE);
                            rigth.setBackgroundColor(Color.WHITE);
                            left.setBackgroundColor(Color.WHITE);
                            down.setBackgroundColor(Color.GREEN);
                            if (!checkd){
                                sendcommand("d",serviceBlue);
                                checkr=false;
                                checku=false;
                                checkl=false;
                                checkd=true;
                            }

                        }

                    }//end else if


                }//end if


            }//end sensorchange

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }//end onacccur

        };//end slistener

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == serviceBluetooth.CONNECTED) {

                    manager.registerListener(slistener,sensor,SensorManager.SENSOR_DELAY_GAME);//register sensor

                } else if (msg.what == serviceBluetooth.STOPCONNECT) {

                    Log.e("can not connect","No connect");
                }
                return true;
            }
        });//end handel

           startsensor.setOnClickListener(new View.OnClickListener() {
               @TargetApi(Build.VERSION_CODES.KITKAT)
               @Override
               public void onClick(View v) {
                    serviceBlue = new serviceBluetooth(device, mHandler);
                   serviceBlue.start();
                        }
           });//end startsensorclick

        sdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.unregisterListener(slistener);
                sendcommand("s",serviceBlue);
                up.setBackgroundColor(Color.WHITE);
                rigth.setBackgroundColor(Color.WHITE);
                left.setBackgroundColor(Color.WHITE);
                down.setBackgroundColor(Color.WHITE);
            }
        });//end sdown click

    }//end start


    @Override
    protected void onResume() {
        super.onResume();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.registerListener(slistener,sensor,SensorManager.SENSOR_DELAY_GAME);//register sensor
            }
        });//end start click


            }//end onresume


    @Override
    protected void onDestroy() {
        manager.unregisterListener(slistener);
       if (serviceBlue!=null){
           serviceBlue.cancle();
       }
        super.onDestroy();

    }//end destroy

    @Override
    protected void onPause() {
        manager.unregisterListener(slistener);
        super.onPause();

    }//end onpause

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
            Toast.makeText(sensorControl.this, "can not connect bluetooth", Toast.LENGTH_SHORT).show();
        }

    }//end function sendcommand


}//end class sensorControl
