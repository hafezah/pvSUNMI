package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.accountActivity;
import com.sunmi.pocketvendor.activity.navMainMenuActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.utils.AidlUtil;
import com.sunmi.pocketvendor.utils.ESCUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class pocketDepo2 extends AppCompatActivity {

    TextView dbrand, dref, damount, ddate, dtime;
    String getReceiptStr;
    AppConn appConn;

    Bitmap logobitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_depo2);

        dbrand = findViewById(R.id.tv_dbrand);
        dref = findViewById(R.id.tv_dref);
        damount = findViewById(R.id.tv_damount);
        ddate = findViewById(R.id.tv_ddate);
        dtime = findViewById(R.id.tv_dtime);

        getReceiptStr = getIntent().getStringExtra("stamp");

        //<stamped><and>brand<eq>PugusCo<and>ref<eq>D190313000929<and>amount<eq>5.00<and>date<eq>2019-03-13<and>time<eq>15:43:33<and>

        String receipt[] = getReceiptStr.split("<and>");
        if (receipt.length > 0){
            for (String rec : receipt){
                String rspt[] = rec.split("<eq>");
                Log.e("-->pair:", rec);

                if (rspt[0].equals("brand")){
                    dbrand.setText(rspt[1]);

                } else if (rspt[0].equals("ref")){
                    dref.setText(rspt[1]);

                } else if (rspt[0].equals("amount")){
                    damount.setText("Amount deposit : $" + rspt[1]);

                } else if (rspt[0].equals("date")){
                    ddate.setText("(" + rspt[1]);

                } else if (rspt[0].equals("time")){
                    dtime.setText(" / " + rspt[1] + ")");

                }

            }
        }

    }

    public void pocketBack(View view){
        exit();
    }

    public void DPPrint(View view){
//            String top;
//            StringBuilder sb = new StringBuilder();
//            sb.append("\n" + dbrand.getText().toString());
//            top = sb.toString();
//
//            String body;
//            StringBuilder sbB = new StringBuilder();
//            sbB.append("SMS will be sent to you shortly\n\n");
//            sbB.append("Merchant    : " + preferences.getString("merchantname", null) + "\n");
//            sbB.append("Cashier     : " + preferences.getString("cashiername", null)  + "\n");
//            sbB.append("Date & Time : " + transdate + " | " + transtime.substring(0, 5) + "\n");
//            sbB.append("Ref no      : " + refno + "\n");
//            sbB.append("Amount      : " + amount.replace("$", "") + "\n");
//            sbB.append("Mobile no   : " + phoneno + "\n");
//            body = sbB.toString();
//
//            AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
//            AidlUtil.getInstance().printText(top, 23, true, false);
//            AidlUtil.getInstance().printText(body, 23, false, false);


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

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(pocketDepo2.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pocketDepo2.this);
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
                    startActivity(new Intent(pocketDepo2.this, navMainMenuActivity.class)
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
//        super.onBackPressed();
        exit();
    }
}
