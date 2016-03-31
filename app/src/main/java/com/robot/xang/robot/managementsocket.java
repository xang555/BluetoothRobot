package com.robot.xang.robot;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by xang on 23/8/2015.
 */

class managementsocket extends Thread{
    private InputStream inputStream;
    private OutputStream outputStream;
    private String data;
    private BluetoothSocket socket;
    public managementsocket(String data,BluetoothSocket socket){
        this.data=data;
        this.socket=socket;
        try {
            inputStream=socket.getInputStream();
            outputStream=socket.getOutputStream();
        } catch (IOException e) {
            Log.e("BLUETOOTH", "error in get stream");
        }

    }

    @Override
    public  void run() {
        write(data);

    }//end run

//============write command to hc-05==========================//
    public void write(String data){
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//end write

//===============read data from hc-05================================//
    public String read(){
        byte[] b=new byte[1024];
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
       String line=null;
        try {
            while ((line=reader.readLine())!=null)
                return line;
                    } catch (IOException e) {
            e.printStackTrace();
            try {
                reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }//end read



}
