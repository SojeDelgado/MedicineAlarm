package com.example.ejemplorecyclerview;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    protected void onResume() {
        super.onResume();
        actualizarLista();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MedicamentosDatabase(this);

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
        intent.putExtra("id", element.getId());
        intent.putExtra("ListElement", element);
        int position = elements.indexOf(element);
        intent.putExtra("position", position);
        startActivityForResult(intent, EDITAR_ELEMENTO_REQUEST);
    }

    //Método para recibir el resultado de la actividad "AgregarElementoActivity".
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void actualizarLista() {
        // Obtener los elementos de la base de datos y actualizar la lista
        elements = dbHelper.obtenerMedicamentos();

        // Actualizar el RecyclerView con los datos actualizados
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
                        // Eliminar el elemento de la base de datos
                        Log.d(TAG, "Eliminando medicamento: " + item.getNombre());
                        dbHelper.eliminarMedicamento(item);
                        // Remover el elemento de la lista
                        int position = elements.indexOf(item);
                        elements.remove(position);
                        // Notificar al adapter que se removió un elemento
                        listAdapter.notifyItemRemoved(position);
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