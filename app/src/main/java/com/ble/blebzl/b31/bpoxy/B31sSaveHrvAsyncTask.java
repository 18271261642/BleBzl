package com.ble.blebzl.b31.bpoxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ble.blebzl.MyApp;
import com.ble.blebzl.b31.model.B31HRVBean;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.google.gson.Gson;
import com.veepoo.protocol.model.datas.HRVOriginData;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin
 * Date 2019/9/25
 */
public class B31sSaveHrvAsyncTask extends AsyncTask<List<HRVOriginData>,Void,Void> {

    private Gson gson = new Gson();


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MyApp.getContext().registerReceiver(broadcastReceiver,new IntentFilter(WatchUtils.B31_HRV_COMPLETE));

    }

    @Override
    protected Void doInBackground(List<HRVOriginData>... lists) {
        if(isCancelled())return null;
        List<HRVOriginData> hrvOriginDataList = lists[0];
        if(hrvOriginDataList != null){
            saveB31sHrvData(hrvOriginDataList);
        }

        return null;
    }

    private void saveB31sHrvData(List<HRVOriginData> hrvOriginDataList) {
        List<B31HRVBean> list = new ArrayList<>();
        String bleMac = MyApp.getInstance().getMacAddress();
        String filePath = Environment.getExternalStorageDirectory()+"/DCIM/";
        for(HRVOriginData hrvOriginData : hrvOriginDataList){

            //new GetJsonDataUtil().writeTxtToFile(gson.toJson(hrvOriginData),filePath,"hrv.json");
            //只保存7点之前的
            int timeBefore = hrvOriginData.getmTime().getHMValue();
            //Log.e("HRV","--------hrvO="+timeBefore);
            if(timeBefore <= 420){
                //new GetJsonDataUtil().writeTxtToFile(gson.toJson(hrvOriginData),filePath,"hrv2.json");
                B31HRVBean hrvBean = new B31HRVBean();
                hrvBean.setBleMac(bleMac);
                hrvBean.setDateStr(hrvOriginData.getDate());
                hrvBean.setHrvDataStr(gson.toJson(hrvOriginData));
                list.add(hrvBean);
            }

        }

        int delHRV = LitePal.deleteAll(B31HRVBean.class, "dateStr=? and bleMac=?", WatchUtils.getCurrentDate()
                , bleMac);
        Log.e("SaveHRV","---------删除hrv="+delHRV);
        LitePal.saveAll(list);
        //发送广播，通知更新UI
        Intent intent = new Intent();
        intent.setAction(WatchUtils.B31_HRV_COMPLETE);
        MyApp.getContext().sendBroadcast(intent);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(broadcastReceiver != null)
            MyApp.getContext().unregisterReceiver(broadcastReceiver);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
