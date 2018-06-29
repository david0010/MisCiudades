package com.example.david.misciudades.modelo;

import java.io.Serializable;

public class Ciudad implements Serializable {
    private Integer _id;
    private String nombre;
    private String pais;
    private double latitud;
    private double longitud;
    private String idGoogle;

    public Ciudad(String nombre, String pais, double latitud, double longitud, String idGoogle) {
        this._id = -1;
        this.nombre = nombre;
        this.pais = pais;
        this.latitud = latitud;
        this.longitud = longitud;
        this.idGoogle = idGoogle;
    }

    public Ciudad() {
        this(null, null, 0, 0, null);
    }

    public long getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getIdGoogle() {
        return idGoogle;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
    }

    @Override
    public String toString() {
        String str = "ID = " + _id + " | " +
                "Nombre: " + nombre + " | " +
                "Pais: " + pais + " | " +
                "Latitud: " + latitud + " | " +
                "Longitud: " + longitud + " | " +
                "ID_Google: " + idGoogle;
        return str;
    }
}
