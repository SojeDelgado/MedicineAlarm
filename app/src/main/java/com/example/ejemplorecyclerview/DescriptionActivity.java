package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {

    TextView titleDescriptionTextView;
    TextView nombreDescriptionTextView;
    TextView medicamentoDescriptionTextView;
    TextView horaDescriptionTextView;

    private ListElement element; // variable para almacenar el elemento actual

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        // obtener el elemento de la actividad anterior
        element = (ListElement) getIntent().getSerializableExtra("ListElement");

        titleDescriptionTextView = findViewById(R.id.titleDescriptionTextView);
        nombreDescriptionTextView = findViewById(R.id.nombreDescriptionTextView);
        medicamentoDescriptionTextView = findViewById(R.id.medicamentoDescriptionTextView);
        horaDescriptionTextView = findViewById(R.id.horaDescriptionTextView);

        nombreDescriptionTextView.setText(element.getNombre());
        medicamentoDescriptionTextView.setText(element.getMedicamento());
        horaDescriptionTextView.setText(element.getHora());

        Button modificarNombreButton = findViewById(R.id.modificarNombreButton);
        modificarNombreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abrir un cuadro de diálogo para ingresar el nuevo nombre
                AlertDialog.Builder builder = new AlertDialog.Builder(DescriptionActivity.this);
                builder.setTitle("Modificar nombre");

                // agregar un EditText para ingresar el nuevo nombre
                final EditText input = new EditText(DescriptionActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(element.getNombre()); // mostrar el nombre actual
                builder.setView(input);

                // agregar los botones de "Aceptar" y "Cancelar"
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nuevoNombre = input.getText().toString();
                        modificarNombre(nuevoNombre); // llamar al método modificarNombre()
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    // método para modificar el nombre del elemento actual
    public void modificarNombre(String nuevoNombre) {
        element.setNombre(nuevoNombre); // establecer el nuevo nombre
        nombreDescriptionTextView.setText(nuevoNombre); // actualizar la vista
    }

    public void back(View v) {
        finish();
    }

}