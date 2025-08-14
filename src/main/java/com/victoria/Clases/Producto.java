package com.victoria.Clases;

public class Producto {
    String descripcion;
    String talle;
    String id_producto;
    double precio;
    String color;
    String marca;
    String tipo; // el tipo depende del tipoProducto, ejemplo: Gorras si es tipoProducto = Accs, ejemplo: remera si es tipoProducto = Ropa
    String tipoProducto; // esto se refiere a: Remera o Accs

    public Producto(String id, String des, String talle, double precio, String color, String marca, String tipo, String tipoProducto) {
            this.id_producto = id;
            this.descripcion = des;
            this.talle= talle;
            this.precio = precio;
            this.color=color;
            this.marca= marca;
            this.tipo = tipo;
            this.tipoProducto= tipoProducto;
        }
    public String getTipoProducto() {
        return tipoProducto;
    }
    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    public String getId_producto() {
        return id_producto;
    }
    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    // setters
    public void setId(String id) {
        this.id_producto = id;
    }
    public void setDescripcion(String d) {
        this.descripcion=d;
    }
    public void setTalle(String t) {
        this.talle = t;
    }
    public void setPrecio(double p) {
        this.precio = p;
    }
    public void setColor(String c) {
        this.color = c;
    }
    public void setMarca(String m) {
        this.marca = m;
    }
    // getters
    public String getId() {
        return id_producto;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getTalle() {
        return talle;
    }
    public double getPrecio() {
        return precio;
    }
    public String getColor() {
        return color;
    }
    public String getMarca() {
        return marca;
    }

    /*String ya tiene implementado su propio hashCode(), y devuelve un int, 
    como necesita Java para identificar objetos en colecciones (HashMap, HashSet, etc.).
    Y el método equals() compara por contenido, no por referencia 
    (es decir, si los textos son iguales, lo considera el mismo producto). */
    /*Porque estamos usando Producto como clave en un Map. 
    Java necesita saber cómo comparar dos productos para saber si son el mismo.
    Entonces, comparamos por id:
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto otro = (Producto) obj;
        return id_producto.equals(otro.id_producto);
    }
    @Override
    public int hashCode() {
        return id_producto.hashCode(); // ← este ya devuelve un int
    }

}
