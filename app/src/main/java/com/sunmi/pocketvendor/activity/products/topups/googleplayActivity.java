package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.errorActivity;
import com.sunmi.pocketvendor.activity.navMainMenuActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

public class googleplayActivity extends BaseActivity {

    private AppConn appConn;
    TextView deno5, deno10, deno15,  deno25;
    LinearLayout laygoogle, lv5, lv10, lv15, lv25;
    EditText etgp_phoneno;
    ImageView clearGP;
    Button google1;

    private String us5, us10, us15, us25, gp_phone, requestamt = "", selected_deno;
     String targetGPphone, gpPid, gpmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googleplay);

        init();
    }

    private void init(){
        etgp_phoneno = findViewById(R.id.et_gpphone);

        deno5  = findViewById(R.id.tv_gp0);
        deno10 = findViewById(R.id.tv_gp1);
        deno15 = findViewById(R.id.tv_gp2);
        deno25 = findViewById(R.id.tv_gp3);

        lv5  = findViewById(R.id.lv_gp5);
        lv10 = findViewById(R.id.lv_gp10);
        lv15 = findViewById(R.id.lv_gp15);
        lv25 = findViewById(R.id.lv_gp25);

        laygoogle = findViewById(R.id.lay_google);
        google1 = findViewById(R.id.btn_google1);

        clearGP = findViewById(R.id.cls_gpphone);

        deno5.setText("");
        deno10.setText("");
        deno15.setText("");
        deno25.setText("");

        getGOOGLEfield();
        getdeno();

//        etgp_phoneno.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                laygoogle.setBackgroundResource(R.drawable.rounded_stroke);
//            }
//        });

        clearGP.setVisibility(View.GONE);
        etgp_phoneno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etgp_phoneno.length() > 0){
                    clearGP.setVisibility(View.VISIBLE);

//                    if (!requestamt.isEmpty()){
//                        google1.setBackgroundResource(R.drawable.rounded_new_button);
//                    }

                } else {
                    clearGP.setVisibility(View.GONE);
                    //google1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getGOOGLEfield(){
        if (getIntent().getStringExtra("qrGOOGLEno") != null){
            targetGPphone = getIntent().getStringExtra("qrGOOGLEno");
            gpPid = getIntent().getStringExtra("pid");
            gpmail = getIntent().getStringExtra("pmail");

            etgp_phoneno.setText(targetGPphone);
        } else {
            targetGPphone = null;
            gpPid = null;
            gpmail = null;
        }
    }

    private void getdeno(){
        final ProgressDialog pdialog = new ProgressDialog(googleplayActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        appConn = new AppConn();
        appConn.deno_amount = "10";
        appConn.deno_type = "googleplayus";
        appConn.webLink = Global.URL;
        appConn.commandpost = "denominationcheck";
        appConn.denoChecker(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.denoFailedcode != null){
                    pdialog.dismiss();
                    Toast.makeText(googleplayActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                } else {

                    if (appConn.res_deno.equals("yes")){
                        //pdialog.dismiss();
                        us10 = appConn.res_denobnd;
                        deno10.setText("BND " + us10);

                    } else {
                        pdialog.dismiss();
                        deno10.setTextColor(getResources().getColor(R.color.colorAccent));
                        deno10.setTextSize(11);
                        deno10.setText("UNAVAILABLE");
                        deno10.setEnabled(false);
                        deno10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(googleplayActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Toast.makeText(googleplayActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                    }
                    //
                    appConn = new AppConn();
                    appConn.deno_amount = "15";
                    appConn.deno_type = "googleplayus";
                    appConn.webLink = Global.URL;
                    appConn.commandpost = "denominationcheck";
                    appConn.denoChecker(new ResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            if (appConn.denoFailedcode != null){
                                pdialog.dismiss();
                                Toast.makeText(googleplayActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                            } else {

                                if (appConn.res_deno.equals("yes")){
                                    //pdialog.dismiss();
                                    us15 = appConn.res_denobnd;
                                    deno15.setText("BND " + us15);

                                } else {
                                    pdialog.dismiss();
                                    deno15.setTextColor(getResources().getColor(R.color.colorAccent));
                                    deno15.setTextSize(11);
                                    deno15.setText("UNAVAILABLE");
                                    deno15.setEnabled(false);
                                    deno15.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(googleplayActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //Toast.makeText(googleplayActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                                }
                                //
                                appConn = new AppConn();
                                appConn.deno_amount = "25";
                                appConn.deno_type = "googleplayus";
                                appConn.webLink = Global.URL;
                                appConn.commandpost = "denominationcheck";
                                appConn.denoChecker(new ResponseListener() {
                                    @Override
                                    public void onResponse(Response response) {
                                        if (appConn.denoFailedcode != null){
                                            pdialog.dismiss();
                                            Toast.makeText(googleplayActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                                        } else {

                                            if (appConn.res_deno.equals("yes")){
                                                pdialog.dismiss();
                                                us25 = appConn.res_denobnd;
                                                deno25.setText("BND " + us25);

                                            } else {
                                                pdialog.dismiss();
                                                deno25.setTextColor(getResources().getColor(R.color.colorAccent));
                                                deno25.setTextSize(11);
                                                deno25.setText("UNAVAILABLE");
                                                deno25.setEnabled(false);
                                                deno25.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Toast.makeText(googleplayActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //Toast.makeText(googleplayActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                                            }

                                            ////////////////////////////////////////////////////////
                                            appConn = new AppConn();
                                            appConn.deno_amount = "5";
                                            appConn.deno_type = "googleplayus";
                                            appConn.webLink = Global.URL;
                                            appConn.commandpost = "denominationcheck";
                                            appConn.denoChecker(new ResponseListener() {
                                                @Override
                                                public void onResponse(Response response) {
                                                    if (appConn.denoFailedcode != null){
                                                        pdialog.dismiss();
                                                        Toast.makeText(googleplayActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        if (appConn.res_deno.equals("yes")){
                                                            pdialog.dismiss();
                                                            us5 = appConn.res_denobnd;
                                                            deno5.setText("BND " + us5);

                                                        } else {
                                                            pdialog.dismiss();
                                                            deno25.setTextColor(getResources().getColor(R.color.colorAccent));
                                                            deno25.setTextSize(11);
                                                            deno25.setText("UNAVAILABLE");
                                                            deno25.setEnabled(false);
                                                            deno25.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Toast.makeText(googleplayActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    Toast.makeText(googleplayActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(googleplayActivity.this, errorActivity.class));
                                                }
                                            });
                                            ////////////////////////////////////////////////////////


                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Toast.makeText(googleplayActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(googleplayActivity.this, errorActivity.class));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(googleplayActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(googleplayActivity.this, errorActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(googleplayActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(googleplayActivity.this, errorActivity.class));
            }
        });
    }

    public void gp5(View view){
        lv5.setBackgroundResource(R.drawable.new_gcard_button);
        lv10.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv15.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv25.setBackgroundResource(R.drawable.gcard_buttonbg);

        deno5.setTypeface(null, Typeface.BOLD);
        deno5.setTextColor(getResources().getColor(R.color.white));
        deno10.setTypeface(null, Typeface.NORMAL);
        deno10.setTextColor(getResources().getColor(R.color.fontcolor));
        deno15.setTypeface(null, Typeface.NORMAL);
        deno15.setTextColor(getResources().getColor(R.color.fontcolor));
        deno25.setTypeface(null, Typeface.NORMAL);
        deno25.setTextColor(getResources().getColor(R.color.fontcolor));

        selected_deno = "0";
        requestamt = us5;

//        if (etgp_phoneno.getText().length() > 0){
//            google1.setBackgroundResource(R.drawable.rounded_new_button);
//            laygoogle.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            google1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void gp10(View view){
        lv5.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv10.setBackgroundResource(R.drawable.new_gcard_button);
        lv15.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv25.setBackgroundResource(R.drawable.gcard_buttonbg);

        deno5.setTypeface(null, Typeface.NORMAL);
        deno5.setTextColor(getResources().getColor(R.color.fontcolor));
        deno10.setTypeface(null, Typeface.BOLD);
        deno10.setTextColor(getResources().getColor(R.color.white));
        deno15.setTypeface(null, Typeface.NORMAL);
        deno15.setTextColor(getResources().getColor(R.color.fontcolor));
        deno25.setTypeface(null, Typeface.NORMAL);
        deno25.setTextColor(getResources().getColor(R.color.fontcolor));

        selected_deno = "1";
        requestamt = us10;

//        if (etgp_phoneno.getText().length() > 0){
//            google1.setBackgroundResource(R.drawable.rounded_new_button);
//            laygoogle.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            google1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void gp15(View view){
        lv5.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv10.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv15.setBackgroundResource(R.drawable.new_gcard_button);
        lv25.setBackgroundResource(R.drawable.gcard_buttonbg);

        deno5.setTypeface(null, Typeface.NORMAL);
        deno5.setTextColor(getResources().getColor(R.color.fontcolor));
        deno10.setTypeface(null, Typeface.NORMAL);
        deno10.setTextColor(getResources().getColor(R.color.fontcolor));
        deno15.setTypeface(null, Typeface.BOLD);
        deno15.setTextColor(getResources().getColor(R.color.white));
        deno25.setTypeface(null, Typeface.NORMAL);
        deno25.setTextColor(getResources().getColor(R.color.fontcolor));

        selected_deno = "2";
        requestamt = us15;

//        if (etgp_phoneno.getText().length() > 0){
//            google1.setBackgroundResource(R.drawable.rounded_new_button);
//            laygoogle.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            google1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void gp25(View view){
        lv5.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv10.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv15.setBackgroundResource(R.drawable.gcard_buttonbg);
        lv25.setBackgroundResource(R.drawable.new_gcard_button);

        deno5.setTypeface(null, Typeface.NORMAL);
        deno5.setTextColor(getResources().getColor(R.color.fontcolor));
        deno10.setTypeface(null, Typeface.NORMAL);
        deno10.setTextColor(getResources().getColor(R.color.fontcolor));
        deno15.setTypeface(null, Typeface.NORMAL);
        deno15.setTextColor(getResources().getColor(R.color.fontcolor));
        deno25.setTypeface(null, Typeface.BOLD);
        deno25.setTextColor(getResources().getColor(R.color.white));

        selected_deno = "3";
        requestamt = us25;

//        if (etgp_phoneno.getText().length() > 0){
//            google1.setBackgroundResource(R.drawable.rounded_new_button);
//            laygoogle.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            google1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void GClearPhone(View view){
        etgp_phoneno.setText("");

    }

    public void GNext(View view){
        gp_phone = etgp_phoneno.getText().toString();
        if (gp_phone.isEmpty()){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (gp_phone.length() < 7){
            Toast.makeText(this, "Please a vaild 7 phone number", Toast.LENGTH_SHORT).show();
        } else if (!gp_phone.substring(0, 1).equals("8") && !gp_phone.substring(0, 1).equals("7")){
            Toast.makeText(this, "Please a vaild phone number", Toast.LENGTH_SHORT).show();
        } else if (requestamt == null || requestamt.equals("") || requestamt.isEmpty()){
            Toast.makeText(this, "Please select Top-Up amount", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, googleplayConfirm.class)
                    .putExtra("gpphone", gp_phone)
                    .putExtra("gpamt", requestamt)
                    .putExtra("gpselect", selected_deno)
                    .putExtra("gpuspid", gpPid)
                    .putExtra("gpusmail", gpmail));
        }

    }

    public void gpBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(googleplayActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(googleplayActivity.this);
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
                    startActivity(new Intent(googleplayActivity.this, navMainMenuActivity.class)
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
