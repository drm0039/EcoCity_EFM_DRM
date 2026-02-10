package com.example.ecocity_efm_drm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private int index;
    private EditText etTitulo, etDesc;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 1. Vinculamos con los IDs del XML nuevo
        etTitulo = findViewById(R.id.etDetailTitle);
        etDesc = findViewById(R.id.etDetailDesc);
        img = findViewById(R.id.imgDetail);
        Button btnGuardar = findViewById(R.id.btnSaveChanges);
        Button btnEliminar = findViewById(R.id.btnDelete);
        Button btnBack = findViewById(R.id.btnBackDetail);

        // 2. Recibir el índice de la incidencia seleccionada
        index = getIntent().getIntExtra("index", -1);

        // 3. Cargar datos si el índice es válido
        if (index != -1 && index < DataHolder.incidencias.size()) {
            Incidencia inc = DataHolder.incidencias.get(index);

            // Ponemos los textos actuales
            etTitulo.setText(inc.titulo);
            etDesc.setText(inc.descripcion);

            // Si hay foto, la ponemos. Si no, dejamos la de defecto.
            if(inc.foto != null) {
                img.setImageBitmap(inc.foto);
            }
        }

        // 4. Lógica del botón VOLVER
        btnBack.setOnClickListener(v -> finish());

        // 5. Lógica del botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (index != -1 && index < DataHolder.incidencias.size()) {
                DataHolder.incidencias.remove(index); // Borra de la lista global
                Toast.makeText(this, "Incidencia eliminada", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la pantalla y vuelve a la lista
            }
        });

        // 6. Lógica del botón GUARDAR CAMBIOS (Editar)
        btnGuardar.setOnClickListener(v -> {
            if (index != -1 && index < DataHolder.incidencias.size()) {
                // Recuperamos el objeto de la lista
                Incidencia inc = DataHolder.incidencias.get(index);

                // Sobrescribimos con lo que haya escrito el usuario
                inc.titulo = etTitulo.getText().toString();
                inc.descripcion = etDesc.getText().toString();

                Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la pantalla
            }
        });
    }
}