package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgregarElementoActivity extends AppCompatActivity {
    private TextView nombreTextview;
    private EditText nombreEditText;
    private EditText tipoEditText;
    private TimePicker nombreTimePicker;
    ListView nombrelistView;
    private MedicamentosDatabase mDatabase;
    private List<Medicamento> mMedicamentos;
    private MedicamentosAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_elemento);

        mDatabase = new MedicamentosDatabase(this);
        mMedicamentos = mDatabase.obtenerMedicamentos();
        mAdapter = new MedicamentosAdapter(this, (ArrayList<Medicamento>) mMedicamentos);

        //ListView listView = findViewById(R.id.list_medicamentos);
        //listView.setAdapter(mAdapter);

        //Variable para asignar los valores de los EditTExt
        nombreTextview = findViewById(R.id.title_text_view);
        nombreEditText = findViewById(R.id.editTextNombre);
        tipoEditText = findViewById(R.id.editTextTipo);
        nombreTimePicker = findViewById(R.id.time_picker_hora_toma);
        nombrelistView = findViewById(R.id.list_medicamentos);


        Button btnAceptar = findViewById(R.id.btnAgregarElemento);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtener los datos ingresados de los EditText
                String nombre = nombreEditText.getText().toString();
                String tipo = tipoEditText.getText().toString();
                int hora = nombreTimePicker.getCurrentHour();
                int minutos = nombreTimePicker.getCurrentMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hora);
                calendar.set(Calendar.MINUTE, minutos);
                calendar.set(Calendar.SECOND, 0);
                Date horaToma = calendar.getTime();

                long id = mDatabase.insertarMedicamento(nombre, horaToma);
                if (id == -1) {
                    Toast.makeText(AgregarElementoActivity.this, "No se pudo agregar el medicamento", Toast.LENGTH_SHORT).show();
                } else {
                    Medicamento medicamento = new Medicamento(nombre, horaToma);
                    mMedicamentos.add(medicamento);
                    mAdapter.notifyDataSetChanged();

                    // Configurar la alarma
                    configurarAlarma(id, horaToma);

                    Toast.makeText(AgregarElementoActivity.this, "Medicamento agregado correctamente", Toast.LENGTH_SHORT).show();
                }
                tipoEditText.setText("");

                //Intent para agregar los datos como extras
                Intent intent = new Intent();
                intent.putExtra("nombre", nombre);
                intent.putExtra("tipo", tipo);
                intent.putExtra("hora",hora);
                // Establecer el resultado y finalizar la actividad
                setResult(RESULT_OK, intent);

                finish();

            }
        });
    }

    private void configurarAlarma(long id,Date horaToma) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaToma);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}
