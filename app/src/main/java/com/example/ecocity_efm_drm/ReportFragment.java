package com.example.ecocity_efm_drm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReportFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng ubicacionSeleccionada;
    private Bitmap fotoCapturada;
    private ImageView imgPreview;

    // Lanzador de cámara
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == -1 && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    fotoCapturada = (Bitmap) extras.get("data");
                    if (imgPreview != null) {
                        imgPreview.setImageBitmap(fotoCapturada);
                        imgPreview.setVisibility(View.VISIBLE);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        try {
            // Referencias UI con chequeo de nulos
            EditText etTitulo = view.findViewById(R.id.etTitulo);
            EditText etDesc = view.findViewById(R.id.etDesc);
            Spinner spinner = view.findViewById(R.id.spinnerUrgencia);
            imgPreview = view.findViewById(R.id.imgPreview);
            Button btnFoto = view.findViewById(R.id.btnFoto);
            Button btnGuardar = view.findViewById(R.id.btnGuardar);

            // 1. Spinner
            String[] opciones = {"Baja", "Media", "Alta"};
            if (getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, opciones);
                spinner.setAdapter(adapter);
            }

            // 2. Mapa (Intento seguro)
            Fragment mapFragment = getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment instanceof SupportMapFragment) {
                ((SupportMapFragment) mapFragment).getMapAsync(this);
            }

            // 3. Botón Cámara
            btnFoto.setOnClickListener(v -> {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    cameraLauncher.launch(takePictureIntent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al abrir cámara", Toast.LENGTH_SHORT).show();
                }
            });

            // 4. Guardar
            btnGuardar.setOnClickListener(v -> {
                if (ubicacionSeleccionada == null) {
                    // Si no hay mapa o no seleccionó, usamos una ubicación por defecto (Madrid) para que no falle
                    ubicacionSeleccionada = new LatLng(40.416, -3.703);
                    Toast.makeText(getContext(), "Usando ubicación por defecto (Madrid)", Toast.LENGTH_SHORT).show();
                }

                Incidencia nueva = new Incidencia(
                        etTitulo.getText().toString(),
                        etDesc.getText().toString(),
                        spinner.getSelectedItem() != null ? spinner.getSelectedItem().toString() : "Baja",
                        ubicacionSeleccionada,
                        fotoCapturada
                );

                DataHolder.incidencias.add(nueva);
                Toast.makeText(getContext(), "Reporte añadido", Toast.LENGTH_SHORT).show();

                // Volver a Home
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                }
            });

        } catch (Exception e) {
            e.printStackTrace(); // Mira el Logcat si falla
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng madrid = new LatLng(40.416, -3.703);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 10));

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Incidencia aquí"));
            ubicacionSeleccionada = latLng;
        });
    }
}