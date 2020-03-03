package com.ble.blebzl.w30s.ble;


import com.ble.blebzl.w30s.ble.w30.servicebean.W30SDeviceData;
import com.ble.blebzl.w30s.ble.w30.servicebean.W30SHeartData;
import com.ble.blebzl.w30s.ble.w30.servicebean.W30SSleepData;
import com.ble.blebzl.w30s.ble.w30.servicebean.W30SSportData;

/**
 * Created by Admin
 * Date 2019/7/4
 */
public interface CallDatasBackListenter {

    void callDatasBackSportListenter(W30SSportData sportData);

    void callDatasBackSleepListenter(W30SSleepData sleepData);

    void callDatasBackDeviceDataListenter(W30SDeviceData deviceData);

    void callDatasBackHeartListenter(W30SHeartData heartData);

    //血压数据W37有血压，
    void callDatasBackBloodListener(W37BloodBean w37BloodBean);

    void callDatasBackListenterIsok();//数据返回完成
}
