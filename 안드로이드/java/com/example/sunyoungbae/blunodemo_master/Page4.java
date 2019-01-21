package com.example.sunyoungbae.blunodemo_master;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class Page4  extends Activity {
    private Button numButton;
    private TextView numText;

    MyInFoManager myInFoManager = new MyInFoManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4);

        String curDate = getDate();
        if(!myInFoManager.isExist(curDate)) {
            int gNum = myInFoManager.get_GoalWarningNumber(DeviceEventReceiver.sDate);
            if(gNum != -1) {    // 현재 시간의 데이터가 존재하는 경우
                myInFoManager.set_MyInfo(curDate, Integer.toString(gNum) );
            }
            myInFoManager.set_MyInfo(curDate);
            DeviceEventReceiver.sDate = curDate;
        }

        numText = (TextView) findViewById(R.id.NumText);
        numText.setText(myInFoManager.get_GoalWarningNumber(getDate()) + " 회");

        // 알림창 start
        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        aDialog.setTitle("변경할 목표 경고 횟수");
        final EditText et = new EditText(this);
        et.setInputType(TYPE_CLASS_NUMBER);
        aDialog.setView(et);

        aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String value = et.getText().toString(); // 값 받기
                myInFoManager.update_GoalWarningNumber(getDate(), value);
                numText.setText(myInFoManager.get_GoalWarningNumber(getDate()) + " 회");
                dialog.dismiss();   // 닫기
            }
        });
        aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog ad = aDialog.create();
        // 알림창 end

        numButton = (Button) findViewById(R.id.NumButton);
        numButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ad.show();
            }
        });
    }

    public String getDate() {
        // 날짜는 현재 날짜로 고정
        // 현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 출력될 포맷 설정
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH");
        return simpleDateFormat.format(date);
    }
}