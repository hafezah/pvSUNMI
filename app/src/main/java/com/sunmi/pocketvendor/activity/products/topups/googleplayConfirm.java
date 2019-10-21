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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pocketvendor.AppConn;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.errorActivity;
import com.sunmi.pocketvendor.activity.successActivity;
import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.network.Response;
import com.sunmi.pocketvendor.network.ResponseListener;

public class googleplayConfirm extends AppCompatActivity {

    private AppConn appConn;
    TextView phoneno, gpamt;
    ImageView gpselection;
    Button gp_confrim;
    private String user_select, gpus_phone, gpus_amount, denois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googleplay_confirm);

        gpus_phone = getIntent().getStringExtra("gpphone");
        gpus_amount = getIntent().getStringExtra("gpamt");

        phoneno = findViewById(R.id.tv_phoneGP);
        gpamt = findViewById(R.id.tv_amountGP);
        gpselection = findViewById(R.id.iv_selectedgp);
        gp_confrim = findViewById(R.id.btn_gConfirm);

        phoneno.setText("+673 " + gpus_phone);
        gpamt.setText("BND " + gpus_amount);

        user_select = getIntent().getStringExtra("gpselect");

        if (user_select.equals("0")){
            gpselection.setBackgroundResource(R.drawable.gp5);
            denois = "5";

        } else if (user_select.equals("1")){
            gpselection.setBackgroundResource(R.drawable.gp10);
            denois = "10";

        } else if (user_select.equals("2")){
            gpselection.setBackgroundResource(R.drawable.gp15);
            denois = "15";

        } else if (user_select.equals("3")){
            gpselection.setBackgroundResource(R.drawable.gp25);
            denois = "25";

        } else {
            gpselection.setBackgroundResource(R.drawable.gp10);
            denois = "";

        }
    }

    public void gConfirm(View view){

        // Preventing multiple clicks, using threshold of 4 minutes
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                gp_confrim.setEnabled(false);
                gp_confrim.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                gp_confrim.setEnabled(true);
                gp_confrim.setText("CONFIRM");
            }
        }.start();

        final ProgressDialog pdialog = new ProgressDialog(googleplayConfirm.this);
        pdialog.setMessage("Processing, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.transactionphone = "673" + gpus_phone;
        appConn.piname = "";

        if (getIntent().getStringExtra("gpuspid") != null){
            appConn.memberid = getIntent().getStringExtra("gpuspid");
        } else {
            appConn.memberid = "";
        }

        if(getIntent().getStringExtra("gpusmail") != null){
            appConn.pocketEmail = getIntent().getStringExtra("gpusmail");
        } else {
            appConn.pocketEmail = "";
        }

        appConn.commandpost = "transaction";
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.terminal_id = preferences.getString("t_id", null);//Global.tID;
        appConn.uuid = preferences.getString("uuid", null);
        appConn.transactionamount = gpus_amount;
        appConn.transactionservicetype = "googleplayus";
        appConn.transactionserviceno = denois;
        appConn.executeTrans(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.transactionFailedcode != null){
                    pdialog.dismiss();
                    alertMessage("Error",appConn.transactionFailedcode);

                } else {

                    if (appConn.res_server.equals("yes")){
                        pdialog.dismiss();
                        startActivity(new Intent(googleplayConfirm.this, successActivity.class)
                                .putExtra("phone", gpus_phone)
                                .putExtra("amount",gpus_amount)
                                .putExtra("trans_date", appConn.transactiondate)
                                .putExtra("trans_time", appConn.transactiontime)
                                .putExtra("refno", appConn.refNo)
                                .putExtra("title", "GooglePlay Gift Card")
                                .putExtra("product", denois));
                        //finish();
                    } else {
                        pdialog.dismiss();
                        //alertMessage("TRANSACTION FAILED", "Error detected, transaction = no");
                        alertMessage("TRANSACTION FAILED", "Reply code : " + appConn.res_server);
                    }
                }
            }

            @Override
            public void onError(String error) {
                //alertMessage("ERROR", error);
                pdialog.dismiss();
                finish();
                Toast.makeText(googleplayConfirm.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(googleplayConfirm.this, errorActivity.class));
            }
        });
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

    public void gpCBack(View view){
        finish();
    }

}
