package com.example.validacion.Objetos;

public class MarkerInfo {

    Double latitud_inicio, longitud_inicio, latitud_destino, longitud_destino;

    String titulo;

    public MarkerInfo(Double latitud_inicio, Double longitud_inicio, Double latitud_destino, Double longitud_destino, String titulo) {
        this.latitud_inicio = latitud_inicio;
        this.longitud_inicio = longitud_inicio;
        this.latitud_destino = latitud_destino;
        this.longitud_destino = longitud_destino;
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getLatitud_inicio() {
        return latitud_inicio;
    }

    public void setLatitud_inicio(Double latitud_inicio) {
        this.latitud_inicio = latitud_inicio;
    }

    public Double getLongitud_inicio() {
        return longitud_inicio;
    }

    public void setLongitud_inicio(Double longitud_inicio) {
        this.longitud_inicio = longitud_inicio;
    }

    public Double getLatitud_destino() {
        return latitud_destino;
    }

    public void setLatitud_destino(Double latitud_destino) {
        this.latitud_destino = latitud_destino;
    }

    public Double getLongitud_destino() {
        return longitud_destino;
    }

    public void setLongitud_destino(Double longitud_destino) {
        this.longitud_destino = longitud_destino;
    }
}
