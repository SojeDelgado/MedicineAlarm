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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private List<ListElement> elements;
    private ListAdapter listAdapter;

    //Variable para identificar el elemento esperado en la actividad Agregar.
    private static final int AGREGAR_ELEMENTO_REQUEST = 1;
    private static final int EDITAR_ELEMENTO_REQUEST = 2;

    Button btnAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void moveToDescriptoin(ListElement element){
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
            String tipo = data.getStringExtra("tipo");
            String hora = data.getStringExtra("hora");
            // Agregar un nuevo elemento al ArrayList
            elements.add(new ListElement(generarColorAleatorio(), nombre, tipo, hora));

        } else if (requestCode == EDITAR_ELEMENTO_REQUEST && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                ListElement updatedElement = (ListElement) data.getSerializableExtra("ListElement");
                elements.set(position, updatedElement);
            }
        }
        // Actualizar el RecyclerView con el nuevo elemento
        listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToDescriptoin(item);
            }
        }, new ListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(ListElement item) {
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

    public void init(){
        elements = new ArrayList<>();
        elements.add(new ListElement("#000000","Paracetamol","Pastilla","01:21 p.m."));
        elements.add(new ListElement("#000000","Simvastatina ","Pastilla","06:00 a.m."));
        elements.add(new ListElement("#000000","Aspirina ","Pastilla","02:00 a.m."));
        elements.add(new ListElement("#000000","Omeprazol ","Pastilla","05:34 p.m."));
        elements.add(new ListElement("#000000","Lexotiroxina","Pastilla","08:30 p.m."));
        elements.add(new ListElement("#000000","Ramipril ","Pastilla","12:00 p.m."));
        elements.add(new ListElement("#000000","Amlodipina ","Pastilla","11:00 a.m."));
        elements.add(new ListElement("#000000","Atorvastatina ","Pastilla","01:21 a.m."));

        //Pasamos todos los valores que creamos a la página principal mediante el ListAdapter
        listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToDescriptoin(item);
            }
        }, new ListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(ListElement item) {
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

    //Generar colores aleatorios para los colores de las casillas
    private String generarColorAleatorio() {
        Random random = new Random();

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // Asegurarse de que el color no sea demasiado brillante o demasiado oscuro
        while (red + green + blue > 500 || red + green + blue < 200) {
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
        }

        // Convertir los valores RGB a formato hexadecimal
        String color = String.format("#%02x%02x%02x", red, green, blue);
        return color;
    }
}