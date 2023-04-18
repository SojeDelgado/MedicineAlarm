package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity {
    EditText nombreEditText;
    Spinner tipoMedicamentoSpinner;
    TimePicker nombreTimePicker;
    private MedicamentosDatabase mDatabase;
    private List<Medicamento> mMedicamentos;
    private MedicamentosAdapter mAdapter;

    private ListElement element; // variable para almacenar el elemento actual

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        // obtener el elemento de la actividad anterior
        element = (ListElement) getIntent().getSerializableExtra("ListElement");

        String[] tipoMedicamentos = {
                "Analgesicos",
                "Antibioticos",
                "Antidepresivos",
                "Antihistamínicos",
                "Antinflamatorios"
        };

        nombreEditText = findViewById(R.id.nombreDescriptionTextView);
        tipoMedicamentoSpinner = findViewById(R.id.spinnerTipoMedicamento);
        nombreTimePicker = findViewById(R.id.time_picker_hora_toma);

        nombreEditText.setText(element.getNombre());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoMedicamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoMedicamentoSpinner.setAdapter(adapter);

        //Seleccionar el valor actual del elemento en el spinner
        int spinnerPosition = adapter.getPosition(element.getMedicamento());
        tipoMedicamentoSpinner.setSelection(spinnerPosition);

        Button btnAceptar = findViewById(R.id.btnAgregar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtener los datos ingresados de los EditText
                String nombre = nombreEditText.getText().toString();
                String tipo = (String) tipoMedicamentoSpinner.getSelectedItem();
                int hora = nombreTimePicker.getCurrentHour();
                int minutos = nombreTimePicker.getCurrentMinute();
                int horaYMinutos = hora * 100 + minutos;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hora);
                calendar.set(Calendar.MINUTE, minutos);
                calendar.set(Calendar.SECOND, 0);

                //Actualizar los valores del objeto ListElement
                ListElement updatedElement = new ListElement(element.getColor(), nombre, tipo, horaYMinutos);

                //Intent para agregar los datos como extras
                Intent intent = new Intent();
                intent.putExtra("position", getIntent().getIntExtra("position", -1));
                intent.putExtra("ListElement", updatedElement);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void configurarAlarma(long id, Date horaToma) {
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

    public void back(View v){
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}