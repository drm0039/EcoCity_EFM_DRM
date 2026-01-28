package com.example.ecocity_efm_drm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearIncidenciaActivity extends AppCompatActivity {

    EditText etTitulo, etDescripcion, etUrgencia; // Para establecer texto personalizado en los campos correspondientes
    Button btnGuardar; // Botón para guardar la incidencia creada

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        // Campos del activity_crear_incidencia.xml (Layout de este archivo java)
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etUrgencia = findViewById(R.id.etUrgencia);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Funcionalidad para el botón de guardar incidencia creada
        btnGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String titulo = etTitulo.getText().toString().trim();
                String descripcion = etDescripcion.getText().toString().trim();
                String urgencia = etUrgencia.getText().toString().trim();

                if (titulo.isEmpty() || descripcion.isEmpty() || urgencia.isEmpty()) {

                    Toast.makeText(CrearIncidenciaActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;

                }

                // Guardamos la incidencia en la base de datos

                DBHelper db = new DBHelper(CrearIncidenciaActivity.this);
                db.insertar(titulo, descripcion, urgencia, 0, 0, System.currentTimeMillis());

                Toast.makeText(CrearIncidenciaActivity.this, "Incidencia creada", Toast.LENGTH_SHORT).show();
                finish(); // Cerramos la actividad y volvemos a MainActivity

            }

        });

    }

}
