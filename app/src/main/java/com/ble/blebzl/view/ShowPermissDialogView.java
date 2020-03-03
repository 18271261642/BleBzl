package com.ble.blebzl.view;


import android.os.Bundle;
import android.view.View;
import com.ble.blebzl.R;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.util.SharedPreferencesUtils;


/**
 * Created by Admin
 * Date 2020/1/11
 */
public class ShowPermissDialogView extends WatchBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permiss_layout);

        findViewById(R.id.permissBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setParam(ShowPermissDialogView.this,"is_first",true);
               finish();
              //startActivity(NewLoginActivity.class);
            }
        });


    }
}
