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
import android.net.Uri;
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
        long id = intent.getLongExtra("id", 0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // Crea un intent para abrir la actividad correspondiente cuando se toque la notificación
        Intent resultIntent = new Intent(context, NotificationService.class);
        resultIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        resultIntent.putExtra("id", id); // Agrega el ID de la notificación al Intent
        resultIntent.putExtra("title", title); // Agrega el título de la notificación al Intent
        resultIntent.putExtra("message", message);
        ContextCompat.startForegroundService(context, resultIntent );
    }
}
