package com.ble.blebzl.b18;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.KeyEvent;

import com.ble.blebzl.R;
import com.ble.blebzl.adpter.FragmentAdapter;
import com.ble.blebzl.b18.fragment.B18HomeFragment;
import com.ble.blebzl.b30.b30run.ChildGPSFragment;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.siswatch.mine.WatchMineFragment;
import com.ble.blebzl.widget.NoScrollViewPager;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.hplus.bluetooth.BleProfileManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin
 * Date 2019/10/31
 */
public class B18HomeActivity extends WatchBaseActivity {

    private static final String TAG = "B18HomeActivity";


    @BindView(R.id.b18View_pager)
    NoScrollViewPager b18ViewPager;
    @BindView(R.id.b18BottomBar)
    BottomBar b18BottomBar;
    @BindView(R.id.b18Coordinator)
    CoordinatorLayout b18Coordinator;
    @BindView(R.id.b18_home_bottomsheet)
    BottomSheetLayout b18HomeBottomsheet;


    FragmentStatePagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b18_home_layout);
        ButterKnife.bind(this);

        initViews();

        Log.e("B18", "-------isConn=" + BleProfileManager.getInstance().isConnected());

        initData();

    }

    private void initData() {

    }


    private void initViews() {
        fragmentList.add(new B18HomeFragment());
        fragmentList.add(new ChildGPSFragment());
        fragmentList.add(new WatchMineFragment());
        fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        if (b18ViewPager != null) {
            b18ViewPager.setAdapter(fragmentPagerAdapter);
            b18ViewPager.setCurrentItem(0);
        }

        if (b18BottomBar != null)
            b18BottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(int tabId) {
                    switch (tabId) {
                        case R.id.b30_tab_home: //首页
                            b18ViewPager.setCurrentItem(0, false);
                            break;
                        case R.id.b30_tab_set:  //开跑
                            b18ViewPager.setCurrentItem(1, false);
                            break;
                        case R.id.b30_tab_my:   //我的
                            b18ViewPager.setCurrentItem(2, false);
                            break;
                    }
                }

            });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            moveTaskToBack(true);
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}