package com.example.sunyoungbae.blunodemo_master;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Page1 extends Activity {
    TextView GoalWNumText;
    TextView TotalWNumText;
    TextView SittingTime;
    ImageView imageView;

    MyInFoManager myInFoManager = new MyInFoManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);

        String curDate = getDate();
        if(!myInFoManager.isExist(curDate)) {
            int gNum = myInFoManager.get_GoalWarningNumber(DeviceEventReceiver.sDate);
            if(gNum != -1) {    // 현재 시간의 데이터가 존재하는 경우
                myInFoManager.set_MyInfo(curDate, Integer.toString(gNum) );
            }
            myInFoManager.set_MyInfo(curDate);
            DeviceEventReceiver.sDate = curDate;
        }

        GoalWNumText = (TextView) findViewById(R.id.GoalWNumText);
        TotalWNumText = (TextView) findViewById(R.id.TotalWNumText);
        SittingTime = (TextView) findViewById(R.id.SittingTime);
        imageView = (ImageView) findViewById(R.id.ImageView);
        GoalWNumText.setText(myInFoManager.get_GoalWarningNumber(curDate) + " 회");
        TotalWNumText.setText(myInFoManager.get_TotalWarningNumber(curDate) + " 회");

        // 하루동안 앉은 시간 구하기
        String splitDate[] = curDate.split("-");
        int min = 0;    // 분
        for( int i = 0; i < 24 ; ++i ) {
            String searchDate;
            if( i < 10 ) {
                searchDate = splitDate[0] + "-0" + Integer.toString(i);
            } else {
                searchDate = splitDate[0] + "-" + Integer.toString(i);
            }
            int result = myInFoManager.get_TotalSittingTime(searchDate);
            if(result != -1) {
                min += result;
            }
        }
        int hour = min/60;
        min = min%60;
        SittingTime.setText(Integer.toString(hour) + "시간 " + Integer.toString(min) + "분");

        // 목표 평가
        switch ( myInFoManager.get_GoalEvaluation(curDate) ) {
            case 1:     // Good
                imageView.setImageResource(R.drawable.ic_good);
                break;
            case 0:     // So-so
                imageView.setImageResource(R.drawable.ic_soso);
                break;
            case -1:    // Bad
                imageView.setImageResource(R.drawable.ic_bad);
                break;
        }
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