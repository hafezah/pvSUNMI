package com.sunmi.pocketvendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.easiActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class supportActivity extends BaseActivity {

    private AppConn appConn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        log("SUPPORT ACTIVITY");
    }

    public void suppBack(View view){
        exit();
    }

    public void senderrorlogs(View view){
        // get logcat
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("I/")){  // V/ is verbose, D/ is debug, I/ is info, W/ is warn, E/ is error, A/ is assert
                    log.append(line + "\n");
                }
            }

            System.out.println("==>> error logs : " + log.toString());

        } catch (IOException e) {
            // Handle Exception
        }

        // clear logcat
        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void getsupport(View view){
        startActivity(new Intent(supportActivity.this, DevLogcatActivity.class));
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(supportActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(supportActivity.this);
        //
        appConn = new AppConn();
        appConn.versionBuild = Global.VER;
        appConn.merchant_id = preferences.getString("m_id", null);
        appConn.terminal_id = preferences.getString("t_id", null);
        appConn.terminal_imei = preferences.getString("t_imei", null);
        appConn.terminal_pin = preferences.getString("t_pin", null);
        appConn.uuid = preferences.getString("uuid", null);
        appConn.webLink = Global.URL;
        appConn.commandpost = "login";
        appConn.cardAccess(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                pdialog.dismiss();
                if (appConn.cardFailedcode != null){
                    finish();
                } else {
                    startActivity(new Intent(supportActivity.this, navMainMenuActivity.class)
                            .putExtra("sPower"      , appConn.res_powerinstant)
                            .putExtra("sPCSB"       , appConn.res_progresif)
                            .putExtra("sPCSBZ"       , appConn.res_zoom)
                            .putExtra("sEasi"       , appConn.res_easi)
                            .putExtra("sGooglePlayUS", appConn.res_googleplayus)
                            .putExtra("sITunesUS"   , appConn.res_itunesus)
                            .putExtra("sTelbruPrepaidWIFI"   , appConn.res_telbruprepaidwifi)

                            .putExtra("statuspi"    , appConn.stat_powerinstant)
                            .putExtra("statusp"     , appConn.stat_progresif)
                            .putExtra("statuspz"     , appConn.stat_zoom)
                            .putExtra("statuse"     , appConn.stat_easi)
                            .putExtra("statusgp"    , appConn.stat_googleplayus)
                            .putExtra("statusit"    , appConn.stat_itunesus)
                            .putExtra("statustbpw"   , appConn.sta_telbruprepaidwifi));
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                pdialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
