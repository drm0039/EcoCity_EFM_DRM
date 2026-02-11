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


    private static final String INSTRUCCIONES_SISTEMA =
            "ACTÚA COMO: El Asistente de Soporte Ciudadano de la App 'EcoCity'.\n" +
                    "TU OBJETIVO: Ayudar a los usuarios a reportar incidencias urbanas mediante fotos.\n" +
                    "CONTEXTO: El usuario está en la calle y usa la app para denunciar problemas (farolas caídas, ríos desbordados, baches, basura acumulada, señales rotas).\n" +
                    "REGLAS OBLIGATORIAS:\n" +
                    "1. SEGURIDAD PRIMERO: Si el usuario menciona algo peligroso (cables eléctricos sueltos, fuego, inundación grave), dile INMEDIATAMENTE que se aleje y llame a Emergencias (112) antes de usar la app.\n" +
                    "2. GUÍA DE REPORTE: Si preguntan cómo usar la app, explícales: 'Haz una foto clara de la incidencia, añade una descripción breve y envíala para que el ayuntamiento lo gestione'.\n" +
                    "3. TIPO DE INCIDENCIA: Ayuda a clasificar. Si dicen 'hay agua en la calle', pregunta si es una fuga o lluvia para categorizarlo bien.\n" +
                    "4. TONO: Profesional, directo y de servicio público. Respuestas cortas (máximo 2 frases).\n" +
                    "5. RESTRICCIÓN: No hables de temas personales, chistes o política. Solo incidencias de la ciudad.\n" +
                    "--- Fin de instrucciones ---\n\n";


    private static final String API_KEY = "Aqui va la API que esta en el comentario de la tarea";


    private static final String URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatLayout = findViewById(R.id.chatLayout);
        etMessage = findViewById(R.id.etMessage);
        scrollView = findViewById(R.id.scrollViewChat);

        View rootView = findViewById(R.id.rootView);
        if (rootView != null) {
            rootView.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
                if (b < ob) scrollDown();
            });
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Mensaje de bienvenida orientado a la acción
        addBotMessage("👋 Hola. Soy el asistente de EcoCity. ¿Qué incidencia urbana has detectado hoy?");

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

    private void llamarGeminiAI(String preguntaUsuario) {
        new Thread(() -> {
            try {
                // Protección contra espacios en la clave
                URL url = new URL(URL_GEMINI + API_KEY.trim());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Inyectamos el Prompt de Sistema junto con la pregunta
                String promptFinal = INSTRUCCIONES_SISTEMA + "Usuario reporta/pregunta: " + preguntaUsuario;

                JSONObject jsonBody = new JSONObject();
                JSONArray contents = new JSONArray();
                JSONObject content = new JSONObject();
                JSONArray parts = new JSONArray();
                JSONObject part = new JSONObject();

                part.put("text", promptFinal);

                parts.put(part);
                content.put("parts", parts);
                contents.put(content);
                jsonBody.put("contents", contents);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
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
                    String errorMsg = "Error técnico: " + responseCode;
                    runOnUiThread(() -> addBotMessage(errorMsg));
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> addBotMessage("Revisa tu conexión."));
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
            return "No pude procesar la respuesta.";
        }
    }

    private void addUserMessage(String text) {
        TextView tv = crearBurbuja(text, Color.WHITE, true);
        chatLayout.addView(tv);
        scrollDown();
    }

    private void addBotMessage(String text) {
        TextView tv = crearBurbuja(text.replace("**", ""), Color.BLACK, false);
        chatLayout.addView(tv);
        scrollDown();
    }

    private TextView crearBurbuja(String text, int textColor, boolean isUser) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(textColor);
        if (isUser) {
            tv.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde EcoCity
            try { tv.setBackgroundResource(R.drawable.bg_chat_user); } catch (Exception e) {}
        } else {
            tv.setBackgroundColor(Color.LTGRAY);
            try { tv.setBackgroundResource(R.drawable.bg_chat_bot); } catch (Exception e) {}
        }
        tv.setPadding(30, 20, 30, 20);
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


