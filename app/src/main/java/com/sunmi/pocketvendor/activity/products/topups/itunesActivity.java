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

public class itunesActivity extends BaseActivity {

    private AppConn appConn;
    TextView it5, it10, it15, it25;
    LinearLayout layitunes, lvit5, lvit10, lvit15, lvit25;
    EditText itphone;
    ImageView clearIT;
    Button itunes1;

    private String us5, us10, us15, us25, it_phone, requestamt = "", selected_deno;
    String targetITphone, itPid, itmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itunes);

        init();

    }

    private void init(){
        itphone = findViewById(R.id.et_itphone);

        it5  = findViewById(R.id.tv_it5);
        it10 = findViewById(R.id.tv_it10);
        it15 = findViewById(R.id.tv_it15);
        it25 = findViewById(R.id.tv_it25);

        lvit5  = findViewById(R.id.lv_it5);
        lvit10 = findViewById(R.id.lv_it10);
        lvit15 = findViewById(R.id.lv_it15);
        lvit25 = findViewById(R.id.lv_it25);

        layitunes = findViewById(R.id.lay_itunes1);
        itunes1 = findViewById(R.id.btn_itunes1);

        clearIT = findViewById(R.id.cls_itbphone);

        it5.setText("");
        it10.setText("");
        it15.setText("");
        it25.setText("");

        getITUNESfield();
        getdeno();

//        itphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                layitunes.setBackgroundResource(R.drawable.rounded_stroke);
//            }
//        });

        clearIT.setVisibility(View.GONE);
        itphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itphone.length() > 0){
                    clearIT.setVisibility(View.VISIBLE);

//                    if (!requestamt.isEmpty()){
//                        itunes1.setBackgroundResource(R.drawable.rounded_new_button);
//                    }

                } else {
                    clearIT.setVisibility(View.GONE);
                    //itunes1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getITUNESfield(){
        if (getIntent().getStringExtra("qrITUNESno") != null){
            targetITphone = getIntent().getStringExtra("qrITUNESno");
            itPid = getIntent().getStringExtra("pid");
            itmail = getIntent().getStringExtra("pmail");

            itphone.setText(targetITphone);
        } else {
            targetITphone = null;
            itPid = null;
            itmail = null;
        }
    }

    private void getdeno(){
        final ProgressDialog pdialog = new ProgressDialog(itunesActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        appConn = new AppConn();
        appConn.deno_amount = "5";
        appConn.deno_type = "itunesus";
        appConn.webLink = Global.URL;
        appConn.commandpost = "denominationcheck";
        appConn.denoChecker(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.denoFailedcode != null){
                    pdialog.dismiss();
                    Toast.makeText(itunesActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();
                } else {

                    if (appConn.res_deno.equals("yes")){
                        //pdialog.dismiss();
                        us5 = appConn.res_denobnd;
                        it5.setText("BND " + us5);

                    } else {
                        pdialog.dismiss();
                        it5.setTextColor(getResources().getColor(R.color.colorAccent));
                        it5.setTextSize(11);
                        it5.setText("UNAVAILABLE");
                        it5.setEnabled(false);
                        it5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(itunesActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Toast.makeText(itunesActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                    }
                }

                appConn = new AppConn();
                appConn.deno_amount = "10";
                appConn.deno_type = "itunesus";
                appConn.webLink = Global.URL;
                appConn.commandpost = "denominationcheck";
                appConn.denoChecker(new ResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (appConn.denoFailedcode != null){
                            pdialog.dismiss();
                            Toast.makeText(itunesActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                        } else {

                            if (appConn.res_deno.equals("yes")){
                                //pdialog.dismiss();
                                us10 = appConn.res_denobnd;
                                it10.setText("BND " + us10);


                            } else {
                                pdialog.dismiss();
                                it10.setTextColor(getResources().getColor(R.color.colorAccent));
                                it10.setTextSize(11);
                                it10.setText("UNAVAILABLE");
                                it10.setEnabled(false);
                                it10.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(itunesActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Toast.makeText(itunesActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                            }
                            //
                            appConn = new AppConn();
                            appConn.deno_amount = "15";
                            appConn.deno_type = "itunesus";
                            appConn.webLink = Global.URL;
                            appConn.commandpost = "denominationcheck";
                            appConn.denoChecker(new ResponseListener() {
                                @Override
                                public void onResponse(Response response) {
                                    if (appConn.denoFailedcode != null){
                                        pdialog.dismiss();
                                        Toast.makeText(itunesActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                                    } else {

                                        if (appConn.res_deno.equals("yes")){
                                            //pdialog.dismiss();
                                            us15 = appConn.res_denobnd;
                                            it15.setText("BND " + us15);



                                        } else {
                                            pdialog.dismiss();
                                            it15.setTextColor(getResources().getColor(R.color.colorAccent));
                                            it15.setTextSize(10);
                                            it15.setText("UNAVAILABLE");
                                            it15.setEnabled(false);
                                            it15.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Toast.makeText(itunesActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            //Toast.makeText(itunesActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                                        }
                                        //
                                        appConn = new AppConn();
                                        appConn.deno_amount = "25";
                                        appConn.deno_type = "itunesus";
                                        appConn.webLink = Global.URL;
                                        appConn.commandpost = "denominationcheck";
                                        appConn.denoChecker(new ResponseListener() {
                                            @Override
                                            public void onResponse(Response response) {
                                                if (appConn.denoFailedcode != null){
                                                    pdialog.dismiss();
                                                    Toast.makeText(itunesActivity.this, "failedcode : " + appConn.denoFailedcode, Toast.LENGTH_SHORT).show();

                                                } else {

                                                    if (appConn.res_deno.equals("yes")){
                                                        pdialog.dismiss();
                                                        us25 = appConn.res_denobnd;
                                                        it25.setText("BND " + us25);
                                                    } else {
                                                        pdialog.dismiss();
                                                        it25.setTextColor(getResources().getColor(R.color.colorAccent));
                                                        it25.setTextSize(10);
                                                        it25.setText("UNAVAILABLE");
                                                        it25.setEnabled(false);
                                                        it25.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Toast.makeText(itunesActivity.this, "This product is currently unavailable", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        //Toast.makeText(itunesActivity.this, "Invalid denomination", Toast.LENGTH_SHORT).show();
                                                    }
                                                    //


                                                }
                                            }

                                            @Override
                                            public void onError(String error) {
                                                Toast.makeText(itunesActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(itunesActivity.this, errorActivity.class));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(itunesActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(itunesActivity.this, errorActivity.class));
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(itunesActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(itunesActivity.this, errorActivity.class));
                    }
                });


            }

            @Override
            public void onError(String error) {
                Toast.makeText(itunesActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(itunesActivity.this, errorActivity.class));
            }
        });



    }

    public void it5(View view){
        lvit5.setBackgroundResource(R.drawable.new_gcard_button);
        lvit10.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit15.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit25.setBackgroundResource(R.drawable.gcard_buttonbg);

        it5.setTypeface(null, Typeface.BOLD);
        it5.setTextColor(getResources().getColor(R.color.white));
        it10.setTypeface(null, Typeface.NORMAL);
        it10.setTextColor(getResources().getColor(R.color.fontcolor));
        it15.setTypeface(null, Typeface.NORMAL);
        it15.setTextColor(getResources().getColor(R.color.fontcolor));
        it25.setTypeface(null, Typeface.NORMAL);
        it25.setTextColor(getResources().getColor(R.color.fontcolor));

        selected_deno = "1";
        requestamt = us5;

//        if (itphone.getText().length() > 0){
//            itunes1.setBackgroundResource(R.drawable.rounded_new_button);
//            layitunes.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            itunes1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void it10(View view){
        lvit5.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit10.setBackgroundResource(R.drawable.new_gcard_button);
        lvit15.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit25.setBackgroundResource(R.drawable.gcard_buttonbg);
        selected_deno = "2";
        requestamt = us10;

        it5.setTypeface(null, Typeface.NORMAL);
        it5.setTextColor(getResources().getColor(R.color.fontcolor));
        it10.setTypeface(null, Typeface.BOLD);
        it10.setTextColor(getResources().getColor(R.color.white));
        it15.setTypeface(null, Typeface.NORMAL);
        it15.setTextColor(getResources().getColor(R.color.fontcolor));
        it25.setTypeface(null, Typeface.NORMAL);
        it25.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (itphone.getText().length() > 0){
//            itunes1.setBackgroundResource(R.drawable.rounded_new_button);
//            layitunes.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            itunes1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void it15(View view){
        lvit5.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit10.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit15.setBackgroundResource(R.drawable.new_gcard_button);
        lvit25.setBackgroundResource(R.drawable.gcard_buttonbg);
        selected_deno = "3";
        requestamt = us15;

        it5.setTypeface(null, Typeface.NORMAL);
        it5.setTextColor(getResources().getColor(R.color.fontcolor));
        it10.setTypeface(null, Typeface.NORMAL);
        it10.setTextColor(getResources().getColor(R.color.fontcolor));
        it15.setTypeface(null, Typeface.BOLD);
        it15.setTextColor(getResources().getColor(R.color.white));
        it25.setTypeface(null, Typeface.NORMAL);
        it25.setTextColor(getResources().getColor(R.color.fontcolor));

//        if (itphone.getText().length() > 0){
//            itunes1.setBackgroundResource(R.drawable.rounded_new_button);
//            layitunes.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            itunes1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void it25(View view){
        lvit5.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit10.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit15.setBackgroundResource(R.drawable.gcard_buttonbg);
        lvit25.setBackgroundResource(R.drawable.new_gcard_button);
        selected_deno = "4";
        requestamt = us25;

        it5.setTypeface(null, Typeface.NORMAL);
        it5.setTextColor(getResources().getColor(R.color.fontcolor));
        it10.setTypeface(null, Typeface.NORMAL);
        it10.setTextColor(getResources().getColor(R.color.fontcolor));
        it15.setTypeface(null, Typeface.NORMAL);
        it15.setTextColor(getResources().getColor(R.color.fontcolor));
        it25.setTypeface(null, Typeface.BOLD);
        it25.setTextColor(getResources().getColor(R.color.white));

//        if (itphone.getText().length() > 0){
//            itunes1.setBackgroundResource(R.drawable.rounded_new_button);
//            layitunes.setBackgroundResource(R.drawable.rounded_edittext);
//        } else {
//            itunes1.setBackgroundResource(R.drawable.rounded_new_inactivebtn);
//        }
    }

    public void IClearPhone(View view){
        itphone.setText("");

    }

    public void INext(View view){
        it_phone = itphone.getText().toString();
        if (it_phone.isEmpty()){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (it_phone.length() < 7){
            Toast.makeText(this, "Please enter a valid 7 digit number", Toast.LENGTH_SHORT).show();
        } else if (!it_phone.substring(0, 1).equals("8") && !it_phone.substring(0, 1).equals("7")){
            Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
        } else if (requestamt == null || requestamt.equals("") || requestamt.isEmpty()){
            Toast.makeText(this, "Please select Top-Up amount", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, itunesConfirm.class)
                    .putExtra("itphone", it_phone)
                    .putExtra("itamt", requestamt)
                    .putExtra("itselect", selected_deno)
                    .putExtra("ituspid", itPid)
                    .putExtra("itusmail", itmail));
        }

    }

    public void itBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(itunesActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(itunesActivity.this);
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
                    startActivity(new Intent(itunesActivity.this, navMainMenuActivity.class)
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
