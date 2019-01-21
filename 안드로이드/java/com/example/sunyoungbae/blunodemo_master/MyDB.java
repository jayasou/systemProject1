package com.example.sunyoungbae.blunodemo_master;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDB extends SQLiteOpenHelper {
    private static MyDB myDB = null;
    private static SQLiteDatabase db;
    public int exInfo_cnt = 0;

    public static MyDB getInstance(Context context, String name, int version) {
        if( myDB == null ) {
            myDB = new MyDB(context, name, version);
            Log.d("MyDB", "create");
        }
        return myDB;
    }

    public static MyDB getInstance() {
        if( myDB != null ) {
            return  myDB;
        }
        return null;
    }

    private  MyDB(Context context, String name, int version) {
        super(context, name, null, version);
        db = this.getWritableDatabase();
        Log.d("MyDB", "creater");
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        Log.d("MyDB", "onCreate() start");
        db.execSQL("CREATE TABLE MyInfo (ID TEXT PRIMARY KEY , GoalWarningNumber INTEGER, TotalWarningNumber INTEGER, TotalSittingTime INTEGER);");
        db.execSQL("CREATE TABLE ExerciseInfo (ID INTEGER PRIMARY KEY , Title TEXT, URL TEXT );");
        initDB(db);
        Log.d("MyDB", "onCreate() end");
    }

    public void initDB(SQLiteDatabase db) {

        // 날짜는 현재 날짜로 고정
        // 현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 출력될 포맷 설정
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH");
        String stringDate = simpleDateFormat.format(date);

        db.execSQL("INSERT INTO MyInfo (ID, GoalWarningNumber, TotalWarningNumber, TotalSittingTime)" +
                "\n VALUES( '" + stringDate + "', ' 30', '0', '0');");
        db.execSQL("INSERT INTO ExerciseInfo (ID, Title, URL)" +
                "\n VALUES( '" + ++exInfo_cnt + "', '회사에서 하기 좋은 스트레칭_1', 'https://youtu.be/40spd1w5Cw0');");
        db.execSQL("INSERT INTO ExerciseInfo (ID, Title, URL)" +
                "\n VALUES( '" + ++exInfo_cnt + "', '회사에서 하기 좋은 스트레칭_2', 'https://youtu.be/-JzaMksAeew');");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String tableName, String date, String value[]) {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("\n INSERT INTO " + tableName);
        switch (tableName) {
            case "MyInfo":
                sqlStr.append("(ID, GoalWarningNumber, TotalWarningNumber, TotalSittingTime)");
                sqlStr.append("\n VALUES( '" + date + "', '" + value[0] + "', '" + value[1] + "', '" + value[2] + "');");
                break;
            case "ExerciseInfo":
                exInfo_cnt++;
                sqlStr.append("(ID, Title, URL)");
                sqlStr.append("\n VALUES( '" + exInfo_cnt + "', '" + value[0] + "', '" + value[1] + "');");
                break;
            default:
                return;
        }
        db.execSQL(sqlStr.toString());
    }

    public void update(String tableName, String fieldToChange, String dataToChange, String id) {
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("\n UPDATE " + tableName + " SET " + fieldToChange + "=" + dataToChange + " WHERE ID='" + id + "';");
        db.execSQL(sqlStr.toString());
    }

    public String getResult(String tableName, String id) {
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력

        if(tableName == "ExerciseInfo")
        {
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName , null);
            while (cursor.moveToNext()) {
                result += cursor.getString(0)
                        + ","
                        + cursor.getString(1)
                        + ","
                        + cursor.getString(2)
                        + "\n";
            }
        }
        else {
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE ID = '" + id + "';", null);
            //Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
            while (cursor.moveToNext()) {
                result += cursor.getString(0)
                        + ","
                        + cursor.getString(1)
                        + ","
                        + cursor.getString(2)
                        + ","
                        + cursor.getString(3)
                        + "\n";
            }
        }
        return result;
    }

    public String getLastResult(String tableName) {     // 마지막 행의 정보 read
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + ";", null );
        cursor.moveToLast();
        result += cursor.getString(0)
                + ","
                + cursor.getString(1)
                + ","
                + cursor.getString(2)
                + ","
                + cursor.getString(3)
                + "\n";
        return result;
    }

    public String getId(String tableName) {
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT ID FROM " + tableName , null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) + "\n";
        }
        return result;
    }
}