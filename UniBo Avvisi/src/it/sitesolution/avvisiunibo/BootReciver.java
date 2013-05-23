package it.sitesolution.avvisiunibo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, Servizio.class);
        context.startService(startServiceIntent);
    }
}