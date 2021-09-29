package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().compareTo(Intent.ACTION_AIRPLANE_MODE_CHANGED)==0)
        {
            Toast.makeText(context, "Broadcasting!", Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().compareTo(context.getString(R.string.broadcast_click)) == 0)
        {
            Toast.makeText(context, "Clicked Button!", Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().compareTo(context.getString(R.string.alarm_broadcast)) == 0)
        {
            Toast.makeText(context, "Alarm!", Toast.LENGTH_LONG).show();
        }
    }
}
