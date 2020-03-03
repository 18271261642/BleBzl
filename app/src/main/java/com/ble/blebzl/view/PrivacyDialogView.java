package com.ble.blebzl.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.ble.blebzl.R;

/**
 * Created by Admin
 * Date 2020/1/11
 */
public class PrivacyDialogView extends Dialog implements View.OnClickListener {

    private Button cancleBtn,sureBtn;

    private OnPirvacyClickListener onPirvacyClickListener;

    public void setOnPirvacyClickListener(OnPirvacyClickListener onPirvacyClickListener) {
        this.onPirvacyClickListener = onPirvacyClickListener;
    }

    public PrivacyDialogView(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_dialog_view);

        initViews();

        String url = "file:///android_asset/privacy_zh.html";

    }

    private void initViews() {
        cancleBtn = findViewById(R.id.privacyCancleBtn);
        sureBtn = findViewById(R.id.privacySureBtn);
        cancleBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.privacyCancleBtn: //取消
                if(onPirvacyClickListener != null){
                    onPirvacyClickListener.disAgreeView();
                    cancel();
                }
                break;
            case R.id.privacySureBtn:
                if(onPirvacyClickListener != null){
                    onPirvacyClickListener.disCancleView();
                    cancel();
                }
                break;
        }
    }

    public interface OnPirvacyClickListener{
        void disCancleView();

        void disAgreeView();
    }
}
