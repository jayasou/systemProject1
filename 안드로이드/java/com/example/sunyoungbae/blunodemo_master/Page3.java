package com.example.sunyoungbae.blunodemo_master;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class Page3 extends Activity {
    GridView gridView;

    ExcerciseInfoManager exInfoManager = new ExcerciseInfoManager();

    ArrayList<String> urlArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);

        Integer[] picArr = { R.drawable.stretching1, R.drawable.stretching2 };

        String result = exInfoManager.get_AllExInfo();
        String array[] = result.split("\\\n");

        for (int i = 0; i < array.length; ++i) {
            String tmp[] = array[i].split(",");
            urlArr.add(i, tmp[tmp.length-1]);
        }

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new MyImageAdapter(this, picArr));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(urlArr.get(position));
                i.setData(u);
                startActivity(i);
            }
        });
    }
}