package com.example.ecocity_efm_drm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Botón del chat
        Button btnChat = view.findViewById(R.id.btnStartChat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cargamos la lista cada vez que entramos a la pantalla
        cargarIncidencias();
    }

    private void cargarIncidencias() {
        View view = getView();
        if (view == null) return;

        // Buscamos el contenedor donde vamos a meter las tarjetas
        // AHORA SÍ EXISTE EN EL XML
        LinearLayout listaItems = view.findViewById(R.id.listaItems);

        if (listaItems != null) {
            // 1. Limpiamos lo anterior para no duplicar
            listaItems.removeAllViews();

            // 2. Recorremos la lista de datos guardados
            for (int i = 0; i < DataHolder.incidencias.size(); i++) {
                Incidencia inc = DataHolder.incidencias.get(i);
                int index = i;

                // 3. Creamos la tarjeta visualmente (CardView)
                CardView card = new CardView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 20, 0, 0); // Margen entre tarjetas
                card.setLayoutParams(params);
                card.setCardBackgroundColor(Color.parseColor("#2A3B30"));
                card.setRadius(20);
                card.setContentPadding(30, 30, 30, 30);

                // 4. Texto dentro de la tarjeta
                TextView tv = new TextView(getContext());
                tv.setText(inc.titulo + "\nURGENCIA: " + inc.urgencia);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(16);
                card.addView(tv);

                // 5. Acción al pulsar (Ir a Detalles)
                card.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("index", index);
                    startActivity(intent);
                });

                // 6. Añadimos la tarjeta a la lista visual
                listaItems.addView(card);
            }
        }
    }
}