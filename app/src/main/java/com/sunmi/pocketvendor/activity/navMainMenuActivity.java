package com.sunmi.pocketvendor.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.AppLogg;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.easiActivity;
import com.sunmi.pocketvendor.activity.products.topups.googleplayActivity;
import com.sunmi.pocketvendor.activity.products.topups.itunesActivity;
import com.sunmi.pocketvendor.activity.products.topups.pocketDepo;
import com.sunmi.pocketvendor.activity.products.topups.powerInstantActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class navMainMenuActivity extends BaseActivity {

    private DrawerLayout drawer;
    private final static int INTERVAL = 1000 * 60 * 2;

    boolean doubleBackToExitPressedOnce = false, pcsb = true;
    double accountBalance;
    String serPower, serPcsb, serZoom, serEasi, serGoogleUS, serItunesUS, serTPrepaidWifi,
            getPow, getPro, getZoom, getEas, getGoogleUS, getItunesUS, getTPrepaidWifi;
    String comm_pi, comm_pcsb, comm_easi, comm_easi2, comm_gp, comm_it, comm_tel;
    String mercBalanceState;
    String depin;
    double balance;

    ProgressDialog progressDialog;
    private AppConn appConn;

    int counter = 0;

    ImageView alertAccount;
    TextView ver, mm_merchantname;
    TextView itemtext1, itemtext2, itemtext3, itemtext4, itemtext5, itemtext6;
    LinearLayout layout, item1, item2, item3, item4, item5, item6, llou;

    CountDownTimer refreshTime = new CountDownTimer(INTERVAL, 100){
        public void onTick(long mililisUntilFinished){
        }

        @Override
        public void onFinish() {
            counter = 0;
            getBalanceInterval();
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_mainmenu);


        AppLogg appLogg = new AppLogg();
        appLogg.getlog(getApplicationContext(), "MAIN");

        setToolbar();
        configureNavigationDrawer();

        mm_merchantname = findViewById(R.id.tv_mm_merchant);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(navMainMenuActivity.this);
        if (pref.getString("slot", null).isEmpty()){
            mm_merchantname.setText(pref.getString("merchantname", null).toUpperCase());
        } else {

            mm_merchantname.setText(pref.getString("merchantname", null).toUpperCase() + " (" + pref.getString("slot", null).toUpperCase() + ")");
        }
        //mm_merchantname.setText(pref.getString("merchantname", null).toUpperCase());

        ver = findViewById(R.id.tv_version);
        ver.setText("ver "+ Global.VER);

        item1     = findViewById(R.id.lv_item1);
        item2     = findViewById(R.id.lv_item2);
        item3     = findViewById(R.id.lv_item3);
        item4     = findViewById(R.id.lv_item4);
        item5     = findViewById(R.id.lv_item5);
        item6     = findViewById(R.id.lv_item6);

        llou      = findViewById(R.id.ll_officeuse);

        itemtext1 = findViewById(R.id.tv_item1);
        itemtext2 = findViewById(R.id.tv_item2);
        itemtext3 = findViewById(R.id.tv_item3);
        itemtext4 = findViewById(R.id.tv_item4);
        itemtext5 = findViewById(R.id.tv_item5);
        itemtext6 = findViewById(R.id.tv_item6);

        layout = findViewById(R.id.linear_main);
        if (Global.URL.equals("https://www.threegmedia.com/merchantpostest")){
            Toast.makeText(this, "This device is connected to TEST SERVER", Toast.LENGTH_LONG).show();
            layout.setBackgroundResource(R.drawable.bgtest);
            llou.setVisibility(View.VISIBLE);
            depositcall();
        } else {
            llou.setVisibility(View.GONE);
        }

        // INDIVIDUAL SWITCH
        serPower            = getIntent().getStringExtra("sPower");
        serPcsb             = getIntent().getStringExtra("sPCSB");
        serZoom             = getIntent().getStringExtra("sPCSBZ");
        serEasi             = getIntent().getStringExtra("sEasi");
        serGoogleUS         = getIntent().getStringExtra("sGooglePlayUS");
        serItunesUS         = getIntent().getStringExtra("sITunesUS");
        serTPrepaidWifi     = getIntent().getStringExtra("sTelbruPrepaidWIFI");

        // GLOBAL SWITCH
        getPow              = getIntent().getStringExtra("statuspi");
        getPro              = getIntent().getStringExtra("statusp");
        getZoom             = getIntent().getStringExtra("statuspz");
        getEas              = getIntent().getStringExtra("statuse");
        getGoogleUS         = getIntent().getStringExtra("statusgp");
        getItunesUS         = getIntent().getStringExtra("statusit");
        getTPrepaidWifi     = getIntent().getStringExtra("statustbpw");

        productCheck();
        getBalanceInterval();
        refreshTime.start();
    }


//    MAIN METHODS
    public void productCheck(){
        ImageView iPowerinstant = findViewById(R.id.ivPowerinstant);
        ImageView iProgresif = findViewById(R.id.ivProgresif);
        ImageView iEasi = findViewById(R.id.ivEasi);
        ImageView iGooglePlay = findViewById(R.id.ivGoogle);
        ImageView iItunes = findViewById(R.id.iviTunes);
        ImageView iTelbru = findViewById(R.id.ivTelbru);

        // service product availability
//        if (serPower.equals("no")) {iPowerinstant.setVisibility(View.GONE);     itemtext1.setVisibility(View.GONE);}
//        if (serPcsb.equals("no")) {iProgresif.setVisibility(View.GONE);         itemtext2.setVisibility(View.GONE);}
//        if (serEasi.equals("no")) {iEasi.setVisibility(View.GONE);              itemtext3.setVisibility(View.GONE);}
//        if (serItunesUS.equals("no")) {iItunes.setVisibility(View.GONE);        itemtext4.setVisibility(View.GONE);}
//        if (serGoogleUS.equals("no")) {iGooglePlay.setVisibility(View.GONE);    itemtext5.setVisibility(View.GONE);}
//        if (serTPrepaidWifi.equals("no")){iTelbru.setVisibility(View.GONE);     itemtext6.setVisibility(View.GONE);}

        // Individual switch checker
        ///////////////////////////////////////////////////////////////////////////////////////////
        if (serPower.equals("no"))
        {item1.setVisibility(View.GONE);}

        if (serPcsb.equals("no") && serZoom.equals("no"))
        {item2.setVisibility(View.GONE);}

        if (serEasi.equals("no"))
        {item3.setVisibility(View.GONE);}

        if (serItunesUS.equals("no"))
        {item4.setVisibility(View.GONE);}

        if (serGoogleUS.equals("no"))
        {item5.setVisibility(View.GONE);}

        if (serTPrepaidWifi.equals("no"))
        {item6.setVisibility(View.GONE);}

        // Global switch checker
        ///////////////////////////////////////////////////////////////////////////////////////////
        if (getPow.equals("no"))
        {iPowerinstant.setImageResource(R.drawable.offpower);}

        if (getPro.equals("no") && getZoom.equals("no"))
        {iProgresif.setImageResource(R.drawable.offprogresif);pcsb=false;}

        if (getEas.equals("no"))
        {iEasi.setImageResource(R.drawable.offeasi);}

        if (getGoogleUS.equals("no"))
        {iGooglePlay.setImageResource(R.drawable.offgoogle);}

        if (getItunesUS.equals("no"))
        {iItunes.setImageResource(R.drawable.offitunes);}

        if (getTPrepaidWifi.equals("no"))
        {iTelbru.setImageResource(R.drawable.offtelbru);}

    }

    public void getBalanceInterval(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(navMainMenuActivity.this);

        appConn = new AppConn();
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.webLink = Global.URL;
        appConn.commandpost = "refresh";
        appConn.refreshCheck(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.refreshFailedcode != null){
                    Toast.makeText(navMainMenuActivity.this, "refresh failed code", Toast.LENGTH_LONG).show();
                } else {
                    balance = appConn.merchat_balance;
                    mercBalanceState = appConn.balanceState;
                    String accAlert = appConn.salestarget;
                    if (accAlert.equals("no")){
                        // display acc icon without alert
                        alertAccount.setBackgroundResource(R.drawable.alertaccount);

                    } else {
                        // display acc icon without alert

                    }

                    comm_pi     = String.format("%.1f", appConn.cr_powerinstant);
                    comm_pcsb   = String.format("%.1f", appConn.cr_pcsb);
                    comm_easi   = String.format("%.1f", appConn.cr_easi);
                    comm_easi2  = String.format("%.1f", appConn.cr_easi2);
                    comm_gp     = String.format("%.1f", appConn.cr_googleplay);
                    comm_it     = String.format("%.1f", appConn.cr_itunes);
                    comm_tel    = String.format("%.1f", appConn.cr_telbru);

                    balanceNotification();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(navMainMenuActivity.this, "error code : " + error, Toast.LENGTH_LONG).show();
                System.out.println("appconn merchant_balance = " + appConn.merchat_balance);
                System.out.println("appconn refreshfailedcode = " + appConn.refreshFailedcode);
                System.out.println("appconn error : " + error);
            }
        });

    }

    public void balanceNotification(){
        if (mercBalanceState.equals("yes")) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Pocket Vendor Alert")
                    .setSound(alarmSound)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorIcon))
                    .setContentText("Low account balance, please topup your account.")
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.pv_launcher_ic));

            // changes required regarding cashier admin and non-cashier admin
//                Intent mainIntent = new Intent(this, account2Activity.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);

            final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(-1, builder.build());
        }

    }

    public void scanQR(){
        startActivity(new Intent(navMainMenuActivity.this, qrActivity.class)
                .putExtra("sPower", serPower)
                .putExtra("sPcsb", serPcsb)
                .putExtra("sZoom", serZoom)
                .putExtra("sEasi", serEasi)
                .putExtra("sGoogleUS", serGoogleUS)
                .putExtra("sItunesUS", serItunesUS)
                .putExtra("sTPW", serTPrepaidWifi)
                .putExtra("gPower", getPow)
                .putExtra("gPcsb", getPro)
                .putExtra("gZoom", getZoom)
                .putExtra("gEasi", getEas)
                .putExtra("gGoogleUS", getGoogleUS)
                .putExtra("gItunesUS", getItunesUS)
                .putExtra("gTPW", getTPrepaidWifi));

    }

    public void depositcall() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Getting Deposit");
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://threegmedia.com/merchantpos/pocket/depositphone.php?phone=8138346").build(); //8138346
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    final String res = response.body().string();
                    navMainMenuActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("====> depo result : ", res);
                            System.out.println("=== depo result : " + res);

                            if (res.equals("false")){
                                llou.setVisibility(View.GONE);
                                depin = "";
                            } else {
                                llou.setVisibility(View.VISIBLE);
                                String[] p = res.split("=", -1);
                                depin = p[1];
                            }


                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Log.e("====>", "depo result : reply fail");
                    System.out.println("=== depo result : reply fail");
                }

            }
        });
    }


//    ON CLICK METHODS
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(navMainMenuActivity.this);
            preferences.edit().clear().apply();

            startActivity(new Intent(navMainMenuActivity.this, splashActivity.class));
            finishAffinity();
            //System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to log out", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void powerInstant(View view){
        if (getPow.equals("yes")){
            startActivity(new Intent(this, powerInstantActivity.class));
            finish();
        } else {
            Toast.makeText(this, "POWERINSTANT is currently unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void progresif(View view){
//        if (getPro.equals("yes")){
//            startActivity(new Intent(this, progresifActivity.class));
//            finish();
//        } else {
//            Toast.makeText(this, "PROGRESIF is currently unavailable", Toast.LENGTH_SHORT).show();
//        }

        if (pcsb){
            startActivity(new Intent(navMainMenuActivity.this, pcsbProducts.class)
                    .putExtra("prepaid",getPro)
                    .putExtra("zoom", getZoom)
                    .putExtra("sprepaid", serPcsb)
                    .putExtra("szoom", serZoom));
        } else {
            Toast.makeText(this, "PROGRESIF is currently unavailable", Toast.LENGTH_SHORT).show();
        }



    }

    public void easi(View view){
        if (getEas.equals("yes")){
            startActivity(new Intent(this, easiActivity.class));
            finish();
        } else {
            Toast.makeText(this, "EASI is currently unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void googleplay(View view){
        if (getGoogleUS.equals("yes")){
            startActivity(new Intent(this, googleplayActivity.class));
            finish();
        } else {
            Toast.makeText(this, "GooglePlay (US) is currently unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public void itunes(View view){
        if (getItunesUS.equals("yes")){
            startActivity(new Intent(this, itunesActivity.class));
            finish();
        } else {
            Toast.makeText(this, "iTunes (US) is currently unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public void telbruproducts(View view){

        if (getTPrepaidWifi.equals("yes")){
            startActivity(new Intent(navMainMenuActivity.this, telbruProducts.class));
        } else {
            Toast.makeText(this, "TelBru services is currently unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public void pocketdepo(View view){
        //startActivity(new Intent(navMainMenuActivity.this, pocketDepo.class));
        startActivity(new Intent(navMainMenuActivity.this, pocketDepo.class)
                .putExtra("depin", depin));
    }


//    Navigation Drawer and Toolbar
    private void setToolbar(){
        Toolbar nav_toolbar         = findViewById(R.id.toolbar_main);
        TextView title_toolbar      = nav_toolbar.findViewById(R.id.tv_toolbartitle);
        LinearLayout lv_qr          = findViewById(R.id.lv_qrscan);
        LinearLayout lv_opendrawer  = findViewById(R.id.lv_opensidenav);

        nav_toolbar.setTitle("");
        title_toolbar.setText("POCKET VENDOR");
        setSupportActionBar(nav_toolbar);

//        ActionBar abar = getSupportActionBar();
//        abar.setDisplayHomeAsUpEnabled(true);
//        abar.setHomeAsUpIndicator(R.drawable.home);

        lv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR();
            }
        });

        lv_opendrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void configureNavigationDrawer(){
        drawer = findViewById(R.id.drawer_mainmenu);
        NavigationView nav = findViewById(R.id.navigation_mainwithdrawer);
        View headerview = nav.getHeaderView(0);
        TextView nav_mname = headerview.findViewById(R.id.tv_nav_merchantname);


        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(navMainMenuActivity.this);
        nav_mname.setText(pref.getString("merchantname", null).toUpperCase());
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Fragment f = null;
                int itemId = menuItem.getItemId();

                if (itemId == R.id.account){
                    if (pref.getString("cashieradmin", null).equals("no")){
                        Toast.makeText(navMainMenuActivity.this, "Only ADMIN user can access this page", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(navMainMenuActivity.this, accountActivity.class)
                                .putExtra("pi_rate", comm_pi)
                                .putExtra("pcsb_rate", comm_pcsb)
                                .putExtra("easi_rate", comm_easi)
                                .putExtra("easi2_rate", comm_easi2)
                                .putExtra("googleplay_rate", comm_gp)
                                .putExtra("itunes_rate",comm_it)
                                .putExtra("telbru_rate", comm_tel));
                        drawer.closeDrawers();
                    }


                } else if (itemId == R.id.transaction_activity){
                    startActivity(new Intent(navMainMenuActivity.this, reportActivity.class)
                            .putExtra("pi_rate",            comm_pi)
                            .putExtra("pcsb_rate",          comm_pcsb)
                            .putExtra("easi_rate",          comm_easi)
                            .putExtra("easi2_rate",         comm_easi2)
                            .putExtra("googleplay_rate",    comm_gp)
                            .putExtra("itunes_rate",        comm_it)
                            .putExtra("telbru_rate",        comm_tel));
                    drawer.closeDrawers();

                } else if (itemId == R.id.support){
                    startActivity(new Intent(navMainMenuActivity.this, supportActivity.class));
                    drawer.closeDrawers();

                } else if (itemId == R.id.faq){
                    startActivity(new Intent(navMainMenuActivity.this, faqActivity.class));
                    drawer.closeDrawers();
                }

//                if (f != null){
//                    FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//                    trans.replace(R.id.frame, f);
//                    trans.commit();
//                    drawer.closeDrawers();
//                    return true;
//                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_empty, menu);

        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
