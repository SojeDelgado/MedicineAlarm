package com.example.ejemplorecyclerview;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Obtener los datos del medicamento de la intención
        int medicamentoId = intent.getIntExtra("medicamentoId", -1);
        String medicamentoNombre = intent.getStringExtra("medicamentoNombre");

        if (medicamentoId != -1 && medicamentoNombre != null) {
            // Mostrar una notificación para recordar al usuario que tome el medicamento
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context, "channel_id")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("Recordatorio de medicamento")
                            .setContentText("Es hora de tomar el medicamento " + medicamentoNombre)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id",
                        "Channel Name",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Channel Description");
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(medicamentoId, notificationBuilder.build());
        } else {
            Toast.makeText(context, "Error: medicamento no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
