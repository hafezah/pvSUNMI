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

public class telbruActivity extends BaseActivity {

    private AppConn appConn;

    int firstdigit = 0;
    String prepaid_deno = "", targetTPhone, Tpid, Tmail;

    TextView pre3u, pre3, pre5, pre10, pre15;
    EditText tb_phone;
    ImageView product_logo, clearphone;
    String telb_product;
    Button telbru1;
    LinearLayout lay_telbru;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telbru);

        telb_product = getIntent().getStringExtra("product");

        initView();
        getTfield();

        if (telb_product.equals("prepaidwifi")){
            prepaid_wifi_layout();

        } else {

        }

    }

    private void initView(){

        pre3u           = findViewById(R.id.tv_pre3unlimited);
        pre3            = findViewById(R.id.tv_pre3);
        pre5            = findViewById(R.id.tv_pre5);
        pre10           = findViewById(R.id.tv_pre10);
        pre15           = findViewById(R.id.tv_pre15);

        tb_phone        = findViewById(R.id.et_tbphone);
        product_logo    = findViewById(R.id.iv_telbru);
        clearphone      = findViewById(R.id.cls_tbphone);

        telbru1 = findViewById(R.id.btn_telbru1);
        lay_telbru = findViewById(R.id.lay_telbru1);

        product_logo.setImageResource(R.drawable.telbru);

//        tb_phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lay_telbru.setBackgroundResource(R.drawable.rounded_stroke);
//            }
//        });

        clearphone.setVisibility(View.GONE);
        tb_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tb_phone.length() > 0){
                    clearphone.setVisibility(View.VISIBLE);

//                    if (!prepaid_deno.isEmpty()){
//                        telbru1.setBackgroundResource(R.drawable.rounded_new_button);
//                    }

                } else {
                    clearphone.setVisibility(View.GONE);
                    //telbru1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getTfield(){
        if (getIntent().getStringExtra("qrTPWno") != null){
            targetTPhone = getIntent().getStringExtra("qrTPWno");
            tb_phone.setText(targetTPhone);
            Tpid = getIntent().getStringExtra("pid");
            Tmail = getIntent().getStringExtra("pmail");
        } else {
            targetTPhone = null;
            Tmail = null;
            tb_phone.requestFocus();
        }
    }

    private void prepaid_wifi_layout(){
        product_logo.setImageResource(R.drawable.telprepaid);
    }

    public void bnd3unlimited(View view){
        prepaid_deno = "1";

        pre3u.setBackgroundResource(R.drawable.rounded_solid);
        pre3.setBackgroundResource(R.drawable.rounded_edittext);
        pre5.setBackgroundResource(R.drawable.rounded_edittext);
        pre10.setBackgroundResource(R.drawable.rounded_edittext);
        pre15.setBackgroundResource(R.drawable.rounded_edittext);

        pre3u.setTypeface(null, Typeface.BOLD);
        pre3u.setTextColor(getResources().getColor(R.color.white));
        pre3.setTypeface(null, Typeface.NORMAL);
        pre3.setTextColor(getResources().getColor(R.color.fontcolor));
        pre5.setTypeface(null, Typeface.NORMAL);
        pre5.setTextColor(getResources().getColor(R.color.fontcolor));
        pre10.setTypeface(null, Typeface.NORMAL);
        pre10.setTextColor(getResources().getColor(R.color.fontcolor));
        pre15.setTypeface(null, Typeface.NORMAL);
        pre15.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (tb_phone.getText().length() > 0){
//            telbru1.setBackgroundResource(R.drawable.rounded_new_button);
//            lay_telbru.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            telbru1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void bnd3(View view){
        prepaid_deno = "2";

        pre3u.setBackgroundResource(R.drawable.rounded_edittext);
        pre3.setBackgroundResource(R.drawable.rounded_solid);
        pre5.setBackgroundResource(R.drawable.rounded_edittext);
        pre10.setBackgroundResource(R.drawable.rounded_edittext);
        pre15.setBackgroundResource(R.drawable.rounded_edittext);

        pre3u.setTypeface(null, Typeface.NORMAL);
        pre3u.setTextColor(getResources().getColor(R.color.fontcolor));
        pre3.setTypeface(null, Typeface.BOLD);
        pre3.setTextColor(getResources().getColor(R.color.white));
        pre5.setTypeface(null, Typeface.NORMAL);
        pre5.setTextColor(getResources().getColor(R.color.fontcolor));
        pre10.setTypeface(null, Typeface.NORMAL);
        pre10.setTextColor(getResources().getColor(R.color.fontcolor));
        pre15.setTypeface(null, Typeface.NORMAL);
        pre15.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (tb_phone.getText().length() > 0){
//            telbru1.setBackgroundResource(R.drawable.rounded_new_button);
//            lay_telbru.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            telbru1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void bnd5(View view){
        prepaid_deno = "3";

        pre3u.setBackgroundResource(R.drawable.rounded_edittext);
        pre3.setBackgroundResource(R.drawable.rounded_edittext);
        pre5.setBackgroundResource(R.drawable.rounded_solid);
        pre10.setBackgroundResource(R.drawable.rounded_edittext);
        pre15.setBackgroundResource(R.drawable.rounded_edittext);

        pre3u.setTypeface(null, Typeface.NORMAL);
        pre3u.setTextColor(getResources().getColor(R.color.fontcolor));
        pre3.setTypeface(null, Typeface.NORMAL);
        pre3.setTextColor(getResources().getColor(R.color.fontcolor));
        pre5.setTypeface(null, Typeface.BOLD);
        pre5.setTextColor(getResources().getColor(R.color.white));
        pre10.setTypeface(null, Typeface.NORMAL);
        pre10.setTextColor(getResources().getColor(R.color.fontcolor));
        pre15.setTypeface(null, Typeface.NORMAL);
        pre15.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (tb_phone.getText().length() > 0){
//            telbru1.setBackgroundResource(R.drawable.rounded_new_button);
//            lay_telbru.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            telbru1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void bnd10(View view){
        prepaid_deno = "4";

        pre3u.setBackgroundResource(R.drawable.rounded_edittext);
        pre3.setBackgroundResource(R.drawable.rounded_edittext);
        pre5.setBackgroundResource(R.drawable.rounded_edittext);
        pre10.setBackgroundResource(R.drawable.rounded_solid);
        pre15.setBackgroundResource(R.drawable.rounded_edittext);

        pre3u.setTypeface(null, Typeface.NORMAL);
        pre3u.setTextColor(getResources().getColor(R.color.fontcolor));
        pre3.setTypeface(null, Typeface.NORMAL);
        pre3.setTextColor(getResources().getColor(R.color.fontcolor));
        pre5.setTypeface(null, Typeface.NORMAL);
        pre5.setTextColor(getResources().getColor(R.color.fontcolor));
        pre10.setTypeface(null, Typeface.BOLD);
        pre10.setTextColor(getResources().getColor(R.color.white));
        pre15.setTypeface(null, Typeface.NORMAL);
        pre15.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (tb_phone.getText().length() > 0){
//            telbru1.setBackgroundResource(R.drawable.rounded_new_button);
//            lay_telbru.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            telbru1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void bnd15(View view){
        prepaid_deno = "5";

        pre3u.setBackgroundResource(R.drawable.rounded_edittext);
        pre3.setBackgroundResource(R.drawable.rounded_edittext);
        pre5.setBackgroundResource(R.drawable.rounded_edittext);
        pre10.setBackgroundResource(R.drawable.rounded_edittext);
        pre15.setBackgroundResource(R.drawable.rounded_solid);

        pre3u.setTypeface(null, Typeface.NORMAL);
        pre3u.setTextColor(getResources().getColor(R.color.fontcolor));
        pre3.setTypeface(null, Typeface.NORMAL);
        pre3.setTextColor(getResources().getColor(R.color.fontcolor));
        pre5.setTypeface(null, Typeface.NORMAL);
        pre5.setTextColor(getResources().getColor(R.color.fontcolor));
        pre10.setTypeface(null, Typeface.NORMAL);
        pre10.setTextColor(getResources().getColor(R.color.fontcolor));
        pre15.setTypeface(null, Typeface.BOLD);
        pre15.setTextColor(getResources().getColor(R.color.white));

//        if (tb_phone.getText().length() > 0){
//            telbru1.setBackgroundResource(R.drawable.rounded_new_button);
//            lay_telbru.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            telbru1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void TBClearPhone(View view){
        tb_phone.setText("");
    }

    public void TBNext(View view){
        if (tb_phone.getText().length()>0){
            firstdigit = Integer.parseInt(tb_phone.getText().toString().substring(0, 1));
            System.out.println("=== firstdigit : " + firstdigit);
        }

        if (tb_phone.getText().toString().isEmpty() || tb_phone.getText().toString().equals("")){
            Toast.makeText(telbruActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (tb_phone.length() < 7){
            Toast.makeText(telbruActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
        } else if (prepaid_deno.equals("")){
            Toast.makeText(telbruActivity.this, "Please select prepaid amount", Toast.LENGTH_SHORT).show();
        } else if (firstdigit < 7 || firstdigit > 8){
            Toast.makeText(telbruActivity.this, "Please enter a valid phone (8)", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog pdialog = new ProgressDialog(telbruActivity.this);
            pdialog.setMessage("Loading, Please wait...");
            pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdialog.setCancelable(false);
            pdialog.show();

            appConn = new AppConn();
            appConn.deno_amount = prepaid_deno;
            appConn.deno_type = "telbruprepaidwifi";
            appConn.webLink = Global.URL;
            appConn.commandpost = "denominationcheck";
            appConn.denoChecker(new ResponseListener() {
                @Override
                public void onResponse(Response response) {
                    if (appConn.denoFailedcode != null){
                        pdialog.dismiss();
                        Toast.makeText(telbruActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                    } else {
                        if (appConn.res_deno.equals("yes")){
                            pdialog.dismiss();
                            //Toast.makeText(telbruActivity.this, "return : " + appConn.res_denobnd, Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(telbruActivity.this, telbruConfirm.class)
                                    .putExtra("tbphone", tb_phone.getText().toString())
                                    .putExtra("tbproduct", "prepaidwifi")
                                    .putExtra("tbproductcode", prepaid_deno)
                                    .putExtra("tbdesc", appConn.res_deno_desc)
                                    .putExtra("tbdeno", appConn.res_denobnd)
                                    .putExtra("tpid", Tpid)
                                    .putExtra("tpmail", Tmail));

                        } else {
                            pdialog.dismiss();
                            Toast.makeText(telbruActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e("QueryString","denoError : " + error);
                }
            });










        }

    }

    public void telbruBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(telbruActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(telbruActivity.this);
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
                    startActivity(new Intent(telbruActivity.this, navMainMenuActivity.class)
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
