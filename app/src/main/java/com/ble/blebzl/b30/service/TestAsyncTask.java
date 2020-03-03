package com.ble.blebzl.b30.service;

import android.os.AsyncTask;

import com.ble.blebzl.MyApp;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.veepoo.protocol.listener.base.IBleWriteResponse;

/**
 * Created by Admin
 * Date 2019/10/10
 */
public class TestAsyncTask extends AsyncTask<Integer,Void,Void> {

    private static final String TAG = "TestAsyncTask";

    private int tagCode = 0;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        tagCode = (int) SharedPreferencesUtils.getParam(MyApp.getContext(),"tagCode",0);
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        if(isCancelled())
            return null;




        return null;

     }


    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }


    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
