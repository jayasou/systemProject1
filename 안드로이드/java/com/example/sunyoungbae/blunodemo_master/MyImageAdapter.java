package com.example.sunyoungbae.blunodemo_master;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MyImageAdapter extends BaseAdapter {

    private Context context;	// calling activity context
    Integer[] smallImages;		// thumbnail data set

    public MyImageAdapter(Context callingActivityContext,
                          Integer[] thumbnails) {
        context = callingActivityContext;
        smallImages = thumbnails;
    }

    // how many entries are there in the data set
    public int getCount() {
        return smallImages.length;
    }

    // what is in a given 'position' in the data set
    public Object getItem(int position) {
        return smallImages[position];
    }

    // what is the ID of data item in given 'position'
    public long getItemId(int position) {
        return position;
    }
    // create a view for each thumbnail in the data set
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        // if possible, reuse (convertView) image already held in cache
        if (convertView == null) {
            // new image in GridView formatted to:
            // 100x75 pixels (its actual size)
            // center-cropped, and 5dp padding all around
            imageView = new ImageView(context);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(100*3, 75*3)); 													// length
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); //CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(smallImages[position]);
        return imageView;
    }

}
