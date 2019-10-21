package com.sunmi.pocketvendor.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppLogg;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.network.Utils;
import com.sunmi.pocketvendor.network.WebAsyncTask;
import com.sunmi.pocketvendor.pvApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Random;

import sunmi.paylib.SunmiPayKernel;

public class splashActivity extends Activity {

    private SunmiPayKernel sunmiPayKernel;

    String imei, tpin;
    String return_tid, return_mid, return_fi;
    String file_mid, file_tid, file_tpin;
    File folderPath;
    String filename;
    String encryptedname;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppLogg appLogg = new AppLogg();
        appLogg.getlog(getApplicationContext(), "SPLSH");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Initializing terminal");
        pd.setMessage("Please wait for a moment...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);

        if (!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            initializesplash();
        }
    }

    private void initializesplash(){
        conn();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imei = tm.getDeviceId();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().clear().apply();

        //initSplash();
        initSetup();
    }

    private void conn(){
        sunmiPayKernel = SunmiPayKernel.getInstance();
        sunmiPayKernel.connectPayService(getApplicationContext(), connCallBack);
    }

    private SunmiPayKernel.ConnCallback connCallBack = new SunmiPayKernel.ConnCallback() {
        @Override
        public void onServiceConnected() {
            System.out.println("==== service connected");
            try {
                pvApplication.mReadCardOpt = sunmiPayKernel.mReadCardOpt;
                pvApplication.initSecretKey();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected() {
            System.out.println("==== service disconnected");
            //
        }
    };

    private void initSetup(){
        final ProgressDialog dialog = new ProgressDialog(splashActivity.this);
        dialog.setMessage("Processing, Please wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("command", "checkterminal");
        hash.put("terminal_imei", imei);

        WebAsyncTask checkterminalimei = new WebAsyncTask("treg", Global.URL, hash, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                dialog.dismiss();
                String CTImeiresult = response.getResult();
                if (CTImeiresult.contains("failedcode=")){

                    tpin = generatePIN(6);

                    HashMap<String, String> hash = new HashMap<>();
                    hash.put("command", "registerterminal");
                    hash.put("terminal_imei", imei);
                    hash.put("terminal_pin", tpin);
                    hash.put("terminal_remarks", "SUNMI P1-4G");
                    hash.put("terminal_position", "Store Room");

                    WebAsyncTask regterminal = new WebAsyncTask("treg", Global.URL, hash, new ResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            // GET from server
                            dialog.dismiss();
                            String result = response.getResult();

                            if (result != null){
                                dialog.dismiss();
                                if (result.contains("failedcode=")){
                                    messageAlert("REGISTER TERMINAL", "RETURN : FAILEDCODE");
                                    System.out.println("RT | RESPONSE : " + return_tid);

                                } else {
                                    String pairs[] = result.split("&");
                                    for (String pair : pairs){
                                        String values[] = pair.split("=", -1);
                                        if (values[0].equals("terminal_id")){
                                            return_tid = values[1];
                                            System.out.println("=== RT | t_id : " + return_tid);
                                        }
                                    }

                                    HashMap<String, String> hash = new HashMap<>();
                                    hash.put("command", "registermerchant");
                                    hash.put("terminal_id", return_tid);

                                    WebAsyncTask checkmerchant = new WebAsyncTask("treg", Global.URL, hash, new ResponseListener() {
                                        @Override
                                        public void onResponse(Response response) {
                                            // GET from server
                                            dialog.dismiss();
                                            String result = response.getResult();

                                            if (result != null){
                                                dialog.dismiss();
                                                if (result.contains("failedcode=")){
                                                    messageAlert("REGISTER MERCHANT", "RETURN : FAILEDCODE");
                                                    System.out.println("=== CM | FAILEDCODE : " + result);
                                                } else {
                                                    System.out.println("CM | RESPONSE : " + result);
                                                    String pairs[] = result.split("&");
                                                    for (String pair : pairs){
                                                        String values[] = pair.split("=", -1);
                                                        if (values[0].equals("merchantid")){
                                                            return_mid = values[1];
                                                            System.out.println("=== CM | m_id : " + return_mid);
                                                        }
                                                    }
                                                    fileset();
                                                }
                                            } else {
                                                dialog.dismiss();
                                                System.out.println("=== CM | SERVER RESPONSE : NULL");
                                                messageAlert("REGISTER TERMINAL", "RETURN : NULL");
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {
                                            // GET timeout
                                            dialog.dismiss();
                                            System.out.println("=== CM TIMEOUT = " + error);
                                            messageAlert("REGISTER TERMINAL", "RETURN : " + error);
                                        }
                                    });
                                    checkmerchant.execute();
                                }
                            } else {
                                dialog.dismiss();
                                System.out.println("=== SERVER RESPONSE : NULL");
                                messageAlert("REGISTER TERMINAL", "RETURN : NULL");
                            }
                        }

                        @Override
                        public void onError(String error) {
                            // GET timeout
                            dialog.dismiss();
                            System.out.println("=== RT TIMEOUT = " + error);
                            messageAlert("REGISTER TERMINAL", "RETURN : " + error);
                        }
                    });
                    regterminal.execute();

                } else {
                    String pairs[] = CTImeiresult.split("&", -1);
                    for (String pair : pairs){
                        String values[] = pair.split("=", -1);
                        if (values[0].equals("terminal_id")){
                            return_tid = values[1];

                            HashMap<String, String> hash1 = new HashMap<>();
                            hash1.put("command", "registermerchant");
                            hash1.put("terminal_id", return_tid);

                            WebAsyncTask checkterminalid = new WebAsyncTask("treg", Global.URL, hash1, new ResponseListener() {
                                @Override
                                public void onResponse(Response response) {
                                    dialog.dismiss();
                                    String CTIdresult = response.getResult();

                                    if (CTIdresult.contains("failedcode=")){
                                        System.out.println("=== CHECK MERCHANT FAILEDCODE\n" + CTIdresult);



                                    } else {
                                        String pairs[] = CTIdresult.split("&", -1);
                                        for (String pair : pairs){
                                            String values[] = pair.split("=", -1);
                                            if (values[0].equals("merchantid")){
                                                return_mid = values[1];
                                            } else if (values[0].equals("firstinstall")){
                                                return_fi = values[1];
                                            }
                                        }

                                        if (return_fi.equals("0")){
                                            final ProgressDialog pdialog = new ProgressDialog(splashActivity.this);
                                            pdialog.setMessage("Processing, Please wait...");
                                            pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            pdialog.setCancelable(false);
                                            pdialog.show();

                                            tpin = generatePIN(6);
                                            HashMap<String, String> hash2 = new HashMap<>();
                                            hash2.put("command", "updateterminalpin");
                                            hash2.put("terminal_imei", imei);
                                            hash2.put("terminal_pin", tpin);
                                            hash2.put("terminal_remarks", "SUNMI P1-4G");

                                            WebAsyncTask updateterminalpin = new WebAsyncTask("treg", Global.URL, hash2, new ResponseListener() {
                                                @Override
                                                public void onResponse(Response response) {
                                                    String UTPresult = response.getResult();

                                                    if (UTPresult.contains("failedcode=")){
                                                        messageAlert2("ALERT", UTPresult.replace("failedcode=",""));
                                                        System.out.println("=== CHECK MERCHANT FAILEDCODE\n" + UTPresult);
                                                    } else {
                                                        fileset();
                                                    }
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    pdialog.dismiss();
                                                    System.out.println("=== RT TIMEOUT = " + error);
                                                }
                                            });
                                            updateterminalpin.execute();

                                        } else if (return_fi.equals("1")){
                                            tpin = null;
                                            fileset();
                                        }

                                    }

                                }

                                @Override
                                public void onError(String error) {
                                    dialog.dismiss();
                                    System.out.println("=== CM TIMEOUT = " + error);
                                }
                            });
                            checkterminalid.execute();
                        }
                    }
                }

            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                Toast.makeText(splashActivity.this, "SERVER ERROR = " + error, Toast.LENGTH_SHORT).show();
                System.out.println("=== CT TIMEOUT = " + error);
                messageAlert2("TERMINAL ERROR", error);
            }
        });
        checkterminalimei.execute();
    }

    private void fileset(){
        File rootPath = Environment.getExternalStorageDirectory();
        folderPath = new File(rootPath.getAbsolutePath() + Global.FOLDER);
        try {
            encryptedname = Utils.encrypt("login", "123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        filename = encryptedname + ".txt";

        String getline;
        File textFile = new File(folderPath, filename);
        if (textFile.exists()){
            try {
                FileInputStream fis = new FileInputStream(textFile);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                while ( (getline = br.readLine()) != null ) {
                    String fileand[] = getline.split("&");
                    for (String filepair : fileand){
                        String filevalues[] = filepair.split("=");

                        if (filevalues[0].equals("merchant_id")){
                            file_mid = filevalues[1];
                        } else if (filevalues[0].equals("terminal_id")){
                            file_tid = filevalues[1];
                        } else if (filevalues[0].equals("terminal_pin")){
                            file_tpin = filevalues[1];
                        }
                    }
                }
                fis.close();
                br.close();
            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            // overwrite
            if (file_mid != return_mid){
                file_mid = return_mid;
                if (file_tid != return_tid){
                    file_tid = return_tid;
                    if (tpin != null){
                        file_tpin = tpin;

                        try {
                            textFile.delete();
                            textFile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(textFile);
                            OutputStreamWriter osw = new OutputStreamWriter(fos);

                            StringBuilder sb = new StringBuilder();
                            sb.append("merchant_id=" + file_mid);
                            sb.append("&terminal_id=" + file_tid);
                            sb.append("&terminal_pin=" + file_tpin);

                            osw.append(sb);
                            osw.close();

                            fos.flush();
                            fos.close();

                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            startActivity(new Intent(splashActivity.this, cardScanActivity.class)
                    .putExtra("mid", file_mid)
                    .putExtra("tid", file_tid)
                    .putExtra("tpin", file_tpin));
            // overwrite

        } else {
            folderPath.mkdir();
            try {
                textFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(textFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                StringBuilder sb = new StringBuilder();
                sb.append("merchant_id=" + return_mid);
                sb.append("&terminal_id=" + return_tid);
                sb.append("&terminal_pin=" + tpin);

                osw.append(sb);
                osw.close();

                fos.flush();
                fos.close();

                System.out.println("=== sb : " + sb.toString());
                System.out.println("=== check file");

            } catch (IOException e){
                e.printStackTrace();
            }

            startActivity(new Intent(splashActivity.this, cardScanActivity.class)
                    .putExtra("mid", return_mid)
                    .putExtra("tid", return_tid)
                    .putExtra("tpin", tpin));
        }
    }

    private void messageAlert(String eTitle, String eMessage){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(eTitle);
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

    private void messageAlert2(String eTitle, String eMessage){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(eTitle);
        b.setMessage(eMessage);
        b.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        });
        AlertDialog aDialog = b.create();
        aDialog.show();
    }

    private String generatePIN(int length){

        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random ran = new Random();

        for (int i = 0; i < length; i++){
            char c = chars[ran.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL){
            initializesplash();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        //
    }
}
