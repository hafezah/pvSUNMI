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

public class itunesConfirm extends AppCompatActivity {

    private AppConn appConn;
    TextView citphone, citamount;
    ImageView itselection;
    Button it_confirm;
    private String user_select, itus_phone, itus_amount, denois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itunes_confirm);

        itus_phone = getIntent().getStringExtra("itphone");
        itus_amount = getIntent().getStringExtra("itamt");

        citphone = findViewById(R.id.tv_phoneIT);
        citamount = findViewById(R.id.tv_amountIT);
        itselection = findViewById(R.id.iv_selectedit);
        it_confirm = findViewById(R.id.btn_iConfirm);

        citphone.setText("+673 " + itus_phone);
        citamount.setText("BND " + itus_amount);

        user_select = getIntent().getStringExtra("itselect");

        if (user_select.equals("1")){
            itselection.setBackgroundResource(R.drawable.it5);
            denois = "5";

        } else if (user_select.equals("2")){
            itselection.setBackgroundResource(R.drawable.it10);
            denois = "10";

        } else if (user_select.equals("3")){
            itselection.setBackgroundResource(R.drawable.it15);
            denois = "15";

        } else if (user_select.equals("4")){
            itselection.setBackgroundResource(R.drawable.it25);
            denois = "25";

        } else {
            itselection.setBackgroundResource(R.drawable.it10);
            denois = "";

        }
    }

    public void iConfirm(View view){

        // Preventing multiple clicks, using threshold of 4 minutes
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                it_confirm.setEnabled(false);
                it_confirm.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                it_confirm.setEnabled(true);
                it_confirm.setText("CONFIRM");
            }
        }.start();

        final ProgressDialog pdialog = new ProgressDialog(itunesConfirm.this);
        pdialog.setMessage("Processing, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.transactionphone = "673" + itus_phone;
        appConn.piname = "";

        if (getIntent().getStringExtra("ituspid") != null){
            appConn.memberid = getIntent().getStringExtra("ituspid");
        } else {
            appConn.memberid = "";
        }

        if(getIntent().getStringExtra("itusmail") != null){
            appConn.pocketEmail = getIntent().getStringExtra("itusmail");
        } else {
            appConn.pocketEmail = "";
        }

        appConn.commandpost = "transaction";
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.terminal_id = preferences.getString("t_id", null);//Global.tID;
        appConn.uuid = preferences.getString("uuid", null);
        appConn.transactionamount = itus_amount;
        appConn.transactionservicetype = "itunesus";
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
                        startActivity(new Intent(itunesConfirm.this, successActivity.class)
                                .putExtra("phone", itus_phone)
                                .putExtra("amount",itus_amount)
                                .putExtra("trans_date", appConn.transactiondate)
                                .putExtra("trans_time", appConn.transactiontime)
                                .putExtra("refno", appConn.refNo)
                                .putExtra("title", "iTunes Gift Card")
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
                Toast.makeText(itunesConfirm.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(itunesConfirm.this, errorActivity.class));
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

    public void itCBack(View view){
        finish();
    }
}
