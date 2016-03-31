package com.robot.xang.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;


public class findblutoothDevice extends ActionBarActivity {
    private ListView listView;
    private ArrayList<String> arraydata=new ArrayList<String>();
    private ArrayAdapter<String> listadapter;
    private Button search;
    Handler mHandler=new Handler();
    private BroadcastReceiver receiver;
   private BluetoothAdapter adapter;
 public static final String EXTRA_ADD="ADD";
   public static  final int BLUESTART=1;
    public static final int BLUEFIND=2;
    //=========================variable===========================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findblutooth_device);
        listView=(ListView)findViewById(R.id.showbluetooth);
        search=(Button)findViewById(R.id.search);
         adapter=BluetoothAdapter.getDefaultAdapter();
       listadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        Set<BluetoothDevice> setBluetoothdevice=adapter.getBondedDevices();
        if (setBluetoothdevice!=null){
            for (BluetoothDevice device:setBluetoothdevice){
                 arraydata.add(device.getAddress());
                listadapter.add(device.getName());

            }

                    }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.startDiscovery();
                search.setText("search.....");
            }
        });

          receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice device=(BluetoothDevice)intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    arraydata.add(device.getAddress());
                    listadapter.add(device.getName());

                }else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                    adapter.cancelDiscovery();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            search.setText("search device");
                        }
                    });
                }

            }
        };//end broadcast


        final IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        listView.setAdapter(listadapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent();
               intent.putExtra(EXTRA_ADD,arraydata.get(position));
               setResult(RESULT_OK,intent);
               finish();
           }

       });




    }//end create


    @Override
    protected void onStart() {
        super.onStart();
        startbluetooth();
    }//end start

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        adapter.cancelDiscovery();

    }//end destroy

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==BLUESTART && resultCode==RESULT_CANCELED){
            setResult(RESULT_CANCELED);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }//end activityresult


    //=================function start bluetooth=====================//

    public void startbluetooth(){
            if (!adapter.isEnabled()){
            Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,BLUESTART);
        }

    }//end start bluetooth


}
