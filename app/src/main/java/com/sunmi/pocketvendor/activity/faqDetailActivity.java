package com.sunmi.pocketvendor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sunmi.pocketvendor.BaseActivity;
import com.sunmi.pocketvendor.R;
import com.sunmi.pocketvendor.network.listRAdapter;
import com.sunmi.pocketvendor.network.listRRAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class faqDetailActivity extends BaseActivity {

    TextView titlebar;
    LinearLayout linearLogo;
    ImageView imageLogo;
    ListView lv_right;
    ExpandableListView elv_right;
    String getItem, title;
    String contents[];
    List<String> headerlist;
    HashMap<String, List<String>> listHash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_detail);

        linearLogo = findViewById(R.id.ll_logo);
        imageLogo = findViewById(R.id.iv_logo);
        titlebar = findViewById(R.id.titlebar);
        lv_right = findViewById(R.id.lv_right);
        elv_right = findViewById(R.id.elv_right);
        getItem = getIntent().getStringExtra("listitem");

        System.out.println("=== get item : " + getItem);

        if (getItem.equals("item0")){
            //LOG IN AND ID CARD
            linearLogo.setVisibility(View.GONE);
            title = "LOG IN AND ID CARD";
            //contents = getResources().getStringArray(R.array.header1);
            //lv_right.setAdapter(new listRAdapter(faqDetailActivity.this, contents));
            faq1();

        } else if (getItem.equals("item1")){
            //SELLING PROCESS AND COMMISSION RATES
            linearLogo.setVisibility(View.GONE);
            title = "SELLING PROCESS AND COMMISSION RATES";
            //contents = getResources().getStringArray(R.array.header2);
            //lv_right.setAdapter(new listRAdapter(faqDetailActivity.this, contents));
            faq2();

        } else if (getItem.equals("item2")){
            //PRODUCTS
            title = "PRODUCTS";

        } else if (getItem.equals("item3")){
            //TRANSACTION ACTIVITY
            linearLogo.setVisibility(View.GONE);
            title = "TRANSACTION ACTIVITY";
            //contents = getResources().getStringArray(R.array.header4);
            //lv_right.setAdapter(new listRAdapter(faqDetailActivity.this, contents));
            faq4();

        } else if (getItem.equals("item4")){
            //ACCOUNT AND BALANCE RECHARGE
            linearLogo.setVisibility(View.GONE);
            title = "ACCOUNT AND BALANCE RECHARGE";
            //contents = getResources().getStringArray(R.array.header5);
            //lv_right.setAdapter(new listRAdapter(faqDetailActivity.this, contents));
            faq5();

        } else if (getItem.equals("item5")){
            //RENTAL FEE
            linearLogo.setVisibility(View.GONE);
            title = "RENTAL FEE";
            //contents = getResources().getStringArray(R.array.header6);
            //lv_right.setAdapter(new listRAdapter(faqDetailActivity.this, contents));
            faq6();

        } else if (getItem.equals("item6")){
            //WEB PORTAL
            linearLogo.setVisibility(View.GONE);
            title = "WEB PORTAL";
            //contents = getResources().getStringArray(R.array.header7);
            //lv_right.setAdapter(new listRAdapter(faqDetailActivity.this, contents));
            faq7();

        } else if (getItem.equals("item7")){
            //PRODUCT EASI
            linearLogo.setVisibility(View.VISIBLE);
            imageLogo.setImageResource(R.drawable.etp);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subeasi);
            easi();

        } else if (getItem.equals("item8")){
            //PRODUCT PCSB TOPUP
            imageLogo.setImageResource(R.drawable.ptp);
            linearLogo.setVisibility(View.VISIBLE);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subpcsbtp);
            pcsbtp();

        } else if (getItem.equals("item9")){
            //PRODUCT PCSB ZOOM
            imageLogo.setImageResource(R.drawable.ptpz);
            linearLogo.setVisibility(View.VISIBLE);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subpcsbzm);
            pcsbzm();

        } else if (getItem.equals("item10")){
            //PRODUCT POWERINSTANT
            imageLogo.setImageResource(R.drawable.pi);
            linearLogo.setVisibility(View.VISIBLE);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subpowerinstant);
            power();

        } else if (getItem.equals("item11")){
            //PRODUCT ITUNES
            imageLogo.setImageResource(R.drawable.itp);
            linearLogo.setVisibility(View.VISIBLE);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subitunes);
            itunes();

        } else if (getItem.equals("item12")){
            //PRODUCT GOOGLE
            imageLogo.setImageResource(R.drawable.gtp);
            linearLogo.setVisibility(View.VISIBLE);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subgoogle);
            google();

        } else if (getItem.equals("item13")){
            //PRODUCT TELBRU PREPAID WIFI
            imageLogo.setImageResource(R.drawable.telbru);
            title = "PRODUCTS";
            //contents = getResources().getStringArray(R.array.subtelbru);
            telbru();

        }

        titlebar.setText(title);
        elv_right.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    elv_right.collapseGroup(previousGroup);
                previousGroup = groupPosition;

            }
        });

    }

    private void faq1(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("How many cards can access the terminal?");
        headerlist.add("Can we get more cards?\n");
        headerlist.add("What should we do if we lose the ID card? Can we get a new one?");
        headerlist.add("What should we do if our ID card is stolen? Can we get a new one?");
        headerlist.add("Can 1 card be shared among my employees?\n");
        headerlist.add("We can’t seem to log in using our card, what should we do?");
        headerlist.add("Can other merchant’s ID card be used to log in our terminal?");
        headerlist.add("I have multiple branches with multiple terminals, can the ID cards access on all the terminals?");

        List<String> g1 = new ArrayList<>();
        g1.add("Depends on the number of ID cards that you have requested. We only provide 2 free ID cards.");

        List<String> g2 = new ArrayList<>();
        g2.add("Yes, additional ID cards can be given upon request and will be charged BND2 each.");

        List<String> g3 = new ArrayList<>();
        g3.add("Please contact our hotline (+673 8323999) immediately and we will disable the card. For a new card, you will be charged BND2.");

        List<String> g4 = new ArrayList<>();
        g4.add("Please contact our hotline (+673 8323999) immediately and we will disable the card. For a new card, you will be charged BND2.");

        List<String> g5 = new ArrayList<>();
        g5.add("Yes but it is better if 1 employee has 1 ID card. This is for security purposes.");

        List<String> g6 = new ArrayList<>();
        g6.add("Try to restart your terminal. If you still can’t log in, please contact our hotline (+673 8323999) and we will assist you.");

        List<String> g7 = new ArrayList<>();
        g7.add("No, each merchant has his/her own ID cards and terminal and the ID cards can only strictly be used on the designated terminal.");

        List<String> g8 = new ArrayList<>();
        g8.add("Yes.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);
        listHash.put(headerlist.get(6), g7);
        listHash.put(headerlist.get(7), g8);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void faq2(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Can we choose which product to sell? ");
        headerlist.add("How do we know the amount of money that we have earned from Pocket Vendor? ");
        headerlist.add("What should we do if we/the customer wrongly entered the amount?  ");
        headerlist.add("What should we do if we/the customer wrongly entered the phone number for Easi Top Up and PCSB TopUp? ");
        headerlist.add("What should we do if we/the customer wrongly entered the phone number for PCSB Zoom, PowerInstant, TelBru, Google Play giftcard and iTunes giftcard? ");
        headerlist.add("What should we do if customer refuses to give his/her phone number? ");
        headerlist.add("What should we do when customers want to top up later? ");
        headerlist.add("My customer has come back and requested for the receipt, what should we do? ");
        headerlist.add("What is the commission rate that we get for each of the product? ");
        headerlist.add("Can we get higher commission rates if we sell more? ");

        List<String> g1 = new ArrayList<>();
        g1.add("You may choose not to sell certain products, however the products will still be shown on your terminal. ");

        List<String> g2 = new ArrayList<>();
        g2.add("Your profit earned is stated on the web portal. ");

        List<String> g3 = new ArrayList<>();
        g3.add("ThreeG Media is not responsible for any replacement or reversal of Easi Prepaid Top Up, PCSB Prepaid Top Up or POWERInstant token as a result of wrongly entered mobile no., Prepaid Meter no. or amount by the Merchant and the customer.");

        List<String> g4 = new ArrayList<>();
        g4.add("ThreeG Media is not responsible for any replacement or reversal of Easi Prepaid Top Up, PCSB Prepaid Top Up or POWERInstant token as a result of wrongly entered mobile no., Prepaid Meter no. or amount by the Merchant and the customer.");

        List<String> g5 = new ArrayList<>();
        g5.add("ThreeG Media is not responsible if the PIN has been used as a result of wrongly entered number in the first place. You may contact our hotline (+673 8323999) and we will assist you.");

        List<String> g6 = new ArrayList<>();
        g6.add("At the moment, we are working on that matter.");

        List<String> g7 = new ArrayList<>();
        g7.add("At the moment, we are working on that matter. ");

        List<String> g8 = new ArrayList<>();
        g8.add("Please go to “Transaction Activity”, select the transaction and click “Print”. ");

        List<String> g9 = new ArrayList<>();
        g9.add("You can refer the commission rates on the agreement form that we have provided. You can also contact our hotline (+673 8323999) for more info.");

        List<String> g10 = new ArrayList<>();
        g10.add("All commission rates are currently fixed and this applies to all merchants. ");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);
        listHash.put(headerlist.get(6), g7);
        listHash.put(headerlist.get(7), g8);
        listHash.put(headerlist.get(8), g9);
        listHash.put(headerlist.get(9), g10);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void faq4(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("What do the red and green lights indicate on the transaction activity?");
        headerlist.add("Can we print out the list from the transaction activity?");
        headerlist.add("Can we select the dates to print out the list? For example from 8th July to 10th July. ");
        headerlist.add("Are the PINs shown on the transaction activity?\n");

        List<String> g1 = new ArrayList<>();
        g1.add("Red right means unsuccessful transaction, which means you should not be accepting the customer’s money and your balance should not be affected. Green light means successful transaction. ");

        List<String> g2 = new ArrayList<>();
        g2.add("Yes, simply select the date and click “Print List”. However, you are advised to use the web portal to see your transactions for a better view. ");

        List<String> g3 = new ArrayList<>();
        g3.add("You may do so on the web portal. Simply select the dates you want to print. ");

        List<String> g4 = new ArrayList<>();
        g4.add("For security purposes, the PINs are not shown on the receipt and the transaction activity. Only customers that have purchased the products can see the PINs via SMS. ");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void faq5(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("How do we recharge our balance?");
        headerlist.add("Is there a minimum or maximum amount to recharge our balance?");
        headerlist.add("Which banks can we recharge our balance to?\n");
        headerlist.add("Can we recharge our balance using BIBD Biller?\n");
        headerlist.add("How is the balance deducted?\n");
        headerlist.add("How does the sales target indicator work?\n");

        List<String> g1 = new ArrayList<>();
        g1.add("Please make the payment to any of our bank accounts, send the receipt with your Merchant Account Card via Whatsapp to +673 8323999. ");

        List<String> g2 = new ArrayList<>();
        g2.add("Minimum of BND300 and no maximum.");

        List<String> g3 = new ArrayList<>();
        g3.add("BIBD, Baiduri Bank and Standard Chartered. Full details can be found on the “Account” page.");

        List<String> g4 = new ArrayList<>();
        g4.add("Yes, simply select “Pocket Vendor” from the list, input your Merchant ID in the “Bill/Reference No. 1” box and enter the amount you would like to recharge. Send the receipt via Whatsapp to +673 8323999.");

        List<String> g5 = new ArrayList<>();
        g5.add("The amount deducted is the net price not the selling price. For example, when you sell PowerInstant $10, the amount deducted will be BND9.85 instead of BND10.");

        List<String> g6 = new ArrayList<>();
        g6.add("On a monthly basis, if you have not reached sales target of BND2000, the indicator will be red. After you have reached sales of BND2000, the indicator will turn green, which means the rental fee of BND20 will be waived. ");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void faq6(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("How does the rental fee work?");
        headerlist.add("How will you charge the rental fee?\n");
        headerlist.add("What will happen if we don’t have enough balance to pay the rental fee?");

        List<String> g1 = new ArrayList<>();
        g1.add("Each month, you need to reach a sales target of BND2000. If you manage to reach the sales target, the rental fee of BND20 will be waived. ");

        List<String> g2 = new ArrayList<>();
        g2.add("We will deduct from your balance.");

        List<String> g3 = new ArrayList<>();
        g3.add("The rental fee will be charged the next time you recharge your balance. ");


        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void faq7(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("How do we access to the web portal?");
        headerlist.add("Can we have multiple people to access the web portal?");
        headerlist.add("We have not received the mPIN even after a few minutes, what should we do?");

        List<String> g1 = new ArrayList<>();
        g1.add("Once your merchant account is signed up, you will receive a link via email to go to the web portal. If you have not received the email, please contact our hotline (+673 8323999).");

        List<String> g2 = new ArrayList<>();
        g2.add("Yes, as long as you have the password and received the mPIN.");

        List<String> g3 = new ArrayList<>();
        g3.add("Please reload the page and re-log in. If problem still occurs, please contact our hotline (+673 8323999). ");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void easi(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Is the validity period same with the physical card?");
        headerlist.add("Do customers get the same bonus credit like the physical card?");
        headerlist.add("My customer tried to purchase Easi Top Up but the transaction does not go through, what should we do?");

        List<String> g1 = new ArrayList<>();
        g1.add("Yes.");

        List<String> g2 = new ArrayList<>();
        g2.add("Yes");

        List<String> g3 = new ArrayList<>();
        g3.add("Customer’s line might be barred or Prima line or non-DST line. Please make sure your balance is not affected and refund the customer’s money.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void pcsbtp(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Is the validity period same with the physical card?");
        headerlist.add("Do customers get the same bonus credit like the physical card?");
        headerlist.add("My customer has purchased Progresif TopUp but the transaction does not go through, what should we do?");
        headerlist.add("Can customers purchase top up with change? For example, BND7.50?\n");

        List<String> g1 = new ArrayList<>();
        g1.add("Yes.");
        List<String> g2 = new ArrayList<>();
        g2.add("Yes.");
        List<String> g3 = new ArrayList<>();
        g3.add("Customer’s line might be barred or non-PCSB line. Please make sure your balance is not affected and refund the customer’s money.");
        List<String> g4 = new ArrayList<>();
        g4.add("No, top up purchased must be rounded off, therefore either BND7 or BND8.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void pcsbzm(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Is the validity period same with the physical card?");
        headerlist.add("Is this a direct top up?\n");
        headerlist.add("How do customers top up the PIN?\n");
        headerlist.add("Customer says he/she can’t top up the PIN, what should we do?");
        headerlist.add("My customer has requested the PIN to be printed on the receipt, what should we do?");
        headerlist.add("My customer has accidentally deleted the SMS that contains the PIN, what should we do?");
        headerlist.add("My customer does not remember his/her Zoom number, what should we do?");

        List<String> g1 = new ArrayList<>();
        g1.add("Yes, \nBND 4 - 2 day unlimited of data usage \nBND 8 - 3 days unlimited of data usage \nBND 35 - 18 days unlimited");
        List<String> g2 = new ArrayList<>();
        g2.add("No, the PINs will be send via SMS and customers are required to top up themselves.");
        List<String> g3 = new ArrayList<>();
        g3.add("Dial *178*PIN Number#");
        List<String> g4 = new ArrayList<>();
        g4.add("Please ensure that the customer os using a Progresif Zoom number to top up the PIN. If the customer is already using Progresif Zoom, kindly contact Progresif Customer Care line 177.");
        List<String> g5 = new ArrayList<>();
        g5.add("The PIN will only be shown in the SMS.");
        List<String> g6 = new ArrayList<>();
        g6.add("Please go to 'Transaction Activity', choose the transaction and click the 'Resend' button on the top right.");
        List<String> g7 = new ArrayList<>();
        g7.add("Any number can be used to purchase this product.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);
        listHash.put(headerlist.get(6), g7);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));
    }

    private void power(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Do customers get the same standard tariff like normal PowerKad?");
        headerlist.add("How do customers use the token given?\n");
        headerlist.add("What happen if we/the customers have entered the wrong Prepaid Meter number?");
        headerlist.add("What should we do if customer does not know or forget his/her meter number? Can they still purchase PowerInstant?");

        List<String> g1 = new ArrayList<>();
        g1.add("Yes.");
        List<String> g2 = new ArrayList<>();
        g2.add("Customers can enter the 20-digit token received from the SMS into their prepaid meter and press enter. Their meter will display “ACCEPTED” showing their 20-digit token had been successfully entered, and they can see the credit top-up value has increased either in B$ amount or KWh.");
        List<String> g3 = new ArrayList<>();
        g3.add("If you/customer have entered an active Prepaid Meter number that belongs to other person, the customer will still receive the 20-digit token for the Prepaid Meter number but it will not valid to your Prepaid Meter number.");
        g3.add("\nYou should always show the confirmation page to customers and make sure the right phone number and meter number have been entered.");
        List<String> g4 = new ArrayList<>();
        g4.add("PowerInstant can only be purchased when the meter number is entered. You can advice the customer to search “Pocket.com.bn” on Google Play or App Store. Customers can store their information such as meter number and phone number for future purchases.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));

    }

    private void itunes(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Can customers with non US account use the gift card?");
        headerlist.add("Is this a direct top up?\n");
        headerlist.add("How can customers redeem the code?\n");
        headerlist.add("The customer has already purchased the gift card but he/she just realizes that he/she cannot redeem because it is not a US account, hence the customer asks for a refund. What should we do?");
        headerlist.add("My staff accidentally accepted the wrong amount, i.e. BND 10 instead of BND 19, what should we do?");
        headerlist.add("My customer has requested the PIN to be printed on the receipt, what should we do?");
        headerlist.add("My customer has accidentally deleted the SMS that contains the PIN, what should we do?");

        List<String> g1 = new ArrayList<>();
        g1.add("No, the gift card is only valid for US account.");
        List<String> g2 = new ArrayList<>();
        g2.add("No, the PINs will be sent via SMS and customers are required to redeem the code themselves.");
        List<String> g3 = new ArrayList<>();
        g3.add("1. On iPhone, iPad, or iPod touch, open the App Store app.\n2. Tap Today, then tap the icon or your photo in the upper-right corner.\n3. Tap 'Redeem Gift Card or Code,' then sign in with your Apple ID.\n4. Enter the 16-digit code.\n5. Tap ‘Done’.");
        List<String> g4 = new ArrayList<>();
        g4.add("Once it is purchased, it can no longer be refunded.");
        List<String> g5 = new ArrayList<>();
        g5.add("ThreeG Media Sdn Bhd is not responsible if you have collected the wrong amount for these products.  You may contact the customer for the payment.");
        List<String> g6 = new ArrayList<>();
        g6.add("The PIN will only be shown in the SMS");
        List<String> g7 = new ArrayList<>();
        g7.add("Please go to “Transaction Activity” page, choose the transaction and click the “Resend” button on the top right.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);
        listHash.put(headerlist.get(6), g7);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));

    }

    private void google(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Can customers with non US account use the gift card?");
        headerlist.add("Is this a direct top up?\n");
        headerlist.add("How can customers redeem the code?\n");
        headerlist.add("The customer has already purchased the gift card but he/she just realizes that he/she cannot redeem because it is not a US account, hence the customer asks for a refund. What should we do?");
        headerlist.add("My staff accidentally accepted the wrong amount, i.e. BND 10 instead of BND 19, what should we do?");
        headerlist.add("My customer has requested the PIN to be printed on the receipt, what should we do?");
        headerlist.add("My customer has accidentally deleted the SMS that contains the PIN, what should we do?");

        List<String> g1 = new ArrayList<>();
        g1.add("No, the gift card is only valid for US account.");
        List<String> g2 = new ArrayList<>();
        g2.add("No, the PINs will be sent via SMS and customers are required to redeem the code themselves.");
        List<String> g3 = new ArrayList<>();
        g3.add("1. Open the Google Play Store app.\n2. Tap Menu > Redeem.\n3. Enter code.\n4. Tap Redeem.");
        List<String> g4 = new ArrayList<>();
        g4.add("Once it is purchased, it can no longer be refunded.");
        List<String> g5 = new ArrayList<>();
        g5.add("ThreeG Media Sdn Bhd is not responsible if you have collected the wrong amount for these products.  You may contact the customer for the payment.");
        List<String> g6 = new ArrayList<>();
        g6.add("The PIN will only be shown in the SMS.");
        List<String> g7 = new ArrayList<>();
        g7.add("Please go to “transaction activity”, choose the transaction and click the “resend” button on the top right.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);
        listHash.put(headerlist.get(6), g7);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));

    }

    private void telbru(){
        lv_right.setVisibility(View.GONE);
        elv_right.setVisibility(View.VISIBLE);
        headerlist = new ArrayList<>();
        headerlist.add("Is this a direct top up?");
        headerlist.add("How do customers top up the PIN?\n");
        headerlist.add("How to know where is the TelBru WiFi Zone?\n");
        headerlist.add("What is the validity of each denomination?\n");
        headerlist.add("My customer has requested the PIN to be printed on the receipt, what should we do?");
        headerlist.add("My customer has accidentally deleted the SMS that contains the PIN, what should we do?");

        List<String> g1 = new ArrayList<>();
        g1.add("No, the PINs will be sent via SMS and customers are required to top up themselves.");
        List<String> g2 = new ArrayList<>();
        g2.add("Select “TelBru WiFi” network at any TelBru WiFi Zone. Once connected, they will be redirected to a splash page.");
        g2.add("Select 'TelBru WiFi Prepaid'. Enter the Prepaid Plan PIN Code and click 'Agree and Submit'.");
        List<String> g3 = new ArrayList<>();
        g3.add("You can find TelBru WiFi Zone on \nhttps://www.telbru.com.bn/");
        List<String> g4 = new ArrayList<>();
        g4.add("$3 – 1 day unlimited\n$3 – 500MB valid for 3 days\n$5 – 1GB valid for 3 days\n$10 – 3GB valid for 14 days\n$15 – 7GB valid for 28 days");
        List<String> g5 = new ArrayList<>();
        g5.add("The PIN will only be shown in the SMS.");
        List<String> g6 = new ArrayList<>();
        g6.add("Please go to “Transaction Activity”, choose the transaction and click the “Resend” button on the top right.");

        listHash = new HashMap<>();
        listHash.put(headerlist.get(0), g1);
        listHash.put(headerlist.get(1), g2);
        listHash.put(headerlist.get(2), g3);
        listHash.put(headerlist.get(3), g4);
        listHash.put(headerlist.get(4), g5);
        listHash.put(headerlist.get(5), g6);

        elv_right.setAdapter(new listRRAdapter(faqDetailActivity.this, headerlist, listHash));

    }

    public void faqDetailBack(View view){
        finish();
    }

}


