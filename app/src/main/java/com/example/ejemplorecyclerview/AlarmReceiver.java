package com.example.ejemplorecyclerview;

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

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Verificar permiso
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permiso concedido, mostrar notificación
            showNotification(context);
        } else {
            // Permiso denegado, solicitar permiso al usuario
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void showNotification(Context context) {
        // Crear objeto PendingIntent
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Crear objeto NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.first_aid_kit)
                .setContentTitle("Título de la notificación")
                .setContentText("Texto de la notificación")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Definir el id de la notificación
        int notificationId = 1;

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, mostrar notificación
                showNotification(context);
            } else {
                // Permiso denegado, no se puede mostrar la notificación
                Toast.makeText(context, "El permiso ha sido denegado. No se puede mostrar la notificación.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
