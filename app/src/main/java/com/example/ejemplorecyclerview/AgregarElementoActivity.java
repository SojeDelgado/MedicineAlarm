package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AgregarElementoActivity extends AppCompatActivity {
    private EditText nombreEditText;
    private EditText tipoEditText;
    private EditText horaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_elemento);

        //Variable para asignar los valores de los EditTExt
        nombreEditText = findViewById(R.id.editTextNombre);
        tipoEditText = findViewById(R.id.editTextTipo);
        horaEditText = findViewById(R.id.editTextHora);


        Button btnAceptar = findViewById(R.id.btnAgregarElemento);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtener los datos ingresados de los EditText
                String nombre = nombreEditText.getText().toString();
                String tipo = tipoEditText.getText().toString();
                String hora = horaEditText.getText().toString();

                //Intent para agregar los datos como extras
                Intent intent = new Intent();
                intent.putExtra("nombre", nombre);
                intent.putExtra("tipo", tipo);
                intent.putExtra("hora", hora);
                // Establecer el resultado y finalizar la actividad
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
