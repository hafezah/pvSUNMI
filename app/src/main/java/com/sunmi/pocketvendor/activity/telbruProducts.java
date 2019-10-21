package com.sunmi.pocketvendor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.telbruActivity;

public class telbruProducts extends Activity {

    String tbpw, tb098, tbbc, STATtbpw, STATtb098, STATtbbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telbru_products);

        getProducts();
    }

    private void getProducts(){
//        tbpw        = getIntent().getStringExtra("PrepaidWifi");
//        tb098       = getIntent().getStringExtra("");
//        tbbc        = getIntent().getStringExtra("");

//        STATtbpw    = getIntent().getStringExtra("statusPrepaidWifi");
//        STATtb098   = getIntent().getStringExtra("");
//        STATtbbc    = getIntent().getStringExtra("");

        // products and status conditions here
    }

    public void prepaidwifi(View view){
//        if (tbpw.equals("no")){
//            Toast.makeText(telbruProducts.this, "This service is currently unavailable", Toast.LENGTH_SHORT).show();
//        } else {

//        }

        startActivity(new Intent(telbruProducts.this, telbruActivity.class)
        .putExtra("product", "prepaidwifi"));
    }

    public void bgClick(View view){
        finish();
    }

}
