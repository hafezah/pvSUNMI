package com.sunmi.pocketvendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sunmi.pocketvendor.activity.splashActivity;

public class BaseActivity extends AppCompatActivity implements LogoutListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApp)getApplication()).registerSessionListener(this);
        ((BaseApp)getApplication()).startUserSession();
    }

    public void log(String activity){
        Log.i("PVSUNMI/", activity);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        ((BaseApp)getApplication()).onUserInteracted();
    }

    @Override
    public void onSessionLogout() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit()
                .clear()
                .apply();

        finish();
        startActivity(new Intent(this, splashActivity.class));
        //System.exit(0);
    }
}
