package com.example.ejemplorecyclerview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.content.Context;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class Utils {
    public static void configurarAlarma(long id, Date horaToma, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaToma);

        // Establecer la alarma con una repetición exacta y un intervalo de 24 horas
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        } /*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }*/
    }
}
