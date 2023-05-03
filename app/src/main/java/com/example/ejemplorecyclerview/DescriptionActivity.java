package com.example.ejemplorecyclerview;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity {
    EditText nombreEditText;
    EditText cantidadEditText;
    Spinner tipoMedicamentoSpinner;
    TimePicker nombreTimePicker;
    private MedicamentosDatabase mDatabase;
    private List<Medicamento> mMedicamentos;
    private MedicamentosAdapter mAdapter;

    private MedicamentoElement element; // variable para almacenar el elemento actual

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_description);

        mDatabase = new MedicamentosDatabase(this);
        // obtener el elemento de la actividad anterior

        element = (MedicamentoElement) getIntent().getSerializableExtra("ListElement");

        String[] tipoMedicamentos = {
                "Analgesicos",
                "Antibioticos",
                "Antidepresivos",
                "Antihistamínicos",
                "Antinflamatorios"
        };

        nombreEditText = findViewById(R.id.nombreDescriptionTextView);
        cantidadEditText = findViewById(R.id.editTextCantidad);
        tipoMedicamentoSpinner = findViewById(R.id.spinnerTipoMedicamento);
        nombreTimePicker = findViewById(R.id.time_picker_hora_toma);

        nombreEditText.setText(element.getNombre());
        cantidadEditText.setText(""+element.getCantidad());

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
                int id = getIntent().getIntExtra("id",-1);
                //Obtener los datos ingresados de los EditText
                String nombre = nombreEditText.getText().toString();
                String cantidadString = cantidadEditText.getText().toString();
                float cantidad;
                if (cantidadString.equals("")) {
                    cantidad = 0;
                }else{
                    cantidad = Float.parseFloat(cantidadString);
                }
                String tipo = (String) tipoMedicamentoSpinner.getSelectedItem();
                int hora = nombreTimePicker.getCurrentHour();
                int minutos = nombreTimePicker.getCurrentMinute();
                Date horaDate = new Date();
                horaDate.setHours(hora);
                horaDate.setMinutes(minutos);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hora);
                calendar.set(Calendar.MINUTE, minutos);
                calendar.set(Calendar.SECOND, 0);

                MedicamentoElement updatedElement = new MedicamentoElement(
                        id,
                        element.getColor(),
                        nombre,
                        cantidad,
                        tipo,
                        horaDate,
                        true
                );

                // Obtener el id del elemento que se desea actualizar



                // Actualizar los datos en la base de datos
                mDatabase.actualizarMedicamento(
                        id,
                        "#000000",
                        nombre,
                        cantidad,
                        tipo,
                        horaDate,
                        true
                );
                Log.d(TAG, "Medicamento actualizado: " + updatedElement.toString());

                //Intent para agregar los datos como extras
                Intent intent = new Intent();

                intent.putExtra("position", getIntent().getIntExtra("position", -1));
                intent.putExtra("ListElement", updatedElement);
                setResult(RESULT_OK, intent);

                Utils.configurarAlarma(id, horaDate,DescriptionActivity.this);
                finish();
            }
        });

    }

    public void back(View v){
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}