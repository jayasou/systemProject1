package com.example.sunyoungbae.blunodemo_master;

/**
 * Created by SunYoungBae on 2016-11-17.
 */

public class BluetoothLeServiceSingleton {
    private static BluetoothLeService bLs = new BluetoothLeService();
    public static BluetoothLeService getInstance() {
        return bLs;
    }
}
