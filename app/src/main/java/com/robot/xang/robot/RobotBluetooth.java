package com.robot.xang.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class RobotBluetooth extends ActionBarActivity {
private Button connect;
    private Button rigth;
    private Button left;
    private Button up;
    private Button down;
    private Button stop;
    private TextView txtstat;
    private BluetoothAdapter bluetoothAdapter;
    public static  final int BLUESTART=1;
    public static final int BLUEFIND=2;
    private BluetoothDevice device;
    private Handler mHandler;
    private serviceBluetooth serviceB=null;
    private boolean checku=false;
    private boolean checkl=false;
    private boolean checkr=false;
    private boolean checkd=false;
 //====================variable========================================//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_bluetooth);
        overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        connect=(Button)findViewById(R.id.connect);
        txtstat=(TextView)findViewById(R.id.txtstat);
        left=(Button)findViewById(R.id.left);
        rigth=(Button)findViewById(R.id.trunrigth);
        up=(Button)findViewById(R.id.up);
        down=(Button)findViewById(R.id.down);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();



    }//end create


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        parcelable parcel=(parcelable)intent.getParcelableExtra(MainActivity.KEYSOCKET);
        device=parcel.device;
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == serviceBluetooth.CONNECTED) {
                    txtstat.setText("Connect to " + device.getName());
                } else if (msg.what == serviceBluetooth.STOPCONNECT) {
                    txtstat.setText("unnableConnect");
                }
                return true;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceB = new serviceBluetooth(device, mHandler);
                serviceB.start();
            }
        });


    }//end start


    @Override
    protected void onResume() {
        super.onResume();
//==============================================left click==================================================//
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                        if (!checkl){
                            sendcommand("l",serviceB);
                            checkl=true;
                            checkd=false;
                            checkr=false;
                            checku=false;
                        }


                }else if (event.getAction()==MotionEvent.ACTION_UP){
                  sendcommand("s",serviceB);
                    checkl=false;
                    checkd=false;
                    checkr=false;
                    checku=false;
                }
                return false;
            }
        });

//=============================rigth click===================================================//
        rigth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                  if (!checkr){
                      sendcommand("r",serviceB);
                      checkl=false;
                      checkd=false;
                      checkr=true;
                      checku=false;
                  }
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                   sendcommand("s",serviceB);
                    checkl=false;
                    checkd=false;
                    checkr=false;
                    checku=false;
                }
                return false;
            }
        });

//===============================================up click===============================================================//

        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                  if (!checku){
                      sendcommand("u",serviceB);
                      checkl=false;
                      checkd=false;
                      checkr=false;
                      checku=true;
                  }
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                   sendcommand("s",serviceB);
                    checkl=false;
                    checkd=false;
                    checkr=false;
                    checku=false;

                }
                return false;
            }
        });

//===========================================================down click=============================================//

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){

                    if (!checkd){
                        sendcommand("d",serviceB);
                        checkl=false;
                        checkd=true;
                        checkr=false;
                        checku=false;
                    }
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                  sendcommand("s",serviceB);
                    checkl=false;
                    checkd=false;
                    checkr=false;
                    checku=false;
                }
                return false;
            }

        });




    }//end resume



    @Override
    protected void onDestroy() {
      if (serviceB!=null){
          serviceB.cancle();
      }
        super.onDestroy();
    }

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
            Toast.makeText(RobotBluetooth.this, "can not connect bluetooth", Toast.LENGTH_SHORT).show();
        }

    }//end function sendcommand


}//end class RobotBluetooth
