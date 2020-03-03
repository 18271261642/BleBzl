package com.ble.blebzl.xwatch.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aigestudio.wheelpicker.widgets.ProfessionPick;
import com.ble.blebzl.Commont;
import com.ble.blebzl.MyApp;
import com.ble.blebzl.R;
import com.ble.blebzl.siswatch.NewSearchActivity;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.ble.blebzl.xwatch.ble.XWatchBleAnalysis;
import com.ble.blebzl.xwatch.ble.XWatchGoalListener;
import com.ble.blebzl.xwatch.ble.XWatchTimeModelListener;
import com.ble.blebzl.xwatch.ble.XWatchUnitListener;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Admin
 * Date 2020/2/20
 */
public class XWatchDeviceFragment extends Fragment {

    View view;
    @BindView(R.id.commentB30BackImg)
    ImageView commentB30BackImg;
    @BindView(R.id.commentB30TitleTv)
    TextView commentB30TitleTv;
    @BindView(R.id.xWatchDeviceSportGoalTv)
    TextView xWatchDeviceSportGoalTv;
    @BindView(R.id.xWatchDeviceSleepGoalTv)
    TextView xWatchDeviceSleepGoalTv;
    @BindView(R.id.xWatchDeviceUnitTv)
    TextView xWatchDeviceUnitTv;
    Unbinder unbinder;


    FragmentManager fragmentManager;

    private XWatchBleAnalysis xWatchBleAnalysis;


    //运动目标
    ArrayList<String> sportGoalList;

    //公英制
    private AlertDialog.Builder unitBuilder;
    //时间格式
    private AlertDialog.Builder timeTypeBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xWatchBleAnalysis = XWatchBleAnalysis.getW37DataAnalysis();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_x_watch_device_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        initViews();

        initData();

        return view;
    }

    private void initData() {
        sportGoalList = new ArrayList<>();
        for (int i = 1000; i <= 64000; i += 1000) {
            sportGoalList.add(i + "");
        }
    }

    private void initViews() {
        commentB30BackImg.setVisibility(View.VISIBLE);
        commentB30TitleTv.setText(getResources().getString(R.string.menu_settings));
        xWatchBleAnalysis.getDeviceSportGoal(new XWatchGoalListener() {
            @Override
            public void backDeviceGoal(int goal) {
                xWatchDeviceSportGoalTv.setText(goal+"");
                SharedPreferencesUtils.setParam(getActivity(),Commont.SPORT_GOAL_STEP,goal);
                readDeviceTimeModel();
            }
        });

    }

    //时间模式
    private void readDeviceTimeModel(){
        xWatchBleAnalysis.getWatchTimeType(new XWatchTimeModelListener() {
            @Override
            public void deviceeTimeModel(int model) {
                xWatchDeviceSleepGoalTv.setText(model == 0 ? "12h" : "24h");
                readDeviceUnit();
            }
        });
    }


    //距离单位 km or mile
    private void readDeviceUnit(){
        xWatchBleAnalysis.getDeviceKmUnit(new XWatchUnitListener() {
            @Override
            public void backDeviceUnit(int unit) {
                xWatchDeviceUnitTv.setText(unit == 0 ? getResources().getString(R.string.setkm) : getResources().getString(R.string.setmi));
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.commentB30BackImg, R.id.xWatchDeviceSportRel,
            R.id.b18DeviceUnitRel, R.id.xWatchDeviceMsgRel,
            R.id.xWatchDeviceAlarmRel,R.id.xWatchDeviceSleepRel,
            R.id.xWatchDeviceunBindRel})
    public void onClick(View view) {
        fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.commentB30BackImg:
                fragmentManager.popBackStack();
                break;
            case R.id.xWatchDeviceSportRel:     //运动目标
                setSportGoal();
                break;
            case R.id.xWatchDeviceSleepRel: //时间格式
                setTimeType();
                break;
            case R.id.b18DeviceUnitRel:     //单位
                showUnitDialog();
                break;
            case R.id.xWatchDeviceMsgRel:   //消息提醒
                fragmentTransaction.addToBackStack(null)
                        .replace(R.id.xWatchDeviceFrameLayout, new XWatchMsgAlertFragment())
                        .commit();
                break;
            case R.id.xWatchDeviceAlarmRel: //闹钟
                fragmentTransaction.addToBackStack(null)
                        .replace(R.id.xWatchDeviceFrameLayout, new XWatchAlarmFragment())
                        .commit();
                break;
            case R.id.xWatchDeviceunBindRel:    //断开连接
                disDeviceBle();
                break;
        }
    }

    //断开连接
    private void disDeviceBle() {
        new MaterialDialog.Builder(getActivity())
                .title(getResources().getString(R.string.prompt))
                .content(getResources().getString(R.string.confirm_unbind_strap))
                .positiveText(getResources().getString(R.string.confirm))
                .negativeText(getResources().getString(R.string.cancle))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        unbindBle();
                    }
                }).show();
    }

    private void unbindBle() {
        MyApp.getInstance().getW37BleOperateManager().disBleDeviceByMac(MyApp.getInstance().getMacAddress());
        MyApp.getInstance().setMacAddress("");
        startActivity(new Intent(getActivity(), NewSearchActivity.class));
        getActivity().finish();

    }

    //设置时间格式
    private void setTimeType() {
        String[] timeItem = new String[]{"12h","24h"};
        timeTypeBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.time_forma))
                .setItems(timeItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xWatchDeviceSleepGoalTv.setText(which == 0 ? "12h" : "24h");
                        xWatchBleAnalysis.setWatchTimeType(which);
                    }
                }).setNegativeButton(getResources().getText(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        timeTypeBuilder.show();
    }


    //设置运动目标
    private void setSportGoal() {
        ProfessionPick professionPick = new ProfessionPick.Builder(getActivity(),
                new ProfessionPick.OnProCityPickedListener() {
                    @Override
                    public void onProCityPickCompleted(String profession) {
                        xWatchDeviceSportGoalTv.setText(profession);
                        xWatchBleAnalysis.setDeviceSportGoal(Integer.valueOf(profession.trim()));
                    }
                }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(18) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(sportGoalList) //min year in loop
                .dateChose("8000") // date chose when init popwindow
                .build();
        professionPick.showPopWin(getActivity());
    }



    //展示公英制
    private void showUnitDialog() {

        String runTypeString[] = new String[]{getResources().getString(R.string.setkm),
                getResources().getString(R.string.setmi)};
        unitBuilder = new AlertDialog.Builder(getActivity());
        unitBuilder.setTitle(getResources().getString(R.string.select_running_mode))
                .setItems(runTypeString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (i == 0) {
                            xWatchDeviceUnitTv.setText(getResources().getString(R.string.setkm));
                            SharedPreferencesUtils.setParam(MyApp.getContext(), Commont.ISSystem, true);//是否为公制
                            // changeCustomSetting(true);
                        } else {
                            //changeCustomSetting(false);
                            xWatchDeviceUnitTv.setText(getResources().getString(R.string.setmi));
                            SharedPreferencesUtils.setParam(MyApp.getContext(), Commont.ISSystem, false);//是否为公制
                        }
                        //设置公英制
                        xWatchBleAnalysis.setDeviceKmUnit(i);
                    }
                }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        }).show();
    }
}
