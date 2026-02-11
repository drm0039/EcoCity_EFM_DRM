package com.example.ecocity_efm_drm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.ViewHolder> {

    private ArrayList<Incidencia> lista; // Para crear un ArrayList con los elementos de la clase Incidencia

    public IncidenciaAdapter(ArrayList<Incidencia> lista) {

        this.lista = lista;

    }

    // Este trozo de código se ejecutará cuando el RecyclerView necesita crear un nuevo ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(v);

    }

    // Para trabajar los datos con la bista, es decir, que se unifican
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Incidencia i = lista.get(position);
        holder.tvTitulo.setText(i.titulo);
        holder.tvDescripcion.setText(i.descripcion);
        holder.tvUrgencia.setText(i.urgencia);

    }

    // Mostrará cuantas incidencias hay (RecyclerView)

    @Override
    public int getItemCount() {

        return lista.size();

    }

    // Par representar digamos cada "tarjeta" que representa una incidencia
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvDescripcion, tvUrgencia;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvUrgencia = itemView.findViewById(R.id.tvUrgencia);

        }

    }

}

