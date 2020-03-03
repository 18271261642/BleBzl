package com.ble.blebzl.b15p.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ble.blebzl.MyApp;
import com.ble.blebzl.R;
import com.ble.blebzl.b31.MessageHelpActivity;
import com.ble.blebzl.bleutil.MyCommandManager;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.tjdL4.tjdmain.AppIC;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/13.
 */

/**
 * B30消息提醒页面
 */
public class B15PMessAlertActivity extends WatchBaseActivity {

    private static final String TAG = "B30MessAlertActivity";

    @BindView(R.id.b30SkypeTogg)
    ToggleButton b30SkypeTogg;
    @BindView(R.id.b30WhatsAppTogg)
    ToggleButton b30WhatsAppTogg;
    @BindView(R.id.b30FacebookTogg)
    ToggleButton b30FacebookTogg;
    @BindView(R.id.b30LinkedTogg)
    ToggleButton b30LinkedTogg;
    @BindView(R.id.b30TwitterTogg)
    ToggleButton b30TwitterTogg;
    @BindView(R.id.b30LineTogg)
    ToggleButton b30LineTogg;
    @BindView(R.id.b30WechatTogg)
    ToggleButton b30WechatTogg;
    @BindView(R.id.b30QQTogg)
    ToggleButton b30QQTogg;
    @BindView(R.id.b30MessageTogg)
    ToggleButton b30MessageTogg;
    @BindView(R.id.b30PhoneTogg)
    ToggleButton b30PhoneTogg;
    @BindView(R.id.newSearchTitleTv)
    TextView newSearchTitleTv;


    @BindView(R.id.b30InstagramTogg)
    ToggleButton b30InstagramTogg;
    @BindView(R.id.b30GmailTogg)
    ToggleButton b30GmailTogg;
    @BindView(R.id.b30SnapchartTogg)
    ToggleButton b30SnapchartTogg;
    @BindView(R.id.b30OhterTogg)
    ToggleButton b30OhterTogg;

    @BindView(R.id.google_gmail)
    LinearLayout google_gmail;
    @BindView(R.id.google_gmail_line)
    View google_gmail_line;
    @BindView(R.id.newSearchRightImg1)
    ImageView newSearchRightImg1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b15p_msgalert);
        ButterKnife.bind(this);

        initViews();

        //申请权限
        //requestPermiss();

        getPhoneStatus();
        //读取社交消息设置
        readSocialMsg();

        //getDoNotDisturb();

    }


    private void getPhoneStatus() {
        AudioManager audioManager = (AudioManager) MyApp.getInstance().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            int ringMode = audioManager.getRingerMode();
            //audioManager.getStreamVolume()
            Log.e(TAG, "-------手环模式=" + ringMode);
            switch (ringMode) {
                case AudioManager.RINGER_MODE_NORMAL:
                    //普通模式
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    //振动模式
                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    //静音模式
                    break;
            }

        }
    }


    //获取Do not disturb权限,才可进行音量操作
    private void getDoNotDisturb() {
        NotificationManager notificationManager =
                (NotificationManager) MyApp.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            MyApp.getInstance().getApplicationContext().startActivity(intent);
        }

    }


    //申请电话权限
    private void requestPermiss() {
        if (!AndPermission.hasPermissions(B15PMessAlertActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG})) {
            AndPermission.with(B15PMessAlertActivity.this)
                    .runtime()
                    .permission(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG)
//                            ,Manifest.permission.WRITE_CALL_LOG)
                    .rationale(new Rationale<List<String>>() {
                        @Override
                        public void showRationale(Context context, List<String> data, RequestExecutor executor) {
                            executor.execute();
                        }
                    })
                    .start();
        }

        if (!AndPermission.hasPermissions(B15PMessAlertActivity.this, new String[]{
//                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS})) {
            AndPermission.with(B15PMessAlertActivity.this)
                    .runtime()
                    .permission(
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG)//,Manifest.permission.WRITE_CALL_LOG)
                    .start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


//    /**
//     * 跳转到通知使用权
//     *
//     * @param context
//     * @return
//     */
//    private boolean gotoNotificationAccessSetting(Context context) {
//        try {
//            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            return true;
//        } catch (ActivityNotFoundException e) {
//            try {
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
//                intent.setComponent(cn);
//                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
//                context.startActivity(intent);
//                return true;
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return false;
//        }
//    }
//
//
//    private void registerPhoneStateListener() {
//        CustomPhoneStateListener customPhoneStateListener = new CustomPhoneStateListener(this);
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (telephonyManager != null) {
//            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//        }
//    }

    private void readSocialMsg() {
        if (MyCommandManager.DEVICENAME != null) {

            int pushMsg_qq = AppIC.SData().getIntData("pushMsg_QQ");
            int pushMsg_call = AppIC.SData().getIntData("pushMsg_call");
            int pushMsg_Wx = AppIC.SData().getIntData("pushMsg_Wx");
            int pushMsg_Sms = AppIC.SData().getIntData("pushMsg_Sms");
            int pushMsg_FaceBook = AppIC.SData().getIntData("pushMsg_FaceBook");
            int pushMsg_TwitTer = AppIC.SData().getIntData("pushMsg_TwitTer");
            int pushMsg_WhatsApp = AppIC.SData().getIntData("pushMsg_WhatsApp");
            int pushMsg_Line = AppIC.SData().getIntData("pushMsg_Line");


            b30QQTogg.setChecked(pushMsg_qq == 1);
            b30PhoneTogg.setChecked(pushMsg_call == 1);
            b30WechatTogg.setChecked(pushMsg_Wx == 1);
            b30MessageTogg.setChecked(pushMsg_Sms == 1);
            b30FacebookTogg.setChecked(pushMsg_FaceBook == 1);
            b30TwitterTogg.setChecked(pushMsg_TwitTer == 1);
            b30WhatsAppTogg.setChecked(pushMsg_WhatsApp == 1);
            b30LineTogg.setChecked(pushMsg_Line == 1);


        }
    }

    private void initViews() {
        newSearchTitleTv.setText(getResources().getString(R.string.string_ocial_message));//社交小心哦
        newSearchRightImg1.setVisibility(View.VISIBLE);
        b30SkypeTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30WhatsAppTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30FacebookTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30LinkedTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30TwitterTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30LineTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30WechatTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30QQTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30MessageTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30PhoneTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());


        b30InstagramTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30GmailTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30SnapchartTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
        b30OhterTogg.setOnCheckedChangeListener(new ToggCheckChanageListener());
    }

    @OnClick({R.id.newSearchTitleLeft, R.id.newSearchRightImg1, R.id.msgOpenNitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.newSearchTitleLeft:
                finish();
                break;
            case R.id.newSearchRightImg1:
                startActivity(MessageHelpActivity.class);
                break;
            case R.id.msgOpenNitBtn:    //打开通知
                Intent intentr = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivityForResult(intentr, 1001);
                break;
        }
    }

    //监听
    private class ToggCheckChanageListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed())
                return;
            switch (buttonView.getId()) {
//                case R.id.b30SkypeTogg: //skype
////                    isSkype = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISSkype, isChecked);
//
//                    showLoadingDialog("setting...");
//                    handler.sendEmptyMessageDelayed(0x88, 200);
//                    break;
                case R.id.b30WhatsAppTogg:  //whatspp
//                    isWhats = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISWhatsApp, isChecked);


                    AppIC.SData().setIntData("pushMsg_WhatsApp", isChecked ? 1 : 0);
                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
                case R.id.b30FacebookTogg:  //facebook
//                    isFaceBook = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISFacebook, isChecked);


                    AppIC.SData().setIntData("pushMsg_FaceBook", isChecked ? 1 : 0);

                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
//                case R.id.b30LinkedTogg:    //linked
////                    isLinkin = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISLinkendln, isChecked);
//
//                    showLoadingDialog("setting...");
//                    handler.sendEmptyMessageDelayed(0x88, 200);
//                    break;
                case R.id.b30TwitterTogg:   //twitter
//                    isTwitter = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISTwitter, isChecked);

                    AppIC.SData().setIntData("pushMsg_TwitTer", isChecked ? 1 : 0);

                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
                case R.id.b30LineTogg:  //line
//                    isLine = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISLINE, isChecked);


                    AppIC.SData().setIntData("pushMsg_Line", isChecked ? 1 : 0);

                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
                case R.id.b30WechatTogg:    //wechat
//                    isWeChat = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISWechart, isChecked);

                    AppIC.SData().setIntData("pushMsg_Wx", isChecked ? 1 : 0);
                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
                case R.id.b30QQTogg:    //qq
//                    isQQ = isChecked;
                    //SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISQQ, isChecked);

                    AppIC.SData().setIntData("pushMsg_QQ", isChecked ? 1 : 0);
                    showLoadingDialog("setting...");


                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
                case R.id.b30MessageTogg:   //msg
                    //requestPermiss();
//                    isMsg = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISMsm, isChecked);

                    AppIC.SData().setIntData("pushMsg_Sms", isChecked ? 1 : 0);

                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);
                    break;
                case R.id.b30PhoneTogg: //phone
                    //requestPermiss();
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISPhone, isChecked);
//                    isOpenPhone = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISCallPhone, isChecked);


                    AppIC.SData().setIntData("pushMsg_call", isChecked ? 1 : 0);
                    showLoadingDialog("setting...");
                    handler.sendEmptyMessageDelayed(0x88, 200);

                    break;
                /**
                 * b30InstagramTogg
                 * b30GmailTogg
                 * b30SnapchartTogg
                 * b30OhterTogg
                 */
//                case R.id.b30InstagramTogg:
////                    isb30Instagram = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISInstagram, isChecked);
//
//                    showLoadingDialog("setting...");
//                    handler.sendEmptyMessageDelayed(0x88, 200);
//                    break;
//                case R.id.b30GmailTogg:
////                    isb30Gmail = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISGmail, isChecked);
//
//                    showLoadingDialog("setting...");
//                    handler.sendEmptyMessageDelayed(0x88, 200);
//                    break;
//                case R.id.b30SnapchartTogg:
////                    isb30Snapchart = isChecked;
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISSnapchart, isChecked);
//
//                    showLoadingDialog("setting...");
//                    handler.sendEmptyMessageDelayed(0x88, 200);
//                    break;
//                case R.id.b30OhterTogg:
////                    isb30Ohter = isChecked;
//
//                    SharedPreferencesUtils.setParam(B15PMessAlertActivity.this, Commont.ISOhter, isChecked);
//                    showLoadingDialog("setting...");
//                    handler.sendEmptyMessageDelayed(0x88, 200);
//                    break;

            }


        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0x88:
                    closeLoadingDialog();
//                    setSocailMsg();
                    readSocialMsg();
                    break;
            }
            return false;
        }
    });

}