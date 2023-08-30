package com.example.validacion;

public class Rutas {

String id, id_arrastre, latitud_origen, longitud_origen, latitud_destino, longitud_destino, kilometros;

    public Rutas(String id, String id_arrastre, String latitud_origen, String longitud_origen, String latitud_destino, String longitud_destino, String kilometros) {
        this.id = id;
        this.id_arrastre = id_arrastre;
        this.latitud_origen = latitud_origen;
        this.longitud_origen = longitud_origen;
        this.latitud_destino = latitud_destino;
        this.longitud_destino = longitud_destino;
        this.kilometros = kilometros;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_arrastre() {
        return id_arrastre;
    }

    public void setId_arrastre(String id_arrastre) {
        this.id_arrastre = id_arrastre;
    }

    public String getLatitud_origen() {
        return latitud_origen;
    }

    public void setLatitud_origen(String latitud_origen) {
        this.latitud_origen = latitud_origen;
    }

    public String getLongitud_origen() {
        return longitud_origen;
    }

    public void setLongitud_origen(String longitud_origen) {
        this.longitud_origen = longitud_origen;
    }

    public String getLatitud_destino() {
        return latitud_destino;
    }

    public void setLatitud_destino(String latitud_destino) {
        this.latitud_destino = latitud_destino;
    }

    public String getLongitud_destino() {
        return longitud_destino;
    }

    public void setLongitud_destino(String longitud_destino) {
        this.longitud_destino = longitud_destino;
    }

    public String getKilometros() {
        return kilometros;
    }

    public void setKilometros(String kilometros) {
        this.kilometros = kilometros;
    }
}
