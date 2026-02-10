package com.example.ecocity_efm_drm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout chatLayout;
    private EditText etMessage;
    private ScrollView scrollView;

    // --- CONFIGURACIÓN CRÍTICA ---
    // 1. BORRA LA CLAVE VIEJA (la que termina en ...xfc)
    // 2. PEGA AQUÍ LA DE LA FOTO (la que termina en ...hWyc)
    // --- CONFIGURACIÓN CRÍTICA ---
    // MANTÉN TU CLAVE NUEVA (la que termina en ...hWyc)
    private static final String API_KEY = "AIzaSyC1zxSx6U5KRuv0nw5Fh1-VEQDnUyWhWyc";

    // CAMBIO IMPORTANTE: Usamos "gemini-pro" en lugar de flash. Es el modelo más compatible.
    private static final String URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatLayout = findViewById(R.id.chatLayout);
        etMessage = findViewById(R.id.etMessage);
        scrollView = findViewById(R.id.scrollViewChat);

        // Ajuste de teclado
        View rootView = findViewById(R.id.rootView);
        if (rootView != null) {
            rootView.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
                if (b < ob) scrollDown();
            });
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        addBotMessage("¡Hola! Soy la IA de EcoCity. ¿En qué te puedo ayudar?");

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                addUserMessage(msg);
                etMessage.setText("");
                llamarGeminiAI(msg);
            }
        });
    }

    private void llamarGeminiAI(String pregunta) {
        new Thread(() -> {
            try {
                URL url = new URL(URL_GEMINI);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // JSON Body
                JSONObject jsonBody = new JSONObject();
                JSONArray contents = new JSONArray();
                JSONObject content = new JSONObject();
                JSONArray parts = new JSONArray();
                JSONObject part = new JSONObject();

                part.put("text", pregunta + " (Responde brevemente sobre EcoCity)");
                parts.put(part);
                content.put("parts", parts);
                contents.put(content);
                jsonBody.put("contents", contents);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();

                // LEER RESPUESTA (SEA ÉXITO O ERROR)
                InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) response.append(line);
                br.close();

                if (responseCode == 200) {
                    String textoIA = parsearRespuestaGemini(response.toString());
                    runOnUiThread(() -> addBotMessage(textoIA));
                } else {
                    // AQUÍ VEREMOS EL ERROR REAL SI FALLA
                    String errorMsg = "Error " + responseCode + ": " + response.toString();
                    runOnUiThread(() -> addBotMessage(errorMsg));
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> addBotMessage("Fallo de conexión: " + e.getMessage()));
            }
        }).start();
    }

    private String parsearRespuestaGemini(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception e) {
            return "La IA no pudo responder (Error de lectura).";
        }
    }

    private void addUserMessage(String text) {
        TextView tv = crearBurbuja(text, Color.WHITE, R.drawable.bg_chat_user, true);
        chatLayout.addView(tv);
        scrollDown();
    }

    private void addBotMessage(String text) {
        TextView tv = crearBurbuja(text.replace("**", ""), Color.BLACK, R.drawable.bg_chat_bot, false);
        chatLayout.addView(tv);
        scrollDown();
    }

    private TextView crearBurbuja(String text, int textColor, int bgRes, boolean isUser) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(textColor);
        try { tv.setBackgroundResource(bgRes); } catch (Exception e) { tv.setBackgroundColor(Color.GRAY); }
        tv.setPadding(25, 20, 25, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        params.gravity = isUser ? android.view.Gravity.END : android.view.Gravity.START;
        tv.setLayoutParams(params);
        return tv;
    }

    private void scrollDown() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}