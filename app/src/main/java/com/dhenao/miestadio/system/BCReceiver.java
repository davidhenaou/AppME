package com.dhenao.miestadio.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BCReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SECRET_CODE".equals(intent.getAction())) {
            /*Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, Diagnoser.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/
            Toast.makeText(context, "codigo clave", Toast.LENGTH_SHORT).show();
        }
    }
}