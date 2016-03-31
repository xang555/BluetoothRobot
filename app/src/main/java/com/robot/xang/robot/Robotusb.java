package com.robot.xang.robot;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Robotusb extends ActionBarActivity {
    private TextView checkusbconnect;
    private Button up;
    private Button down;
    private Button left;
    private Button rigth;
    private UsbAccessory usbAccessory;
    private UsbManager usbManager;
    private ParcelFileDescriptor parcelFileDescriptor;
    private String USB_PERMISSION="com.robot.xang.robot.usb.permission";
    private PendingIntent pendingIntent;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static final String Tag="RobotUSB";
    private Boolean checkpermission=false;
   private BroadcastReceiver receiver;
    //===============================variable===================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robotusb);
        checkusbconnect=(TextView)findViewById(R.id.chusbconnect);
        up=(Button)findViewById(R.id.usbup);
        down=(Button)findViewById(R.id.usbdown);
        left=(Button)findViewById(R.id.usbleft);
        rigth=(Button)findViewById(R.id.usbrigth);
        usbManager=(UsbManager)getSystemService(USB_SERVICE);
        pendingIntent=PendingIntent.getBroadcast(this,100,new Intent(USB_PERMISSION),0);
        IntentFilter filter=new IntentFilter(USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);

        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(USB_PERMISSION)){
                    usbAccessory=(UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED,false)){
                        if (usbAccessory!=null){
                            openaccesory(usbAccessory);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    checkusbconnect.setText("connect to "+usbAccessory.getModel());
                                }
                            });
                        }
                           //do owrk
                                    checkpermission=false;
                    }

                }else if (intent.getAction().equals(UsbManager.ACTION_USB_ACCESSORY_DETACHED)){
                            closeaccesory();
                    //end work

                }


            }
        };//end broadcases

        registerReceiver(receiver,filter);



    }


    @Override
    protected void onResume() {
        super.onResume();
        UsbAccessory[] usblist=usbManager.getAccessoryList();
        UsbAccessory usbAcc=usblist==null? null:usblist[0];
        if (usbAcc!=null){
           if (usbManager.hasPermission(usbAcc)){
               openaccesory(usbAcc);

           }else{

               synchronized (receiver){
                  if (!checkpermission){
                      usbManager.requestPermission(usbAcc,pendingIntent);
                  }

               }

           }


        }
//=============================== work for accessory===================================//
                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            writebayeTostream("u");
                        }catch (Exception e){
                            Toast.makeText(Robotusb.this,"can not send data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            writebayeTostream("d");
                        }catch (Exception e){
                            Toast.makeText(Robotusb.this,"can not send data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            writebayeTostream("l");
                        }catch (Exception e){
                            Toast.makeText(Robotusb.this,"can not send data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                rigth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            writebayeTostream("r");
                        }catch (Exception e){
                            Toast.makeText(Robotusb.this,"can not send data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //==============================================================================================================
    }//end resum


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        closeaccesory();
    }


    public void openaccesory(UsbAccessory accessory){
        parcelFileDescriptor=usbManager.openAccessory(accessory);
        if (parcelFileDescriptor!=null){
            FileDescriptor descriptor=parcelFileDescriptor.getFileDescriptor();
            inputStream=new FileInputStream(descriptor);
            outputStream=new FileOutputStream(descriptor);
            Log.e(Tag,"open accesory successfully");
        }else{
            Log.e(Tag,"can not open accesory");
        }

    }//end open accesory

    public void writebayeTostream(String comand){
        try {
            outputStream.write(comand.getBytes());
        } catch (IOException e) {
            Log.e(Tag,"can not write baye to stream");
        }


    }

    public void closeaccesory(){
        try {
            if (usbAccessory!=null){
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            Log.e(Tag,"can can not close accessory");
        }

    }



}
