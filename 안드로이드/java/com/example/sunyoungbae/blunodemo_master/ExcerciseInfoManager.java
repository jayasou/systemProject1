package com.example.sunyoungbae.blunodemo_master;

public class ExcerciseInfoManager {
    private MyDB myDB = MyDB.getInstance();

    public void set_ExInfo(String title, String url) {
        String value[] = { title, url };
        myDB.insert("ExerciseInfo", null, value);
    }

    public String get_AllExInfo() {
        return myDB.getResult("ExerciseInfo", null);
    }
}
