package com.ble.blebzl.b30;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ble.blebzl.Commont;
import com.ble.blebzl.MyApp;
import com.ble.blebzl.R;
import com.ble.blebzl.activity.FeedbackActivity;
import com.ble.blebzl.activity.LogoutActivity;
import com.ble.blebzl.activity.ModifyPasswordActivity;
import com.ble.blebzl.activity.NewLoginActivity;
import com.ble.blebzl.b15p.common.B15PContentState;
import com.ble.blebzl.b18.B18BleConnManager;
import com.ble.blebzl.bleutil.MyCommandManager;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.siswatch.bleus.H8ConnstateListener;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.ble.blebzl.util.Common;
import com.ble.blebzl.util.LocalizeTool;
import com.ble.blebzl.util.ToastUtil;
import com.ble.blebzl.view.PrivacyActivity;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.hplus.bluetooth.BleProfileManager;
import com.tjdL4.tjdmain.Dev;
import com.tjdL4.tjdmain.L4M;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * B30手环系统设置页面
 */
public class B30SysSettingActivity extends WatchBaseActivity {


    @BindView(R.id.bar_titles)
    TextView barTitles;
    @BindView(R.id.version_tv)
    TextView versionTv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b30_syssetting);
        ButterKnife.bind(this);


        initViews();

        initData();
    }

    private void initData() {

        try {
            versionTv.setText(packageName(MyApp.getContext()) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateApp();

    }

    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }


    private void initViews() {
        barTitles.setText(getResources().getString(R.string.system_settings));

    }

    @OnClick({R.id.image_back, R.id.updatePwdLin,
            R.id.feebackLin, R.id.aboutLin,
            R.id.commExit_login, R.id.privacyLin,R.id.logoutLin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:   //返回
                finish();
                break;
            case R.id.updatePwdLin:  //修改密码
                SharedPreferences share = getSharedPreferences("Login_id", 0);
                String userId = (String) SharedPreferencesUtils.readObject(B30SysSettingActivity.this, "userId");
                if (!WatchUtils.isEmpty(userId) && userId.equals("9278cc399ab147d0ad3ef164ca156bf0")) {
                    ToastUtil.showToast(B30SysSettingActivity.this, getResources().getString(R.string.noright));
                } else {
                    int isoff = share.getInt("id", 0);
                    if (isoff == 0) {
                        startActivity(ModifyPasswordActivity.class);
                    } else {
                        ToastUtil.showToast(B30SysSettingActivity.this, getResources().getString(R.string.string_third_login_changepass));
                    }
                }
                break;
            case R.id.feebackLin:    //意见反馈
                startActivity(FeedbackActivity.class);
                break;
            case R.id.privacyLin:    //隐私政策
                startActivity(PrivacyActivity.class);
                break;
            case R.id.aboutLin:
                updateApp();
                break;
            case R.id.commExit_login:   //退出登录
                showExitdialog();
                break;
            case R.id.logoutLin:    //注销账号
                startActivity(LogoutActivity.class);
                break;
        }
    }

    //更新APP
    private void updateApp() {
        String appInfo = getPackageName();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    //退出提示
    private void showExitdialog() {
        final String bleName = (String) SharedPreferencesUtils.readObject(B30SysSettingActivity.this, Commont.BLENAME);
        if (!WatchUtils.isEmpty(bleName)) {
            new MaterialDialog.Builder(this)
                    .title(R.string.exit_login)
                    .content(R.string.confrim_exit)
                    .positiveText(getResources().getString(R.string.confirm))
                    .negativeText(getResources().getString(R.string.cancle))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            new MaterialDialog.Builder(B30SysSettingActivity.this)
                                    .title(getResources().getString(R.string.prompt))
                                    .content(getResources().getString(R.string.confirm_unbind_strap))
                                    .positiveText(getResources().getString(R.string.confirm))
                                    .negativeText(getResources().getString(R.string.cancle))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                            alertLogoutMsg(bleName);
                                        }
                                    }).show();

                        }
                    }).show();
        } else {
            clearCommLogoutApp();
        }

    }

    //退出提示
    private void alertLogoutMsg(String bleName) {
        if (bleName.equals("bozlun")) {   //H8手表
            // showLoadingDialog("disconn...");
            if (MyCommandManager.DEVICENAME != null) {
                SharedPreferencesUtils.saveObject(B30SysSettingActivity.this, Commont.BLEMAC, "");
                MyApp.getInstance().h8BleManagerInstance().disConnH8(new H8ConnstateListener() {
                    @Override
                    public void h8ConnSucc() {

                    }

                    @Override
                    public void h8ConnFailed() {

                    }
                });
            }

        } else if (bleName.equals("B30")
                || bleName.equals("B36")
                || bleName.equals("B31")
                || bleName.equals("B31S")
                || bleName.equals("500S")) {
            if (MyCommandManager.DEVICENAME != null) {
                SharedPreferencesUtils.saveObject(B30SysSettingActivity.this, Commont.BLEMAC, "");
                MyApp.getInstance().getVpOperateManager().disconnectWatch(new IBleWriteResponse() {
                    @Override
                    public void onResponse(int stateCode) {
                        if (stateCode == -1) {
                            SharedPreferencesUtils.setParam(MyApp.getContext(), Commont.DEVICESCODE, "0000");
                            //clearCommLogoutApp();
                        }
                    }
                });
            }
        } else if (bleName.equals("B15P")) {

            if (L4M.Get_Connect_flag() == 2) {//已经连接的状态下
                /**
                 *手动断开  b15p
                 */
                Dev.RemoteDev_CloseManual();
            }
            B15PContentState.getInstance().setManualDis(true);//设置为手动断开
            B15PContentState.getInstance().stopSeachDevices();//停止扫描
        } else if (bleName.equals("W30") || bleName.equals("W31") || bleName.equals("W37")) {
            if (MyCommandManager.DEVICENAME != null) {
                String bleMac = (String) SharedPreferencesUtils.readObject(this, Commont.BLEMAC);
                if (!WatchUtils.isEmpty(bleMac))
                    MyApp.getInstance().getW37BleOperateManager().disBleDeviceByMac(bleMac);
            }

        }else if(bleName.contains("B16") || bleName.contains("B18")){
            if(BleProfileManager.getInstance().getConnectController().isConnected())
               B18BleConnManager.getB18BleConnManager().disConnB18Device(this);
        }
        clearCommLogoutApp();
    }

    //清除数据
    private void clearCommLogoutApp() {
        MyCommandManager.ADDRESS = null;
        MyCommandManager.DEVICENAME = null;
        SharedPreferencesUtils.saveObject(B30SysSettingActivity.this, Commont.BLENAME, "");
        SharedPreferencesUtils.saveObject(B30SysSettingActivity.this, Commont.BLEMAC, "");
        MyApp.getInstance().setMacAddress(null);// 清空全局
        SharedPreferencesUtils.saveObject(B30SysSettingActivity.this, "userId", null);
        SharedPreferencesUtils.saveObject(MyApp.getContext(), "userInfo", "");
        SharedPreferencesUtils.setParam(B30SysSettingActivity.this, "stepsnum", "0");
        Common.customer_id = null;

        new LocalizeTool(MyApp.getContext()).putUpdateDate(WatchUtils
                .obtainFormatDate(1));// 同时把数据更新时间清楚更新最后更新数据的时间

        startActivity(NewLoginActivity.class);
        finish();
    }


}
