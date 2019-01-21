package com.example.sunyoungbae.blunodemo_master;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends BlunoLibrary {
    private MyDB myDB;
    private DeviceEventReceiver receiver;
    private Button buttonPage1;
    private Button buttonPage2;
    private Button buttonPage3;
    private Button buttonPage4;

    private Button buttonScan;
    private TextView connectText;

    long startSittingTime;
    long endSittingTime;
    long pastTime = -1;
    int sittingTime = 0;

    MyInFoManager myInFoManager;
    BluetoothLeServiceSingleton mBluetoothLeServiceSingleton = new BluetoothLeServiceSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDB = MyDB.getInstance(getApplicationContext(), "MyDB.db", 1);
        myInFoManager = new MyInFoManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPage1 = (Button)findViewById(R.id.buttonPage1);
        buttonPage2 = (Button)findViewById(R.id.buttonPage2);
        buttonPage3 = (Button) findViewById(R.id.buttonPage3);
        buttonPage4 = (Button) findViewById(R.id.buttonPage4);

        receiver = new DeviceEventReceiver();

        String curDate = getDate();
        if(!myInFoManager.isExist(curDate)) {
            int gNum = myInFoManager.get_GoalWarningNumber(DeviceEventReceiver.sDate);
            if(gNum != -1) {    // 현재 시간의 데이터가 존재하는 경우
                myInFoManager.set_MyInfo(curDate, Integer.toString(gNum) );
            }
            myInFoManager.set_MyInfo(curDate);
            DeviceEventReceiver.sDate = curDate;
        }

        onCreateProcess();
        serialBegin(115200);
        buttonScan = (Button) findViewById(R.id.buttonScan);                    //initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device
            }
        });
        connectText = (TextView) findViewById(R.id.ConnectText);
        connectText.setText("FAILED");

        startSittingTime = 0;
        endSittingTime = 0;
    }

    public void onClickPage1(View v) {
        Intent i = new Intent(getApplicationContext(), Page1.class);
        startActivity(i);
    }

    public void onClickPage2(View v) {
        Intent i = new Intent(getApplicationContext(), Page2.class);
        startActivity(i);
    }

    public void onClickPage3(View v) {
        Intent i = new Intent(getApplicationContext(), Page3.class);
        startActivity(i);
    }

    public void onClickPage4(View v) {
        Intent i = new Intent(getApplicationContext(), Page4.class);
        startActivity(i);
    }

    public MyDB getMyDB() {
        return myDB;
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

    protected void onResume(){
        super.onResume();
        System.out.println("MainActivity onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
        System.out.println("MainActivity onPause");
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
        System.out.println("MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                connectText.setText("SUCCESS");
                break;
            case isConnecting:
                connectText.setText("CONNECTING");
                break;
            case isToScan:
                connectText.setText("FAILED");
                break;
            case isScanning:
                connectText.setText("SCANNING");
                break;
            case isDisconnecting:
                connectText.setText("FAILED");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        Log.d("MainActivity", "[receiveText] \\\n" + theString);
        decisionPosture(theString);
        Toast.makeText(this, "[receiveText] \\\n" + theString ,Toast.LENGTH_SHORT).show();
    }

    public void decisionPosture(String theString) {
        long curTime = getCurTime();
        if( startSittingTime == 0 && !theString.contains("2")) {
            startSittingTime = curTime;
        } else {
            if( theString.contains("2") ) { // 안 앉아있을 때
                endSittingTime = curTime;
                sittingTime = 0;
            } else {
                Log.d("pastTime", Long.toString(pastTime));
                if( pastTime == -1 ) {
                    pastTime = curTime;
                } else {
                    DateFormat df = new SimpleDateFormat("mm");
                    String sST = df.format(pastTime);
                    String cT = df.format(curTime);
                    Log.d("MainActivity", "sSt = " + sST + "cT = " + cT);
                    if (Integer.parseInt(sST) != Integer.parseInt(cT)) {   // 분이 다르면
                        Log.d("MainActivity", "add_TotalSittingTime()");
                        myInFoManager.add_TotalSittingTime(getDate());
                        pastTime = curTime;
                        sittingTime++;  // 분 추가
                    }
                }
                if( theString.contains("0") ) { // 바르지 못한 자세
                    myInFoManager.add_TotalWarningNumber(getDate());    // 경고횟수 +1
                    mBluetoothLeService.actionNotification(true);   // 자세 경고 알림
                }
                if(sittingTime >= 50) {
                    Log.d("MainActivity", "505법칙알림");
                    mBluetoothLeService.actionNotification(false);  // 505법칙알림
                }
            }
        }
    }

    private int getDiffTime(long start, long end) { // 분단위
        long mills = end - start;
        long min = mills / 60000;
        StringBuffer diffTime = new StringBuffer();
        diffTime.append(min);
        String dif = diffTime.toString();
        return Integer.parseInt(dif.toString());
    }

    private long getCurTime() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return date.getTime();
    }
}
