package com.robot.xang.robot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by xang on 22/8/2015.
 */
public class vpagerAdapter extends FragmentPagerAdapter {


    public vpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return bluetoothControl.newInstance("","");
        }else if (position==1){
            return usbControl.newInstance("","");
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
