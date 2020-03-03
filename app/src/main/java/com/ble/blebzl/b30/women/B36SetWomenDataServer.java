package com.ble.blebzl.b30.women;


import android.util.Log;

import com.ble.blebzl.Commont;
import com.ble.blebzl.MyApp;
import com.ble.blebzl.b18.B18BleConnManager;
import com.ble.blebzl.bleutil.MyCommandManager;
import com.ble.blebzl.siswatch.utils.DateTimeUtils;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.hplus.bluetooth.BleProfileManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IWomenDataListener;
import com.veepoo.protocol.model.datas.TimeData;
import com.veepoo.protocol.model.datas.WomenData;
import com.veepoo.protocol.model.enums.ESex;
import com.veepoo.protocol.model.enums.EWomenStatus;
import com.veepoo.protocol.model.settings.WomenSetting;

/**
 * 设置手环提醒，经期，备孕期，和宝妈期
 * Created by Admin
 * Date 2018/12/7
 */
public class B36SetWomenDataServer {

    private static final String TAG = "B36SetWomenDataServer";

    public B36SetWomenDataServer() {

    }

    //设置数据同步至手环
    static void setB30WomenData(int statusCode) {
        if (MyCommandManager.DEVICENAME == null)
            return;
        if (!MyCommandManager.DEVICENAME.equals("B36") && !MyCommandManager.DEVICENAME.equals("B16") &&!MyCommandManager.DEVICENAME.equals("B50"))
            return;
        //baby的性别
        String baby_sex = (String) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.WOMEN_BABY_SEX, "M");
        if (WatchUtils.isEmpty(baby_sex))
            baby_sex = "M";
        //最后一次月经的时间
        String lastMenDate = (String) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.WOMEN_LAST_MENSTRUATION_DATE, WatchUtils.getCurrentDate());
        if (WatchUtils.isEmpty(lastMenDate))
            lastMenDate = WatchUtils.getCurrentDate();

        int year = DateTimeUtils.getCurrYear(lastMenDate);
        int month = DateTimeUtils.getCurrMonth(lastMenDate);
        int day = DateTimeUtils.getCurrDay(lastMenDate);

        //最后一次月经日
        TimeData lastTimeD = new TimeData(year, month, day);

        //baby的生日
        String baby_birth = (String) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.WOMEN_BABY_BIRTHDAY, WatchUtils.getCurrentDate());
        if (WatchUtils.isEmpty(baby_birth))
            baby_birth = WatchUtils.getCurrentDate();
        int birthYear = DateTimeUtils.getCurrYear(baby_birth);
        int birthMonth = DateTimeUtils.getCurrMonth(baby_birth);
        int birthDay = DateTimeUtils.getCurrDay(baby_birth);

        //月经间隔长度
        String menesInterval = (String) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.WOMEN_MEN_INTERVAL, "28");
        if (WatchUtils.isEmpty(menesInterval))
            menesInterval = "28";
        //月经持续长度
        String menseLength = (String) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.WOMEN_MEN_LENGTH, "8");
        if (WatchUtils.isEmpty(menseLength))
            menseLength = "8";

        //怀孕期 宝宝出生日
        String babyMens = (String) SharedPreferencesUtils.getParam(MyApp.getContext(),Commont.BABY_BORN_DATE,WatchUtils.getCurrentDate());
        if(WatchUtils.isEmpty(babyMens))
            babyMens = WatchUtils.getCurrentDate();
        int babyYear = DateTimeUtils.getCurrYear(babyMens);
        int babyMonth = DateTimeUtils.getCurrMonth(babyMens);
        int babyDay = DateTimeUtils.getCurrDay(babyMens);




        WomenSetting womenSetting = null;

        switch (statusCode) {
            case 0:     //月经期
            case 1:     //备孕期
                womenSetting = new WomenSetting(EWomenStatus.MENES,
                        Integer.valueOf(menseLength.trim()), Integer.valueOf(menesInterval.trim()), lastTimeD);
//                womenSetting = new WomenSetting(EWomenStatus.PREREADY,
//                        Integer.valueOf(menseLength.trim()), Integer.valueOf(menesInterval.trim()),
//                        lastTimeD, ESex.MAN, new TimeData(DateTimeUtils.getCurrYear(), DateTimeUtils.getCurrMonth(), DateTimeUtils.getCurrDay()));
                break;
            case 2:     //怀孕期
                womenSetting = new WomenSetting(EWomenStatus.PREING,lastTimeD,new TimeData(babyYear,babyMonth,babyDay));
                break;
            case 3:     //宝妈期

                womenSetting = new WomenSetting(EWomenStatus.MAMAMI,
                        Integer.valueOf(menseLength.trim()),
                        Integer.valueOf(menesInterval.trim())
                        , lastTimeD, baby_sex.equals("男") ? ESex.MAN : ESex.WOMEN, new TimeData(birthYear, birthMonth, birthDay));
                break;
        }


        if(MyCommandManager.DEVICENAME.equals("B36")){
            boolean isB36Noti = (boolean) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.IS_B36_JINGQI_NOTI, false);
            if (isB36Noti) {
                MyApp.getInstance().getVpOperateManager().settingWomenState(new IBleWriteResponse() {
                    @Override
                    public void onResponse(int i) {

                    }
                }, new IWomenDataListener() {
                    @Override
                    public void onWomenDataChange(WomenData womenData) {
                        Log.e(TAG, "--------设置手环数据=" + womenData.toString());
                    }
                }, womenSetting);
            }
        }

        if(MyCommandManager.DEVICENAME.equals("B18") || MyCommandManager.DEVICENAME.equals("B50") || MyCommandManager.DEVICENAME.equals("B16")){
            if(!BleProfileManager.getInstance().isConnected())
                return;
            B18BleConnManager.getB18BleConnManager().setWomenData(year,month,day,Integer.valueOf(menesInterval),Integer.valueOf(menseLength));

        }

    }


}
