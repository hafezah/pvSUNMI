package com.sunmi.pocketvendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;

public class faqActivity extends BaseActivity {

    TextView h1, h2, h3, h4, h5, h6, h7, sh1, sh2, sh3, sh4, sh5, sh6, sh7;
    ImageView expandcollapse;
    LinearLayout sub;
    String header[];
    String subheader[];
    boolean expand = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        header = getResources().getStringArray(R.array.headerstr);
        subheader = getResources().getStringArray(R.array.subheader);

        method1();

    }

    private void method1(){
        h1  = findViewById(R.id.tv_h1);
        h2  = findViewById(R.id.tv_h2);
        h3  = findViewById(R.id.tv_h3);
        h4  = findViewById(R.id.tv_h4);
        h5  = findViewById(R.id.tv_h5);
        h6  = findViewById(R.id.tv_h6);
        h7  = findViewById(R.id.tv_h7);
        sh1 = findViewById(R.id.tv_sh1);
        sh2 = findViewById(R.id.tv_sh2);
        sh3 = findViewById(R.id.tv_sh3);
        sh4 = findViewById(R.id.tv_sh4);
        sh5 = findViewById(R.id.tv_sh5);
        sh6 = findViewById(R.id.tv_sh6);
        sh7 = findViewById(R.id.tv_sh7);
        sub = findViewById(R.id.ll_sub);
        expandcollapse = findViewById(R.id.iv_ec);

        if (!expand){
            sub.setVisibility(View.GONE);
        } else {
            sub.setVisibility(View.VISIBLE);
        }

        h1.setText("Log In and ID Card");
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                .putExtra("listitem", "item0"));
            }
        });
        h2.setText("Selling Process and Commission Rates");
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                .putExtra("listitem", "item1"));
            }
        });
        h3.setText("Products");
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand){
                    sub.setVisibility(View.GONE);
                    expandcollapse.setImageResource(R.drawable.listexpand);
                    expand = false;
                } else {
                    sub.setVisibility(View.VISIBLE);
                    expandcollapse.setImageResource(R.drawable.listcollapes);
                    expand = true;
                }
            }
        });
        h4.setText("Transaction Activity");
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                .putExtra("listitem", "item3"));
            }
        });
        h5.setText("Account and Balance Recharge");
        h5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                .putExtra("listitem", "item4"));
            }
        });
        h6.setText("Rental Fee");
        h6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                .putExtra("listitem", "item5"));
            }
        });
        h7.setText("Web Portal");
        h7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                .putExtra("listitem", "item6"));
            }
        });

        sh1.setText("Easi Top-Up");
        sh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item7"));
            }
        });
        sh2.setText("Progresif TopUp");
        sh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item8"));
            }
        });
        sh3.setText("Progresif Zoom");
        sh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item9"));
            }
        });
        sh4.setText("POWERInstant");
        sh4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item10"));
            }
        });
        sh5.setText("iTunes Gift Card");
        sh5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item11"));
            }
        });
        sh6.setText("Google Play Gift Card");
        sh6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item12"));
            }
        });
        sh7.setText("Telbru Prepaid Wifi");
        sh7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(faqActivity.this, faqDetailActivity.class)
                        .putExtra("listitem", "item13"));
            }
        });
    }

    public void excol(View view){
        if (expand){
            sub.setVisibility(View.GONE);
            expandcollapse.setImageResource(R.drawable.listexpand);
            expand = false;
        } else {
            sub.setVisibility(View.VISIBLE);
            expandcollapse.setImageResource(R.drawable.listcollapes);
            expand = true;
        }
    }

    public void faqBack(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
