package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {
    EditText nombreEditText;
    TextView tipoEditText;
    TextView horaEditText;

    private ListElement element; // variable para almacenar el elemento actual

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        // obtener el elemento de la actividad anterior
        element = (ListElement) getIntent().getSerializableExtra("ListElement");


        nombreEditText = findViewById(R.id.nombreDescriptionTextView);
        tipoEditText = findViewById(R.id.medicamentoDescriptionTextView);
        horaEditText = findViewById(R.id.horaDescriptionTextView);

        nombreEditText.setText(element.getNombre());
        tipoEditText.setText(element.getMedicamento());
        horaEditText.setText(element.getHora());

        Button btnAceptar = findViewById(R.id.btnAgregar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtener los datos ingresados de los EditText
                String nombre = nombreEditText.getText().toString();
                String tipo = tipoEditText.getText().toString();
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
}