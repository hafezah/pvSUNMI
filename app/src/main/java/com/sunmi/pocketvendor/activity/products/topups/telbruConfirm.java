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

public class telbruConfirm extends AppCompatActivity {

    private AppConn appConn;

    ImageView productlogo;
    TextView telphone, telproduct, telamount;
    Button tbConfirm;
    String userphone, userproduct, userdeno, productdesc, productcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telbru_confirm);

        initView();

    }

    private void initView(){
        userphone   = getIntent().getStringExtra("tbphone");
        userproduct = getIntent().getStringExtra("tbproduct");
        userdeno    = getIntent().getStringExtra("tbdeno");
        productdesc = getIntent().getStringExtra("tbdesc");
        productcode = getIntent().getStringExtra("tbproductcode");

        productlogo = findViewById(R.id.iv_tel_confirm);
        telphone    = findViewById(R.id.tv_tel_phone);
        telproduct  = findViewById(R.id.tv_tel_product);
        telamount   = findViewById(R.id.tv_tel_deno);

        tbConfirm   = findViewById(R.id.btn_tbConfirm);

        telphone    .setText("+673 " + userphone);
        telamount   .setText("BND " + userdeno);

        if (userproduct.contains("prepaid")){
            // telbru prepaid - wifi
            productlogo.setImageResource(R.drawable.telprepaid);
            telproduct  .setText("Prepaid WiFi " + productdesc);


        } else if (userproduct.contains("098")){
            // telbru 098 calling card
            // productlogo.setImageResource(R.drawable.telprepaid);

        } else if (userproduct.contains("budget")){
            // telbru budget call
            // productlogo.setImageResource(R.drawable.telprepaid);

        }
    }

    public void telbruCBack(View view){
        finish();
    }

    public void tbConfirm(View view){
        //Toast.makeText(telbruConfirm.this, "Execute payment", Toast.LENGTH_SHORT).show();
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                tbConfirm.setEnabled(false);
                tbConfirm.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                tbConfirm.setEnabled(true);
                tbConfirm.setText("CONFIRM");
            }
        }.start();

        final ProgressDialog pdialog = new ProgressDialog(telbruConfirm.this);
        pdialog.setMessage("Processing, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.transactionphone = "673" + userphone;
        appConn.piname = "";

        if (getIntent().getStringExtra("tpid") != null){
            appConn.memberid = getIntent().getStringExtra("tpid");
        } else {
            appConn.memberid = "";
        }

        if(getIntent().getStringExtra("tpmail") != null){
            appConn.pocketEmail = getIntent().getStringExtra("tpmail");
        } else {
            appConn.pocketEmail = "";
        }

        appConn.commandpost = "transaction";
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.terminal_id = preferences.getString("t_id", null);//Global.tID;
        appConn.uuid = preferences.getString("uuid", null);
        appConn.transactionamount = userdeno;
        appConn.transactionservicetype = "telbruprepaidwifi";
        appConn.transactionserviceno = productcode; // product code: 1, 2, 3, 4, 5
        appConn.executeTrans(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.transactionFailedcode != null){
                    pdialog.dismiss();
                    alertMessage("Error",appConn.transactionFailedcode);

                } else {

                    if (appConn.res_server.equals("yes")){
                        pdialog.dismiss();
                        startActivity(new Intent(telbruConfirm.this, successActivity.class)
                                .putExtra("phone", userphone)
                                .putExtra("amount",userdeno)
                                .putExtra("trans_date", appConn.transactiondate)
                                .putExtra("trans_time", appConn.transactiontime)
                                .putExtra("refno", appConn.refNo)
                                .putExtra("title", "TelBru Prepaid WiFi")
                                .putExtra("product", productdesc));
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
                pdialog.dismiss();
                finish();
                Toast.makeText(telbruConfirm.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(telbruConfirm.this, errorActivity.class));
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
}
