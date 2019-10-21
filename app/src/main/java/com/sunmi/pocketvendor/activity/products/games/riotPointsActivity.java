package com.sunmi.pocketvendor.activity.products.games;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sunmi.pocketvendor.R;

public class riotPointsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riot_points);
    }

    public void rpNext(View view){
        startActivity(new Intent(riotPointsActivity.this, gamesConfirm.class)
                .putExtra("gphone", "")
                .putExtra("gdesc", "")
                .putExtra("gamount", ""));
    }
}
