package com.sunmi.pocketvendor;

import android.os.Environment;
import android.util.Log;

import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.network.Utils;
import com.sunmi.pocketvendor.network.WebAsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;

public class AppConn {

    public int httpReturn;
    public String webLink;
    public String commandpost;
    public String uuid;
    public String postCashier;
    public String sortType;
    public String dateReport;
    public String balanceState;
    public String piname;
    public String refNo;
    public String powerUnit;

    //date time
    public int cDay, cMonth, cYear, cHour, cMinute, cSeconds;
    public String cDate;
    public String cTime;
    public String postDate;

    // terminal
    public String terminal_id, terminal_pin, terminal_imei;

    // merchant
    public String merchant_id;
    public double merchat_balance;
    public String merchantname;
    public String merchantaccountno;
    public String merchantsalestargetamount;
    public String currentsales;
    public String salestarget;
    public String reg_date;
    public String acctype;
    public String crlimit;
    public String outstandingbal;

    // transaction
    public String meter_no;
    public String transactionservicetype;
    public String transactionserviceno;
    public String transactiondate;
    public String transactiontime;
    public String transactionamount;
    public String transactionstatus;
    public String transactionphone;
    public String transactionid;
    public String deno_type, deno_amount;

    // pocket qr
    public String memberid;
    public String pocketEmail;
    public String versionBuild;

    // success response
    public String res_merchantname;
    public String res_server;
    public String wp_version_result;
    public String res_cashiername;
    public String res_cashieradmin;
    public String res_name;
    public String res_terminalslot;
    public String res_token;
    public String res_deno, res_denobnd, res_deno_desc;
    public String resend_status;
    public String res_depo;

    public String res_powerinstant, stat_powerinstant;
    public String res_progresif, stat_progresif, res_zoom, stat_zoom;
    public String res_easi, stat_easi;
    public String global_easi2, res_easi2;
    public String res_googleplayus, stat_googleplayus;
    public String res_itunesus, stat_itunesus;
    public String res_telbruprepaidwifi, sta_telbruprepaidwifi;
    public double cr_powerinstant, cr_pcsb, cr_easi, cr_easi2, cr_googleplay, cr_itunes, cr_telbru;

    // failed code response
    public String cardFailedcode;
    public String refreshFailedcode;
    public String meterFailedcode;
    public String transactionFailedcode;
    public String accountReportFailedcode;
    public String settlementFailedcode;
    public String dateReportFailedcode;
    public String denoFailedcode;
    public String resendFailedcode;

    public void cardAccess(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("version_build", versionBuild);
        hashMap.put("terminal_pin", terminal_pin);
        hashMap.put("uuid", uuid);
        hashMap.put("terminal_imei", terminal_imei);
        hashMap.put("terminal_id", terminal_id);
        hashMap.put("merchant_id", merchant_id);
        hashMap.put("command", commandpost);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();

                if (result != null){
                    Log.e("====> Result : ", result);
                    String pairs[] = result.split("&");

                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){

                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    cardFailedcode = values[1];

                                } else {
                                    if (values[0].equals("loginRes")) {
                                        res_server = values[1];
                                    } else if(values[0].equals("versionbuild")){
                                        wp_version_result = values[1];
                                    } else if (values[0].equals("merchantname")){
                                        res_merchantname = values[1];
                                    } else if (values[0].equals("cashiername")){
                                        res_cashiername = values[1];
                                    } else if (values[0].equals("cashieradmin")){
                                        res_cashieradmin = values[1];
                                    } else if (values[0].equals("easi")){
                                        res_easi = values[1];
                                    } else if (values[0].equals("pcsb")){
                                        res_progresif = values[1];
                                    } else if (values[0].equals("powerinstant")){
                                        res_powerinstant = values[1];
                                    } else if (values[0].equals("googleplayus")){
                                        res_googleplayus = values[1];
                                    } else if (values[0].equals("itunesus")){
                                        res_itunesus = values[1];
                                    } else if (values[0].equals("pcsbzoom")){
                                        res_zoom = values[1];
                                    } else if (values[0].equals("statuspowerinstant")) {
                                        stat_powerinstant = values[1];
                                    } else if (values[0].equals("statuspcsb")){
                                        stat_progresif = values[1];
                                    } else if (values[0].equals("statuseasi")){
                                        stat_easi = values[1];
                                    } else if (values[0].equals("statuseasi2dollars")){
                                        global_easi2 = values[1];
                                    } else if (values[0].equals("easi2dollars")){
                                        res_easi2 = values[1];
                                    } else if (values[0].equals("statusgoogleplayus")){
                                        stat_googleplayus = values[1];
                                    } else if (values[0].equals("statusitunesus")){
                                        stat_itunesus = values[1];
                                    } else if (values[0].equals("telbruprepaidwifi")){
                                        res_telbruprepaidwifi = values[1];
                                    } else if (values[0].equals("statustelbruprepaidwifi")){
                                        sta_telbruprepaidwifi = values[1];
                                    } else if (values[0].equals("statuspcsbzoom")){
                                        stat_zoom = values[1];
                                    } else if (values[0].equals("terminalslotname")){
                                        res_terminalslot = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Error : " + result);
                    }
                }
            }
            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void refreshCheck(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("merchant_id", merchant_id);
        hashMap.put("command", commandpost);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);

                    String pairs[] = result.split("&", -1);
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    refreshFailedcode = values[1];
                                } else {
                                    if (values[0].equals("merchantbalance")){
                                        merchat_balance = Double.parseDouble(values[1]);

                                    } else if (values[0].equals("lowbalance")){
                                        balanceState = values[1];

                                    } else if (values[0].equals("salestarget")){
                                        salestarget = values[1];

                                    } else if (values[0].equals("merchantcommpowerinstant")){
                                        cr_powerinstant = Double.parseDouble(values[1]);

                                    } else if (values[0].equals("merchantcommpcsb")){
                                        cr_pcsb = Double.parseDouble(values[1]);

                                    } else if (values[0].equals("merchantcommeasi")){
                                        cr_easi = Double.parseDouble(values[1]);

                                    } else if(values[0].equals("merchantcommeasi2dollars")){
                                        cr_easi2 = Double.parseDouble(values[1]);

                                    } else if (values[0].equals("merchantcommitunesus")){
                                        cr_itunes = Double.parseDouble(values[1]);

                                    } else if (values[0].equals("merchantcommgoogleplayus")){
                                        cr_googleplay = Double.parseDouble(values[1]);

                                    } else if (values[0].equals("merchantcommtelbruprepaidwifi")){
                                        cr_telbru = Double.parseDouble(values[1]);

                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void accountReport (final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cashieradmin", postCashier);
        hashMap.put("merchantid", merchant_id);
        hashMap.put("terminalid", terminal_id);
        hashMap.put("command", commandpost);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);
                    res_server = result;

                    String hashtag[] = result.split("#");
                    int array;
                    for (array=0; array != hashtag.length; array++){
                        System.out.println("hashtag is = " + hashtag[array]);
                        String pairs[] = hashtag[array].split("&");
                        if (pairs.length > 0){
                            for (String pair : pairs){
                                String values[] = pair.split("=", -1);
                                Log.e("--> pairs: ", pair);

                                if (values[0].equals("failedcode")){
                                    accountReportFailedcode = values[1];
                                } else if (values[0].equals("merchantbalance")){
                                    merchat_balance = Double.parseDouble(values[1]);

                                } else if (values[0].equals("merchantname")){
                                    merchantname = values[1];

                                } else if (values[0].equals("merchantaccountno")){
                                    merchantaccountno = values[1];

                                } else if (values[0].equals("terminalslotsalestargetamount")){
                                    merchantsalestargetamount = values[1];

                                } else if (values[0].equals("currentsales")){
                                    currentsales = values[1];

                                } else if (values[0].equals("merchantregistereddate")){
                                    reg_date = values[1];

                                } else if (values[0].equals("merchantaccounttype")){
                                    acctype = values[1];

                                } else if (values[0].equals("merchantcrlimit")){
                                    crlimit = values[1];

                                } else if (values[0].equals("outstandingbalance")){
                                    outstandingbal = values[1];

                                }
                            }
                            listener.onResponse(response);
                        } else {
                            listener.onError("Protocol Error: " + result);
                        }
                    }
                }
//                if (result != null){
//                    Log.e("====> Result : ", result);
//
//                    String pairs[] = result.split("&");
//                    if (pairs.length > 0){
//                        for (String pair : pairs){
//                            String values[] = pair.split("=");
//                            Log.e("--> pair: ", pair);
//
//                            if (values.length > 0){
//                                if (values[0].equals("failedcode")){
//                                    response.setFailedCode(values[1]);
//                                    accountReportFailedcode = values[1];
//                                } else {
//                                    if (values[0].equals("merchantbalance")){
//                                        merchat_report = values[1];
//                                    }
//                                }
//                            }
//                        }
//                        listener.onResponse(response);
//                    } else {
//                        listener.onError("Protocol Error : " + result);
//                    }
//                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void meterChecker(final ResponseListener listener){
        HashMap<String, String > hashMap = new HashMap<>();
        hashMap.put("command", commandpost);
        hashMap.put("transactionserviceno", meter_no);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                httpReturn = response.getStatusCode();
                System.out.println("Http return = " + httpReturn);

                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    meterFailedcode = values[1];
                                } else {
                                    if (values[0].equals("meter")) {
                                        res_server = values[1];
                                    } else if (values[0].equals("name")) {
                                        res_name = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void denoChecker(final ResponseListener listener){
        HashMap<String, String > hashMap = new HashMap<>();
        hashMap.put("command", commandpost);
        hashMap.put("transactionserviceno", deno_amount);
        hashMap.put("transactionservicetype", deno_type);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                httpReturn = response.getStatusCode();
                System.out.println("Http return = " + httpReturn);

                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);
                    System.out.println("====> Result : " + result);

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    denoFailedcode = values[1];
                                } else {
                                    if (values[0].equals("deno")) {
                                        res_deno = values[1];
                                    } else if (values[0].equals("transactionamount")) {
                                        res_denobnd = values[1];
                                    } else if (values[0].equals("transactionserviceno")){
                                        res_deno_desc = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void executeTrans(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("transactionqremail", pocketEmail);
        hashMap.put("transactionqrmemberid", memberid);
        hashMap.put("powerinstantownername", piname);
        hashMap.put("transactionamount", transactionamount);
        hashMap.put("transactionphoneno", transactionphone);
        hashMap.put("transactionserviceno", transactionserviceno);
        hashMap.put("transactionservicetype", transactionservicetype);
        hashMap.put("cashieruuid", uuid);
        hashMap.put("terminalid", terminal_id);
        hashMap.put("merchantid", merchant_id);
        hashMap.put("command", commandpost);

        System.out.println("hash trans = " + hashMap);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    transactionFailedcode = values[1];
                                } else {
                                    if (values[0].equals("transaction")){
                                        res_server = values[1];
                                    } else if (values[0].equals("transactiondate")){
                                        transactiondate = values[1];
                                    } else if (values[0].equals("transactiontime")){
                                        transactiontime = values[1];
                                    } else if (values[0].equals("transactionid")){
                                        refNo = values[1];
                                    } else if (values[0].equals("powerunit")){
                                        powerUnit = values[1];
                                    } else if (values[0].equals("token")){
                                        res_token = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void generateSettlement(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", dateReport);
        hashMap.put("sort", sortType);
        hashMap.put("cashieradmin", postCashier);
        hashMap.put("cashieruuid", uuid);
        hashMap.put("merchantid", merchant_id);
        hashMap.put("command", commandpost);
        hashMap.put("terminalid", terminal_id);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);
                    res_server = result;

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    settlementFailedcode = values[1];
                                } else {
                                    if (values[0].equals("transactiondate")){
                                        transactiondate = values[1];
                                    } else if (values[0].equals("transactiontime")){
                                        transactiontime = values[1];
                                    } else if (values[0].equals("transactionservicetype")){
                                        transactionservicetype = values[1];
                                    } else if (values[0].equals("transactionstatus")){
                                        transactionstatus = values[1];
                                    } else if (values[0].equals("transactionamount")){
                                        transactionamount = values[1];
                                    } else if (values[0].equals("transactionserviceno")){
                                        transactionserviceno = values[1];
                                    } else if (values[0].equals("transactionid")){
                                        transactionid = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void getterminallist(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("command", commandpost);
        hashMap.put("merchantid", merchant_id);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);
                    res_server = result;

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    settlementFailedcode = values[1];
                                } else {
//                                    if (values[0].equals("transactiondate")){
//                                        transactiondate = values[1];
//                                    } else if (values[0].equals("transactiontime")){
//                                        transactiontime = values[1];
//                                    } else if (values[0].equals("transactionservicetype")){
//                                        transactionservicetype = values[1];
//                                    } else if (values[0].equals("transactionstatus")){
//                                        transactionstatus = values[1];
//                                    } else if (values[0].equals("transactionamount")){
//                                        transactionamount = values[1];
//                                    } else if (values[0].equals("transactionserviceno")){
//                                        transactionserviceno = values[1];
//                                    } else if (values[0].equals("transactionid")){
//                                        transactionid = values[1];
//                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();

    }

    public void resend(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("transactionid", refNo);
        hashMap.put("command", commandpost);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    resendFailedcode = values[1];
                                } else {
                                    if (values[0].equals("merchantbalance")){
                                         resend_status = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void dateReport(final ResponseListener listener){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cashieradmin", postCashier);
        hashMap.put("date", postDate);
        hashMap.put("cashieruuid", uuid);
        hashMap.put("merchantid", merchant_id);
        hashMap.put("terminalid", terminal_id);
        hashMap.put("command", commandpost);

        WebAsyncTask task = new WebAsyncTask(commandpost, webLink, hashMap, new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                String result = response.getResult();
                if (result != null){
                    Log.e("====> Result : ", result);
                    res_server = result;

                    String pairs[] = result.split("&");
                    if (pairs.length > 0){
                        for (String pair : pairs){
                            String values[] = pair.split("=", -1);
                            Log.e("--> pair: ", pair);

                            if (values.length > 0){
                                if (values[0].equals("failedcode")){
                                    response.setFailedCode(values[1]);
                                    dateReportFailedcode = values[1];
                                } else {
                                    if (values[0].equals("transactiondate")){
                                        transactiondate = values[1];
                                    } else if (values[0].equals("transactiontime")){
                                        transactiontime = values[1];
                                    } else if (values[0].equals("transactionservicetype")){
                                        transactionservicetype = values[1];
                                    } else if (values[0].equals("transactionstatus")){
                                        transactionstatus = values[1];
                                    } else if (values[0].equals("transactionamount")){
                                        transactionamount = values[1];
                                    } else if (values[0].equals("transactionserviceno")){
                                        transactionserviceno = values[1];
                                    }
                                }
                            }
                        }
                        listener.onResponse(response);
                    } else {
                        listener.onError("Protocol Error : " + result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
        task.execute();
    }

    public void dateTime(){
        Calendar calendar = Calendar.getInstance();
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH) + 1;
        cYear = calendar.get(Calendar.YEAR);
        cHour = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute = calendar.get(Calendar.MINUTE);
        cSeconds = calendar.get(Calendar.SECOND);

        if (cMonth == 12){
            cMonth = 1;
        }

        cDate = cYear + "-" + cMonth + "-" + cDay;
        cTime = cHour + ":" + cMinute + ":" + cSeconds;
    }

}