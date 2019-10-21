package com.sunmi.pocketvendor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.activity.products.topups.progresifActivity;

public class pcsbProducts extends Activity {

    LinearLayout lvTopup, lvZoom;
    ImageView pre, zoo;

    String prepaid, zoom, servicepre, servicezoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcsb_products);

        lvTopup = findViewById(R.id.lv_topup);
        lvZoom  = findViewById(R.id.lv_zoom);
        pre     = findViewById(R.id.iv_prepaidprod);
        zoo     = findViewById(R.id.iv_zoomprod);

        prepaid     = getIntent().getStringExtra("prepaid");
        zoom        = getIntent().getStringExtra("zoom");
        servicepre  = getIntent().getStringExtra("sprepaid");
        servicezoom = getIntent().getStringExtra("szoom");

        if (prepaid.equals("no")){ pre.setImageResource(R.drawable.offprogresifp); }

        if (zoom.equals("no")){ zoo.setImageResource(R.drawable.offpzoom); }

        if (servicepre.equals("no")){ lvTopup.setVisibility(View.GONE); }

        if (servicezoom.equals("no")){ lvZoom.setVisibility(View.GONE); }
    }

    public void bgClick(View view){
        finish();
    }

    public void pcsbprepaid(View view){
        if (prepaid.equals("no")){
            Toast.makeText(pcsbProducts.this, "Progresif TopUp is currently unavailable", Toast.LENGTH_SHORT).show();

        } else {
            startActivity(new Intent(pcsbProducts.this, progresifActivity.class)
                    .putExtra("title", "PROGRESIF TOPUP")
                    .putExtra("pcsbtype","PCSB"));
            finish();
        }
    }

    public void pcsbzoom(View view){
        if (zoom.equals("no")){
            Toast.makeText(pcsbProducts.this, "Prepaid Zoom is currently unavailable", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(pcsbProducts.this, progresifActivity.class)
                    .putExtra("title", "PROGRESIF ZOOM")
                    .putExtra("pcsbtype","pcsbzoom"));
            finish();
        }
    }
}
