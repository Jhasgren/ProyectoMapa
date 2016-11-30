package com.example.proyectomapa;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ing_l on 29/11/2016.
 */

public class Enlace {
    private HttpsURLConnection enlace;

    public Enlace(String dir) {
        try {
            enlace = (HttpsURLConnection) new URL(dir).openConnection();
            String datos = enlace.getContent().toString();
            Log.e("Enlace", "datos:" + datos);
            if (getEtiqueta(datos, "status", 0).equals("OK")) {
                int i = 0;
                ArrayList<Ubicaciones> ubicaciones = new ArrayList<>();
                //while (i < datos.length() - "<result>".length() + 2) {
                    Resultado result = getEtiqueta(datos, "result", i);
                    Resultado name = getEtiqueta(result.getDatos(), "name", 0);
                    Resultado type = getEtiqueta(result.getDatos(), "type", 0);
                    Resultado rating = getEtiqueta(result.getDatos(), "rating", 0);

                    Resultado geometry = getEtiqueta(result.getDatos(), "geometry", 0);
                    Resultado location = getEtiqueta(geometry.getDatos(), "location", 0);
                    Resultado lat = getEtiqueta(location.getDatos(), "lat", 0);
                    Resultado lng = getEtiqueta(location.getDatos(), "lng", 0);
                    ubicaciones.add(new Ubicaciones(new LatLng(Double.valueOf(lat.getDatos()),
                            Double.valueOf(lng.getDatos())),
                            name.getDatos(), type.getDatos(),
                            Float.valueOf(rating.getDatos())));
                    i = result.getB();
                //}
                onRespuesta(ubicaciones);
            } else {
                onRespuesta(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRespuesta(ArrayList<Ubicaciones> ubicaciones) {

    }

    private Resultado getEtiqueta(String datos, String etiqueta, int pos) {
        int a = datos.indexOf("<" + etiqueta + ">", pos);
        int b = datos.indexOf("</" + etiqueta + ">", a + etiqueta.length() + 2);
        return new Resultado(datos.substring(a + etiqueta.length() + 2, b), a, b);
    }

    private class Resultado {
        private String datos;
        private int a, b;

        public Resultado(String datos, int a, int b) {
            this.datos = datos;
            this.a = a;
            this.b = b;
        }

        public String getDatos() {
            return datos;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }
    }
}
