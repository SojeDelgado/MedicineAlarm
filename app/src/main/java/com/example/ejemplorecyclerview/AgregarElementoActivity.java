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
    private List<MedicamentoElement> mMedicamentos;
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
        mAdapter = new MedicamentosAdapter(this, (ArrayList<MedicamentoElement>) mMedicamentos);

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
                String color = "#000000";
                String nombre = nombreEditText.getText().toString();
                String medicamento = (String) tipoMedicamentoSpinner.getSelectedItem();

                Calendar calendar = Calendar.getInstance();
                int horas = nombreTimePicker.getCurrentHour();
                int minutos = nombreTimePicker.getCurrentMinute();
                calendar.set(Calendar.HOUR_OF_DAY, horas);
                calendar.set(Calendar.MINUTE, minutos);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date horaDate = calendar.getTime();

                int id = (int) mDatabase.insertarMedicamento(color,nombre,medicamento, horaDate,true);
                if (id == -1) {
                    Toast.makeText(AgregarElementoActivity.this, "No se pudo agregar el medicamento", Toast.LENGTH_SHORT).show();
                } else {
                    MedicamentoElement medi = new MedicamentoElement(id,color,nombre,medicamento,horaDate,true);
                    mMedicamentos.add(medi);
                    mAdapter.notifyDataSetChanged();

                    // Configurar la alarma
                    Utils.configurarAlarma(id, horaDate,AgregarElementoActivity.this);

                    Toast.makeText(AgregarElementoActivity.this, "Medicamento agregado correctamente", Toast.LENGTH_SHORT).show();
                }

                //Intent para agregar los datos como extras
                Intent intent = new Intent();
                intent.putExtra("nombre", nombre);
                intent.putExtra("tipo", medicamento);
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

