package com.ble.blebzl.b31;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ble.blebzl.Commont;
import com.ble.blebzl.MyApp;
import com.ble.blebzl.R;
import com.ble.blebzl.b30.bean.B30HalfHourDao;
import com.ble.blebzl.b30.model.CusVPSleepData;
import com.ble.blebzl.bleutil.MyCommandManager;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.ble.blebzl.util.GetJsonDataUtil;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ICustomSettingDataListener;
import com.veepoo.protocol.listener.data.IDeviceFuctionDataListener;
import com.veepoo.protocol.listener.data.IOriginData3Listener;
import com.veepoo.protocol.listener.data.IOriginDataListener;
import com.veepoo.protocol.listener.data.IOriginProgressListener;
import com.veepoo.protocol.listener.data.IPwdDataListener;
import com.veepoo.protocol.listener.data.ISleepDataListener;
import com.veepoo.protocol.listener.data.ISocialMsgDataListener;
import com.veepoo.protocol.model.datas.FunctionDeviceSupportData;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.datas.HRVOriginData;
import com.veepoo.protocol.model.datas.OriginData;
import com.veepoo.protocol.model.datas.OriginData3;
import com.veepoo.protocol.model.datas.OriginHalfHourData;
import com.veepoo.protocol.model.datas.PwdData;
import com.veepoo.protocol.model.datas.SleepData;
import com.veepoo.protocol.model.datas.SleepPrecisionData;
import com.veepoo.protocol.model.datas.Spo2hOriginData;
import com.veepoo.protocol.model.enums.EPwdStatus;
import com.veepoo.protocol.model.settings.CustomSettingData;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * Date 2019/3/12
 */
public class InternalTestActivity extends WatchBaseActivity {

    private static final String TAG = "InternalTestActivity";

    @BindView(R.id.commentB30BackImg)
    ImageView commentB30BackImg;
    @BindView(R.id.commentB30TitleTv)
    TextView commentB30TitleTv;
    @BindView(R.id.commArrowDate)
    TextView commArrowDate;
    @BindView(R.id.showDeviceSleepTv)
    TextView showDeviceSleepTv;

    private Gson gson = new Gson();
    private String currDay = WatchUtils.getCurrentDate();

    private int code = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_layout);
        ButterKnife.bind(this);

        initViews();
        SharedPreferencesUtils.setParam(MyApp.getContext(),"tagCode",1);


        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = activityManager.getMemoryClass();
        int maxHeapSize = activityManager.getLargeMemoryClass();  // manafest.xml   android:largeHeap="true"
        showDeviceSleepTv.setText("可使用内存范围:"+heapSize+"-"+maxHeapSize);


    }

    private void initViews() {
        commArrowDate.setText(currDay);
        commentB30TitleTv.setText("内部测试用");
        commentB30BackImg.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.commentB30BackImg, R.id.commArrowLeft,
            R.id.commArrowRight, R.id.deviceSleepBtn,
            R.id.sqlSleepBtn,R.id.clearSleepBtn,
            R.id.readDBHeartBtn,R.id.readDBBpBtn,
            R.id.readDeviceAllSportBtn,R.id.verticalDevicePwdBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentB30BackImg:
                finish();
                break;
            case R.id.commArrowLeft:
                changeDayData(true);
                break;
            case R.id.commArrowRight:
                changeDayData(false);
                break;
            case R.id.clearSleepBtn:
                showDeviceSleepTv.setText("");
                break;
            case R.id.deviceSleepBtn:
                if(MyCommandManager.DEVICENAME == null){
                    showDeviceSleepTv.setText("设备未连接");
                    return;
                }

                readSleepData();
                break;
            case R.id.sqlSleepBtn:  //从数据库中读取睡眠数据
                code = 0;
                findBySql(0);
                break;
            case R.id.readDeviceAllSportBtn:   //从设备中读取所有的健康数据
                readAllDeviceData();
                //new TestAsyncTask().execute(1);

                break;
            case R.id.readDBHeartBtn:   //从数据库中读取心率数据
                code = 1;
                findBySql(1);
                break;
            case R.id.readDBBpBtn:  //从数据库中读取血压数据
                code = 2;
                findBySql(2);
                break;
            case R.id.verticalDevicePwdBtn:     //验证设备密码
                verticalDevicePwd();
                break;
        }
    }

    private void readAllDeviceData() {
        if(MyCommandManager.DEVICENAME == null)
            return;
        showDeviceSleepTv.setText("数据读取中...");
        stringBuilder.reverse();
        int vpOrignVersion = (int) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.VP_DEVICE_VERSION,0);
        Log.e(TAG,"------vp="+vpOrignVersion);
        MyApp.getInstance().getVpOperateManager().readOriginData(iBleWriteResponse,vpOrignVersion == 0 ? iOriginDataListener : iOriginProgressListener,2);

    }

    final StringBuilder stringBuilder = new StringBuilder();

    //旧版本回调
    private IOriginDataListener iOriginDataListener = new IOriginDataListener() {

        @Override
        public void onOringinFiveMinuteDataChange(OriginData originData) {

        }

        @Override
        public void onOringinHalfHourDataChange(OriginHalfHourData originHalfHourData) {
            Log.e(TAG,"-------旧协议半小时数据="+originHalfHourData.getHalfHourRateDatas().toString());
            stringBuilder.append("血压"+originHalfHourData.getHalfHourBps().toString()+"\n"
                    +"心率="+originHalfHourData.getHalfHourRateDatas().toString()+"\n"+"运动="+originHalfHourData.getHalfHourSportDatas().toString());
        }

        @Override
        public void onReadOriginProgressDetail(int i, String s, int i1, int i2) {

        }

        @Override
        public void onReadOriginProgress(float v) {

        }

        @Override
        public void onReadOriginComplete() {
            showDeviceSleepTv.setText(stringBuilder);
        }
    };



    private IOriginProgressListener iOriginProgressListener = new IOriginData3Listener() {
        @Override
        public void onOriginFiveMinuteListDataChange(List<OriginData3> list) {
            Log.e(TAG,"------22---新的="+list.size());
        }

        @Override
        public void onOriginHalfHourDataChange(OriginHalfHourData originHalfHourData) {
            //Log.e(TAG,"--22--originHalfHourData--="+"--心率="+gson.toJson(originHalfHourData.getHalfHourRateDatas()));
            stringBuilder.append("心率="+gson.toJson(originHalfHourData.getHalfHourRateDatas())+"\n"+"血压="+gson.toJson(originHalfHourData.getHalfHourBps())+"\n"+
            "---运动="+gson.toJson(originHalfHourData.getHalfHourSportDatas()));

        }

        @Override
        public void onOriginHRVOriginListDataChange(List<HRVOriginData> list) {
            Log.e(TAG,"-------HRVOriginData--="+list.size()+"---1="+list.get(0).toString());


        }

        @Override
        public void onOriginSpo2OriginListDataChange(List<Spo2hOriginData> list) {
            Log.e(TAG,"-------Spo2hOriginData--="+list.size()+"---1="+list.get(0).toString());
            //showNewSpo2Data(list);
        }

        @Override
        public void onReadOriginProgressDetail(int i, String s, int i1, int i2) {

        }

        @Override
        public void onReadOriginProgress(float v) {
            Log.e(TAG,"--------onReadOriginProgress="+v);
        }

        @Override
        public void onReadOriginComplete() {
            showDeviceSleepTv.setText(stringBuilder);
        }
    };


    private void showNewSpo2Data(final List<Spo2hOriginData> spo2List){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Spo2hOriginData spo2hOriginData : spo2List){
                    Log.e(TAG,"-----spo2="+spo2hOriginData.toString());
                }
            }
        }).start();
    }



    /**
     * 根据日期切换数据
     */
    private void changeDayData(boolean left) {
        String date = WatchUtils.obtainAroundDate(currDay, left);
        if (date.equals(currDay) || date.isEmpty()) {
            return;// 空数据,或者大于今天的数据就别切了
        }
        currDay = date;
        findBySql(code);
    }

    private void findBySql(int code) {
        commArrowDate.setText(currDay);
        String mac = MyApp.getInstance().getMacAddress();
        if(WatchUtils.isEmpty(mac))
            return;
        switch (code){
            case 0x00:  //数据库中读取睡眠
                int vpOrignVersion = (int) SharedPreferencesUtils.getParam(MyApp.getContext(),Commont.VP_DEVICE_VERSION,0);

                String sleep = B30HalfHourDao.getInstance().findOriginData(mac, currDay,vpOrignVersion == 0 ? B30HalfHourDao
                        .TYPE_SLEEP : B30HalfHourDao.TYPE_PRECISION_SLEEP);

                CusVPSleepData cusVPSleepData = gson.fromJson(sleep,CusVPSleepData.class);
                showDeviceSleepTv.setText(cusVPSleepData==null?"数据为空":("数据库中的睡眠："+cusVPSleepData.toString()));
                break;
            case 0x01:  //数据库中读取心率
                String heartStr = B30HalfHourDao.getInstance().findOriginData(mac, currDay, B30HalfHourDao
                        .TYPE_RATE);
                showDeviceSleepTv.setText(heartStr==null?"数据为空":("数据库中的心率数据："+heartStr));
                break;
            case 0x02:  //数据库中读取血压
                String bpStr = B30HalfHourDao.getInstance().findOriginData(mac, currDay, B30HalfHourDao
                        .TYPE_BP);
                showDeviceSleepTv.setText(bpStr==null?"数据为空":("数据库中的血压数据："+bpStr));
                break;
        }


    }


    private void readSleepData(){
        final StringBuilder stringBuilder = new StringBuilder();
        MyApp.getInstance().getVpOperateManager().readSleepData(iBleWriteResponse, new ISleepDataListener() {
            @Override
            public void onSleepDataChange(SleepData sleepData) {
                Log.e("内测","-----111----睡眠="+sleepData.toString()+"---字段="+sleepData.getSleepLine());
                if(sleepData instanceof SleepPrecisionData){
                    SleepPrecisionData sleepPrecisionData = (SleepPrecisionData) sleepData;
                    stringBuilder.append("精准睡眠："+sleepPrecisionData.toString()+"--睡眠表现形式="+sleepPrecisionData.getSleepLine());
                    Log.e(TAG,"--------精准睡眠="+sleepPrecisionData.toString()+"--睡眠表现形式="+sleepPrecisionData.getSleepLine());
                }else{
                    stringBuilder.append("普通睡眠："+gson.toJson(sleepData));
                }


            }

            @Override
            public void onSleepProgress(float v) {

            }

            @Override
            public void onSleepProgressDetail(String s, int i) {

            }

            @Override
            public void onReadSleepComplete() {
                Log.e("内测","--------设备中的睡眠数据="+stringBuilder);
                showDeviceSleepTv.setText(stringBuilder);
                new GetJsonDataUtil().writeTxtToFile(stringBuilder.toString(),Environment.getExternalStorageDirectory()+"/DCIM/","sleep.json");
            }
        }, 4);
    }


    //验证设备密码
    private void verticalDevicePwd(){

        String b30Pwd = (String) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.DEVICESCODE, "0000");
        final boolean is24Hour = (boolean) SharedPreferencesUtils.getParam(MyApp.getContext(), Commont.IS24Hour, true);//是否为24小时制

        VPOperateManager.getMangerInstance(MyApp.getContext()).confirmDevicePwd(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {

            }
        }, new IPwdDataListener() {
            @Override
            public void onPwdDataChange(PwdData pwdData) {
                Log.e(TAG, "---111--pwdData=" + pwdData.getmStatus() + "=" + pwdData.toString());
                Log.e(TAG, "-------111checkPwd=" + (pwdData.getmStatus() == EPwdStatus.CHECK_AND_TIME_SUCCESS));
                stringBuilder.append("pwdData="+pwdData.toString()+"\n");
                showDeviceSleepTv.setText(stringBuilder);
            }
        }, new IDeviceFuctionDataListener() {
            @Override
            public void onFunctionSupportDataChange(FunctionDeviceSupportData functionDeviceSupportData) {
                Log.e(TAG, "--设备支持的功能--=" + functionDeviceSupportData.toString());
                stringBuilder.append("设备支持的功能="+functionDeviceSupportData.toString()+"\n");

            }
        }, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData functionSocailMsgData) {
                Log.e(TAG, "-----消息通知的开关-=" + functionSocailMsgData);
            }
        },new ICustomSettingDataListener() {
            @Override
            public void OnSettingDataChange(CustomSettingData customSettingData) {
                Log.e(TAG, "----设备开关=" + customSettingData.toString());
                stringBuilder.append("设备开关="+customSettingData.toString()+"\n");

            }
        }, WatchUtils.isEmpty(b30Pwd) ? "0000" : b30Pwd.trim(), is24Hour);


    }




    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
