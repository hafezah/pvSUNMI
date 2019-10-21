package com.sunmi.pocketvendor.activity.products.games;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sunmi.pocketvendor.R;

public class xboxGiftCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xbox_gift_card);
    }

    public void xgiftNext(View view){
        startActivity(new Intent(xboxGiftCardActivity.this, gamesConfirm.class)
                .putExtra("gphone", "")
                .putExtra("gdesc", "")
                .putExtra("gamount", ""));
    }
}
