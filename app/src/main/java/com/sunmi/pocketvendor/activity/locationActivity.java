package com.sunmi.pocketvendor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;

public class locationActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

    }

    public void locBack(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
