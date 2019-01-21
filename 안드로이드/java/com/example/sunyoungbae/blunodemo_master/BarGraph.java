package com.example.sunyoungbae.blunodemo_master;

import android.graphics.Color;
import java.util.ArrayList;

public class BarGraph
{
    private String name = null;
    private int color = Color.BLUE;
    private ArrayList<Integer> coordinateArr = null;

    public BarGraph (String name , int color , ArrayList<Integer> coordinateArr)
    {
        this.setName(name);
        this.setColor(color);
        this.setCoordinateArr(coordinateArr);
    }

    public String getName () {
        return this.name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public int getColor () {
        return this.color;
    }

    public void setColor ( int color ) {
        this.color = color;
    }

    public ArrayList<Integer> getCoordinateArr () { return this.coordinateArr; }

    public void setCoordinateArr ( ArrayList<Integer> coordArr )
    {
        this.coordinateArr = coordArr;
    }
}
