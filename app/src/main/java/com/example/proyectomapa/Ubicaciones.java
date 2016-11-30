package com.example.proyectomapa;

import com.google.android.gms.maps.model.LatLng;

public class Ubicaciones {
    private LatLng pos;
    private String nombre;
    private String tipo;
    private float raiting;

    public Ubicaciones(LatLng pos, String nombre, String tipo, float raiting) {
        this.pos = pos;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raiting = raiting;
    }

    public LatLng getPos() {
        return pos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public float getRaiting() {
        return raiting;
    }
}
