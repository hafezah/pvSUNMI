package com.sunmi.pocketvendor.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;
import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.AppLogg;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.pvApplication;

import java.util.List;

import sunmi.sunmiui.utils.LogUtil;

public class cardScanActivity extends Activity {

    private String imei_no, cardSerial;
    String getmid, gettid, gettpin;
    public AppConn appConn;

    private ReadCardOpt readCardOpt;
    TextView cardscan;

    private long mLastClickTime = 0;

    Bitmap logobitmap;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_scan);

        cardscan = findViewById(R.id.card_scan);
        readCardOpt = pvApplication.mReadCardOpt;

        appConn = new AppConn();
        appConn.dateTime();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
         return;
        }
        imei_no = telephonyManager.getDeviceId();



        getmid  = getIntent().getStringExtra("mid");
        gettid  = getIntent().getStringExtra("tid");
        gettpin = getIntent().getStringExtra("tpin");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit()
                .putString("m_id",  getmid)
                .putString("t_id",  gettid)
                .putString("t_pin", gettpin)
                .putString("t_imei", imei_no)
                .apply();

        scanCardId();
    }

    public void loginPV(View view){
        cardscan.setText("SCAN YOUR CARD ID TO AUTHORIZE");

        try {
            readCardOpt.cancelCheckCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        scanCardId();
    }

    private void scanCardId(){
        try {
            readCardOpt.checkCard(AidlConstants.CardType.MIFARE.getValue(), readCardCallBack, 30000);
        } catch (RemoteException e){
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
            LogUtil.e("CardScanActivity", "onStartCheckCard: reading card...");
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
      @Override
        public void handleMessage(Message msg){
          super.handleMessage(msg);
          switch (msg.what){
              case 0x01:
                  cardscan.setText("LOGGING IN, PLEASE WAIT...");
                  CardInfo cardInfo = (CardInfo) msg.obj;
                  cardSerial = cardInfo.uuid.toString().toLowerCase();
                  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cardScanActivity.this);
                  preferences.edit()
                          .putString("uuid", cardSerial)
                          .apply();

                  verifyCard();
                  break;

              case 0x02:
                  Toast.makeText(cardScanActivity.this, "Check Timeout", Toast.LENGTH_SHORT).show();
                  new CountDownTimer(5000, 1000) {
                      @Override
                      public void onTick(long l) {
                          cardscan.setText("RE-SCAN IN " + l/1000 + "s");

                      }

                      @Override
                      public void onFinish() {
                          cardscan.setText("SCAN CARD TO LOGIN");
                          scanCardId();
                      }
                  }.start();
                  break;

              case 0x03:
                  Toast.makeText(cardScanActivity.this, "Scan Failed ", Toast.LENGTH_SHORT).show();
                  cardscan.setText("TAP TO RE-SCAN CARD");
                  new CountDownTimer(5000, 1000) {
                      @Override
                      public void onTick(long l) {

                          try {
                              readCardOpt.cancelCheckCard();
                          } catch (RemoteException e) {
                              e.printStackTrace();
                          }

                          cardscan.setText("RE-SCAN IN " + l/1000 + "s");

                      }

                      @Override
                      public void onFinish() {
                          cardscan.setText("SCAN YOUR CARD ID TO AUTHORIZE");
                          scanCardId();
                      }
                  }.start();
                  break;

          }
      }
    };

    private void verifyCard(){

        try {
            readCardOpt.cancelCheckCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        AppLogg appLogg = new AppLogg();
        appLogg.getlog(getApplicationContext(), "CSCAN-[" + cardSerial + "]");

        final ProgressDialog pdialog = new ProgressDialog(cardScanActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cardScanActivity.this);
        //
        appConn = new AppConn();
        appConn.versionBuild = Global.VER;
        appConn.merchant_id = getmid;   //preferences.getString("m_id", null);
        appConn.terminal_id = gettid;   //preferences.getString("t_id", null);
        appConn.terminal_imei = imei_no;
        appConn.terminal_pin = gettpin; //preferences.getString("t_pin", null);
        preferences.edit().putString("uuid", cardSerial).apply();
        appConn.uuid = cardSerial;
        appConn.webLink = Global.URL;
        appConn.commandpost = "login";
        appConn.cardAccess(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                pdialog.dismiss();
                if (appConn.cardFailedcode != null){
                    Toast.makeText(cardScanActivity.this, "Tap the screen to rescan your card", Toast.LENGTH_SHORT).show();
                    errorAlert(appConn.cardFailedcode);
                } else {

                    if (appConn.wp_version_result.contains("Latest Version")){
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cardScanActivity.this);
                        preferences.edit()
                                .putString("merchantname"   , appConn.res_merchantname)
                                .putString("slot"           , appConn.res_terminalslot)
                                .putString("cashiername"    , appConn.res_cashiername)
                                .putString("cashieradmin"   , appConn.res_cashieradmin)

                                // INDIVIDUAL SWITCH
                                .putString("sPI"    , appConn.res_powerinstant)
                                .putString("sP"     , appConn.res_progresif)
                                .putString("sPZ"    , appConn.res_zoom)
                                .putString("sE"     , appConn.res_easi)
                                .putString("sGP"    , appConn.res_googleplayus)
                                .putString("sIT"    , appConn.res_itunesus)
                                .putString("sTPW"    , appConn.res_telbruprepaidwifi)

                                // GLOBAL SWITCH
                                .putString("staPI"  , appConn.stat_powerinstant)
                                .putString("staP"   , appConn.stat_progresif)
                                .putString("staPZ"   , appConn.stat_zoom)
                                .putString("staE"   , appConn.stat_easi)
                                .putString("globaleasi2", appConn.global_easi2)
                                .putString("easi2"  , appConn.res_easi2)
                                .putString("staGP"  , appConn.stat_googleplayus)
                                .putString("staIT"  , appConn.stat_itunesus)
                                .putString("staTPW"    , appConn.sta_telbruprepaidwifi)
                                .apply();

                        startActivity(new Intent(cardScanActivity.this, navMainMenuActivity.class)
                                // INDIVIDUAL SWITCH
                                .putExtra("sPower"      , appConn.res_powerinstant)
                                .putExtra("sPCSB"       , appConn.res_progresif)
                                .putExtra("sPCSBZ"       , appConn.res_zoom)
                                .putExtra("sEasi"       , appConn.res_easi)
                                .putExtra("sGooglePlayUS", appConn.res_googleplayus)
                                .putExtra("sITunesUS"   , appConn.res_itunesus)
                                .putExtra("sTelbruPrepaidWIFI"   , appConn.res_telbruprepaidwifi)

                                // GLOBAL SWITCH
                                .putExtra("statuspi"    , appConn.stat_powerinstant)
                                .putExtra("statusp"     , appConn.stat_progresif)
                                .putExtra("statuspz"     , appConn.stat_zoom)
                                .putExtra("statuse"     , appConn.stat_easi)
                                .putExtra("statusgp"    , appConn.stat_googleplayus)
                                .putExtra("statusit"    , appConn.stat_itunesus)
                                .putExtra("statustbpw"   , appConn.sta_telbruprepaidwifi));
                        finish();
                    } else if (appConn.wp_version_result.contains("missing data")){
                        AlertDialog.Builder a = new AlertDialog.Builder(cardScanActivity.this);
                        a.setTitle("");
                        a.setMessage("");
                        a.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        });
                        AlertDialog bDialog = a.create();
                        bDialog.show();

                    } else {
                        //
                        AlertDialog.Builder b = new AlertDialog.Builder(cardScanActivity.this);
                        b.setTitle("New Version Detected");
                        b.setMessage("Please update Pocket Vendor to the latest version");
                        b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //dialogInterface.dismiss();
                                String packageName = "com.spocketvendor";
                                String uri = String.format("market://woyou.market/appDetail?packageName=%s",packageName);
                                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                PackageManager packageManager = getPackageManager();
                                List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                boolean isIntentSafe = activities.size() > 0;
                                if (isIntentSafe) {
                                    startActivity(intent);
                                }

                                finishAffinity();
                            }
                        });
                        AlertDialog aDialog = b.create();
                        aDialog.show();
                        //
                    }
                }
            }

            @Override
            public void onError(String error) {
                pdialog.dismiss();
                if (error.equals("Request time out")){
                    Toast.makeText(cardScanActivity.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(cardScanActivity.this, errorActivity.class));
                } else {
                    errorAlert(error);
                    Toast.makeText(cardScanActivity.this, "Tap the screen to rescan your card", Toast.LENGTH_LONG).show();
                }

                cardscan.setText("TAP TO RE-SCAN YOU CARD");
            }
        });
        //

        cardscan.setText("SCAN YOUR CARD ID TO AUTHORIZE");

        pdialog.dismiss();
    }

    private Bitmap scaleImage(Bitmap bitmap1) {
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

    private void errorAlert(String eMessage){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(eMessage);
        b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog aDialog = b.create();
        aDialog.show();
    }

    @Override
    public void onBackPressed() {
        //
    }

    // testing purposes, delete later

//    public void testbtn(View view){
//        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
//        logobitmap = scaleImage(logobitmap);
//
//        String top;
//        StringBuilder sb = new StringBuilder();
//        sb.append("\niTunes Gift Card");
//        top = sb.toString();
//
//        String body;
//        StringBuilder sbB = new StringBuilder();
//        sbB.append("Token will be sent to you\nshortly\n\n");
//        sbB.append("Merchant    : ThreeG Media\n");
//        sbB.append("Cashier     : Nudzibrem\n");
//        sbB.append("Date & Time : 2018-10-09 | 13:45\n");
//        sbB.append("Ref no      : 5828\n");
//        sbB.append("Product     : iTunes Gift Card\n");
//        sbB.append("              USD 25\n");
//        sbB.append("Price       : BND 45\n");
//        sbB.append("Mobile no   : 8888888\n");
//        body = sbB.toString();
//
//        AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
//        AidlUtil.getInstance().printText(top, 23, true, false);
//        AidlUtil.getInstance().printText(body, 23, false, false);
//    }


}
