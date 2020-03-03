package com.ble.blebzl.w30s.ble;


import com.ble.blebzl.w30s.ble.w30.servicebean.W30SDeviceData;

/**
 * Created by Admin
 * Date 2019/7/5
 */
public interface BleDeviceDataListener {

    void callBleDeviceData(W30SDeviceData deviceData);
}
