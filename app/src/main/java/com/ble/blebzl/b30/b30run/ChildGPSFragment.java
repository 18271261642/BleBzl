package com.ble.blebzl.b30.b30run;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ble.blebzl.Commont;
import com.ble.blebzl.MyApp;
import com.ble.blebzl.R;
import com.ble.blebzl.b30.GPSSportHisyory;
import com.ble.blebzl.bean.AmapSportBean;
import com.ble.blebzl.bleutil.MyCommandManager;
import com.ble.blebzl.bzlmaps.gaodemaps.AmapHistorySportActivity;
import com.ble.blebzl.bzlmaps.gaodemaps.AmapSportActivity;
import com.ble.blebzl.siswatch.LazyFragment;
import com.ble.blebzl.siswatch.adapter.OutDoorSportAdapterNew;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import org.litepal.LitePal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Admin
 * Date 2018/12/13
 */
public class ChildGPSFragment extends LazyFragment implements OutDoorSportAdapterNew.OnOutDoorSportItemClickListener {

    private static final String TAG = "ChildGPSFragment";

    View view;
    Unbinder unbinder;


    //当天运动汇总的数据
    @BindView(R.id.gpsSportAllTotalTv)
    TextView gpsSportAllTotalTv;

    //跑步或骑车的总里程
    @BindView(R.id.gpsSportTypeTotalKmTv)
    TextView gpsSportTypeTotalKmTv;

    @BindView(R.id.gpsSportRunTv)
    TextView gpsSportRunTv;
    @BindView(R.id.gpsSportCycleTv)
    TextView gpsSportCycleTv;
    @BindView(R.id.gpsSportRecyclerView)
    RecyclerView gpsSportRecyclerView;
    @BindView(R.id.gpsSportNoDataImg)
    ImageView gpsSportNoDataImg;
    @BindView(R.id.gpsSportTypeTv)
    TextView gpsSportTypeTv;

    //数据集合
    private List<AmapSportBean> runSportList;
    //适配器
    private OutDoorSportAdapterNew outDoorSportAdapter;
    //跑步或骑行的标识 跑步：0;骑行:1
    private int runTag = 0;

    //临时的list
    private List<AmapSportBean> tmpSportList;

    boolean isUnit = true;//是否为公制
    int h9_step_util = 0;

    @BindView(R.id.childGpsHisImg)
    ImageView childGpsHisImg;

    private Context mContext;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:      //计算总距离数
                    BigDecimal bd = (BigDecimal) msg.obj;
                    if (bd == null || getActivity() == null || getActivity().isFinishing())
                        return;
                    if (isUnit) {     //公制
                        gpsSportAllTotalTv.setText(bd.doubleValue() + "km");
                    } else {
                        gpsSportAllTotalTv.setText(WatchUtils.unitToImperial(bd.doubleValue(), 3) + " Mi");
                    }
                    break;
                case 1002:      //跑步或骑行的距离总和
                    BigDecimal bigDecimal = (BigDecimal) msg.obj;
                    if (bigDecimal == null || getActivity() == null || getActivity().isFinishing())
                        return;

                    if (isUnit) {     //公制
                        gpsSportTypeTotalKmTv.setText(bigDecimal.doubleValue() + "km");
                    } else {
                        gpsSportTypeTotalKmTv.setText(WatchUtils.unitToImperial(bigDecimal.doubleValue(), 3) + " Mi");
                    }
                    break;
            }

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "------onCreate-------");
        runSportList = new ArrayList<>();
        tmpSportList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gps_sport_layout, container, false);
        unbinder = ButterKnife.bind(this, view);


        initViews();

        initData();


        return view;
    }

    private Context getmContext(){
        return mContext == null ? MyApp.getContext():mContext;
    }


    @Override
    public void onResume() {
        super.onResume();
        //findSportData(runTag);
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        Log.e(TAG, "--------isVisible----=" + isVisible);
        if (isVisible)
            findSportData(runTag);
    }

    //查询保存的运动数据
    private void findSportData(int code) {
        if (getActivity() == null || getActivity().isFinishing())
            return;
        isUnit = (boolean) SharedPreferencesUtils.getParam(getmContext(), Commont.ISSystem, true);
        gpsSportAllTotalTv.setText(isUnit?"0 km":"0 mi");
        h9_step_util = (int) SharedPreferencesUtils.getParam(getmContext(), "H9_UTIT", 0);
        try {
            runSportList.clear();
            tmpSportList.clear();
            String bleMac = (String) SharedPreferencesUtils.readObject(getmContext(), Commont.BLEMAC);
            String userId = (String) SharedPreferencesUtils.readObject(getmContext(), Commont.USER_ID_DATA);
            if (WatchUtils.isEmpty(userId)) return;

            String where = "rtc = ? and userId = ?";
            List<AmapSportBean> sportMapsList = LitePal.where(where,WatchUtils.getCurrentDate(),userId).find(AmapSportBean.class);

            if (sportMapsList == null || sportMapsList.size() == 0) {
                gpsSportRecyclerView.setVisibility(View.GONE);
                gpsSportNoDataImg.setVisibility(View.VISIBLE);

                if ((!TextUtils.isEmpty(MyCommandManager.DEVICENAME) && MyCommandManager.DEVICENAME.equals("H9")) ||
                        !TextUtils.isEmpty(MyCommandManager.DEVICENAME) && MyCommandManager.DEVICENAME.equals("W06X")) {
                    // 0位公制 1为英制
                    if (h9_step_util == 0) {
                        gpsSportTypeTotalKmTv.setText("0.0km");
                    } else {
                        gpsSportTypeTotalKmTv.setText("0.0mi");
                    }

                } else {
                    if (isUnit) {     //公制
                        gpsSportTypeTotalKmTv.setText("0.0 km");
                    } else {
                        gpsSportTypeTotalKmTv.setText("0.0 mi");
                    }
                }
                return;
            }
            //计算跑步或骑行的总里程
            BigDecimal bigDecimal = new BigDecimal("0.00");
            //计算全部的总里程
            BigDecimal totalBig = new BigDecimal("0.00");

            for (AmapSportBean sportMaps : sportMapsList) {
                //计算所有的距离
                String disStr = sportMaps.getCountDisance();
                BigDecimal totalBigD = new BigDecimal(disStr);
                totalBig = totalBigD.add(totalBig);
                if (sportMaps.getSportType() == code) {        //判断类型，跑步还是骑行
                    tmpSportList.add(sportMaps);
                    //计算总距离
                    BigDecimal bigD = new BigDecimal(disStr);
                    bigDecimal = bigD.add(bigDecimal);
                }
            }

            Message msg = new Message();
            msg.what = 1001;
            msg.obj = totalBig;
            handler.sendMessage(msg);

            gpsSportNoDataImg.setVisibility(View.GONE);
            //判断跑步或骑行是否有数据
            if (tmpSportList.size() == 0) {
                gpsSportRecyclerView.setVisibility(View.GONE);
                gpsSportNoDataImg.setVisibility(View.VISIBLE);

                if ((!TextUtils.isEmpty(MyCommandManager.DEVICENAME) && MyCommandManager.DEVICENAME.equals("H9")) ||
                        !TextUtils.isEmpty(MyCommandManager.DEVICENAME) && MyCommandManager.DEVICENAME.equals("W06X")) {
                    // 0位公制 1为英制
                    if (h9_step_util == 0) {
                        gpsSportTypeTotalKmTv.setText("0.0km");
                    } else {
                        gpsSportTypeTotalKmTv.setText("0.0mi");
                    }

                } else {
                    if (isUnit) {     //公制
                        gpsSportTypeTotalKmTv.setText("0.0 km");
                    } else {
                        gpsSportTypeTotalKmTv.setText("0.0 mi");
                    }
                }

                return;
            }
            gpsSportRecyclerView.setVisibility(View.VISIBLE);
            runSportList.addAll(tmpSportList);
            if (outDoorSportAdapter != null) {
                outDoorSportAdapter.notifyDataSetChanged();
            }
            Message message = handler.obtainMessage();
            message.what = 1002;
            message.obj = bigDecimal;
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gpsSportRecyclerView.setLayoutManager(linearLayoutManager);

        outDoorSportAdapter = new OutDoorSportAdapterNew(runSportList, getActivity());
        gpsSportRecyclerView.setAdapter(outDoorSportAdapter);
        outDoorSportAdapter.setListener(this);

    }

    private void initViews() {
        String bleName = WatchUtils.getSherpBleName(getmContext());
        if(!WatchUtils.isEmpty(bleName)){
            if(bleName.equals("W31") || bleName.equals("W30")){
                childGpsHisImg.setVisibility(View.VISIBLE);
            }
        }
        clearClickTvStyle(0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "------onDestroyView-------");
        unbinder.unbind();
    }

    @OnClick({R.id.gpsSportRunImg, R.id.gpsSportCycleImg,
            R.id.gpsSportRunTv, R.id.gpsSportCycleTv,R.id.childGpsHisImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gpsSportRunImg:   //开始跑步
                Intent intent1 = new Intent(getContext(), AmapSportActivity.class);
                intent1.putExtra("sport_type",0);
                startActivity(intent1);

                break;
            case R.id.gpsSportCycleImg: //开始骑行
                Intent intent2 = new Intent(getContext(),AmapSportActivity.class);
                intent2.putExtra("sport_type",1);
                startActivity(intent2);

                break;
            case R.id.gpsSportRunTv:    //
                runTag = 0;
                clearClickTvStyle(runTag);
                break;
            case R.id.gpsSportCycleTv:
                runTag = 1;
                clearClickTvStyle(runTag);
                break;
            case R.id.childGpsHisImg:   //历史记录
                startActivity(new Intent(getmContext(), GPSSportHisyory.class));
                break;
        }
    }


    private void clearClickTvStyle(int code) {
        try {
            if (gpsSportRunTv != null) {

                gpsSportRunTv.setBackgroundResource(R.drawable.newh9data_selecte_text_shap);
                gpsSportRunTv.setTextColor(Color.parseColor("#333333"));
            }

            if (gpsSportCycleTv != null) {
                gpsSportCycleTv.setBackgroundResource(R.drawable.newh9data_selecte_text_shap);
                gpsSportCycleTv.setTextColor(Color.parseColor("#333333"));
            }


            switch (code) {
                case 0:
                    if (gpsSportRunTv == null)
                        return;
                    gpsSportTypeTv.setText(getResources().getString(R.string.string_sport_all_data));
                    gpsSportRunTv.setTextColor(getResources().getColor(R.color.white));
                    gpsSportRunTv.setBackgroundResource(R.drawable.newh9data_unselecte_text_shap);
                    break;
                case 1:
                    if (gpsSportCycleTv == null)
                        return;
                    gpsSportTypeTv.setText(getResources().getString(R.string.string_run_all_data));
                    gpsSportCycleTv.setTextColor(getResources().getColor(R.color.white));
                    gpsSportCycleTv.setBackgroundResource(R.drawable.newh9data_unselecte_text_shap);
                    break;
            }

            findSportData(code);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //item的点击
    @Override
    public void doItemClick(int position) {

        if(runSportList.size()==0)
            return;
        String indexStr = new Gson().toJson(runSportList.get(position));
        Intent intent = new Intent(getActivity(), AmapHistorySportActivity.class);
        intent.putExtra("sport_str", indexStr);
        startActivity(intent);
    }
}
