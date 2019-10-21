package com.sunmi.pocketvendor.activity.products.topups;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.errorActivity;
import com.sunmi.pocketvendor.activity.successActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

public class easiConfirm extends BaseActivity {
    private AppConn appConn;
    String getAmount, getPhone, getTopup;
    TextView phoneEasi, amountEasi;
    Button btneConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easi_confirm);

        phoneEasi = findViewById(R.id.tv_phoneEasi);
        amountEasi = findViewById(R.id.tv_amountDeno);
        btneConfirm = findViewById(R.id.btn_eConfirm);

        getTopup = getIntent().getStringExtra("topupamt");
        getAmount = getIntent().getStringExtra("denoAmount");
        getPhone = getIntent().getStringExtra("phoneeasi");

        phoneEasi.setText("+673 " +getPhone);
        amountEasi.setText("BND $" +getTopup+".00");
    }

    public void eConfirm(View view){

        Long tsLong = System.currentTimeMillis()/1000;
        log("cETP(" + tsLong.toString() + ")");

        // Preventing multiple clicks, using threshold of 4 minutes
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                btneConfirm.setEnabled(false);
                btneConfirm.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                btneConfirm.setEnabled(true);
                btneConfirm.setText("CONFIRM");
            }
        }.start();

        final ProgressDialog pdialog = new ProgressDialog(easiConfirm.this);
        pdialog.setMessage("Processing, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.piname = "";
        if (getIntent().getStringExtra("easipid") != null){
            appConn.memberid = getIntent().getStringExtra("easipid");
        } else {
            appConn.memberid = "";
        }

        if (getIntent().getStringExtra("easiPmail") != null){
            appConn.pocketEmail = getIntent().getStringExtra("easiPmail");
        } else {
            appConn.pocketEmail = "";
        }

        appConn.transactionamount = getAmount;
        appConn.transactionphone = "673"+getPhone;
        appConn.transactionserviceno = "673"+getPhone;

        if (getAmount.equals("1002")){
            appConn.transactionservicetype = "easi2dollars";
        } else {
            appConn.transactionservicetype = "EASI"; //easi2dollars
        }

        appConn.uuid = preferences.getString("uuid", null);
        appConn.terminal_id = preferences.getString("t_id", null);//Global.tID;
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.commandpost = "transaction";
        appConn.executeTrans(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.transactionFailedcode != null){
                    pdialog.dismiss();
                    alertMessage("Error",appConn.transactionFailedcode);

                } else {

                    if (appConn.res_server.equals("yes")){
                        pdialog.dismiss();

                        System.out.println("E live ref = " + appConn.refNo);
                        startActivity(new Intent(easiConfirm.this, successActivity.class)
                                .putExtra("title", "EASI Top-Up")
                                .putExtra("phone", getPhone)
                                .putExtra("trans_date", appConn.transactiondate)
                                .putExtra("trans_time", appConn.transactiontime)
                                .putExtra("refno", appConn.refNo)
                                .putExtra("amount", amountEasi.getText()));
                        //finish();
                    } else {
                        pdialog.dismiss();
                        //alertMessage("TRANSACTION FAILED", "Error detected, transaction = no");
                        alertMessage("TRANSACTION FAILED", "Error detected on one of the fields, please redo your transaction");
                    }
                }
            }

            @Override
            public void onError(String error) {
                //alertMessage("ERROR", error);
                pdialog.dismiss();
                finish();
                Toast.makeText(easiConfirm.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(easiConfirm.this, errorActivity.class));
            }
        });
    }

    public void easiCBack(View view){
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
