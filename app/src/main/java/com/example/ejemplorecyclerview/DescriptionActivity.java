package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {
    EditText nombreEditText;
    Spinner tipoMedicamentoSpinner;
    TextView horaEditText;

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
        horaEditText = findViewById(R.id.horaDescriptionTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoMedicamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoMedicamentoSpinner.setAdapter(adapter);

        nombreEditText.setText(element.getNombre());
        horaEditText.setText(element.getHora());

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
                String hora = horaEditText.getText().toString();

                //Actualizar los valores del objeto ListElement
                ListElement updatedElement = new ListElement(element.getColor(), nombre, tipo, hora);

                //Intent para agregar los datos como extras
                Intent intent = new Intent();
                intent.putExtra("position", getIntent().getIntExtra("position", -1));
                intent.putExtra("ListElement", updatedElement);
                setResult(RESULT_OK, intent);
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