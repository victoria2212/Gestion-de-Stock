package com.victoria.Clases;

public class Producto {
    String descripcion;
    String talle;
    private Integer id;
    double precio;
    String color;
    String marca;
    String tipo;
    String tipoProducto;
    String codigoProducto;
    byte[] foto; // ← NUEVO

    public Producto(String des, String talle, double precio, String color, String marca, String tipo, String tipoProducto) {
        this.descripcion = des;
        this.talle = talle;
        this.precio = precio;
        this.color = color;
        this.marca = marca;
        this.tipo = tipo;
        this.tipoProducto = tipoProducto;
    }

    public Producto() {}

    // ── FOTO ──────────────────────────────────────────────
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
    // ──────────────────────────────────────────────────────

    public void setCodigoProducto(String codProd) { this.codigoProducto = codProd; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }
    public Integer getId_producto() { return id; }
    public void setId_producto(Integer id_producto) { this.id = id_producto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setDescripcion(String d) { this.descripcion = d; }
    public void setTalle(String t) { this.talle = t; }
    public void setPrecio(double p) { this.precio = p; }
    public void setColor(String c) { this.color = c; }
    public void setMarca(String m) { this.marca = m; }
    public String getDescripcion() { return descripcion; }
    public String getTalle() { return talle; }
    public double getPrecio() { return precio; }
    public String getColor() { return color; }
    public String getMarca() { return marca; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto otro = (Producto) obj;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}