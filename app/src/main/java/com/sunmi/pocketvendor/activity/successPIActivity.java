package com.sunmi.pocketvendor.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.utils.AidlUtil;
import com.sunmi.pocketvendor.utils.ESCUtil;

public class successPIActivity extends Activity {

    private AppConn appConn;

    String meter , amount, token, transdate, transtime, refno, powerunit, smsphone;
    private String s1, s2, s3, s4, s5;
    Bitmap logobitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_pi);

        meter = getIntent().getStringExtra("meter");
        amount = getIntent().getStringExtra("amount");
        token = getIntent().getStringExtra("token");
        smsphone = getIntent().getStringExtra("smsphone");
        transdate = getIntent().getStringExtra("trans_date");
        transtime = getIntent().getStringExtra("trans_time");
        refno = getIntent().getStringExtra("refno");
        powerunit = getIntent().getStringExtra("powerunit");
    }

    @Override
    public void onBackPressed() {
        //
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

    public void printPI(View view){
        token = getIntent().getStringExtra("token");
        formatedToken();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
        logobitmap = scaleImage(logobitmap);

        String top;
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + "POWERInstant\n");
        sb.append("Prepaid Token Number");
        top = sb.toString();

        String tokenStr;
        StringBuilder sbT = new StringBuilder();
        sbT.append(token);
        tokenStr = sbT.toString();

        String body;
        StringBuilder sbB = new StringBuilder();
        sbB.append("SMS will be sent to you shortly\n\n");
        sbB.append("Merchant    : " + preferences.getString("merchantname", null) + "\n");
        sbB.append("              (" + preferences.getString("slot", null) + ")\n");
        sbB.append("Cashier     : " + preferences.getString("cashiername", null)  + "\n");
        sbB.append("Date & Time : " + transdate + " | " + transtime.substring(0, 5) +"\n");
        sbB.append("Ref no      : " + refno + "\n");
        sbB.append("Amount      : " + amount +"\n");
        sbB.append("Meter no    : " + meter + "\n");
        sbB.append("Mobile no   : " + smsphone + "\n");
        body = sbB.toString();

        String bottom;
        StringBuilder sbM = new StringBuilder();
        sbM.append("YOUR QR CODE HERE\n\n");
        sbM.append("The QR code contains the\n`following\n");
        sbM.append("- Meter number : 37120627874\n\n");
        sbM.append("Use the QR code for your next\npurchase\n\n");
        bottom = sbM.toString();

        AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
        AidlUtil.getInstance().printText(top, 23, false, false);
        AidlUtil.getInstance().printText(tokenStr, 23, true, false);
        AidlUtil.getInstance().printText(body, 23, false, false);
        //AidlUtil.getInstance().printQr("37120627874", 5,30);
        //AidlUtil.getInstance().printText(bottom, 23, false, false);
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

    public void dismissTransPI(View view){
        final ProgressDialog pdialog = new ProgressDialog(successPIActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(successPIActivity.this);
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
                    startActivity(new Intent(successPIActivity.this, navMainMenuActivity.class)
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
                    finishAffinity();
                }
            }

            @Override
            public void onError(String error) {
                pdialog.dismiss();
                finish();
            }
        });
    }
}
