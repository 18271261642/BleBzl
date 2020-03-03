package com.ble.blebzl.xwatch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.blebzl.R;
import com.ble.blebzl.bleutil.Customdata;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.w30s.ble.WriteBackDataListener;
import com.ble.blebzl.xwatch.ble.XWatchBleAnalysis;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * Date 2020/2/19
 */
public class XWatchIntelActivity extends WatchBaseActivity {

    @BindView(R.id.commentB30BackImg)
    ImageView commentB30BackImg;
    @BindView(R.id.commentB30TitleTv)
    TextView commentB30TitleTv;
    @BindView(R.id.xWatchShowTv)
    TextView xWatchShowTv;

    private XWatchBleAnalysis xWatchBleAnalysis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x_watch_intel_layout);
        ButterKnife.bind(this);

        commentB30BackImg.setVisibility(View.VISIBLE);
        commentB30TitleTv.setText("内部调试页面");

        xWatchBleAnalysis = XWatchBleAnalysis.getW37DataAnalysis();

    }

    @OnClick({R.id.commentB30BackImg, R.id.getDeviceTimeBtn,
            R.id.syncDeviceTimeBtn, R.id.getUserInfoBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentB30BackImg:
                finish();
                break;
            case R.id.getDeviceTimeBtn: //获取手表时间

                xWatchBleAnalysis.getWatchTime(new WriteBackDataListener() {
                    @Override
                    public void backWriteData(byte[] data) {
                        StringBuilder stringBuilder = new StringBuilder();
                        xWatchShowTv.setText(stringBuilder.append(Arrays.toString(data))+"\n");
                        for(byte da : data){
                            stringBuilder.append(Customdata.byteToHex(da));
                        }
                        xWatchShowTv.setText(stringBuilder.toString());
                    }
                });
                break;
            case R.id.syncDeviceTimeBtn:
//                xWatchBleAnalysis.readDeviceAlarm(2, new XWatchAlarmBackListener() {
//                    @Override
//                    public void backAlarmOne(B18AlarmBean b18AlarmBean1) {
//                        xWatchShowTv.setText(b18AlarmBean1.toString());
//                    }
//                });


                //LitePal.deleteAll(B30HalfHourDB.class,"address=? and date =? and type=?", MyApp.getInstance().getMacAddress(), WatchUtils.getCurrentDate(), B30HalfHourDao.XWATCH_DAY_STEP);

                break;
            case R.id.getUserInfoBtn:
                xWatchBleAnalysis.getUserInfoFormDevice(new WriteBackDataListener() {
                    @Override
                    public void backWriteData(byte[] data) {
                        StringBuilder stringBuilder = new StringBuilder();
                        xWatchShowTv.setText(stringBuilder.append(Arrays.toString(data))+"\n");
                        for(byte da : data){
                            stringBuilder.append(Customdata.byteToHex(da)+" ");
                        }
                        xWatchShowTv.setText(stringBuilder.toString());
                    }
                });
                break;
        }
    }
}
