package com.ble.blebzl.b31.hrv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ble.blebzl.R;
import com.ble.blebzl.b31.bpoxy.enums.EnumGlossary;


/**
 * Created by Administrator on 2019/1/2.
 */

public class GlossaryActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpgloassay_activity);
    }

    public void onOSHAHS(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.OSHAHS.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onBREATHBREAK(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.BREATHBREAK.getValue();
        intent.putExtra("type", value);
        startActivity(intent);


    }

    public void onLOWOXGEN(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.LOWOXGEN.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onHEART(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.HEART.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onRATEVARABLE(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.RATEVARABLE.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onSLEEP(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.SLEEP.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onLOWREAMIN(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.LOWREAMIN.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onSLEEPBREATHBREAKTIP(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.SLEEPBREATHBREAKTIP.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onBREATH(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.BREATH.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }

    public void onOXGEN(View view) {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryDetailActivity.class);
        int value = EnumGlossary.OXGEN.getValue();
        intent.putExtra("type", value);
        startActivity(intent);
    }
}
