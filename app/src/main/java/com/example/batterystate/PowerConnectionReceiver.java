package com.example.batterystate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String chargingStatus;
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL ;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED , -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB ;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC ;


        if(usbCharge)
            chargingStatus = "Charging via USB:" + status;
        else if(acCharge)
            chargingStatus = "Charging via AC:" + status;
        else
            chargingStatus = "Not charging";

        Intent newIntent =  new Intent(context , MainActivity.class);
        newIntent.putExtra("status",chargingStatus);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 ,newIntent , 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context , MainActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Charging status:")
                .setContentText(chargingStatus)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);




        NotificationManagerCompat notoficationManager = NotificationManagerCompat.from(context);

        notoficationManager.notify(MainActivity.notificationId,mBuilder.build());

    }
}