package com.sunmi.pocketvendor.activity.products.games;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sunmi.pocketvendor.R;

public class gamesConfirm extends AppCompatActivity {

    String get_gphone, get_gdesc, get_gamount;
    TextView gPhone, gDeno;
    Button gConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_confirm);

        get_gphone  = getIntent().getStringExtra("gphone");
        get_gdesc   = getIntent().getStringExtra("gdesc");
        get_gamount = getIntent().getStringExtra("gamount");

        gPhone      = findViewById(R.id.tv_phoneGames);
        gDeno       = findViewById(R.id.tv_denoGames);
        gConfirm    = findViewById(R.id.btn_gamesConfirm);

    }

    public void gamesConfirm(View view){
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                gConfirm.setEnabled(false);
                gConfirm.setText("DISABLED (" + l/1000 + "s)");

            }

            @Override
            public void onFinish() {
                gConfirm.setEnabled(true);
                gConfirm.setText("CONFIRM");

            }
        }.start();
    }

}
