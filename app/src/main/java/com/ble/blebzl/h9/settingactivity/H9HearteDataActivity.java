package com.ble.blebzl.h9.settingactivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.ble.blebzl.B18I.b18iutils.B18iUtils;
import com.ble.blebzl.Commont;
import com.ble.blebzl.R;
import com.ble.blebzl.h9.bean.HeartDataBean;
import com.ble.blebzl.h9.utils.H9HearteDataAdapter;
import com.ble.blebzl.h9.utils.H9TimeUtil;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.ble.blebzl.util.URLs;
import com.ble.blebzl.w30s.utils.httputils.RequestPressent;
import com.ble.blebzl.w30s.utils.httputils.RequestView;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @aboutContent: 心率数据
 * @author： 安
 * @crateTime: 2017/10/31 16:50
 * @mailBox: an.****.life@gmail.com
 * @company: 东莞速成科技有限公司
 */

public class H9HearteDataActivity extends WatchBaseActivity implements RequestView {
    private static final String TAG = "H9HearteDataActivity";
    @BindView(R.id.heartedata_list)
    ListView heartedataList;
    @BindView(R.id.bar_titles)
    TextView barTitles;
    @BindView(R.id.image_data_type)
    ImageView imageDataType;
    private RequestPressent requestPressent;
    List<HeartDataBean.HeartRateBean> heartNewDataList = new ArrayList<>();
    List<String> stringDataList = new ArrayList<>();
    H9HearteDataAdapter dataAdapter = null;
    String is18i;
    @BindView(R.id.bar_mores)
    TextView barMores;
    private String systemDatasss;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h9_hearte_data_activity);
        ButterKnife.bind(this);
        barTitles.setText(getResources().getString(R.string.heart_rate));
        is18i = getIntent().getStringExtra("is18i");
        requestPressent = new RequestPressent();
        requestPressent.attach(this);
    }


    /**
     * 获取心率数据
     */
    private void getHeartData(String time) {
        if (heartNewDataList != null || stringDataList != null) {
            heartNewDataList.clear();
            stringDataList.clear();
            if (dataAdapter != null) {
                dataAdapter.notifyDataSetChanged();
            }
        }
//        initLineCharts();
//        getAxisLables();
//        getAxisPoints();
//        initLineChart();
//        leafLineChart.postInvalidate();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();
        JSONObject jsonObect = new JSONObject();
        map.put("userId", (String) SharedPreferencesUtils.readObject(this, "userId"));
        map.put("deviceCode", (String) SharedPreferencesUtils.readObject(this, Commont.BLEMAC));
        map.put("date", time);
        String mapjson = gson.toJson(map);
        if (requestPressent != null) {
            requestPressent.getRequestJSONObject(502, URLs.HTTPs + URLs.getHeartD, H9HearteDataActivity.this, mapjson, 0);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        systemDatasss = B18iUtils.getSystemDatasss();
        Date dateBeforess = H9TimeUtil.getDateBefore(new Date(), 1);
        String nextDay = H9TimeUtil.getValidDateStr2(dateBeforess);
        getHeartData(nextDay.substring(0, 10));
//        getHeartData(B18iUtils.getSystemDatasss());
    }

    private void setSlecteDateTime() {
        View view = LayoutInflater.from(H9HearteDataActivity.this).inflate(R.layout.h9_pop_date_item, null, false);
        PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(view);
        //设置pop数据
        setPopContent(popupWindow, view);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹出框消失.  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));//new BitmapDrawable()
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        //设置可以点击
        popupWindow.setTouchable(true);
        //从顶部显示
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.TOP, 0, 0);
    }

    @SuppressLint("NewApi")
    private void setPopContent(final PopupWindow popupWindow, View view) {
        view.findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView viewById = (TextView) view.findViewById(R.id.bar_titles);
        viewById.setText(getResources().getString(R.string.history_times));
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.h9_calender);
        calendarView.setEnabled(false);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("----选择的日期是-----", year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                Date dateBeforess = H9TimeUtil.getDateBefore(new Date(), 1);
                String nextDay = H9TimeUtil.getValidDateStr2(dateBeforess);
                String m = "1";
                String d = "1";
                if ((month + 1) <= 9) {
                    m = "0" + (month + 1);
                } else {
                    m = "" + (month + 1);
                }
                if (dayOfMonth <= 9) {
                    d = "0" + dayOfMonth;
                } else {
                    d = "" + dayOfMonth;
                }
                String times = year + "-" + m + "-" + d;
                if (systemDatasss.trim().equals(times.trim())) {
                    getHeartData(nextDay);
                } else {
                    getHeartData(times);
                }
                popupWindow.dismiss();
            }
        });
    }

    @OnClick({R.id.image_back, R.id.bar_mores})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.bar_mores:
                setSlecteDateTime();
                break;
        }
    }


    @Override
    public void showLoadDialog(int what) {
        showLoadingDialog("Loging...");
    }


    /**
     * 去除空数据
     *
     * @param list
     * @return
     */
    public List<HeartDataBean.HeartRateBean> m2(List<HeartDataBean.HeartRateBean> list) {
        Iterator<HeartDataBean.HeartRateBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            HeartDataBean.HeartRateBean integer = iterator.next();

            int heartRate = integer.getHeartRate();
            if (heartRate <= 0) {
                //list.remove(temp);// 出现java.util.ConcurrentModificationException
                iterator.remove();// 推荐使用
            }
        }
        return list;
    }

    @Override
    public void successData(int what, Object result, int daystag) {

        //关闭加载
        closeLoadingDialog();
        if (heartedataList != null)
            heartedataList.setVisibility(View.GONE);
        if (imageDataType != null)
            imageDataType.setVisibility(View.VISIBLE);
        //判断返回是否为空
        if (result != null || !WatchUtils.isEmpty(result.toString())) {
            HeartDataBean heartDataBean = new Gson().fromJson(result.toString(), HeartDataBean.class);
            if (heartDataBean != null) {
                String resultCode = heartDataBean.getResultCode();
                if (!WatchUtils.isEmpty(resultCode) && resultCode.equals("001")) {
                    List<HeartDataBean.HeartRateBean> heartRate = heartDataBean.getHeartRate();
                    if (m2(heartRate) != null && !m2(heartRate).isEmpty()) {
                        //升序排列
                        Collections.sort(m2(heartRate), new Comparator<HeartDataBean.HeartRateBean>() {
                            @Override
                            public int compare(HeartDataBean.HeartRateBean o1, HeartDataBean.HeartRateBean o2) {
                                return o1.getRtc().compareTo(o2.getRtc());
                            }
                        });
                        if (heartedataList != null)
                            heartedataList.setVisibility(View.VISIBLE);
                        if (imageDataType != null)
                            imageDataType.setVisibility(View.GONE);
                        dataAdapter = new H9HearteDataAdapter(H9HearteDataActivity.this, m2(heartRate));
                        heartedataList.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(H9HearteDataActivity.this, getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(H9HearteDataActivity.this, getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(H9HearteDataActivity.this, getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(H9HearteDataActivity.this, getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void failedData(int what, Throwable e) {
        closeLoadingDialog();
        e.getMessage();
    }

    @Override
    public void closeLoadDialog(int what) {
        closeLoadingDialog();
    }
}
