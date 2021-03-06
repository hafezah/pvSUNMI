package com.sunmi.pocketvendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.sunmi.pocketvendor.network.Global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

public class AppLogg {

    private File folderPath;
    private String filename, getcontent;

    private Date c = Calendar.getInstance().getTime();

    private SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
    private SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
    private String currentdate = df.format(c);
    private String currenttime = tf.format(c);

    public void getlog(Context context, String str){
        String logstr;
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        logstr = p.getString("applogg", null);
        p.edit().putString("applogg", logstr + "/" + str).apply();

    }

    public void logit(Context context, String contents){
        File rootPath = Environment.getExternalStorageDirectory();
        folderPath = new File(rootPath.getAbsolutePath() + Global.FOLDER);
        filename =  currentdate + ".txt";

        File[] files = folderPath.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            System.out.println("===> filename: " + files[i].getName());

            File textFile = new File(folderPath, filename);
            if (textFile.exists()){
                // append log
                if (currentdate.equals(files[i].getName().replace(".txt", ""))){
                    try {
//                    textFile.delete();
//                    textFile.createNewFile();
                        String getline;
                        FileInputStream fis = new FileInputStream(textFile);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        while ( (getline = br.readLine()) != null ) {
                            getcontent = getline;
                        }
                        fis.close();
                        br.close();

                        FileOutputStream fos = new FileOutputStream(textFile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos);

                        StringBuilder sb = new StringBuilder();
                        sb.append(getcontent+ "\n");
                        sb.append(currenttime + "-> [A]" + contents + "\n");
                        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
                        p.edit().clear().apply();

                        osw.append(sb);
                        osw.close();

                        fos.flush();
                        fos.close();

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } else {
                folderPath.mkdir();
                try {
                    textFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(textFile);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);

                    StringBuilder sb = new StringBuilder();
                    sb.append(currenttime + "-> [A]" + contents + "\n");

                    osw.append(sb);
                    osw.close();

                    fos.flush();
                    fos.close();

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void errlogit(Context context, String contents){
        File rootPath = Environment.getExternalStorageDirectory();
        folderPath = new File(rootPath.getAbsolutePath() + Global.FOLDER);
        filename =  currentdate + ".txt";

        File[] files = folderPath.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            System.out.println("===> filename: " + files[i].getName());

            File textFile = new File(folderPath, filename);
            if (textFile.exists()){
                // append log
                if (currentdate.equals(files[i].getName().replace(".txt", ""))){
                    try {
//                    textFile.delete();
//                    textFile.createNewFile();
                        String getline;
                        FileInputStream fis = new FileInputStream(textFile);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        while ( (getline = br.readLine()) != null ) {
                            getcontent = getline;
                        }
                        fis.close();
                        br.close();

                        FileOutputStream fos = new FileOutputStream(textFile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos);

                        StringBuilder sb = new StringBuilder();
                        sb.append(getcontent+ "\n");
                        sb.append(currenttime + "-> [E]" + contents + "\n");
                        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
                        p.edit().clear().apply();

                        osw.append(sb);
                        osw.close();

                        fos.flush();
                        fos.close();

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } else {
                folderPath.mkdir();
                try {
                    textFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(textFile);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);

                    StringBuilder sb = new StringBuilder();
                    sb.append(currenttime + "-> [E]" + contents + "\n");

                    osw.append(sb);
                    osw.close();

                    fos.flush();
                    fos.close();

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendlog(String fname){
        // get file content
        File rootPath = Environment.getExternalStorageDirectory();
        folderPath = new File(rootPath.getAbsolutePath() + Global.FOLDER);

        String getline;
        File textFile = new File(folderPath, fname + ".txt");

        try {
            FileInputStream fis = new FileInputStream(textFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ( (getline = br.readLine()) != null ) {
                getcontent = getline;
            }
            fis.close();
            br.close();

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        // web call post getcontent to email: pocketvendor@threegmedia.com


    }

    public String timestamp(){
        return currenttime;
    }

    public String logcontent(Context context){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        return p.getString("applogg", null);
    }

}
