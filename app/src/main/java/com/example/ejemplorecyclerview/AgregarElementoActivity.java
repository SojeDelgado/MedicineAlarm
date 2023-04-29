package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgregarElementoActivity extends AppCompatActivity {
    private TextView nombreTextview;
    private EditText nombreEditText;
    private Spinner tipoMedicamentoSpinner;
    private TimePicker nombreTimePicker;
    ListView nombrelistView;
    private MedicamentosDatabase mDatabase;
    private List<Medicamento> mMedicamentos;
    private MedicamentosAdapter mAdapter;
    private MedicamentoElement element;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_elemento);

        element = (MedicamentoElement) getIntent().getSerializableExtra("ListElement");

        String[] tipoMedicamentos = {
                "Analgesicos",
                "Antibioticos",
                "Antidepresivos",
                "Antihistamínicos",
                "Antinflamatorios"
        };

        mDatabase = new MedicamentosDatabase(this);
        mMedicamentos = mDatabase.obtenerMedicamentos();
        mAdapter = new MedicamentosAdapter(this, (ArrayList<Medicamento>) mMedicamentos);

        //Variable para asignar los valores de los EditTExt
        nombreTextview = findViewById(R.id.title_text_view);
        nombreEditText = findViewById(R.id.editTextNombre);
        tipoMedicamentoSpinner = findViewById(R.id.editTextTipo);
        nombreTimePicker = findViewById(R.id.time_picker_hora_toma);
        nombrelistView = findViewById(R.id.list_medicamentos);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoMedicamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoMedicamentoSpinner.setAdapter(adapter);

        if (element != null) {
            int spinnerPosition = adapter.getPosition(element.getMedicamento());
            tipoMedicamentoSpinner.setSelection(spinnerPosition);
        }


        Button btnAceptar = findViewById(R.id.btnAgregarElemento);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtener los datos ingresados de los EditText
                String nombre = nombreEditText.getText().toString();
                String tipo = (String) tipoMedicamentoSpinner.getSelectedItem();
                int hora = nombreTimePicker.getCurrentHour();
                int minutos = nombreTimePicker.getCurrentMinute();

                Date horaDate = new Date();
                horaDate.setHours(hora);
                horaDate.setMinutes(minutos);

                int id = (int) mDatabase.insertarMedicamento(nombre, horaDate);
                if (id == -1) {
                    Toast.makeText(AgregarElementoActivity.this, "No se pudo agregar el medicamento", Toast.LENGTH_SHORT).show();
                } else {
                    Medicamento medicamento = new Medicamento(nombre, horaDate);
                    mMedicamentos.add(medicamento);
                    mAdapter.notifyDataSetChanged();

                    // Configurar la alarma
                    Utils.configurarAlarma(id, horaDate,AgregarElementoActivity.this);

                    Toast.makeText(AgregarElementoActivity.this, "Medicamento agregado correctamente", Toast.LENGTH_SHORT).show();
                }

                //Intent para agregar los datos como extras
                Intent intent = new Intent();
                intent.putExtra("nombre", nombre);
                intent.putExtra("tipo", tipo);
                intent.putExtra("hora",horaDate);
                // Establecer el resultado y finalizar la actividad
                setResult(RESULT_OK, intent);

                finish();

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
    public void cancel(View view) {
        finish();
    }
}

