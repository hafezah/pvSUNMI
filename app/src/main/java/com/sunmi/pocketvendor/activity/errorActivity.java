package com.sunmi.pocketvendor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sunmi.pocketvendor.R;

public class errorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

    }

    public void NoInternetOk(View view){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(errorActivity.this);
        preferences.edit().clear().apply();
        startActivity(new Intent(this, splashActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
