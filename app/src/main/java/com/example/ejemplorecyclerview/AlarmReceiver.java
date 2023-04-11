package com.example.ejemplorecyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("MissingPermission")
    public void onReceive(Context context, Intent intent) {
        // Extrae los datos de la alarma que se guardaron en el Intent
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // Crea el objeto NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.first_aid_kit)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setAutoCancel(true);

        // Crea un intent para abrir la actividad correspondiente cuando se toque la notificación
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Crea la notificación y la muestra
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
    }

}
