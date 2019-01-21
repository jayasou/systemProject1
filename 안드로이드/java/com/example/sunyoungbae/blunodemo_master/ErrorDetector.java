package com.example.sunyoungbae.blunodemo_master;

/**
 * Created by SunYoungBae on 2016-11-16.
 */

public class ErrorDetector {

    public static ErrorCode checkGraphObject(BarGraphVO barGraphVO){
        //1. vo check
        if(barGraphVO == null){
            return ErrorCode.GRAPH_VO_IS_EMPTY;
        }

        //2. legend and graph size check
        int legendSize = barGraphVO.getLegendArr().length;
        for (int i = 0; i < barGraphVO.getArrGraph().size(); i++) {
            if(legendSize !=barGraphVO.getArrGraph().get(i).getCoordinateArr().size()){
                return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
            }
        }

        return ErrorCode.NOT_ERROR;
    }
}