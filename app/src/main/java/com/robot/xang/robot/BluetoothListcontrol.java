package com.robot.xang.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class BluetoothListcontrol extends ActionBarActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private Intent intent;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_bluetooth_listcontrol);
            overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        toolbar=(Toolbar)findViewById(R.id.listToolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("BluetoothControlType");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.listcontrol);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter adapter=new adapter();
        recyclerView.setAdapter(adapter);


    }//end ncreate


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("BluetoothControlType")){
                       finish();
            overridePendingTransition(R.anim.entertransition,R.anim.entertransition);
                   }

        return super.onOptionsItemSelected(item);

    }//end onoptionitemselect

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        parcelable parcel=(parcelable)intent.getParcelableExtra(MainActivity.KEYSOCKET);
        device=parcel.device;
        /*
        serviceBluetooth serviceblue=new serviceBluetooth(device);
        serviceblue.start();*/

    }

    @Override
    public void onBackPressed() {
               super.onBackPressed();
        overridePendingTransition(R.anim.entertransition,R.anim.entertransition);
    }

    //==============class recycleview adapter==============================//
    class adapter extends RecyclerView.Adapter<adapter.holder>{
        String[] title={"Basic Control","Sensor Control","Touchscreen Control","Sound Control"};
            int[] icon={R.drawable.basic,R.drawable.manetic,R.drawable.touchscreen,R.drawable.voidsearch};

        @Override
        public holder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleadapter,viewGroup,false);
            holder vh=new holder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(holder holder, int i) {
                holder.title.setText(title[i]);
                holder.icon.setImageResource(icon[i]);
        }

        @Override
        public int getItemCount() {
            return title.length;
        }
            //===========class holder================//
        public class holder extends RecyclerView.ViewHolder implements View.OnClickListener{
                ImageView icon;
                TextView title;
            public holder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                icon=(ImageView)itemView.findViewById(R.id.iconbluetoothcontrol);
                title=(TextView)itemView.findViewById(R.id.titlebluetoothcontrol);

                            }

                @Override
                public void onClick(View v) {
                        int position=getPosition();
                    if (position==0){
                        Intent intent=new Intent(BluetoothListcontrol.this,RobotBluetooth.class);
                        parcelable parcelable=new parcelable();
                        parcelable.device=device;
                        intent.putExtra(MainActivity.KEYSOCKET,parcelable);
                        startActivity(intent);

                    }else if (position==1){

                        Intent in=new Intent(BluetoothListcontrol.this,sensorControl.class);
                        parcelable parcel=new parcelable();
                        parcel.device=device;
                        in.putExtra(MainActivity.KEYSOCKET,parcel);
                        startActivity(in);

                    }else if (position==2){
                        Intent in=new Intent(BluetoothListcontrol.this,screenControll.class);
                        parcelable parcel=new parcelable();
                        parcel.device=device;
                        in.putExtra(MainActivity.KEYSOCKET,parcel);
                        startActivity(in);

                    }else if (position==3){
                        Intent in=new Intent(BluetoothListcontrol.this,soundControl.class);
                        parcelable parcel=new parcelable();
                        parcel.device=device;
                        in.putExtra(MainActivity.KEYSOCKET,parcel);
                        startActivity(in);
                    }//end if


                }//end onclick


            }//end class holder


    }//end class adapter




}
