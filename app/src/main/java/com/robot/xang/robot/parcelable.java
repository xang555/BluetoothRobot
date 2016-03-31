package com.robot.xang.robot;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xang on 10/9/2015.
 */
public class parcelable implements Parcelable {
        public BluetoothDevice device;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, 0);
    }

    public parcelable() {

    }

    protected parcelable(Parcel in) {
        this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    public static final Creator<parcelable> CREATOR = new Creator<parcelable>() {
        public parcelable createFromParcel(Parcel source) {
            return new parcelable(source);
        }

        public parcelable[] newArray(int size) {
            return new parcelable[size];
        }
    };


}
