package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.sunmi.pocketvendor.activity.navMainMenuActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

public class progresifActivity extends BaseActivity {

    private AppConn appConn;

    LinearLayout lpnno, lpamt, lvPrepaid, lvZoom;
    TextView title, tvtext, tvZ4, tvZ8, tvZ35;
    EditText phonePCSB, amountPCSB;
    Button next;
    String getTitle, getType, targetPPhone, pcsbPid, pcsbMail, serviceNo;
    int i = 0;
    ImageView pcsbLogo, clearPP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresif);

        title = findViewById(R.id.tv_pcsbtitle);
        getTitle = getIntent().getStringExtra("title");
        title.setText(getTitle);

        lpnno = findViewById(R.id.lay_pcsbno);
        lpamt = findViewById(R.id.lay_pcsbamt);
        lvPrepaid   = findViewById(R.id.lv_prepaid);
        lvZoom      = findViewById(R.id.lv_zoom);
        tvtext      = findViewById(R.id.tv_txtpcsb);
        tvZ4        = findViewById(R.id.tv_z4);
        tvZ8        = findViewById(R.id.tv_z8);
        tvZ35       = findViewById(R.id.tv_z35);
        phonePCSB   = findViewById(R.id.et_pcsbphone);
        amountPCSB  = findViewById(R.id.et_pcsbamount);
        pcsbLogo    = findViewById(R.id.iv_pcsblogo);
        clearPP     = findViewById(R.id.cls_pcsbphone);
        next        = findViewById(R.id.btn_pcsbNext);

        clearPP.setVisibility(View.GONE);
//        phonePCSB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lpnno.setBackgroundResource(R.drawable.rounded_stroke);
//                lpamt.setBackgroundResource(R.drawable.rounded_edittext);
//            }
//        });
//
//        amountPCSB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lpamt.setBackgroundResource(R.drawable.rounded_stroke);
//                lpnno.setBackgroundResource(R.drawable.rounded_edittext);
//                next.setBackgroundResource(R.drawable.rounded_new_button);
//            }
//        });
        phonePCSB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (phonePCSB.length() > 0){
                    clearPP.setVisibility(View.VISIBLE);
                } else {
                    clearPP.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getPCSBfield();
    }

    private void getPCSBfield(){
        getType = getIntent().getStringExtra("pcsbtype");
        if (getType.equals("PCSB")){
            lvPrepaid.setVisibility(View.VISIBLE);
            lvZoom.setVisibility(View.GONE);
            pcsbLogo.setBackgroundResource(R.drawable.ptp);
            tvtext.setText("Phone No:");

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PNext();
                }
            });

        } else if (getType.equals("pcsbzoom")){
            lvPrepaid.setVisibility(View.GONE);
            lvZoom.setVisibility(View.VISIBLE);
            pcsbLogo.setBackgroundResource(R.drawable.pcsbz);
            tvtext.setText("Phone No: (to receive PIN)");

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZNext();
                }
            });

        }

        if (getIntent().getStringExtra("qrPCSBno") != null){
            targetPPhone = getIntent().getStringExtra("qrPCSBno");
            //phonePCSB.setText(targetPPhone);
            phonePCSB.setText("***" + targetPPhone.substring(3, targetPPhone.length()));
            pcsbPid = getIntent().getStringExtra("pid");
            pcsbMail = getIntent().getStringExtra("pmail");
            amountPCSB.requestFocus();
        } else {
            targetPPhone = null;
            pcsbMail = null;
            phonePCSB.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void PClearPhone(View view){
        phonePCSB.setText("");
    }

    public void progresifBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(progresifActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(progresifActivity.this);
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
                    startActivity(new Intent(progresifActivity.this, navMainMenuActivity.class)
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

    public void z4(View view){
        i = 4;
        serviceNo = "4";
        tvZ4.setBackgroundResource(R.drawable.rounded_solid);
        tvZ8.setBackgroundResource(R.drawable.rounded_edittext);
        tvZ35.setBackgroundResource(R.drawable.rounded_edittext);

        tvZ4.setTypeface(null, Typeface.BOLD);
        tvZ4.setTextColor(getResources().getColor(R.color.white));
        tvZ8.setTypeface(null, Typeface.NORMAL);
        tvZ8.setTextColor(getResources().getColor(R.color.fontcolor));
        tvZ35.setTypeface(null, Typeface.NORMAL);
        tvZ35.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (phonePCSB.getText().length() > 0){
//            next.setBackgroundResource(R.drawable.rounded_new_button);
//            lpnno.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            next.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void z8(View view){
        i = 8;
        serviceNo = "8";
        tvZ8.setBackgroundResource(R.drawable.rounded_solid);
        tvZ4.setBackgroundResource(R.drawable.rounded_edittext);
        tvZ35.setBackgroundResource(R.drawable.rounded_edittext);

        tvZ8.setTypeface(null, Typeface.BOLD);
        tvZ8.setTextColor(getResources().getColor(R.color.white));
        tvZ4.setTypeface(null, Typeface.NORMAL);
        tvZ4.setTextColor(getResources().getColor(R.color.fontcolor));
        tvZ35.setTypeface(null, Typeface.NORMAL);
        tvZ35.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (phonePCSB.getText().length() > 0){
//            next.setBackgroundResource(R.drawable.rounded_new_button);
//            lpnno.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            next.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void z35(View view){
        i = 35;
        serviceNo = "35";
        tvZ35.setBackgroundResource(R.drawable.rounded_solid);
        tvZ35.setTextColor(getResources().getColor(R.color.white));
        tvZ8.setBackgroundResource(R.drawable.rounded_edittext);
        tvZ4.setBackgroundResource(R.drawable.rounded_edittext);

        tvZ35.setTypeface(null, Typeface.BOLD);
        tvZ35.setTextColor(getResources().getColor(R.color.white));
        tvZ8.setTypeface(null, Typeface.NORMAL);
        tvZ8.setTextColor(getResources().getColor(R.color.fontcolor));
        tvZ4.setTypeface(null, Typeface.NORMAL);
        tvZ4.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (phonePCSB.getText().length() > 0){
//            next.setBackgroundResource(R.drawable.rounded_new_button);
//            lpnno.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            next.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    private void PNext(){
        String userphone;
        if (targetPPhone != null){
            userphone = targetPPhone;
        } else {
            userphone = phonePCSB.getText().toString();
        }

        if (userphone.equals("")){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (amountPCSB.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter top up amount", Toast.LENGTH_SHORT).show();
        } else {
            String first = userphone.substring(0, 2);
            if ((first.equals("81")) || (first.equals("82")) || (first.equals("83"))) {

                if (userphone.length() == 7){

                    i = Integer.parseInt(amountPCSB.getText().toString());

                    if (i < 5 || i> 100){
                        Toast.makeText(this, "Please enter amount ranging $5 - $100 only", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(this, progresifConfirm.class)
                                .putExtra("pcsbPhone", userphone)
                                .putExtra("pcsbAmount", i)
                                .putExtra("pcsbpid", pcsbPid)
                                .putExtra("pcsbmail", pcsbMail)
                                .putExtra("type", getType));
                    }

                } else {
                    Toast.makeText(this, "Invalid mobile number length", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please enter a PROGRESIF phone number", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void ZNext(){
        final String userphone;
        if (targetPPhone != null){
            userphone = targetPPhone;
        } else {
            userphone = phonePCSB.getText().toString();
        }

        if (userphone.equals("")){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (i == 0){
            Toast.makeText(this, "Please select your desired denomination", Toast.LENGTH_SHORT).show();
        } else if (userphone.length() < 7){
            Toast.makeText(this, "Please enter a valid 7 digit number", Toast.LENGTH_SHORT).show();
        } else if (!userphone.substring(0, 1).equals("8") && !userphone.substring(0, 1).equals("7")){
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog pdialog = new ProgressDialog(progresifActivity.this);
            pdialog.setMessage("Loading, Please wait...");
            pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdialog.setCancelable(false);
            pdialog.show();

            appConn = new AppConn();
            appConn.deno_amount = String.valueOf(i);
            appConn.deno_type = "pcsbzoom";
            appConn.webLink = Global.URL;
            appConn.commandpost = "denominationcheck";
            appConn.denoChecker(new ResponseListener() {
                @Override
                public void onResponse(Response response) {
                    if (appConn.denoFailedcode != null){
                        pdialog.dismiss();
                        Toast.makeText(progresifActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                    } else {
                        if (appConn.res_deno.equals("yes")){
                            pdialog.dismiss();
                            //Toast.makeText(telbruActivity.this, "return : " + appConn.res_denobnd, Toast.LENGTH_SHORT).show();
                            System.out.println("===> " + appConn.res_denobnd);
                            System.out.println("===> " + appConn.res_deno_desc);
                            i = Integer.parseInt(appConn.res_denobnd);

                            startActivity(new Intent(progresifActivity.this, progresifConfirm.class)
                                    .putExtra("pcsbPhone", userphone)
                                    .putExtra("pcsbAmount", i)
                                    .putExtra("pcsbpid", pcsbPid)
                                    .putExtra("pcsbmail", pcsbMail)
                                    .putExtra("type", getType)
                                    .putExtra("serviceno", serviceNo));

                        } else {
                            pdialog.dismiss();
                            Toast.makeText(progresifActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e("QueryString","denoError : " + error);
                }
            });
//            startActivity(new Intent(this, progresifConfirm.class)
//                    .putExtra("pcsbPhone", userphone)
//                    .putExtra("pcsbAmount", i)
//                    .putExtra("pcsbpid", pcsbPid)
//                    .putExtra("pcsbmail", pcsbMail)
//                    .putExtra("type", getType));
        }
    }

}
