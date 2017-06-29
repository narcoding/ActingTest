package com.narcoding.actingaptitudetesting.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Belgeler on 28.06.2017.
 */

public class NetworkChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(NetworkHelper.CHECK_INTERNET);
        in.putExtra(NetworkHelper.CHECK_INTERNET,
                NetworkHelper.isInternetConnected(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(in);
    }
}