package com.sunmi.pocketvendor.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.utils.AidlUtil;
import com.sunmi.pocketvendor.utils.ESCUtil;

public class reportDetailActivity extends AppCompatActivity {

    private AppConn appConn;

    LinearLayout lvToken, lvSMS;
    ImageView pi, ptp, etp, gpg, itg, tb, image_resend;
    TextView irDate, irTime, irMerchant, irCashier, irLiveRef, irMeter, irTariff, irAmount, irUnit, irToken,irStatus , irNumber, iReport, smsPhone;
    String title, date, time, merchant, cashier, liveref, meter, tariff, amount, unit, token, status, smsMeterPhone;
    String s1, s2, s3, s4, s5;

    Bitmap logobitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        LoadGUI();
        setText();
    }

    private void LoadGUI(){
        pi              = findViewById(R.id.iv_pi);
        ptp             = findViewById(R.id.iv_ptp);
        etp             = findViewById(R.id.iv_etp);
        gpg             = findViewById(R.id.iv_ggc);
        itg             = findViewById(R.id.iv_igc);
        tb              = findViewById(R.id.iv_tbp);
        iReport         = findViewById(R.id.tv_iReport);
        irDate          = findViewById(R.id.tv_irDate);
        irTime          = findViewById(R.id.tv_irTime);
        irMerchant      = findViewById(R.id.tv_irMerchant);
        irCashier       = findViewById(R.id.tv_irCashier);
        irLiveRef       = findViewById(R.id.tv_irLiveRef);
        irMeter         = findViewById(R.id.tv_irMeter);
        irAmount        = findViewById(R.id.tv_irAmount);
        irToken         = findViewById(R.id.tv_irToken);
        irStatus        = findViewById(R.id.tv_irStatus);
        irNumber        = findViewById(R.id.tv_irNumber);
        smsPhone        = findViewById(R.id.tv_smsPhone);
        image_resend    = findViewById(R.id.iv_resend);

        lvToken         = findViewById(R.id.lv_token);
        lvSMS           = findViewById(R.id.lv_smsphoneno);
    }

    private void setText(){
        title           = getIntent().getStringExtra("title");
        date            = getIntent().getStringExtra("date");
        time            = getIntent().getStringExtra("time");
        merchant        = getIntent().getStringExtra("merchantname");
        cashier         = getIntent().getStringExtra("cashier");
        liveref         = getIntent().getStringExtra("liveref");
        meter           = getIntent().getStringExtra("number");
        amount          = getIntent().getStringExtra("amount");
        status          = getIntent().getStringExtra("status");
        unit            = getIntent().getStringExtra("unit");
        token           = getIntent().getStringExtra("token");
        smsMeterPhone   = getIntent().getStringExtra("smsphone");

        if (meter != null){
//            if (meter.length() == 10){
//                irNumber.setText("Phone No");
//                irMeter.setText(meter.substring(3, 10));
//            } else {
//                irNumber.setText("Meter No");
//                irMeter.setText(meter);
//            }

            if (title.equals("PROGRESIF ZOOM")){
                irMeter.setText("Progresif Zoom");
                smsPhone.setText(smsMeterPhone);

            } else if (title.equals("PROGRESIF TOPUP")){
                irMeter.setText("Progresif Topup");
                smsPhone.setText(meter.substring(3, 10));

            } else if (title.equals("EASI")){
                irMeter.setText("EASI Top-Up");
                smsPhone.setText(meter.substring(3, 10));

            } else if (title.contains("POWER")){
                irMeter.setText(meter);
            }

        } else {
            irNumber.setText("Meter No");
            irMeter.setText(meter);
        }


        irDate      .setText(date);
        irTime      .setText(" ("+time+")");
        irCashier   .setText(cashier);
        irLiveRef   .setText(liveref);
        irMerchant  .setText(merchant);

        amount      = amount.replace("$", "");

        irAmount    .setText("BND " + amount);
        irStatus    .setText(status);

        if (!title.equals("POWERINSTANT")){
            lvToken .setVisibility(View.GONE);
            //lvSMS   .setVisibility(View.GONE);
            iReport .setText(title);

            if (title.equals("PROGRESIF TOPUP")){ // Progresif
                ptp             .setVisibility(View.VISIBLE);
                pi              .setVisibility(View.GONE);
                etp             .setVisibility(View.GONE);
                gpg             .setVisibility(View.GONE);
                itg             .setVisibility(View.GONE);
                tb              .setVisibility(View.GONE);
                image_resend    .setVisibility(View.GONE);
                irNumber.setText("Product");

            } else if (title.equals("PROGRESIF ZOOM")){ // ZOOM
                ptp             .setVisibility(View.VISIBLE);
                ptp.setBackgroundResource(R.drawable.pcsbz);
                pi              .setVisibility(View.GONE);
                etp             .setVisibility(View.GONE);
                gpg             .setVisibility(View.GONE);
                itg             .setVisibility(View.GONE);
                tb              .setVisibility(View.GONE);
                image_resend    .setVisibility(View.VISIBLE);
                irNumber.setText("Product");
                irLiveRef       .setText(liveref.replace("-", ""));

            } else if (title.equals("EASI")){ // Easi
                etp             .setVisibility(View.VISIBLE);
                pi              .setVisibility(View.GONE);
                ptp             .setVisibility(View.GONE);
                gpg             .setVisibility(View.GONE);
                itg             .setVisibility(View.GONE);
                tb              .setVisibility(View.GONE);
                image_resend    .setVisibility(View.GONE);
                irNumber.setText("Product");

            } else if (title.equals("ITUNES (US)")){
                itg             .setVisibility(View.VISIBLE);
                etp             .setVisibility(View.GONE);
                pi              .setVisibility(View.GONE);
                ptp             .setVisibility(View.GONE);
                gpg             .setVisibility(View.GONE);
                tb              .setVisibility(View.GONE);
                image_resend    .setVisibility(View.VISIBLE);
                irNumber        .setText("Product");
                irMeter         .setText("iTunes Gift Card USD " + meter);
                lvSMS           .setVisibility(View.VISIBLE);
                smsPhone        .setText(smsMeterPhone.substring(3, 10));
                irLiveRef       .setText(liveref.replace("-", ""));


            } else if (title.equals("GOOGLEPLAY (US)")){
                gpg             .setVisibility(View.VISIBLE);
                etp             .setVisibility(View.GONE);
                pi              .setVisibility(View.GONE);
                ptp             .setVisibility(View.GONE);
                itg             .setVisibility(View.GONE);
                tb              .setVisibility(View.GONE);
                image_resend    .setVisibility(View.VISIBLE);
                irNumber        .setText("Product");
                irMeter         .setText("GooglePlay Gift Card USD " + meter);
                lvSMS           .setVisibility(View.VISIBLE);
                smsPhone        .setText(smsMeterPhone.substring(3, 10));
                irLiveRef       .setText(liveref.replace("-", ""));

            } else if (title.contains("WIFI")){
                tb                  .setVisibility(View.VISIBLE);
                pi                  .setVisibility(View.GONE);
                ptp                 .setVisibility(View.GONE);
                etp                 .setVisibility(View.GONE);
                gpg                 .setVisibility(View.GONE);
                itg                 .setVisibility(View.GONE);
                image_resend        .setVisibility(View.VISIBLE);
                irNumber            .setText("Product");
                irMeter             .setText("Prepaid Wifi " + meter);
                lvSMS               .setVisibility(View.VISIBLE);
                smsPhone            .setText(smsMeterPhone.substring(3, 10));
                irLiveRef           .setText(liveref.replace("-", ""));

            }

        } else { // PowerInstant
            lvToken             .setVisibility(View.VISIBLE);
            lvSMS               .setVisibility(View.VISIBLE);
            iReport             .setText("POWERInstant");
            pi                  .setVisibility(View.VISIBLE);
            ptp                 .setVisibility(View.GONE);
            etp                 .setVisibility(View.GONE);
            smsPhone            .setText(smsMeterPhone.substring(3, 10));
            image_resend        .setVisibility(View.VISIBLE);
        }

        formatedToken();
        irToken.setText(token);
    }

    private void formatedToken(){
        if (token.length() > 2){
            s1 = token.substring(0, 4);
            s2 = token.substring(4, 8);
            s3 = token.substring(8, 12);
            s4 = token.substring(12, 16);
            s5 = token.substring(16, 20);
            token = s1 +" "+ s2 +" "+ s3 +" "+ s4 +" "+ s5;
        } else {
            token = "0";
        }
    }

    public void irOK(View view){
        finish();
    }

    public void smsresend(View view){
        final ProgressDialog pdialog = new ProgressDialog(reportDetailActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        System.out.println("=== liv ref : " + liveref);
        if (liveref.contains("-")){
            String ref[] = liveref.split("-");

            liveref = ref[0];
            System.out.println("new live ref = " + liveref);

        }

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.refNo = liveref;
        appConn.commandpost = "resendsms";
        appConn.resend(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                System.out.println("=== rd res : " + response.getResult());

                if (appConn.resendFailedcode != null){
                    pdialog.dismiss();
                    alertMessage("Alert!", appConn.resendFailedcode);
                } else {
                    alertMessage("SMS sent!", "An electronic receipt has been sent to this phone no");
                }

            }

            @Override
            public void onError(String error) {
                pdialog.dismiss();
                Toast.makeText(reportDetailActivity.this, "ERROR : " + error, Toast.LENGTH_SHORT).show();
            }
        });

        pdialog.dismiss();

    }

    public void irPrint(View view){
        if (title.equals("POWERINSTANT")){
            // POWERINSTANT PRINT FORMAT
            PIprint();
        } else if (title.contains("ITUNES (US)") || title.equals("GOOGLEPLAY (US)") || title.equals("TELBRU (WIFI)")){
            // ITUNES, GOOGLEPLAY OR TELBRU PRINT FORMAT
            GiftCardprint();
        } else if (title.equals("EASI") || title.equals("PROGRESIF TOPUP") || title.equals("PROGRESIF ZOOM")){
            // PROGRESIF OR EASI PRINT FORMAT
            EASIPCSBprint();
        }
    }

    private void PIprint(){
        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
        logobitmap = scaleImage(logobitmap);

        String top;
        StringBuilder sb = new StringBuilder();
        sb.append("\n"+"POWERInstant\n");
        sb.append("Prepaid Token Number");
        top = sb.toString();

        String tokenStr;
        StringBuilder sbT = new StringBuilder();
        sbT.append(token);
        tokenStr = token;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(reportDetailActivity.this);

        String body;
        StringBuilder sbB = new StringBuilder();
        sbB.append("SMS will be sent to you shortly\n\n");
        sbB.append("Merchant    : " + merchant + "\n");
        sbB.append("              (" + preferences.getString("slot", null) + ")\n");
        sbB.append("Cashier     : " + cashier  + "\n");
        sbB.append("Date & Time : " + date + " | " + time +  "\n");
        sbB.append("Ref no      : " + liveref + "\n");
        sbB.append("Amount      : BND " + amount + "\n");
        sbB.append("Meter no    : " + meter + "\n");
        sbB.append("Mobile no   : " + smsMeterPhone.substring(3, 10) + "\n");
        body = sbB.toString();

        AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
        AidlUtil.getInstance().printText(top, 23, false, false);
        AidlUtil.getInstance().printText(tokenStr, 23, true, false);
        AidlUtil.getInstance().printText(body, 23, false, false);
    }

    private void EASIPCSBprint(){
        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
        logobitmap = scaleImage(logobitmap);

        String top;
        StringBuilder sb = new StringBuilder();
        if (title.equals("PROGRESIF TOPUP")){
            title = "Progresif TopUp";
        } else if (title.equals("PROGRESIF ZOOM")){
            title = "Progresif Zoom";
        } else if (title.equals("EASI")){
            title = "EASI Top-Up";
        }

        sb.append("\n" + title);
        top = sb.toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(reportDetailActivity.this);

        String body;
        StringBuilder sbB = new StringBuilder();
        sbB.append("SMS will be sent to you shortly\n\n");
        sbB.append("Merchant    : " + merchant + "\n");
        sbB.append("              (" + preferences.getString("slot", null) + ")\n");
        sbB.append("Cashier     : " + cashier + "\n");
        sbB.append("Date & Time : " + date + " | " + time + "\n");
        sbB.append("Ref no      : " + liveref + "\n");
        sbB.append("Amount      : BND " + amount + "\n");

        if (title.equals("Progresif Zoom")){
            meter = smsMeterPhone;
        }

        sbB.append("Mobile no   : " + meter.substring(3, 10) + "\n");
        body = sbB.toString();

        AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
        AidlUtil.getInstance().printText(top, 23, true, false);
        AidlUtil.getInstance().printText(body, 23, false, false);
    }

    private void GiftCardprint(){
        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
        logobitmap = scaleImage(logobitmap);
        String prod = "", prodextra = "";

        String top;
        StringBuilder sb = new StringBuilder();
        if (title.equals("ITUNES (US)")){
            title = "iTunes Gift Card";
            prod = "iTunes Gift Card";
            prodextra = "USD " + meter;

        } else if (title.equals("GOOGLEPLAY (US)")){
            title = "GooglePlay Gift Card";
            prod = "GooglePlay Gift";
            prodextra = "Card USD " + meter;


        } else if (title.contains("TELBRU (WIFI)")){
            title = "TelBru Prepaid WiFi";
            prod = "Prepaid WiFi";
            prodextra = meter;
        }
        sb.append("\n" + title);
        top = sb.toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(reportDetailActivity.this);

        String body;
        StringBuilder sbB = new StringBuilder();
        sbB.append("Token will be sent to you\nshortly via SMS\n\n");
        sbB.append("Merchant    : " + merchant + "\n");
        sbB.append("              (" + preferences.getString("slot", null) + ")\n");
        sbB.append("Cashier     : " + cashier + "\n");
        sbB.append("Date & Time : " + date + " | " + time + "\n");
        sbB.append("Ref no      : "+ liveref + "\n");
        sbB.append("Product     : " + prod + "\n");
        sbB.append("              " + prodextra + "\n");
        sbB.append("Price       : BND " + amount + "\n");
        sbB.append("Mobile no   : " + smsMeterPhone.substring(3, 10) + "\n");
        body = sbB.toString();

        AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
        AidlUtil.getInstance().printText(top, 23, true, false);
        AidlUtil.getInstance().printText(body, 23, false, false);
    }

    public Bitmap scaleImage (Bitmap bitmap1){
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        // Set the desired size
        int newWidth = (width/8+1)*8;
        // Calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        // Gets the matrix parameter you want to scale
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, 1);
        // Get a new picture
        Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    public void alertMessage(String title, String eMessage){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(eMessage);
        b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog aDialog = b.create();
        aDialog.show();

    }

    public void tadBack(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
