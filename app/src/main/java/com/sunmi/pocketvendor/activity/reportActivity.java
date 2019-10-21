package com.sunmi.pocketvendor.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.ReportAdapter;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;
import com.sunmi.pocketvendor.utils.AidlUtil;
import com.sunmi.pocketvendor.utils.ESCUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

public class reportActivity extends BaseActivity {

    ListView lv;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    TextView SRtotal, SRsalescount, SRprofit, dateToday;

    int currentSelection;
    Spinner filterBy;
    String filterList[] = {"Select Product", "All Product", "POWERInstant", "Progresif Topup", "Progresif Zoom" , "Easi Topup", "iTunes US", "GooglePlay US", "TelBru"};
    ArrayAdapter<String> adapterFilter;

    int salescount = 0;
    String report[][];
    String formatedDate, postDate;
    String rMonth = "", rDay = "";
    double count, total, profit_earned = 0.0;
    private AppConn appConn;

    private long mLastClickTime = 0;

    Bitmap logobitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        LoadGUI();
        //tlist();
        settlementreport("timeasc");
        filterList();
    }

    public void LoadGUI(){
        dateToday           = findViewById(R.id.textView3);
        SRtotal             = findViewById(R.id.textView6);
        SRsalescount        = findViewById(R.id.tv_salescount);
        SRprofit            = findViewById(R.id.textView7);

        filterBy            = findViewById(R.id.spnFilter);
        adapterFilter       = new ArrayAdapter<String>(reportActivity.this, android.R.layout.simple_list_item_activated_1, filterList);
        filterBy            .setAdapter(adapterFilter);
        currentSelection    = filterBy.getSelectedItemPosition();

        Calendar c          = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df2= new SimpleDateFormat("yyyy-MM-dd");
        formatedDate        = df.format(c.getTime());
        postDate            = df2.format(c.getTime());
        dateToday           .setText(formatedDate);
    }

    public void listClick(final String items[][]){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(reportActivity.this, reportDetailActivity.class)
                        .putExtra("time",           items[i][0])
                        .putExtra("cashier",        items[i][1])
                        .putExtra("title",          items[i][2])
                        .putExtra("status",         items[i][3])
                        .putExtra("amount",         items[i][4])
                        .putExtra("date",           items[i][5])
                        .putExtra("liveref",        items[i][6])
                        .putExtra("number",         items[i][7])
                        .putExtra("merchantname",   items[i][8])
                        .putExtra("PIName",         items[i][9])
                        .putExtra("unit",           items[i][10])
                        .putExtra("token",          items[i][11])
                        .putExtra("smsphone",       items[i][12])
                        .putExtra("memberqr",       items[i][13]));
            }
        });
    }

    public void sortTime(View view){
        salescount = 0;
        total = 0;
        profit_earned = 0.0;

        settlementreport("timeasc");
    }

    public void sortCashier(View view){
        salescount = 0;
        total = 0;
        profit_earned = 0.0;

        settlementreport("cashierasc");
    }

    public void sortProduct(View view){
        salescount = 0;
        total = 0;
        profit_earned = 0.0;

        settlementreport("productasc");
    }

    public void printList(View view){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        logobitmap  = BitmapFactory.decodeResource(getResources(), R.mipmap.pv_bitmap3);
        logobitmap = scaleImage(logobitmap);

        String top;
        StringBuilder sbtop = new StringBuilder();
        sbtop.append("Merchant : " + preferences.getString("merchantname", null) + "\n");
        sbtop.append("Cashier  : " + preferences.getString("cashiername", null) + "\n");
        sbtop.append("--------------------------------");
        top = sbtop.toString();

        String title;
        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("--------------------------------\n");
        sbtitle.append("  REPORT LIST FOR " + dateToday.getText() + "\n");
        sbtitle.append("--------------------------------");
        title = sbtitle.toString();

        String bottom;
        StringBuilder sbot = new StringBuilder();
        sbot.append("--------------------------------\n");
        String salescount[], total[], profit[];
        salescount  = SRsalescount.getText().toString().split(":", -1);
        total       = SRtotal.getText().toString().split(":", -1);
        profit      = SRprofit.getText().toString().split(":", -1);
        sbot.append(    salescount[0] + "          : "  + salescount[1] + "\n");
        sbot.append(    total[0]      + " : "           + total[1] + "\n");
//        sbot.append(    profit[0]     + "   : "         + profit[1] + "\n\n");
        bottom = sbot.toString();

        AidlUtil.getInstance().printText(top, 23, false, false);
        AidlUtil.getInstance().sendRawData(ESCUtil.printBitmap(logobitmap));
        AidlUtil.getInstance().printText(title, 23, true, false);

        for (int i = 0; i < report.length; i++){
            AidlUtil.getInstance().printText("Ref No  : " + report[i][6] + "\nStatus  : " + report[i][3]
                    + "\n" + report[i][0] + " | " + report[i][1] + " | " + report[i][2] + "\nBND " +  report[i][4] , 23, false, false);
        }

        AidlUtil.getInstance().printText(bottom, 23, true, false);
    }

    public void filterList(){
        // on spinner select code
        filterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                ((TextView) adapterView.getChildAt(0)).setTextSize(12);
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.GRAY);

                //Toast.makeText(reportActivity.this, "you selected "+ filterList[position], Toast.LENGTH_SHORT).show();
                if (position == 0){
                    //
                } else if (position == 1){
                    //display all
                    salescount = 0;
                    total = 0.0;
                    profit_earned = 0.0;
                    settlementreport("timeasc");

                } else if (position == 2){
                    //display POWERINSTANT only
                    getServiceFilter("POWER");
                    salescount = 0;

                } else if (position == 3){
                    //display PCSB TOP-UP only
                    getServiceFilter("PROGRESIF TOPUP");
                    salescount = 0;

                } else if (position == 4){
                    //display PCSB TOP-UP only
                    getServiceFilter("PROGRESIF ZOOM");
                    salescount = 0;

                } else if (position == 5){
                    //display EASI TOP-UP only
                    getServiceFilter("EASI");
                    salescount = 0;

                } else if (position == 6){
                    // display iTunes Gift Card
                    getServiceFilter("ITUNES");
                    salescount = 0;

                } else if (position == 7){
                    // display GooglePlay Gift Card
                    getServiceFilter("GOOGLE");
                    salescount = 0;

                } else if (position == 8){
                    // display TelBru products
                    getServiceFilter("TELBRU");
                    salescount = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //return;
            }
        });

    }

    public void selectDate(View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final ProgressDialog pdialog = new ProgressDialog(reportActivity.this);
                pdialog.setMessage("Loading, Please wait...");
                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pdialog.setCancelable(false);
                pdialog.show();

                month = month + 1;

                if (month < 10){
                    rMonth = "0" + month;
                } else {
                    rMonth = String.valueOf(month);
                }
                if (day < 10){
                    rDay = "0" + day;
                } else {
                    rDay = String.valueOf(day);
                }

                Log.d("MainActivity", " onDateSet: dd/mm/yyyy: " + rDay + "/" + rMonth + "/" + year);

                postDate = year + "-" + rMonth + "-" + rDay;
                salescount = 0;
                total = 0;
                profit_earned = 0.0;

                // reset filter spinner selection to 0
                filterBy.setAdapter(adapterFilter);
                currentSelection = filterBy.getSelectedItemPosition();
                System.out.println("currentSelection = "+currentSelection);

                // appCon.getdate

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(reportActivity.this);

                appConn = new AppConn();
                appConn.uuid = preferences.getString("uuid", null);
                appConn.postCashier = preferences.getString("cashieradmin", null);
                appConn.webLink = Global.URL;
                appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
                appConn.terminal_id = preferences.getString("t_id", null);
                appConn.postDate = postDate;
                appConn.commandpost = "getdate";
                appConn.dateReport(new ResponseListener() {
                    @Override
                    public void onResponse(final Response response) {
                        if (appConn.dateReportFailedcode != null){
                            pdialog.dismiss();
                            Toast.makeText(reportActivity.this, "Error " + appConn.dateReportFailedcode, Toast.LENGTH_SHORT).show();
                        } else {
                            pdialog.dismiss();
                            int array;
                            String result = appConn.res_server;
                            String hashtag[] = result.split("#");
                            report = new String[hashtag.length - 1][14];

                            for (array=0; array < hashtag.length; array++){
                                String pairs[] = hashtag[array].split("&");

                                if (pairs.length > 0){
                                    for (String pair : pairs){
                                        String values[] = pair.split("=", -1);

                                        if (values[0].equals("failedcode")){
                                            Toast.makeText(reportActivity.this, "error failedcode", Toast.LENGTH_SHORT).show();
                                        } else {

                                            if (values[0].equals("transactiontime")){
                                                report[array][0] = values[1].substring(0, 5);

                                            } else if (values[0].equals("cashiername")) {
                                                report[array][1] = values[1];

                                            } else if (values[0].equals("transactionservicetype")) {
                                                String product = values[1];
                                                if (product.equals("token")){
                                                    report[array][2] = "POWERINSTANT";

                                                } else if (product.contains("easi2dollars")){
                                                    report[array][2] = "EASI";

                                                } else if (product.equals("PCSB") || product.equals("pcsb")){
                                                    report[array][2] = "PROGRESIF TOPUP";

                                                } else if (product.equals("pcsbzoom")){
                                                    report[array][2] = "PROGRESIF ZOOM";

                                                } else if (product.equals("googleplayus")){
                                                    report[array][2] = "GOOGLEPLAY (US)";

                                                } else if (product.equals("itunesus")){
                                                    report[array][2] = "ITUNES (US)";

                                                }  else if (product.equals("telbruprepaidwifi")){
                                                    report[array][2] = "TELBRU (WIFI)";

                                                } else {
                                                    report[array][2] = values[1];
                                                }
                                            }
                                            else if (values[0].equals("transactionstatus")){
                                                report[array][3] = values[1];
                                                if (report[array][3].equals("SUCCESS")){
                                                    salescount++;
                                                }

                                            } else if (values[0].equals("transactionamount")){
                                                report[array][4] = "$" + values[1];
                                                if (report[array][3].equals("SUCCESS")){
                                                    count = Double.parseDouble(values[1]);
                                                    total += count;
                                                    System.out.println("=== comm : > " + profit_earned);

                                                }
                                            } else if (values[0].equals("transactiondate")){
                                                report[array][5] = values[1];

                                            } else if (values[0].equals("transactionid")){
                                                report[array][6] = values[1];

                                            } else if (values[0].equals("transactionserviceno")){
                                                report[array][7] = values[1];

                                            } else if (values[0].equals("merchantname")) {
                                                report[array][8] = values[1];

                                            } else if (values[0].equals("powerinstantownername")){
                                                report[array][9] = values[1];

                                            } else if (values[0].equals("powerinstantpowerunit")){
                                                report[array][10] = values[1];

                                            } else if (values[0].equals("token")){
                                                report[array][11] = values[1];

                                            } else if (values[0].equals("transactionphoneno")){
                                                report[array][12] = values[1];

                                            } else if (values[0].equals("transactionqrmemberid")){
                                                report[array][13] = values[1];

                                            }

                                            lv = findViewById(R.id.listView);
                                            listClick(report);
                                            lv.setAdapter(new ReportAdapter(reportActivity.this, report));

                                        }
                                    }
                                }

                                SRsalescount.setText("Sales Count : " + salescount);
                                SRtotal     .setText("Total Sales(BND)     : " + String.format("%.2f", total));
                                SRprofit    .setText("Profit Earned(BND) : " + String.format("%.2f", profit_earned));
                            }

                            // sorting report array items REF NO ASCENDING ORDER
                            System.out.println("report length = " + report.length);
                            Arrays.sort(report, new Comparator<String[]>() {
                                @Override
                                public int compare(String[] t1, String[] t2) {
                                    String item1 = t1[6];
                                    String item2 = t2[6];
                                    return item1.compareTo(item2);
                                }
                            });
                            for (int l=0; l<report.length; l++){
                                if (report[l][0] != null) {
                                    System.out.println("report: " + report[l][0] + " " + report[l][1] + " " + report[l][2] + " " + report[l][3] + " " + report[l][4] + " " + report[l][5] + " " + report[l][6]);
                                } else {
                                    System.out.println("report: null");
                                }
                            }
                            //

                        }
                    }

                    @Override
                    public void onError(String error) {
                        pdialog.dismiss();
                        Toast.makeText(reportActivity.this, "Protocol error : " + error, Toast.LENGTH_LONG).show();
                    }
                });

                // apppCon.getdate


                dateToday.setText(rDay + "/" + rMonth + "/" + year);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(reportActivity.this,
                R.style.AppTheme_Date,
                dateSetListener,
                year, month, day);
        datePickerDialog.getWindow();
        datePickerDialog.show();


    }

    private void getServiceFilter(String service){
        int count=0;
        total = 0.0;
        profit_earned = 0.0;
        String transAmount;
        String report2[][] = Arrays.copyOf(report, report.length);

        for (int i=0; i<report2.length;i++){
            if (!(report2[i][2].contains(service))){
                report2[i] = null;
                count++;
            }
        }

        int z =0;
        String temp[][] = new String[report2.length - count][12];
        for (int j=0; j<report2.length;j++){
            if (report2[j] != null){
                temp[z++] = report2[j];
            }
        }

        lv = findViewById(R.id.listView);
        listClick(temp);
        lv.setAdapter(new ReportAdapter(reportActivity.this, temp));

        for (int k=0; k<temp.length; k++){
            if (temp[k][3].equals("SUCCESS")){
                transAmount = temp[k][4].replace("$", "");
                total += Double.parseDouble(transAmount);
            }
        }

        salescount = temp.length;

        SRsalescount.setText("Sales Count : " + salescount);
        SRtotal     .setText("Total Sales(BND)     : " + String.format("%.2f", total));
        SRprofit    .setText("Profit Earned(BND) : " + String.format("%.2f", profit_earned));

    }

    public void settlementreport(String sort){
        final ProgressDialog pdialog = new ProgressDialog(reportActivity.this);
        pdialog.setMessage("Loading, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.dateReport = postDate;
        appConn.uuid = preferences.getString("uuid", null);
        appConn.postCashier = preferences.getString("cashieradmin", null);
        appConn.sortType = sort; //"timeasc";
        appConn.webLink = Global.URL;
        appConn.merchant_id = preferences.getString("m_id", null);
        appConn.terminal_id = preferences.getString("t_id", null);
        appConn.commandpost = "settlementreport";
        appConn.generateSettlement(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.settlementFailedcode != null){
                    pdialog.dismiss();
                    Toast.makeText(reportActivity.this, "Error " + appConn.settlementFailedcode, Toast.LENGTH_SHORT).show();
                } else {
                    pdialog.dismiss();

                    int array;
                    String result = appConn.res_server;
                    String hashtag[] = result.split("#");
                    report = new String[hashtag.length - 1][14];

                    for (array=0; array < hashtag.length; array++){
                        String pairs[] = hashtag[array].split("&");

                        if (pairs.length > 0){
                            for (String pair : pairs){
                                String values[] = pair.split("=", -1);

                                if (values[0].equals("failedcode")){
                                    Toast.makeText(reportActivity.this, "error failedcode", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (values[0].equals("transactiontime")){
                                        report[array][0] = values[1].substring(0, 5);

                                    } else if (values[0].equals("cashiername")) {
                                        report[array][1] = values[1];

                                    } else if (values[0].equals("transactionservicetype")) {
                                        String product = values[1];
                                        if (product.equals("token")){
                                            report[array][2] = "POWERINSTANT";

                                        } else if (product.contains("easi2dollars")){
                                            report[array][2] = "EASI";

                                        } else if (product.equals("pcsb") || product.equals("PCSB")){
                                            report[array][2] = "PROGRESIF TOPUP";

                                        } else if (product.equals("pcsbzoom")){
                                            report[array][2] = "PROGRESIF ZOOM";

                                        } else if (product.equals("googleplayus")){
                                            report[array][2] = "GOOGLEPLAY (US)";

                                        } else if (product.equals("itunesus")){
                                            report[array][2] = "ITUNES (US)";

                                        } else if (product.equals("telbruprepaidwifi")){
                                            report[array][2] = "TELBRU (WIFI)";

                                        } else {
                                            report[array][2] = values[1];
                                        }

                                    }  else if (values[0].equals("transactionstatus")){
                                        report[array][3] = values[1];
                                        if (report[array][3].equals("SUCCESS")){
                                            salescount++;
                                        }

                                    } else if (values[0].equals("transactionamount")){
                                        report[array][4] = "$" + values[1];
                                        if (report[array][3].equals("SUCCESS")){
                                            count = Double.parseDouble(values[1]);
                                            total += count;
                                            System.out.println("=== comm : > " + profit_earned);
                                        }

                                    } else if (values[0].equals("transactiondate")){
                                        report[array][5] = values[1];

                                    } else if (values[0].equals("transactionid")){
                                        report[array][6] = values[1];

                                    } else if (values[0].equals("transactionserviceno")){
                                        report[array][7] = values[1];

                                    } else if (values[0].equals("merchantname")){
                                        report[array][8] = values[1];

                                    } else if (values[0].equals("powerinstantownername")){
                                        report[array][9] = values[1];

                                    } else if (values[0].equals("powerinstantpowerunit")){
                                        report[array][10] = values[1];

                                    } else if (values[0].equals("token")){
                                        report[array][11] = values[1];

                                    } else if (values[0].equals("transactionphoneno")){
                                        report[array][12] = values[1];

                                    } else if (values[0].equals("transactionqrmemberid")){
                                        report[array][13] = values[1];

                                    }

                                    lv = findViewById(R.id.listView);
                                    listClick(report);
                                    lv.setAdapter(new ReportAdapter(reportActivity.this, report));
                                }
                            }
                        }
                        SRsalescount.setText("Sales Count : " + salescount);
                        SRtotal     .setText("Total Sales(BND)     : " + String.format("%.2f", total));
                        SRprofit    .setText("Profit Earned(BND) : " + String.format("%.2f", profit_earned));
                    }
                }

            }

            @Override
            public void onError(String error) {
                // somethings wrong
                pdialog.dismiss();
                Toast.makeText(reportActivity.this, "Protocol error : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void tlist(){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.sortType = "timeasc";
        appConn.webLink = Global.URL;
        appConn.merchant_id = preferences.getString("m_id", null);
        appConn.commandpost = "terminallist";
        appConn.getterminallist(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.settlementFailedcode != null){
                    Toast.makeText(reportActivity.this, "Error " + appConn.settlementFailedcode, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(reportActivity.this, "Res " + appConn.res_server, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(reportActivity.this, "Protocol error : " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void taBack(View view){
        exit();
    }

    private void exit(){
        final ProgressDialog pdialog = new ProgressDialog(reportActivity.this);
        pdialog.setMessage("Exiting, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(reportActivity.this);
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
                    startActivity(new Intent(reportActivity.this, navMainMenuActivity.class)
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
