package com.example.validacion;

public class Cheks {


    String descripcion;

    String categoria;

    String comentarios, id_ser_venta, urlfoto, valorcheck, fechacheck, horacheck, estado;


    public Cheks(String descripcion, String categoria, String comentarios, String id_ser_venta, String urlfoto, String valorcheck, String fechacheck, String horacheck, String estado) {
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.comentarios = comentarios;
        this.id_ser_venta = id_ser_venta;
        this.urlfoto = urlfoto;
        this.valorcheck = valorcheck;
        this.fechacheck = fechacheck;
        this.horacheck = horacheck;
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getId_ser_venta() {
        return id_ser_venta;
    }

    public void setId_ser_venta(String id_ser_venta) {
        this.id_ser_venta = id_ser_venta;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getValorcheck() {
        return valorcheck;
    }

    public void setValorcheck(String valorcheck) {
        this.valorcheck = valorcheck;
    }

    public String getFechacheck() {
        return fechacheck;
    }

    public void setFechacheck(String fechacheck) {
        this.fechacheck = fechacheck;
    }

    public String getHoracheck() {
        return horacheck;
    }

    public void setHoracheck(String horacheck) {
        this.horacheck = horacheck;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
