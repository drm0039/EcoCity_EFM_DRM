package com.example.ecocity_efm_drm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.btnRegisterAction);
        TextView tvLoginBack = findViewById(R.id.tvLoginBack);

        // Simula registro y entra a la app
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        tvLoginBack.setOnClickListener(v -> finish()); // Vuelve al login
    }
}