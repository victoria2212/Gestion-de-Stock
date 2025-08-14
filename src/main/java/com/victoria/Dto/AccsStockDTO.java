package com.victoria.Dto;

import java.time.LocalDateTime;

public class AccsStockDTO {
    String tipoAccs;
    String descripcion;
    String talle;
    double precio;
    String color;
    String marca;
    int cantidad;
    String identificador;
    LocalDateTime fechaActualizacion;

    public AccsStockDTO(String tipoAccs, String descripcion, String talle, double precio,
                        String color, String marca, int cantidad, String identificador, LocalDateTime actualizacion) {
        this.tipoAccs = tipoAccs;
        this.descripcion = descripcion;
        this.talle = talle;
        this.precio = precio;
        this.color = color;
        this.marca = marca;
        this.cantidad = cantidad;
        this.identificador = identificador;
        this.fechaActualizacion=actualizacion;
    }

    public String getTipoAccs() {
        return tipoAccs;
    }

    public void setTipoAccs(String tipoAccs) {
        this.tipoAccs = tipoAccs;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    

}
