package com.example.ecocity_efm_drm;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Incidencia {
    public String titulo;
    public String descripcion;
    public String urgencia;
    public LatLng ubicacion;
    public Bitmap foto;

    public Incidencia(String titulo, String descripcion, String urgencia, LatLng ubicacion, Bitmap foto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urgencia = urgencia;
        this.ubicacion = ubicacion;
        this.foto = foto;
    }
}