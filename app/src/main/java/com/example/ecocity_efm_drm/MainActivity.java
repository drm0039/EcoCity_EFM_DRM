package com.example.ecocity_efm_drm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // 1. Cargar el fragmento de Inicio por defecto al abrir la app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            // Esto asegura que el botón "Inicio" se vea seleccionado visualmente
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        // 2. Listener para los cambios en el menú
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_report) {
                // AQUI ESTABA EL ERROR: Antes tenías HomeFragment, ahora es ReportFragment
                selectedFragment = new ReportFragment();
            } else if (id == R.id.nav_settings) {
                selectedFragment = new SettingsFragments();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}


