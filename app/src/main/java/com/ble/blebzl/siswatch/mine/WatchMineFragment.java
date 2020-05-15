package com.ble.blebzl.siswatch.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ble.blebzl.w30s.activity.W30SSettingActivity;
import com.ble.blebzl.rxandroid.CommonSubscriber;
import com.ble.blebzl.rxandroid.SubscriberOnNextListener;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ble.blebzl.Commont;
import com.ble.blebzl.MyApp;
import com.ble.blebzl.R;
import com.ble.blebzl.activity.MyPersonalActivity;
import com.ble.blebzl.b15p.activity.B15PDeviceActivity;
import com.ble.blebzl.b15p.common.B15PContentState;
import com.ble.blebzl.b18.B18DeviceActivity;
import com.ble.blebzl.b30.B30DeviceActivity;
import com.ble.blebzl.b30.B30SysSettingActivity;
import com.ble.blebzl.b31.B31DeviceActivity;
import com.ble.blebzl.bleutil.MyCommandManager;
import com.ble.blebzl.friend.FriendActivity;
import com.ble.blebzl.net.OkHttpObservable;
import com.ble.blebzl.siswatch.LazyFragment;
import com.ble.blebzl.siswatch.NewSearchActivity;
import com.ble.blebzl.siswatch.WatchDeviceActivity;
import com.ble.blebzl.util.ToastUtil;
import com.ble.blebzl.util.URLs;
import com.ble.blebzl.xwatch.XWatchDeviceActivity;
import com.hplus.bluetooth.BleProfileManager;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.tjdL4.tjdmain.Dev;
import com.tjdL4.tjdmain.L4M;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/17.
 */

/**
 *  我的fragmet
 */
public class WatchMineFragment extends LazyFragment {

    View watchMineView;
    Unbinder unbinder;
    //用户昵称
    @BindView(R.id.watch_mine_uname)
    TextView watchMineUname;
    //头像
    @BindView(R.id.watch_mine_userheadImg)
    ImageView watchMineUserheadImg;
    //总公里数
    @BindView(R.id.watch_distanceTv)
    TextView watchDistanceTv;
    //日平均步数
    @BindView(R.id.watch_mine_avageStepsTv)
    TextView watchMineAvageStepsTv;
    //达标天数
    @BindView(R.id.watch_mine_dabiaoTv)
    TextView watchMineDabiaoTv;

    //显示蓝牙名字和地址
    @BindView(R.id.showBleNameTv)
    TextView showBleNameTv;


    private CommonSubscriber commonSubscriber;
    private SubscriberOnNextListener subscriberOnNextListener;

    private String bleName = null;

    String userId = "9278cc399ab147d0ad3ef164ca156bf0";

    private String saveUserId = null;
    private String saveBleMac = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bleName = (String) SharedPreferencesUtils.readObject(MyApp.getContext(), Commont.BLENAME);
        saveUserId = (String) SharedPreferencesUtils.readObject(MyApp.getContext(), Commont.USER_ID_DATA);
        saveBleMac = (String) SharedPreferencesUtils.readObject(MyApp.getContext(), Commont.BLEMAC);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        watchMineView = inflater.inflate(R.layout.fragment_watch_mine, null);
        unbinder = ButterKnife.bind(this, watchMineView);

        initViews();

        initData();

        getMyInfoData();    //获取我的总数


        return watchMineView;
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        if (bleName == null) {
            showBleNameTv.setText(getResources().getString(R.string.string_not_coon));
            return;
        }
        if (MyCommandManager.DEVICENAME == null) {
            showBleNameTv.setText(getResources().getString(R.string.string_not_coon));
            return;
        }

        String bleMac = (String) SharedPreferencesUtils.readObject(MyApp.getContext(), Commont.BLEMAC);
        if (!WatchUtils.isEmpty(bleMac) && bleName.equals("bozlun")) {
            showBleNameTv.setText("H8" + "  " +
                    ((bleMac.length() >= 5) ? bleMac.substring(bleMac.length() - 5, bleMac.length()) : bleMac));
        } else {
            showBleNameTv.setText(bleName + "  " + ((bleMac.length() >= 5) ? bleMac.substring(bleMac.length() - 5, bleMac.length()) : bleMac));
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
    }

    /**
     * 获取距离等信息，包括用户资料
     */
    private void getMyInfoData() {
        String saveBleMac = WatchUtils.getSherpBleMac(getActivity());
        if (saveBleMac == null)
            return;
        String myInfoUrl = Commont.FRIEND_BASE_URL + URLs.myInfo;
        JSONObject js = new JSONObject();
        try {
            js.put("userId", SharedPreferencesUtils.readObject(getActivity(), "userId"));
            js.put("deviceCode", saveBleMac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        commonSubscriber = new CommonSubscriber(subscriberOnNextListener, getActivity());
        OkHttpObservable.getInstance().getData(commonSubscriber, myInfoUrl, js.toString());

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initData() {

        //数据返回
        subscriberOnNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String result) {
                Log.e("mine", "----ssss--result----" + result);
                if (!WatchUtils.isEmpty(result)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject myInfoJsonObject = jsonObject.getJSONObject("data");
                            if (myInfoJsonObject != null) {
                                String distances = myInfoJsonObject.getString("distance");
                                if (WatchUtils.judgeUnit(MyApp.getContext())) {
                                    //总公里数
                                    watchDistanceTv.setText(WatchUtils.resrevedDeci(distances.trim()) + "km");
                                } else {
                                    watchDistanceTv.setText(WatchUtils.doubleunitToImperial(Double.valueOf(distances), 0) + "mi");
                                }

                                String counts = myInfoJsonObject.getString("count");
                                if (!WatchUtils.isEmpty(myInfoJsonObject.getString("count"))) {
                                    //达标天数
                                    watchMineDabiaoTv.setText("" + myInfoJsonObject.getString("count") + getResources().getString(R.string.data_report_day));
                                }
                                String stepNums = myInfoJsonObject.getString("stepNumber");
                                if (!WatchUtils.isEmpty(stepNums)) {
                                    //平均步数
                                    watchMineAvageStepsTv.setText("" + myInfoJsonObject.getString("stepNumber") + getResources().getString(R.string.daily_numberofsteps_default));
                                }

                                //个人资料
                                JSONObject userJson = myInfoJsonObject.getJSONObject("userInfo");
                                if (userJson != null) {
                                    //昵称
                                    watchMineUname.setText("" + userJson.getString("nickname") + "");
                                    //头像
                                    String imgHead = userJson.getString("image");
                                    if (!WatchUtils.isEmpty(imgHead)) {
                                        //头像
                                        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true);
                                        Glide.with(getActivity()).load(imgHead)
                                                .apply(mRequestOptions).into(watchMineUserheadImg);    //头像
                                    }


                                }


                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };

            }

        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.watchMinepersonalData, R.id.watchMineDevice,
            R.id.watchmineSetting, R.id.watch_mine_userheadImg,
            R.id.card_frend, R.id.mineNotiMsgCardView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mineNotiMsgCardView: //消息中心
                if (getActivity() == null)
                    return;
                startActivity(new Intent(getActivity(), NotiMsgFragment.class));
                break;
            case R.id.watch_mine_userheadImg://用户头像点击
            case R.id.watchMinepersonalData:    //个人资料
                startActivity(new Intent(getActivity(), MyPersonalActivity.class));
                break;
            case R.id.watchMineDevice:  //我的设备
                if (MyCommandManager.DEVICENAME != null) {
                    Set<String> set = new HashSet<>(Arrays.asList(WatchUtils.TJ_FilterNamas));
                    if (MyCommandManager.DEVICENAME.equals("bozlun")) {
                        startActivity(new Intent(getActivity(), WatchDeviceActivity.class));
                        return;
                    }
                    if (MyCommandManager.DEVICENAME.equals("B30") || MyCommandManager.DEVICENAME.equals("B36")
                            || MyCommandManager.DEVICENAME.equals("Ringmii") || MyCommandManager.DEVICENAME.equals("B36M")) {    //B30
                        startActivity(new Intent(getActivity(), B30DeviceActivity.class));
                        return;
                    }
                    if (MyCommandManager.DEVICENAME.equals(WatchUtils.B31_NAME)
                            || MyCommandManager.DEVICENAME.equals(WatchUtils.B31S_NAME)
                            || MyCommandManager.DEVICENAME.equals(WatchUtils.S500_NAME)
                            || MyCommandManager.DEVICENAME.contains("YWK") || MyCommandManager.DEVICENAME.equals("SpO2")) {    //B31,B31S,500s
                        startActivity(new Intent(getActivity(), B31DeviceActivity.class));
                        return;
                    }
                    if (set.contains(MyCommandManager.DEVICENAME)) {//b15p
                        startActivity(new Intent(getActivity(), B15PDeviceActivity.class));
                        return;
                    }
                    if(BleProfileManager.getInstance().isConnected()) {    //B18
                        startActivity(new Intent(getActivity(), B18DeviceActivity.class));
                        return;
                    }
                    if(MyCommandManager.DEVICENAME.equals("W30") || MyCommandManager.DEVICENAME.equals("W31") || MyCommandManager.DEVICENAME.equals("W37")){
                        startActivity(new Intent(getActivity(), W30SSettingActivity.class).putExtra("is18i", "W30S"));
                        return;
                    }

                    if(MyCommandManager.DEVICENAME.equals("XWatch") || MyCommandManager.DEVICENAME.equals("SWatch")){
                        startActivity(new Intent(getActivity(), XWatchDeviceActivity.class));
                        return;
                    }

                } else {
                    String bleName = (String) SharedPreferencesUtils.readObject(MyApp.getContext(), Commont.BLENAME);
                    if(WatchUtils.isEmpty(bleName)){
                        startActivity(new Intent(getActivity(),NewSearchActivity.class));
                        getActivity().finish();
                        return;
                    }

                    if (bleName.equals("bozlun")) {
                        //SharedPreferencesUtils.saveObject(MyApp.getContext(), Commont.BLEMAC, "");
                        if (MyApp.getInstance().h8BleManagerInstance().getH8BleService() != null) {
                            MyApp.getInstance().h8BleManagerInstance().getH8BleService().autoConnByMac(false);
                        }

                    }
                    if (bleName.equals(WatchUtils.B15P_BLENAME)) {
                        if (L4M.Get_Connect_flag() == 2) {//已经连接的状态下
                            /**
                             *手动断开  b15p
                             */
                            Dev.RemoteDev_CloseManual();
                        }
                        B15PContentState.getInstance().stopSeachDevices();//停止扫描

                    }

                    if(bleName.equals("W30") || bleName.equals("W31") || bleName.equals("W37")){
                        MyApp.getInstance().getW37BleOperateManager().stopScan();
                    }

                    MyApp.getInstance().getB30ConnStateService().stopAutoConn();
                    MyApp.getInstance().getW37BleOperateManager().stopScan();
                    startActivity(new Intent(getActivity(), NewSearchActivity.class));
                    if (getActivity() != null)
                        getActivity().finish();
                }

                break;
            case R.id.watchmineSetting:  //系统设置

                startActivity(new Intent(getActivity(), B30SysSettingActivity.class));
                break;
            case R.id.card_frend://亲情互动
                String saveUserId = (String) SharedPreferencesUtils.readObject(getActivity(),Commont.USER_ID_DATA);

                if (!WatchUtils.isEmpty(saveUserId) && !saveUserId.equals(userId)) {
                    startActivity(new Intent(getActivity(), FriendActivity.class));
                } else {
                    ToastUtil.showShort(MyApp.getInstance(), getString(R.string.noright));
                }

                break;
        }
    }
}
