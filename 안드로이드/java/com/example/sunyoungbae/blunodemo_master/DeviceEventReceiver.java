package com.example.sunyoungbae.blunodemo_master;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceEventReceiver extends BroadcastReceiver {
    public static String sDate;     // 저장된 날짜
    //MyDB myDB = MyDB.getInstance();
    MyInFoManager myInFoManager = new MyInFoManager();

    public DeviceEventReceiver() {
        Log.d("DeviceEventReceiver", "CREATER");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH");
        sDate = simpleDateFormat.format(date);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if( Intent.ACTION_TIME_TICK.equals(action) ) {
            // 분이 변경된 경우
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH"); // 연, 월, 일, 시간(0~23)
            String curDate = simpleDateFormat.format(date);
            if( curDate != sDate ) {    // 시간이 다르다면
                myInFoManager.set_MyInfo(sDate);
                sDate = curDate;
            }
        }
    }
}