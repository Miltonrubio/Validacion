package com.example.validacion;

public class Coches {

    String marca, modelo, foto_unidad;


    public Coches(String marca, String modelo, String foto_unidad) {
        this.marca = marca;
        this.modelo = modelo;
        this.foto_unidad = foto_unidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFoto_unidad() {
        return foto_unidad;
    }

    public void setFoto_unidad(String foto_unidad) {
        this.foto_unidad = foto_unidad;
    }
}
