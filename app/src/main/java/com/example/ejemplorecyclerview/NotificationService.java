package com.example.ejemplorecyclerview;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
public class NotificationService extends IntentService {
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    Notification notification;

    // Crear una instancia de la clase Medicamento
    private static final int NOTIFICATION_ID_BASE = 1000; // ID base para las notificaciones
    private static int notificationId = NOTIFICATION_ID_BASE;



    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("SERVICE");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent2) {
        String NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name);
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class);
        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        int medicamentoId = intent2.getIntExtra("medicamento_id", -1);

        // Realizar la consulta de contenido para obtener la información del medicamento
        Cursor cursor = getContentResolver().query(Uri.parse(MedicamentosDatabase.MedicamentosEntry.COLUMN_NOMBRE), null,
                MedicamentosDatabase.MedicamentosEntry._ID + " = ?", new String[] { String.valueOf(medicamentoId) }, null);

        // Mover el cursor al primer registro (el único en este caso) y obtener la información del medicamento
        String medicamentoNombre = null;
        if (cursor != null && cursor.moveToFirst()) {
            medicamentoNombre = cursor.getString(cursor.getColumnIndex(MedicamentosDatabase.MedicamentosEntry.COLUMN_NOMBRE));
        }

        // Cerrar el cursor
        if (cursor != null) {
            cursor.close();
        }

        String nombre = medicamentoNombre;
        String message = getString(R.string.new_notification, nombre);

        int NOTIFY_ID = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NOTIFY_ID = notificationId++;
            String id = NOTIFICATION_CHANNEL_ID; // default_channel_id
            String title = NOTIFICATION_CHANNEL_ID; // Default Channel
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notifManager == null) {
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            builder.setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.ic_notification)   // required
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_notification))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)

                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);

            startForeground(1, notification);

        } else {
            pendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_notification))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                    .setContentText(message).build();
            notificationManager.notify(NOTIFY_ID, notification);
        }
    }
}
