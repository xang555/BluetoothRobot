package com.robot.xang.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by xang on 23/8/2015.
 */
public class serviceBluetooth extends Thread {
    //============================================================================================
    private BluetoothSocket socket;
    private BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice device;
    private static final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Handler mHandler=null;
    public static final int CONNECTED=200;
    public static final int STOPCONNECT=404;
    //=============================================================================================

     public serviceBluetooth(BluetoothDevice device,Handler mHandler){
        this.device=device;
        this.mHandler=mHandler;
        try {
            BluetoothSocket socket=device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            this.socket=socket;
        } catch (IOException e) {
            Log.e("  error","in createsocket");
        }

    }//end servicebluetooth

    public serviceBluetooth(BluetoothDevice device){
        this.device=device;
        try {
            BluetoothSocket socket=device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
             this.socket=socket;
        } catch (IOException e) {
            Log.e("  error","in createsocket");
        }
    }//end construc

    @Override
    public synchronized void run() {

        adapter.cancelDiscovery();
        try {
            socket.connect();
               if (socket.isConnected()){
                   if (mHandler!=null) {
                       mHandler.obtainMessage(CONNECTED).sendToTarget();
                   }

               }else{
                   if (mHandler!=null) {
                       mHandler.obtainMessage(STOPCONNECT).sendToTarget();
                   }
               }


        } catch (IOException e) {

            Log.e("error","in connect socket");
        }

    }//end run


    public synchronized void cancle(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public BluetoothSocket getSocket(){
    return socket;
}


}
