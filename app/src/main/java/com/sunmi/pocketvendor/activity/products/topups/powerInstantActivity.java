package com.sunmi.pocketvendor.activity.products.topups;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;
import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.errorActivity;
import com.sunmi.pocketvendor.activity.navMainMenuActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.pvApplication;

import sunmi.sunmiui.utils.LogUtil;

public class powerInstantActivity extends BaseActivity {

    private AppConn appConn;
    EditText meternumber, piphoneno, piamount;
    ImageView meterStat, clearM, clearP;
    LinearLayout linear1, linear2, linear3;
    String meter, powerphone, poweramount;
    String qrMeter, qrSms, powerinstantPid, powerinstantMail;
    String meterCard;
    Button pi1;

    private ReadCardOpt readCardOpt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_instant);

        LoadUI();
        meterFilter();
        phoneNoFilter();
        maxChar(piamount, 3);
        magDetect();
    }

    public void PINext (View view){

        // cancel mag card detect
        try {
            readCardOpt.cancelCheckCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        final ProgressDialog pdialog = new ProgressDialog(powerInstantActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        if (meternumber.getText().toString() != null){
            meter = meternumber.getText().toString();

            if (piphoneno.getText().toString() != null){
                powerphone = piphoneno.getText().toString();
                int length = piphoneno.getText().length();
                if (length > 6 && length < 8){
                    String firstdigit = powerphone.substring(0, 1);
                    if (firstdigit.equals("8") || firstdigit.equals("7")){

                        if (piamount.getText().toString() != null && !piamount.getText().toString().isEmpty()){
                            poweramount = piamount.getText().toString();
                            int amt = Integer.parseInt(poweramount);
                            if (amt > 4 && amt < 401){
                                appConn = new AppConn();
                                appConn.meter_no = meter;
                                appConn.webLink = Global.URL;
                                appConn.commandpost = "metercheck";
                                appConn.meterChecker(new ResponseListener() {
                                    @Override
                                    public void onResponse(Response response) {
                                        if (appConn.meterFailedcode != null) {
                                            pdialog.dismiss();
                                            Toast.makeText(powerInstantActivity.this, "failedcode : " + appConn.meterFailedcode, Toast.LENGTH_SHORT).show();
                                        } else {
                                            // test for valid meter number : 37120107851 / 98t00642

                                            String meterValidity = appConn.res_server;
                                            System.out.println("meterValidity = " + meterValidity);
                                            if (meterValidity.equals("yes")) {
                                                pdialog.dismiss();
                                                meterStat.setImageResource(R.drawable.truetick);

                                                startActivity(new Intent(powerInstantActivity.this, powerInstantConfirm.class)
                                                        .putExtra("name", appConn.res_name)
                                                        .putExtra("meter", meternumber.getText().toString().toUpperCase())
                                                        .putExtra("phoneno", powerphone)
                                                        .putExtra("amount", piamount.getText().toString())
                                                        .putExtra("piPid", powerinstantPid)
                                                        .putExtra("piMail", powerinstantMail));
                                            } else {
                                                pdialog.dismiss();
                                                meterStat.setImageResource(R.drawable.falsetick);
                                                Toast.makeText(powerInstantActivity.this, "Invalid Meter No.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        pdialog.dismiss();
                                        finish();
                                        Toast.makeText(powerInstantActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(powerInstantActivity.this, errorActivity.class));
                                    }
                                });
                                //

                            } else {
                                pdialog.dismiss();
                                Toast.makeText(this, "Amount range: $5 - $400", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            pdialog.dismiss();
                            Toast.makeText(this, "Please enter amount no.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pdialog.dismiss();
                        Toast.makeText(this, "Invalid phone no.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pdialog.dismiss();
                    Toast.makeText(this, "Invalid phone no. length", Toast.LENGTH_SHORT).show();
                }
            } else {
                pdialog.dismiss();
                Toast.makeText(this, "Please enter phone no.", Toast.LENGTH_SHORT).show();
            }
        } else {
            pdialog.dismiss();
            Toast.makeText(this, "Please enter meter no.", Toast.LENGTH_SHORT).show();
        }

    }

    public void LoadUI(){
        meternumber = findViewById(R.id.et_meterno);
        piphoneno = findViewById(R.id.et_piphone);
        piamount = findViewById(R.id.et_piamount);
        meterStat = findViewById(R.id.iv_meterstatus);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        pi1 = findViewById(R.id.btn_pi1);

        clearM = findViewById(R.id.cls_meter);
        clearP = findViewById(R.id.cls_phone);

        clearM.setVisibility(View.GONE);
        clearP.setVisibility(View.GONE);

        readCardOpt = pvApplication.mReadCardOpt;

//        meternumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linear1.setBackgroundResource(R.drawable.rounded_stroke);
//                linear2.setBackgroundResource(R.drawable.rounded_edittext);
//                linear3.setBackgroundResource(R.drawable.rounded_edittext);
//            }
//        });
//
//        piphoneno.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linear1.setBackgroundResource(R.drawable.rounded_edittext);
//                linear2.setBackgroundResource(R.drawable.rounded_stroke);
//                linear3.setBackgroundResource(R.drawable.rounded_edittext);
//            }
//        });
//
//        piamount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linear1.setBackgroundResource(R.drawable.rounded_edittext);
//                linear2.setBackgroundResource(R.drawable.rounded_edittext);
//                linear3.setBackgroundResource(R.drawable.rounded_stroke);
//            }
//        });

        meternumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (meternumber.length() > 0){
                    clearM.setVisibility(View.VISIBLE);
                } else {
                    clearM.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        piphoneno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (piphoneno.length() > 0){
                    clearP.setVisibility(View.VISIBLE);
                } else {
                    clearP.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // QR scanned
        getPIField();
    }

    private void magDetect(){
        System.out.println("=== mag card");
        try {
            readCardOpt.checkCard(AidlConstants.CardType.MAGNETIC.getValue(), readCardCallBack, 30000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ReadCardCallback readCardCallBack = new ReadCardCallback.Stub() {
        @Override
        public void onCardDetected(CardInfo cardInfo) throws RemoteException {
            Message message = new Message();
            message.what = 0x01;
            message.obj = cardInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(int i, String s) throws RemoteException {
            Message message = new Message();
            message.what = 0x03;
            message.obj = i;
            mHandler.sendMessage(message);
        }

        @Override
        public void onStartCheckCard() throws RemoteException {
            LogUtil.e("PIActivity", "onStartCheckCard: reading card...");
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    CardInfo cardInfo = (CardInfo) msg.obj;
                    System.out.println("=== mag card meter: " + cardInfo.track2.substring(6, 17));
                    meterCard = cardInfo.track2.toString().toLowerCase();
                    meternumber.setText(meterCard.substring(6, 17));
                    piphoneno.requestFocus();
                    magDetect();

                    break;
                case 0x02:
                    Toast.makeText(powerInstantActivity.this, "Check Timeout", Toast.LENGTH_SHORT).show();
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {

                            try {
                                readCardOpt.cancelCheckCard();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            System.out.println("RE-SCAN IN " + l/1000 + "s");

                        }

                        @Override
                        public void onFinish() {
                            System.out.println("SCAN YOUR ID CARD TO ACTIVATE");
                            magDetect();
                        }
                    }.start();

                    break;

                case 0x03:
                    //Toast.makeText(powerInstantActivity.this, "Check Failed", Toast.LENGTH_SHORT).show();
                    System.out.println("MAG failed, re-scanning...");
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {

                            try {
                                readCardOpt.cancelCheckCard();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            System.out.println("=== RE-SCAN IN " + l/1000 + "s");

                        }

                        @Override
                        public void onFinish() {
                            System.out.println("SCAN YOUR ID CARD TO ACTIVATE");
                            magDetect();
                        }
                    }.start();

                    break;

            }
        }
    };

    public void getPIField(){
        if (getIntent().getStringExtra("qrMeter") != null){

            try {
                readCardOpt.cancelCheckCard();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            qrMeter = getIntent().getStringExtra("qrMeter");
            meternumber.setText(qrMeter);
            powerinstantPid = getIntent().getStringExtra("pid");
            powerinstantMail = getIntent().getStringExtra("pmail");
            piamount.requestFocus();
        } else {
            qrMeter = null;
            meternumber.requestFocus();
        }

        if (getIntent().getStringExtra("qrSms") != null){
            qrSms = getIntent().getStringExtra("qrSms");
            piphoneno.setText(qrSms);
            piamount.requestFocus();
        } else {
            qrSms = null;
            meternumber.requestFocus();
        }
    }

    public void maxChar(EditText et, int length){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }

    public void meterFilter(){
        meternumber.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.equals("")){
                    return charSequence;
                }
                if (charSequence.toString().matches("[a-zA-Z 0-9]+")){
                    return charSequence;
                }
                return "";
            }
        }});
        maxChar(meternumber, 11);
    }

    public void phoneNoFilter(){
        piphoneno.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.equals("")){
                    return charSequence;
                }
                if (charSequence.toString().matches("[0-9]+")){
                    maxChar(piphoneno, 7);
                    return charSequence;
                }
                return "";
            }
        }});
    }

    public void PiClearMeter(View view){
        meternumber.setText("");
    }

    public void PiClearPhone(View view){
        piphoneno.setText("");
    }

    public void powerBack(View view){
        exit();
    }

    private void exit(){
        try {
            readCardOpt.cancelCheckCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        final ProgressDialog pdialog = new ProgressDialog(powerInstantActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(powerInstantActivity.this);
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
                    startActivity(new Intent(powerInstantActivity.this, navMainMenuActivity.class)
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
