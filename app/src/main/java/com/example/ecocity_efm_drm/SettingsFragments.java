package com.example.ecocity_efm_drm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

// --- ESTOS SON LOS IMPORTS IMPORTANTES ---
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;


public class SettingsFragments extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Carga el diseño XML
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Lógica del Switch de notificaciones
        SwitchCompat switchNotif = view.findViewById(R.id.switchNotif);
        if (switchNotif != null) {
            switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Toast.makeText(getContext(), "Notificaciones activadas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Notificaciones desactivadas", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Lógica del Botón Cerrar Sesión
        Button btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                // Volver al Login
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                // Cerrar la actividad actual para que no puedan volver atrás
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
        }

        return view;
    }
}