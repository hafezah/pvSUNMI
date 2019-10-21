package com.sunmi.pocketvendor;

import android.app.Application;

import com.sunmi.pocketvendor.network.Global;
import com.sunmi.pocketvendor.utils.AidlUtil;

import java.util.Timer;
import java.util.TimerTask;

public class BaseApp extends Application {

    // logout function
    private LogoutListener listener;
    private Timer timer;

    public void startUserSession() {
        cancelTimer();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.onSessionLogout();
            }
        }, Global.LOGOUTCOUNT);
    }

    private void cancelTimer() {
        if (timer != null) timer.cancel();
    }

    public void registerSessionListener(LogoutListener listener) {
        this.listener = listener;
    }

    public void onUserInteracted() {
        startUserSession();
    }
    //

    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);
    }
}
