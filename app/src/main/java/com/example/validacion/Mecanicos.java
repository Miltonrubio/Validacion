package com.example.validacion;

public class Mecanicos {

    String foto, nombre, motivoingreso, fecha_programada;

    public Mecanicos(String foto, String nombre, String motivoingreso, String fecha_programada) {
        this.foto = foto;
        this.nombre = nombre;
        this.motivoingreso = motivoingreso;
        this.fecha_programada = fecha_programada;
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

    public String getMotivoingreso() {
        return motivoingreso;
    }

    public void setMotivoingreso(String motivoingreso) {
        this.motivoingreso = motivoingreso;
    }

    public String getFecha_programada() {
        return fecha_programada;
    }

    public void setFecha_programada(String fecha_programada) {
        this.fecha_programada = fecha_programada;
    }
}
