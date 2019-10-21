package com.sunmi.pocketvendor.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WebAsyncTask extends AsyncTask<Void, Void, Response> {

    private final HashMap<String, String> data;
    private final ResponseListener listener;

    // configuration
    private String method = "POST";
    private String baseUrl;
    private String endPoint = Global.PHP;
    private String commandPost;
    private int connectionTimeout = 6000; // 6 seconds
    private int readTimeout = 120000; // 60 seconds initial 60000

    public WebAsyncTask(String command, String webLink, HashMap<String, String> data, ResponseListener listener) {
        this.baseUrl = webLink;
        this.data = data;
        this.listener = listener;
        this.commandPost = command;
    }

    private String getPostDataString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : data.entrySet()){

            try {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    @Override
    protected Response doInBackground(Void... params) {
        try {

            // create query string
            String queryString = getPostDataString();

            // create hash
            //String hash = Utils.hmacSha1(queryString, "123456789");

            // append hash to the query string
            //queryString += "&hashstr=" + hash;

            Log.e("QueryString", queryString);

            if (commandPost.equals("login")){
                endPoint = Global.PHP;
            } else if (commandPost.equals("treg")){
                endPoint = Global.TREG;
            } else if (commandPost.equals("refresh")){
                endPoint = Global.REFRESH;
            } else if (commandPost.equals("metercheck")){
                endPoint = Global.METERCHECKER;
            } else if (commandPost.equals("transaction")){
                endPoint = Global.TRANSACTION;
            } else if (commandPost.equals("denominationcheck")){
                endPoint = Global.METERCHECKER;
            } else if (commandPost.equals("accountreport")) {
                endPoint = Global.ACCOUNT;
            } else if (commandPost.equals("settlementreport") || commandPost.equals("terminallist")){
                endPoint = Global.SETTLEMENTREPORT;
            } else if (commandPost.equals("getdate")){
                endPoint = Global.DATEREPORT;
            } else if (commandPost.equals("resendsms")){
                endPoint = Global.SETTLEMENTREPORT;
            } else if (commandPost.equals("depo")){
                endPoint = "test.php";
            } else {
                endPoint = "";
            }

            URL url = new URL(baseUrl + "/" + endPoint);
            System.out.println("=== this url is : " + url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(connectionTimeout);
            conn.setRequestMethod(this.method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(queryString);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int responseCode = conn.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response.append(line);
                }
            } else {
                response.append("");
            }

            return new Response(responseCode, response.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Response s) {
        super.onPostExecute(s);

        if (this.listener != null) {
            if (s == null) {
                this.listener.onError("Request time out, please try again");
            } else if (s.getResult().length() == 0) {
                this.listener.onError("Server unresponsive");
            } else if (!s.getResult().contains("&")) {
                this.listener.onError("Protocol error: " + s.getResult().substring(11, s.getResult().length()));
            } else {
                this.listener.onResponse(s);
            }
        }
    }
}
