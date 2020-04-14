package com.ble.blebzl.xwatch.ble;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin
 * Date 2020/2/22
 */
public class XWatchBleOperate {

    private volatile static  XWatchBleOperate xWatchBleOperate;

    public static XWatchBleOperate getxWatchBleOperate(){
        if(xWatchBleOperate == null){
            synchronized (XWatchBleOperate.class){
                if(xWatchBleOperate == null)
                    xWatchBleOperate = new XWatchBleOperate();
            }
        }
        return xWatchBleOperate;
    }

    private XWatchBleOperate(){

    }


    public void bleConnOperate(final int day, final Context context, final XWatchSyncSuccListener xWatchSyncSuccListener){
        XWatchBleAnalysis.getW37DataAnalysis().getSomeDayForDevice(day, new XWatchCountStepListener() {
            @Override
            public void backDeviceCountStep(int countStep, double kcalStr) {

            }

            @Override
            public void backDeviceDisance(double disance, int sportTime) {
                getDayDetailSport(day,xWatchSyncSuccListener);
            }
        });



    }

    private void getDayDetailSport(int day, final XWatchSyncSuccListener xWatchSyncSuccListener){
        XWatchBleAnalysis.getW37DataAnalysis().getSomeDayDetailSport(day, new XWatchSportDetailListener() {
            @Override
            public void backDeviceSportDetail(Map<String, List<Integer>> sportMap) {
                if(xWatchSyncSuccListener != null)
                    xWatchSyncSuccListener.bleSyncComplete(new byte[]{0x00});
            }
        });
    }


}
