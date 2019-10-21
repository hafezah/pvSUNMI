package com.sunmi.pocketvendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.easiActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class accountActivity extends BaseActivity {

    ImageView alert, salesbar, cba_expand;
    TextView creditBalance, statementReport, m_info, current_sales, target_sales,
            comm_product, comm_rate, crlimit, outbal;
    LinearLayout lvsales, lv_comm, lv_accback, lv_creditbased, lv_cba;
    String curDate,  getDate, power_rate, pcsb_rate, easi_rate, easi2_rate, google_rate, itunes_rate, tbpw_rate;
    private AppConn appConn;

    boolean cba = false, cr = false, sr = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        log("ACCOUNT ACTIVITY");

        creditBalance       = findViewById(R.id.tv_mBalance);
        statementReport     = findViewById(R.id.tv_statement);
        m_info              = findViewById(R.id.tv_merchantinfo);
        current_sales       = findViewById(R.id.tv_cs);
        target_sales        = findViewById(R.id.tv_ts);
        comm_product        = findViewById(R.id.tv_comm_product);
        comm_rate           = findViewById(R.id.tv_comm_rate);


        alert               = findViewById(R.id.img_alertlow);
        salesbar            = findViewById(R.id.iv_salesbar);

        lv_comm             = findViewById(R.id.lv_comm_body);
        lvsales             = findViewById(R.id.lv_tSales);

        lv_accback          = findViewById(R.id.lv_accback);
        lv_accback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        lv_cba              = findViewById(R.id.l_cba);
        lv_creditbased      = findViewById(R.id.llcb);
        crlimit             = findViewById(R.id.tv_crlimit);
        outbal              = findViewById(R.id.tv_outbal);
        cba_expand          = findViewById(R.id.iv_cbaexpand);

        lv_comm.setVisibility(View.GONE);
        statementReport.setVisibility(View.GONE);

        getCurrentDate();
        getBalance();
        getcommrate();
    }






    private void getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat cd = new SimpleDateFormat("yyyy-MM-dd");
        curDate = cd.format(cal.getTime());
    }

    private void getBalance(){
        final ProgressDialog pdialog = new ProgressDialog(accountActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.postCashier = preferences.getString("cashieradmin", null);
        appConn.merchant_id = preferences.getString("m_id", null);
        appConn.terminal_id = preferences.getString("t_id", null);
        appConn.commandpost = "accountreport";
        appConn.accountReport(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.accountReportFailedcode != null){
                    pdialog.dismiss();
                    Toast.makeText(accountActivity.this, "account report failed code", Toast.LENGTH_LONG).show();
                } else {

                    //
                    System.out.println("==== reg_date : " + appConn.reg_date);
                    getDate = appConn.reg_date;
                    rentalActivationCheck(getDate, curDate);
                    //

                    creditBalance.setText("$" + String.format("%.2f", appConn.merchat_balance)); // currency 2 dec pt format
                    m_info.setText(appConn.merchantname + " (" + appConn.merchantaccountno + ")");

                    if (appConn.merchat_balance > 50.00){
                        alert.setVisibility(View.INVISIBLE);
                    } else {
                        alert.setVisibility(View.VISIBLE);
                    }

                    System.out.println("=== cs : " + appConn.currentsales);
                    System.out.println("=== ts : " + appConn.merchantsalestargetamount);

                    current_sales.setText("$" + appConn.currentsales);
                    target_sales.setText("$" + appConn.merchantsalestargetamount);
                    salesTracker(appConn.currentsales, appConn.merchantsalestargetamount);

                    if (appConn.acctype.equals("credit")){
                        lv_creditbased.setVisibility(View.GONE);
                        lv_cba.setVisibility(View.VISIBLE);
                        crlimit.setText(appConn.crlimit);
                        outbal.setText(appConn.outstandingbal);

                    } else {
                        lv_cba.setVisibility(View.GONE);
                        lv_creditbased.setVisibility(View.GONE);
                    }

                    pdialog.dismiss();

                }
            }

            @Override
            public void onError(String error) {
                pdialog.dismiss();
                Toast.makeText(accountActivity.this, "error code : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getcommrate(){
        StringBuilder sbproduct = new StringBuilder();
        StringBuilder sbrate = new StringBuilder();

        power_rate  = getIntent().getStringExtra("pi_rate");
        pcsb_rate   = getIntent().getStringExtra("pcsb_rate");
        easi_rate   = getIntent().getStringExtra("easi_rate");
        easi2_rate  = getIntent().getStringExtra("easi2_rate");
        google_rate = getIntent().getStringExtra("googleplay_rate");
        itunes_rate = getIntent().getStringExtra("itunes_rate");
        tbpw_rate   = getIntent().getStringExtra("telbru_rate");

        sbproduct.append("POWERInstant\n\n");
        sbproduct.append("Progresif TopUp\n\n");
        sbproduct.append("EASI Top-Up\n\n");
        sbproduct.append("EASI Top-Up - $2\n\n");
        sbproduct.append("Google Play Card (US)\n\n");
        sbproduct.append("iTunes Gift Card (US)\n\n");
        sbproduct.append("TelBru\n");
        comm_product.setText(sbproduct);

        sbrate.append(power_rate    + "%\n\n");
        sbrate.append(pcsb_rate     + "%\n\n");
        sbrate.append(easi_rate     + "%\n\n");
        sbrate.append(easi2_rate     + "%\n\n");
        sbrate.append(google_rate   + "%\n\n");
        sbrate.append(itunes_rate   + "%\n\n");
        sbrate.append(tbpw_rate     + "%\n");
        comm_rate.setText(sbrate);

    }

    private void rentalActivationCheck(String dateReg, String dateNow){
        String dateInString = dateReg;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        try {
            c.setTime(sdf.parse(dateInString));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.add(Calendar.DATE, 30);  // add 45 days
        sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date resultdate = new Date(c.getTimeInMillis());   // Get new time
        dateInString = sdf.format(resultdate);

        if (dateInString.compareTo(dateNow) < 0 || dateInString.compareTo(dateNow) < -1){
            //rental active
            Log.e("====> Rental Active :- ", "reg date (" + dateReg + ")");
            lvsales.setVisibility(View.VISIBLE);
        } else {
            // rental inactive
            Log.e("Rental Inactive :- ", "reg date (" + dateReg + ")");
            lvsales.setVisibility(View.GONE);
        }

        //

    }

    private void salesTracker(String cs, String ts){
        double cSales = Double.parseDouble(cs);
        double tSales = Double.parseDouble(ts);

        double csPercent;
        csPercent = (cSales * 100)/tSales;

        System.out.println("==== current sales percentage : " + csPercent);

        if (csPercent == 100 || csPercent > 100){
            salesbar.setBackgroundResource(R.drawable.quadrant4);

        } else if (csPercent < 100 && csPercent >= 75){
            salesbar.setBackgroundResource(R.drawable.quadrant3);

        } else if (csPercent < 75 && csPercent >= 50){
            salesbar.setBackgroundResource(R.drawable.quadrant2);

        } else if (csPercent < 50){
            salesbar.setBackgroundResource(R.drawable.quadrant1);

        }

    }






    public void cbaClick(View view){
        if (!cba){
            lv_creditbased.setVisibility(View.VISIBLE);
            cba = true;
            cba_expand.setImageResource(R.drawable.collapes);
        } else {
            lv_creditbased.setVisibility(View.GONE);
            cba = false;
            cba_expand.setImageResource(R.drawable.expand);
        }
    }

    public void crClick(View view){
        if (!cr){
            lv_comm.setVisibility(View.VISIBLE);
            cr = true;
        } else {
            lv_comm.setVisibility(View.GONE);
            cr = false;
        }
    }

    public void srClick(View view){
        if (!sr){
            statementReport.setVisibility(View.VISIBLE);
            sr = true;
        } else {
            statementReport.setVisibility(View.GONE);
            sr = false;
        }
    }

    public void locationScreen2(View view){
        startActivity(new Intent(this, locationActivity.class));
    }

    public void accBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(accountActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(accountActivity.this);
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
                    startActivity(new Intent(accountActivity.this, navMainMenuActivity.class)
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
