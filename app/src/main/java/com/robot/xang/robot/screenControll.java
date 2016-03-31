package com.robot.xang.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class screenControll extends ActionBarActivity {
private GestureDetector detector;
    private static final int LARGEMOTION=50;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private Handler mHandler=null;
    private serviceBluetooth serviceB=null;
    private GestureOverlayView gestureOverlayView;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_controll);
        gestureOverlayView=(GestureOverlayView)findViewById(R.id.gestureOverlayView);
         overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        Intent intent=getIntent();
        parcelable parcel=(parcelable)intent.getParcelableExtra(MainActivity.KEYSOCKET);
        device=parcel.device;
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == serviceBluetooth.CONNECTED) {
                    Toast.makeText(screenControll.this,"Connect to Robot",Toast.LENGTH_SHORT).show();
                } else if (msg.what == serviceBluetooth.STOPCONNECT) {
                    Toast.makeText(screenControll.this,"unnable to Connect Robot",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });//end handle

        serviceB = new serviceBluetooth(device, mHandler);
        serviceB.start();


    }//end oncreate


    @Override
    protected void onStart() {
        super.onStart();
        //===============================when thouch===========================================//
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //trun left
                if(e1.getX()-e2.getX()>LARGEMOTION){
                    //do left
                    sendcommand("l",serviceB);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendcommand("s",serviceB);
                        }
                    },250);

                }else if ((e2.getX()-e1.getX())>LARGEMOTION){ //trun rigth
                    sendcommand("r",serviceB);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendcommand("s",serviceB);
                        }
                    },250);
                    //do rigth
                }else if((e1.getY()-e2.getY()>LARGEMOTION)){ //forward
                    //do forward
                    sendcommand("u",serviceB);

                }else if ((e2.getY()-e1.getY())>LARGEMOTION){ //down
                    //do down
                    sendcommand("d",serviceB);
                }

                return true;
            }//end onfling

            @Override
            public boolean onDown(MotionEvent e) {
                //do stop
                sendcommand("s",serviceB);
                return true;
            }//end ondown

        });//end detector

          gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return detector.onTouchEvent(event);
                    }
                });//end gestureoverlayviewtouch

    }//end onstart


    @Override
    protected void onDestroy() {
        if (serviceB!=null){
            serviceB.cancle();
        }

        super.onDestroy();
            }//end ondestroy

    @Override
    public void onBackPressed() {
               super.onBackPressed();
        overridePendingTransition(R.anim.entertransition,R.anim.entertransition);

    }//end onbackpressed


    //====================function send command robot=========================//

    private void sendcommand(String Command,serviceBluetooth serviceblue) {
        try {
            managementsocket mag = new managementsocket(Command, serviceblue.getSocket());
            mag.start();
        } catch (Exception e) {
            Toast.makeText(screenControll.this, "can not connect bluetooth", Toast.LENGTH_SHORT).show();
        }

    }//end sendcomand


}//end class
