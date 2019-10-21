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

public class progresifConfirm extends AppCompatActivity {

    String pcsbGetPhone, pcsbTopup, pcsbGetProduct, setTitle;
    int pcsbGetAmount, denoPcsb;
    TextView tvPhone, tvAmount, tvProduct;
    Button btnpConfrim;
    ImageView logo;

    private AppConn appConn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresif_confirm);

        tvPhone     = findViewById(R.id.tv_pcsbPhone);
        tvAmount    = findViewById(R.id.tv_pcsbAmount);
        tvProduct   = findViewById(R.id.tv_pcsbProduct);

        logo        = findViewById(R.id.iv_pcsbtype);

        btnpConfrim = findViewById(R.id.btn_pConfirm);

        pcsbGetPhone = getIntent().getStringExtra("pcsbPhone");
        pcsbGetAmount = getIntent().getIntExtra("pcsbAmount", 0);
        pcsbGetProduct = getIntent().getStringExtra("type");

        if (pcsbGetProduct.equals("PCSB")){
            denoPcsb = 2000 + pcsbGetAmount;
            tvProduct.setText("Progresif TopUp");
            logo.setBackgroundResource(R.drawable.ptp);
        } else if (pcsbGetProduct.equals("pcsbzoom")){
            denoPcsb = pcsbGetAmount;
            tvProduct.setText("Progresif Zoom");
            pcsbGetProduct = "pcsbzoom";
            logo.setBackgroundResource(R.drawable.pcsbz);
        }


        pcsbTopup = Integer.toString(denoPcsb);

        tvPhone.setText("+673 " + pcsbGetPhone);
        tvAmount.setText("BND " + pcsbGetAmount+".00");
    }

    public void pConfirm(View view){

        // Preventing multiple clicks, using threshold of 4 minutes
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                btnpConfrim.setEnabled(false);
                btnpConfrim.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                btnpConfrim.setEnabled(true);
                btnpConfrim.setText("CONFIRM");
            }
        }.start();

        final ProgressDialog pdialog = new ProgressDialog(progresifConfirm.this);
        pdialog.setMessage("Processing, Please wait...");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setCancelable(false);
        pdialog.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        appConn = new AppConn();
        appConn.webLink = Global.URL;
        appConn.transactionphone = "673"+pcsbGetPhone;
        appConn.piname = "";

        if (getIntent().getStringExtra("pcsbpid") != null){
            appConn.memberid = getIntent().getStringExtra("pcsbpid");
        } else {
            appConn.memberid = "";
        }

        if(getIntent().getStringExtra("pcsbmail") != null){
            appConn.pocketEmail = getIntent().getStringExtra("pcsbmail");
        } else {
            appConn.pocketEmail = "";
        }

        appConn.commandpost = "transaction";
        appConn.merchant_id = preferences.getString("m_id", null);//Global.mID;
        appConn.terminal_id = preferences.getString("t_id", null);//Global.tID;
        appConn.uuid = preferences.getString("uuid", null);
        appConn.transactionamount = pcsbTopup;
        appConn.transactionservicetype = pcsbGetProduct;

        if (pcsbGetProduct.equals("PCSB")){
            appConn.transactionserviceno = "673"+pcsbGetPhone;
            setTitle = "Progresif TopUp";
        } else if (pcsbGetProduct.equals("pcsbzoom")){
            appConn.transactionserviceno = getIntent().getStringExtra("serviceno");
            setTitle = "Progresif Zoom";
        }

        appConn.executeTrans(new ResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (appConn.transactionFailedcode != null){
                    pdialog.dismiss();
                    alertMessage("Error",appConn.transactionFailedcode);

                } else {
                    System.out.println("res server = " + appConn.res_server);
                    System.out.println("P live ref = " + appConn.refNo);

                    if (appConn.res_server.equals("yes")){
                        pdialog.dismiss();
                        startActivity(new Intent(progresifConfirm.this, successActivity.class)
                                .putExtra("phone", pcsbGetPhone)
                                .putExtra("amount", tvAmount.getText())
                                .putExtra("trans_date", appConn.transactiondate)
                                .putExtra("trans_time", appConn.transactiontime)
                                .putExtra("refno", appConn.refNo)
                                .putExtra("title", setTitle)
                                .putExtra("type", pcsbGetProduct));
                        //finish();
                    } else {
                        pdialog.dismiss();
                        //alertMessage("TRANSACTION FAILED", "Error detected, transaction = no");
                        alertMessage("TRANSACTION FAILED", "Error detected: Invalid phone number");
                    }
                }
            }

            @Override
            public void onError(String error) {
                //alertMessage("ERROR", error);
                pdialog.dismiss();
                finish();
                Toast.makeText(progresifConfirm.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(progresifConfirm.this, errorActivity.class));
            }
        });
    }

    public void progresifCBack(View view){
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
