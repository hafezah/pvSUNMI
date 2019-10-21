package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.successPIActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

public class powerInstantConfirm extends AppCompatActivity {
    private AppConn appConn;

    private TextView name, meter, phone, totalamout;
    Button btnpiConfirm;
    String cName, cMeter, cPhone, cAmount, cEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_instant_confirm);

        LoadUI();
    }

    public void LoadUI(){
        name = findViewById(R.id.tv_piName);
        meter = findViewById(R.id.tv_piMeter);
        phone = findViewById(R.id.tv_piPhone);
        totalamout = findViewById(R.id.tv_piAmount);
        btnpiConfirm = findViewById(R.id.btn_piConfirm);

        cName = getIntent().getStringExtra("name");
        cMeter = getIntent().getStringExtra("meter");
        cPhone = getIntent().getStringExtra("phoneno");
        cAmount = getIntent().getStringExtra("amount");
        cEmail = getIntent().getStringExtra("piMail");

        name.setText(cName);
        meter.setText(cMeter);
        phone.setText("+673 "+cPhone);
        totalamout.setText("BND "+cAmount+".00");
    }

    public void piConfirm(View view){

        // Preventing multiple clicks, using threshold of 4 minutes
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                btnpiConfirm.setEnabled(false);
                btnpiConfirm.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                btnpiConfirm.setEnabled(true);
                btnpiConfirm.setText("CONFIRM");
            }
        }.start();

        final ProgressDialog pdialog = new ProgressDialog(powerInstantConfirm.this);
        pdialog.setMessage("Processing, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.piname = cName;

        if (getIntent().getStringExtra("piPid") != null){
            appConn.memberid = getIntent().getStringExtra("piPid");
        } else {
            appConn.memberid = "";
        }

        if(getIntent().getStringExtra("piMail") != null){
            appConn.pocketEmail = getIntent().getStringExtra("piMail");
        } else {
            appConn.pocketEmail = "";
        }

        appConn.transactionphone = "673"+cPhone;
        appConn.commandpost = "transaction";
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.terminal_id = preferences.getString("t_id", null);//Global.tID;
        appConn.uuid = preferences.getString("uuid", null);
        appConn.transactionamount = cAmount;//getIntent().getStringExtra("amount");
        appConn.transactionservicetype = "token";
        appConn.transactionserviceno = cMeter;//getIntent().getStringExtra("meter");
        appConn.executeTrans(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.transactionFailedcode != null){
                    pdialog.dismiss();
                    alertMessage("Error",appConn.transactionFailedcode);

                } else {
                    System.out.println("res server = " + appConn.res_server);
                    System.out.println("PI live ref = " + appConn.refNo);

                    if (appConn.res_server.equals("yes")){

                        pdialog.dismiss();

                        startActivity(new Intent(powerInstantConfirm.this, successPIActivity.class)
                                .putExtra("meter", getIntent().getStringExtra("meter"))
                                .putExtra("amount", totalamout.getText())//getIntent().getStringExtra("amount"))
                                .putExtra("token", appConn.res_token)
                                .putExtra("smsphone", cPhone)
                                .putExtra("trans_date", appConn.transactiondate)
                                .putExtra("trans_time", appConn.transactiontime)
                                .putExtra("refno", appConn.refNo)
                                .putExtra("powerunit", appConn.powerUnit));
                    } else {
                        pdialog.dismiss();
                        //alertMessage("TRANSACTION FAILED", "Error detected, transaction = no");
                        alertMessage("TRANSACTION FAILED", "Error detected on one of the fields, please redo your transaction");
                    }
                }
            }

            @Override
            public void onError(String error) {
                alertMessage("ERROR", error);
                pdialog.dismiss();
                //Toast.makeText(powerinstantConfirm.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(powerinstantConfirm.this, ErrorActivity.class));
            }
        });
    }

    public void powerCBack(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
