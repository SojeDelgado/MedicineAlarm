package com.example.ejemplorecyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private List<MedicamentoElement> elements;
    private ListAdapter listAdapter;
    private MedicamentosDatabase dbHelper;

    //Variable para identificar el elemento esperado en la actividad Agregar.
    private static final int AGREGAR_ELEMENTO_REQUEST = 1;
    private static final int EDITAR_ELEMENTO_REQUEST = 2;

    Button btnAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MedicamentosDatabase(this);
        init();

        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarElementoActivity.class);
                startActivityForResult(intent, AGREGAR_ELEMENTO_REQUEST);
            }
        });
    }

    public void moveToDescription(MedicamentoElement element){
        Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
        intent.putExtra("ListElement", element);
        int position = elements.indexOf(element);
        intent.putExtra("position", position);
        startActivityForResult(intent, EDITAR_ELEMENTO_REQUEST);
    }

    //Método para recibir el resultado de la actividad "AgregarElementoActivity".
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AGREGAR_ELEMENTO_REQUEST && resultCode == RESULT_OK) {
            // Obtener los datos ingresados en la actividad "AgregarElementoActivity"
            String nombre = data.getStringExtra("nombre");
            String medicamento = data.getStringExtra("tipo");
            Date hora = (Date) data.getSerializableExtra("hora");
            // Agregar un nuevo elemento al ArrayList
            elements.add(new MedicamentoElement("#000000", nombre, medicamento, hora));
            dbHelper.insertarMedicamento("#000000",nombre, medicamento, hora);
        } else if (requestCode == EDITAR_ELEMENTO_REQUEST && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                MedicamentoElement updatedElement = (MedicamentoElement) data.getSerializableExtra("ListElement");
                elements.set(position, updatedElement);
            }
        }
        // Actualizar el RecyclerView con el nuevo elemento
        listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MedicamentoElement item) {
                moveToDescription(item);
            }
        }, new ListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(MedicamentoElement item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Eliminar elemento");
                builder.setMessage("¿Seguro que desea eliminar este elemento?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        elements.remove(item);
                        RecyclerView recyclerView = findViewById(R.id.listRecycleView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(listAdapter);
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }

        });
        RecyclerView recyclerView = findViewById(R.id.listRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    public void init() {
        Date hora = new Date();
        hora.setHours(10);
        hora.setMinutes(10);
        elements = new ArrayList<>();
        elements.add(new MedicamentoElement("#000000", "Paracetamol", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Simvastatina ", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Aspirina ", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Omeprazol ", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Lexotiroxina", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Ramipril ", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Amlodipina ", "Pastilla", hora));
        elements.add(new MedicamentoElement("#000000", "Atorvastatina ", "Pastilla", hora));

        //Pasamos todos los valores que creamos a la página principal mediante el ListAdapter
        listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MedicamentoElement item) {
                moveToDescription(item);
            }
        }, new ListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(MedicamentoElement item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Eliminar elemento");
                builder.setMessage("¿Seguro que desea eliminar este elemento?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        elements.remove(item);
                        RecyclerView recyclerView = findViewById(R.id.listRecycleView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(listAdapter);
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.listRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}