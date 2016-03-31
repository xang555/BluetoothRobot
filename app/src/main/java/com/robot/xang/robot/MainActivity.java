package com.robot.xang.robot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    private GridView gridView;
    private TextView time;
    String[] showtitle={"Robot Bluetooth","Robot USB","About Us","Facebook FanPage"};
    int[] image={R.drawable.bluetooth,R.drawable.usb,R.drawable.about,R.drawable.facebook};
    private static final int FindbluetoothCode=12;
    public static final String KEYSOCKET="socket";
    private BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter;
    //=====================variablr==========================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robotbanner);
        overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        time=(TextView)findViewById(R.id.time);
        ImageView img=(ImageView)findViewById(R.id.banner_ani);
        AnimationDrawable animationDrawable=(AnimationDrawable)img.getBackground();
        animationDrawable.start();

      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              setContentView(R.layout.activity_main);
               gridView=(GridView)findViewById(R.id.gselect);
              LinearLayout linearLayout=(LinearLayout)findViewById(R.id.contener);
              Animation ani=new AlphaAnimation(0.0f,1.0f);
              ani.setDuration(1500);
              LayoutAnimationController animationController=new LayoutAnimationController(ani,0.5f);
              adapter adp=new adapter();
              gridView.setAdapter(adp);
              linearLayout.setLayoutAnimation(animationController);
              gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      if (position==0){
                          Intent intent=new Intent(MainActivity.this,findblutoothDevice.class);
                          startActivityForResult(intent, FindbluetoothCode);

                      }
                      if (position==1){
                          Intent intent=new Intent(MainActivity.this,Robotusb.class);
                          startActivity(intent);
                      }
                      if (position==2){
                          Intent intent=new Intent(MainActivity.this,aboutapp.class);
                          startActivity(intent);

                      }//end if

                    if (position==3){
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Nuolmakercup"));
                        startActivity(intent);

                    }//end if

                  }
              });

          }
      },5000);//end handle



    }//end create

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }//end ondestroy

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==FindbluetoothCode && resultCode==RESULT_OK){
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            device = bluetoothAdapter.getRemoteDevice(data.getStringExtra(findblutoothDevice.EXTRA_ADD));
             Intent intent=new Intent(MainActivity.this,BluetoothListcontrol.class);
               parcelable parcelable=new parcelable();
                    parcelable.device=device;
            intent.putExtra(KEYSOCKET,parcelable);
            startActivity(intent);

        }//end if

        super.onActivityResult(requestCode, resultCode, data);

    }//end onActivityresult

    //===============class adapter===============================//
    class adapter extends BaseAdapter{


        @Override
        public int getCount() {
            return showtitle.length;
        }

        @Override
        public Object getItem(int position) {
            return showtitle[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_gselect,parent,false);
             ImageView imageView=(ImageView)v.findViewById(R.id.imageselect);
            TextView title=(TextView)v.findViewById(R.id.showtitle);
            title.setText(showtitle[position]);
            imageView.setImageResource(image[position]);
                        return v;
        }
    }//end adapter



}
