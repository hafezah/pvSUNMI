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
import android.util.Log;
import android.view.View;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.easiActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.utils.AidlUtil;
import com.sunmi.pocketvendor.utils.ESCUtil;

public class successActivity extends Activity {

    private AppConn appConn;

    String title, phoneno, transdate, transtime, refno, amount;
    //int amount;

    Bitmap logobitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Long tsLong = System.currentTimeMillis()/1000;
        Log.i("PVSUNMI/", "S(" + tsLong.toString() + ")");

        title     = getIntent().getStringExtra("title");
        phoneno   = getIntent().getStringExtra("phone");
        amount    = getIntent().getStringExtra("amount");
        transdate = getIntent().getStringExtra("trans_date");
        transtime = getIntent().getStringExtra("trans_time");
        refno     = getIntent().getStringExtra("refno");
    }

    @Override
    public void onBackPressed() {
        //
    }

    public void dismissTrans(View view){
        final ProgressDialog pdialog = new ProgressDialog(successActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(successActivity.this);
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
                    startActivity(new Intent(successActivity.this, navMainMenuActivity.class)
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

    public void printTrans(View view){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
        logobitmap = scaleImage(logobitmap);

        if (title.contains("iTunes") || title.contains("GooglePlay") || title.contains("TelBru Prepaid WiFi")){
            String prodextra = "", prod = "";

            String top;
            StringBuilder sb = new StringBuilder();
            if (title.contains("iTunes")){
                title     = "iTunes Gift Card";
                prod      = title;
                prodextra = "USD " + getIntent().getStringExtra("product");

            } else if (title.contains("Google")){
                title     = "GooglePlay Gift Card";
                prod      = "GooglePlay Gift";
                prodextra = "Card USD " + getIntent().getStringExtra("product");

            } else if (title.contains("Prepaid WiFi")){
                title     = "TelBru Prepaid WiFi";
                prod      = "Prepaid WiFi ";
                prodextra = getIntent().getStringExtra("product");

            }
            sb.append("\n" + title);
            top = sb.toString();

            String body;
            StringBuilder sbB = new StringBuilder();
            sbB.append("Token will be sent to you\nshortly via SMS\n\n");
            sbB.append("Merchant    : " + preferences.getString("merchantname", null) + "\n");
            sbB.append("              (" + preferences.getString("slot", null) + ")\n");
            sbB.append("Cashier     : " + preferences.getString("cashiername", null) + "\n");
            sbB.append("Date & Time : " + transdate + " | " + transtime.substring(0, 5) + "\n");
            sbB.append("Ref no      : "+ refno.substring(0, 4)  + "\n");
            sbB.append("Product     : " + prod + "\n");
            sbB.append("              " + prodextra + "\n");
            sbB.append("Price       : BND " + amount + "\n");
            sbB.append("Mobile no   : " + phoneno + "\n");
            body = sbB.toString();

            AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
            AidlUtil.getInstance().printText(top, 23, true, false);
            AidlUtil.getInstance().printText(body, 23, false, false);

        } else {
            String top;
            StringBuilder sb = new StringBuilder();
            sb.append("\n" + title);
            top = sb.toString();

            String body;
            StringBuilder sbB = new StringBuilder();
            sbB.append("SMS will be sent to you shortly\n\n");
            sbB.append("Merchant    : " + preferences.getString("merchantname", null) + "\n");
            sbB.append("              (" + preferences.getString("slot", null) + ")\n");
            sbB.append("Cashier     : " + preferences.getString("cashiername", null)  + "\n");
            sbB.append("Date & Time : " + transdate + " | " + transtime.substring(0, 5) + "\n");
            sbB.append("Ref no      : " + refno + "\n");
            sbB.append("Amount      : " + amount.replace("$", "") + "\n");
            sbB.append("Mobile no   : " + phoneno + "\n");
            body = sbB.toString();

            AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
            AidlUtil.getInstance().printText(top, 23, true, false);
            AidlUtil.getInstance().printText(body, 23, false, false);

        }



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
}
