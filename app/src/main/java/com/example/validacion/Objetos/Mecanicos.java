package com.example.validacion.Objetos;

public class Mecanicos {

    String foto, nombre, fecha_programada, idusuario, observaciones;

    public Mecanicos(String foto, String nombre, String fecha_programada, String idusuario, String observaciones) {
        this.foto = foto;
        this.nombre = nombre;
        this.fecha_programada = fecha_programada;
        this.idusuario = idusuario;
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_programada() {
        return fecha_programada;
    }

    public void setFecha_programada(String fecha_programada) {
        this.fecha_programada = fecha_programada;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }
}
