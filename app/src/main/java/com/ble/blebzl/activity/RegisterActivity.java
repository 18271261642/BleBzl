package com.ble.blebzl.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.blebzl.Commont;
import com.ble.blebzl.R;
import com.ble.blebzl.bean.UserInfoBean;
import com.ble.blebzl.siswatch.WatchBaseActivity;
import com.ble.blebzl.siswatch.utils.WatchUtils;
import com.ble.blebzl.util.Common;
import com.ble.blebzl.util.Md5Util;
import com.ble.blebzl.util.SharedPreferencesUtils;
import com.ble.blebzl.util.ToastUtil;
import com.ble.blebzl.util.URLs;
import com.ble.blebzl.view.PrivacyActivity;
import com.ble.blebzl.w30s.utils.httputils.RequestPressent;
import com.ble.blebzl.w30s.utils.httputils.RequestView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by thinkpad on 2017/3/4.
 * 邮箱注册页面
 */

public class RegisterActivity extends WatchBaseActivity implements RequestView {


    @BindView(R.id.username_input)
    TextInputLayout usernameInput;

    @BindView(R.id.textinput_password_regster)
    TextInputLayout textinputPassword;

    @BindView(R.id.username_regsiter)
    EditText usernameEdit;

    @BindView(R.id.password_logonregigter)
    EditText passwordEdit;


    @BindView(R.id.login_btn_reger)
    Button loginBtnReger;

    @BindView(R.id.commentB30BackImg)
    ImageView commentB30BackImg;
    @BindView(R.id.commentB30TitleTv)
    TextView commentB30TitleTv;


    ///user/register

    private RequestPressent requestPressent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);
        ButterKnife.bind(this);

        initViews();

        requestPressent = new RequestPressent();
        requestPressent.attach(this);

    }

    private void initViews() {
        commentB30TitleTv.setText(getResources().getString(R.string.user_emil_regsiter));
        commentB30BackImg.setVisibility(View.VISIBLE);
        usernameInput.setHint(getResources().getString(R.string.input_email));

        //初始化底部声明
        String INSURANCE_STATEMENT = getResources().getString(R.string.register_agreement);
        SpannableString spanStatement = new SpannableString(INSURANCE_STATEMENT);
        ClickableSpan clickStatement = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //跳转到协议页面
                startActivity(new Intent(RegisterActivity.this, PrivacyActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        spanStatement.setSpan(clickStatement, 0, INSURANCE_STATEMENT.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanStatement.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0,
                INSURANCE_STATEMENT.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

    }


    @OnClick({R.id.login_btn_reger,R.id.registerForPhoneTv,
            R.id.commentB30BackImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_reger:
                String eMailTxt = usernameEdit.getText().toString();
                String pwdTxt = passwordEdit.getText().toString();
                if (WatchUtils.isEmpty(eMailTxt) || WatchUtils.isEmpty(pwdTxt)) {
                    ToastUtil.showToast(RegisterActivity.this, getResources().getString(R.string.input_user));
                    return;
                }
                if (pwdTxt.length() < 6) {
                    ToastUtil.showToast(this, getResources().getString(R.string.not_b_less));
                    return;
                }
                registerForEmail(eMailTxt, pwdTxt); //邮箱注册
                break;
            case R.id.registerForPhoneTv:   //手机号注册
                startActivity(RegisterActivity2.class);
                break;
            case R.id.commentB30BackImg:
                finish();
                break;
        }
    }


    //邮箱注册
    private void registerForEmail(String uName, String uPwd) {
        if (requestPressent != null) {
            Map<String, String> params = new HashMap<>();
            params.put("phone", uName);
            params.put("pwd", Md5Util.Md532(uPwd));
            params.put("status", "0");
            params.put("type", "1");
            requestPressent.getRequestJSONObject(0x01, Commont.FRIEND_BASE_URL + URLs.myHTTPs, RegisterActivity.this, new Gson().toJson(params), 1);

        }

    }


    @Override
    public void showLoadDialog(int what) {
        showLoadingDialog("Loading...");
    }

    @Override
    public void successData(int what, Object object, int daystag) {
        Log.e("TAG", "----------obj=" + object.toString());
        closeLoadingDialog();
        if (object == null)
            return;
        try {
            JSONObject jsonObject = new JSONObject(object.toString());
            if (!jsonObject.has("code"))
                return;
            if (jsonObject.getInt("code") == 200) {
                String data = jsonObject.getString("data");
                UserInfoBean userInfoBean = new Gson().fromJson(data, UserInfoBean.class);
                if (userInfoBean != null) {
                    Common.customer_id = userInfoBean.getUserid();
                    SharedPreferencesUtils.saveObject(RegisterActivity.this, Commont.USER_ID_DATA, userInfoBean.getUserid());
                    //SharedPreferencesUtils.saveObject(RegisterActivity2.this, "userId", jsonObject.getJSONObject("userInfo").getString("userId"));
                    startActivity(new Intent(RegisterActivity.this, PersonDataActivity.class));
                    finish();
                }
            } else {
                ToastUtil.showToast(RegisterActivity.this, jsonObject.getString("msg"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void failedData(int what, Throwable e) {
        closeLoadingDialog();
        ToastUtil.showToast(RegisterActivity.this, e.getMessage() + "");
    }

    @Override
    public void closeLoadDialog(int what) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestPressent != null)
            requestPressent.detach();
    }
}
