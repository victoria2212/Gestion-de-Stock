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
    Integer identificador;
    LocalDateTime fechaActualizacion;
    private String codigoProducto;
    private byte[] imagen;

    public AccsStockDTO(
        String tipoAccs,
        String descripcion,
        String talle,
        double precio,
        String color,
        String marca,
        String codigoProducto,
        int cantidad,
        Integer identificador,
        LocalDateTime actualizacion) {

    this.tipoAccs = tipoAccs;
    this.descripcion = descripcion;
    this.talle = talle;
    this.precio = precio;
    this.color = color;
    this.marca = marca;
    this.codigoProducto = codigoProducto;
    this.cantidad = cantidad;
    this.identificador = identificador;
    this.fechaActualizacion = actualizacion;
}
    public byte[] getImagen(){
        return imagen;
    }
    public void setImagen(byte[] i){
        this.imagen=i;
    }

    public void setcodigoProducto(String codP){
        this.codigoProducto = codP;

    }
    public String getCodigoProducto(){
        return codigoProducto;
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

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    

}
