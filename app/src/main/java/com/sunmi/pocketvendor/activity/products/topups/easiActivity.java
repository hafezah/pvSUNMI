package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sunmi.pocketvendor.AppLogg;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.cardScanActivity;
import com.sunmi.pocketvendor.activity.errorActivity;
import com.sunmi.pocketvendor.activity.navMainMenuActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

public class easiActivity extends BaseActivity {

    private AppConn appConn;

    String ePhone, denoAmount = "", topupamt = "", targetEPhone, easiPid, easimail;
    EditText easiPhone;
    ImageView clearEP;
    TextView tv_two, tv_five, tv_ten, tv_twenty, tv_fourty, tv_hundred;
    TextView tv_two2, tv_five2, tv_ten2, tv_twenty2, tv_fourty2, tv_hundred2;
    TextView tv_two_disabled;
    Button easi1;
    LinearLayout layeasi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easi);

        AppLogg appLogg = new AppLogg();
        appLogg.getlog(getApplicationContext(), "EASIP");

        LoadGUI();
    }

    public void LoadGUI(){
        tv_two = findViewById(R.id.tv_two);
        tv_five = findViewById(R.id.tv_five);
        tv_ten = findViewById(R.id.tv_ten);
        tv_twenty = findViewById(R.id.tv_twenty);
        tv_fourty = findViewById(R.id.tv_fourty);
        tv_hundred = findViewById(R.id.tv_hundred);

        easiPhone = findViewById(R.id.et_easino);

        tv_two2 = findViewById(R.id.tv_two2);
        tv_five2 = findViewById(R.id.tv_five2);
        tv_ten2 = findViewById(R.id.tv_ten2);
        tv_twenty2 = findViewById(R.id.tv_twenty2);
        tv_fourty2 = findViewById(R.id.tv_fourty2);
        tv_hundred2 = findViewById(R.id.tv_hundred2);
        easi1 = findViewById(R.id.btn_easi1);
        layeasi = findViewById(R.id.lay_easi);

        tv_two_disabled = findViewById(R.id.tv_two_disable);
        // easi 2 dollars control
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(easiActivity.this);
        String global, easi2dollars;
        global = pref.getString("globaleasi2", null);
        easi2dollars = pref.getString("easi2", null);

        if (global.equals("no")){
            tv_two.setVisibility(View.GONE);
            tv_two2.setVisibility(View.GONE);
            tv_two_disabled.setVisibility(View.GONE);
        }

        if (easi2dollars.equals("no")){
            tv_two.setVisibility(View.GONE);
            tv_two2.setVisibility(View.GONE);
            tv_two_disabled.setVisibility(View.VISIBLE);
        }

        clearEP = findViewById(R.id.cls_easiphone);

        clearEP.setVisibility(View.GONE);

//        easiPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                layeasi.setBackgroundResource(R.drawable.rounded_stroke);
//            }
//        });
        easiPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (easiPhone.length() > 0){
                    clearEP.setVisibility(View.VISIBLE);

//                    if (!denoAmount.isEmpty() || !topupamt.isEmpty()){
//                        easi1.setBackgroundResource(R.drawable.rounded_new_button);
//                    }

                } else {
                    clearEP.setVisibility(View.GONE);
                    //easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getEASIfield();
    }

    private void getEASIfield(){
        if (getIntent().getStringExtra("qrEASIno") != null){
            targetEPhone = getIntent().getStringExtra("qrEASIno");
            easiPid = getIntent().getStringExtra("pid");
            easimail = getIntent().getStringExtra("pmail");

            easiPhone.setText(targetEPhone);
        } else {
            targetEPhone = null;
            easiPid = null;
            easimail = null;
        }
    }

    public void isTwo(View view){
        tv_two.setVisibility(View.GONE);
        tv_five.setVisibility(View.VISIBLE);
        tv_ten.setVisibility(View.VISIBLE);
        tv_twenty.setVisibility(View.VISIBLE);
        tv_fourty.setVisibility(View.VISIBLE);
        tv_hundred.setVisibility(View.VISIBLE);

        tv_two2.setVisibility(View.VISIBLE);
        tv_five2.setVisibility(View.GONE);
        tv_ten2.setVisibility(View.GONE);
        tv_twenty2.setVisibility(View.GONE);
        tv_fourty2.setVisibility(View.GONE);
        tv_hundred2.setVisibility(View.GONE);
        //Toast.makeText(this, "You have selected $5 Top-up", Toast.LENGTH_SHORT).show();
        topupamt = "2";
        denoAmount = "1002";

//        if (easiPhone.getText().length() > 0){
//            easi1.setBackgroundResource(R.drawable.rounded_new_button);
//            layeasi.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void isFive(View view){
        tv_two.setVisibility(View.VISIBLE);
        tv_five.setVisibility(View.GONE);
        tv_ten.setVisibility(View.VISIBLE);
        tv_twenty.setVisibility(View.VISIBLE);
        tv_fourty.setVisibility(View.VISIBLE);
        tv_hundred.setVisibility(View.VISIBLE);

        tv_two2.setVisibility(View.GONE);
        tv_five2.setVisibility(View.VISIBLE);
        tv_ten2.setVisibility(View.GONE);
        tv_twenty2.setVisibility(View.GONE);
        tv_fourty2.setVisibility(View.GONE);
        tv_hundred2.setVisibility(View.GONE);
        //Toast.makeText(this, "You have selected $5 Top-up", Toast.LENGTH_SHORT).show();
        topupamt = "5";
        denoAmount = "1005";

//        if (easiPhone.getText().length() > 0){
//            easi1.setBackgroundResource(R.drawable.rounded_new_button);
//            layeasi.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void isTen(View view){
        tv_two.setVisibility(View.VISIBLE);
        tv_five.setVisibility(View.VISIBLE);
        tv_ten.setVisibility(View.GONE);
        tv_twenty.setVisibility(View.VISIBLE);
        tv_fourty.setVisibility(View.VISIBLE);
        tv_hundred.setVisibility(View.VISIBLE);

        tv_two2.setVisibility(View.GONE);
        tv_five2.setVisibility(View.GONE);
        tv_ten2.setVisibility(View.VISIBLE);
        tv_twenty2.setVisibility(View.GONE);
        tv_fourty2.setVisibility(View.GONE);
        tv_hundred2.setVisibility(View.GONE);
        //Toast.makeText(this, "You have selected $10 Top-up", Toast.LENGTH_SHORT).show();
        topupamt = "10";
        denoAmount = "1010";

//        if (easiPhone.getText().length() > 0){
//            easi1.setBackgroundResource(R.drawable.rounded_new_button);
//            layeasi.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void isTwenty(View view){
        tv_two.setVisibility(View.VISIBLE);
        tv_five.setVisibility(View.VISIBLE);
        tv_ten.setVisibility(View.VISIBLE);
        tv_twenty.setVisibility(View.GONE);
        tv_fourty.setVisibility(View.VISIBLE);
        tv_hundred.setVisibility(View.VISIBLE);

        tv_two2.setVisibility(View.GONE);
        tv_five2.setVisibility(View.GONE);
        tv_ten2.setVisibility(View.GONE);
        tv_twenty2.setVisibility(View.VISIBLE);
        tv_fourty2.setVisibility(View.GONE);
        tv_hundred2.setVisibility(View.GONE);
        //Toast.makeText(this, "You have selected $20 Top-up", Toast.LENGTH_SHORT).show();
        topupamt = "20";
        denoAmount = "1020";

//        if (easiPhone.getText().length() > 0){
//            easi1.setBackgroundResource(R.drawable.rounded_new_button);
//            layeasi.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void isFourty(View view){
        tv_two.setVisibility(View.VISIBLE);
        tv_five.setVisibility(View.VISIBLE);
        tv_ten.setVisibility(View.VISIBLE);
        tv_twenty.setVisibility(View.VISIBLE);
        tv_fourty.setVisibility(View.GONE);
        tv_hundred.setVisibility(View.VISIBLE);

        tv_two2.setVisibility(View.GONE);
        tv_five2.setVisibility(View.GONE);
        tv_ten2.setVisibility(View.GONE);
        tv_twenty2.setVisibility(View.GONE);
        tv_fourty2.setVisibility(View.VISIBLE);
        tv_hundred2.setVisibility(View.GONE);
        //Toast.makeText(this, "You have selected $40 Top-up", Toast.LENGTH_SHORT).show();
        topupamt = "40";
        denoAmount = "1040";

//        if (easiPhone.getText().length() > 0){
//            easi1.setBackgroundResource(R.drawable.rounded_new_button);
//            layeasi.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void isHundred(View view){
        tv_two.setVisibility(View.VISIBLE);
        tv_five.setVisibility(View.VISIBLE);
        tv_ten.setVisibility(View.VISIBLE);
        tv_twenty.setVisibility(View.VISIBLE);
        tv_fourty.setVisibility(View.VISIBLE);
        tv_hundred.setVisibility(View.GONE);

        tv_two2.setVisibility(View.GONE);
        tv_five2.setVisibility(View.GONE);
        tv_ten2.setVisibility(View.GONE);
        tv_twenty2.setVisibility(View.GONE);
        tv_fourty2.setVisibility(View.GONE);
        tv_hundred2.setVisibility(View.VISIBLE);
        //Toast.makeText(this, "You have selected $100 Top-up", Toast.LENGTH_SHORT).show();
        topupamt = "100";
        denoAmount = "1100";

//        if (easiPhone.getText().length() > 0){
//            easi1.setBackgroundResource(R.drawable.rounded_new_button);
//            layeasi.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            easi1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void isTwoDisable(View view){
        Toast.makeText(easiActivity.this, "ERROR : This service is disabled", Toast.LENGTH_SHORT).show();
    }

    public void ENext(View view){
        ePhone = easiPhone.getText().toString();
        if (ePhone.isEmpty()){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (ePhone.length() < 7){
            Toast.makeText(this, "Incorrect phone no. length", Toast.LENGTH_SHORT).show();
        } else if (topupamt.equals("") && denoAmount.equals("")){
            Toast.makeText(this, "Select Top-up amount", Toast.LENGTH_SHORT).show();
        } else {
            String first = easiPhone.getText().toString().substring(0, 1);
            String second = easiPhone.getText().toString().substring(0, 2);
            if ((first.equals("7")) || second.equals("86") || second.equals("87") || second.equals("88") || second.equals("89")) {
                startActivity(new Intent(this, easiConfirm.class)
                        .putExtra("phoneeasi", easiPhone.getText().toString())
                        .putExtra("topupamt", topupamt)
                        .putExtra("denoAmount", denoAmount)
                        .putExtra("easipid", easiPid)
                        .putExtra("easiPmail", easimail));
            } else {
                Toast.makeText(this, "Please enter a valid DST phone number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void EClearPhone(View view){
        easiPhone.setText("");
    }

    public void easiBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(easiActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(easiActivity.this);
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
                    startActivity(new Intent(easiActivity.this, navMainMenuActivity.class)
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
