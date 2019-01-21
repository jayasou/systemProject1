package com.example.sunyoungbae.blunodemo_master;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Page2 extends Activity {
    public static final String TAG = "BAR_GRAPH_ACTIVITY";
    private ViewGroup layoutGraphView = null;
    MyInFoManager myInFoManager = new MyInFoManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphview);

        layoutGraphView = (ViewGroup) findViewById(R.id.layoutGraphView);
        //TextView text1 = findViewById(R.id.textView1);
        layoutGraphView.addView (new BarGraphView(this , createBarGraphVO()));
    }
    private void getDay() {

    }

    private void getSensor() {

    }


    private BarGraphVO createBarGraphVO (){
        BarGraphVO vo = null;

        //String[] legendArr  //날짜
        //float[] graph1  // 수치
        String[] legendArr =  myInFoManager.getAllID().split("\\\n");
        ArrayList<Integer> graph1 = new ArrayList<>();
        for( int i = 0 ; i < legendArr.length ; ++i ) {
            graph1.add(myInFoManager.get_TotalWarningNumber(legendArr[i]));
        }

        List<BarGraph> arrGraph = new ArrayList<BarGraph>();

        arrGraph.add(new BarGraph("android", Color.GRAY, graph1));

        int paddingTop = BarGraphVO.DEFAULT_PADDING;
        int paddingBottom = BarGraphVO.DEFAULT_PADDING;
        int paddingLeft = BarGraphVO.DEFAULT_PADDING;
        int paddingRight = BarGraphVO.DEFAULT_PADDING;
        int marginTop = BarGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight = BarGraphVO.DEFAULT_MARGIN_RIGHT;
        int minValueX = 0;
        int minValueY = 0;
        int maxValueX = 50;
        int maxValueY = 200;
        int incrementX = 10;
        int incrementY = 25;
        int barWidth = 50;

        vo = new BarGraphVO(legendArr, arrGraph,
                paddingTop, paddingBottom, paddingLeft, paddingRight,
                marginTop, marginRight,
                minValueX, minValueY, maxValueX, maxValueY,
                incrementX, incrementY,
                barWidth,
                -1);

        vo.setGraphNameBox(new GraphNameBox());
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
        vo.setAnimationShow(true);

        return vo;
    }
}