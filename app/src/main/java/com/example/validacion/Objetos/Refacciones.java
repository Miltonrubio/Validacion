package com.example.validacion.Objetos;

public class Refacciones {


    private String cantidad;
    private String descripcion;
    private String precio;
    private String idventa;


    public Refacciones(String cantidad, String descripcion, String precio, String idventa) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precio = precio;
        this.idventa = idventa;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getIdventa() {
        return idventa;
    }

    public void setIdventa(String idventa) {
        this.idventa = idventa;
    }

}

