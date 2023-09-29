package com.example.validacion;

public class ActividadadesUnidad {

    String horainicio, horafin, fecha, id_servicio, idpersonal, observaciones, estatus;


    public ActividadadesUnidad(String horainicio, String horafin, String fecha, String id_servicio, String idpersonal, String observaciones, String estatus) {
        this.horainicio = horainicio;
        this.horafin = horafin;
        this.fecha = fecha;
        this.id_servicio = id_servicio;
        this.idpersonal = idpersonal;
        this.observaciones = observaciones;
        this.estatus = estatus;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getIdpersonal() {
        return idpersonal;
    }

    public void setIdpersonal(String idpersonal) {
        this.idpersonal = idpersonal;
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
