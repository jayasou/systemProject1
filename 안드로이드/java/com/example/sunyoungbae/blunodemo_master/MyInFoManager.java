package com.example.sunyoungbae.blunodemo_master;

import android.util.Log;

public class MyInFoManager {
    private MyDB myDB = MyDB.getInstance();
    MyInFoManager() {
        Log.d("MyInFoManager()", "create");
    }

    public void set_MyInfo(String date, String num) {
        String value[] = {num, "0", "0"};
        myDB.insert("MyInfo", date, value);
    }

    public void set_MyInfo(String date) {
        String result = myDB.getLastResult("MyInfo");
        String tmp[] = result.split(",");
        if( tmp.length != 1 ) { // 마지막 데이터가 존재하면
            set_MyInfo(date, tmp[1]);
        }
    }

    public void update_GoalWarningNumber(String date, String num) {
        myDB.update("MyInfo", "GoalWarningNumber", num, date);
    }

    public void update_TotalSittingTime(String date, String num) {
        myDB.update("MyInfo", "TotalSittingTime", num, date);
    }

    public void update_TotalWarningNumber(String date, String num) {
        myDB.update("MyInfo", "TotalWarningNumber", num, date);
    }

    public void add_TotalWarningNumber(String date) {
        int tWNumber = get_TotalWarningNumber(date);
        myDB.update("MyInfo", "TotalWarningNumber", Integer.toString(tWNumber+1), date);
    }

    public void add_TotalSittingTime(String date) {
        int tST = get_TotalSittingTime(date);
        update_TotalSittingTime(date, Integer.toString(tST+1));
    }

    public int get_GoalWarningNumber(String date) {
        String result = myDB.getResult("MyInfo", date);
        String tmp[] = result.split(",");
        if( tmp.length != 1 ) return Integer.parseInt(tmp[1]);
        return -1;
    }

    public int get_TotalWarningNumber(String date) {
        String result = myDB.getResult("MyInfo", date);
        String tmp[] = result.split(",");
        return Integer.parseInt(tmp[2]);
    }

    public int get_TotalSittingTime(String date) {
        String result = myDB.getResult("MyInfo", date);
        Log.d("MyInfoManager", "get_TotalSittingTime: " + result);
        String tmp[] = result.split(",");
        if( tmp.length > 1 ) {
            String num = tmp[3].trim(); // 공백제거
            return Integer.parseInt(num);
        } else {
            return -1;
        }
    }

    public int get_GoalEvaluation(String date) {
        int tWN = get_TotalWarningNumber(date);
        int gWN = get_GoalWarningNumber(date);
        double percent = (double) tWN / gWN;

        if (percent >= 0.6) {   // Bad : 60% 이상
            return -1;
        } else if (percent > 0.4) {    // So-so : 40% 초과~60% 미만
            return 0;
        } else {    // Good : 40% 이하
            return 1;
        }
    }

    public boolean isExist(String date) {
        boolean result = false;
        String id = myDB.getId("MyInfo");
        String tmp[] = id.split("\\\n");
        for( int i = 0 ; i < tmp.length ; ++i ) {
            if( tmp[i].equals(date) ) result = true;
        }
        return result;
    }

    public String getAllID() {
        return myDB.getId("MyInfo");
    }
}
