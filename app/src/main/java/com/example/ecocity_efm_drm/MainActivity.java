package com.example.ecocity_efm_drm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    IncidenciaAdapter adapter;
    ArrayList<Incidencia> listaIncidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Para ajustar el tema de márgenes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        // Inicializamos RecyclerView

        recyclerView = findViewById(R.id.recyclerIncidencias);
        listaIncidencias = new ArrayList<>();

        cargarIncidencias(); // Para leer las incidencias de la base de datos

        adapter = new IncidenciaAdapter(listaIncidencias);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Botón flotante para crear la nueva incidencia

        FloatingActionButton fab = findViewById(R.id.fabNueva);
        fab.setOnClickListener(v -> {

            // Para abrir la nueva actividad
            Intent intent = new Intent(MainActivity.this, CrearIncidenciaActivity.class);
            startActivity(intent);

        });
    }

    // Cada vez que volvemos a esta actividad recargamos la lista

    @Override
    protected void onResume() {

        super.onResume();
        cargarIncidencias();
        adapter.notifyDataSetChanged();

    }

    // Función que lee todas las incidencias de la DB

    private void cargarIncidencias() {

        listaIncidencias.clear(); // Para limpiar la lista anterior
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.obtenerTodas();

        if (cursor.moveToFirst()) {

            do {

                Incidencia i = new Incidencia();

                i.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                i.titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                i.descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                i.urgencia = cursor.getString(cursor.getColumnIndexOrThrow("urgencia"));
                listaIncidencias.add(i);

            } while (cursor.moveToNext());
        }

        cursor.close(); // Para cerrar el cursor, liberamos recursos

    }
}
