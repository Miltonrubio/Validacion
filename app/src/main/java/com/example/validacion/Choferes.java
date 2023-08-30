package com.example.validacion;

public class Choferes {

    String id, chofer, marca, modelo, nombre, foto_mapa, fecha_inicio, hora_inicio, fecha_final, hora_final, kilometros, costo_kilometro, importe, observaciones,estatus;

    public Choferes(String id, String chofer, String marca, String modelo, String nombre, String foto_mapa, String fecha_inicio, String hora_inicio, String fecha_final, String hora_final, String kilometros, String costo_kilometro, String importe, String observaciones, String estatus) {
        this.id = id;
        this.chofer = chofer;
        this.marca = marca;
        this.modelo = modelo;
        this.nombre = nombre;
        this.foto_mapa = foto_mapa;
        this.fecha_inicio = fecha_inicio;
        this.hora_inicio = hora_inicio;
        this.fecha_final = fecha_final;
        this.hora_final = hora_final;
        this.kilometros = kilometros;
        this.costo_kilometro = costo_kilometro;
        this.importe = importe;
        this.observaciones = observaciones;
        this.estatus = estatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChofer() {
        return chofer;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto_mapa() {
        return foto_mapa;
    }

    public void setFoto_mapa(String foto_mapa) {
        this.foto_mapa = foto_mapa;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public String getKilometros() {
        return kilometros;
    }

    public void setKilometros(String kilometros) {
        this.kilometros = kilometros;
    }

    public String getCosto_kilometro() {
        return costo_kilometro;
    }

    public void setCosto_kilometro(String costo_kilometro) {
        this.costo_kilometro = costo_kilometro;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
