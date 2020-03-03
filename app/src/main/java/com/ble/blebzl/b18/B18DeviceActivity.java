package com.ble.blebzl.b18;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ble.blebzl.R;
import com.ble.blebzl.b18.fragment.B18DeviceFragment;
import com.ble.blebzl.siswatch.WatchBaseActivity;


/**
 * Created by Admin
 * Date 2019/11/10
 */
public class B18DeviceActivity extends WatchBaseActivity {


    private FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b18_device_layout);



        initViews();


    }

    private void initViews() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.b18DeviceFragment,new B18DeviceFragment());
        fragmentTransaction.commit();
    }


}
