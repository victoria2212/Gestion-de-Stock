package com.victoria.Clases;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class Stock {
    /*Es un mapa (como un diccionario) que guarda productos y sus cantidades.
    La clave es un objeto Producto.
    El valor es un número entero que representa cuántos hay de ese producto. */
    private Map<Producto, Integer> productos; // Producto → Cantidad
    LocalDateTime ultimaActualizacion;
    //Por POLIMORFISMO: como Ropa y Accesorio heredan de Producto-> qse puede usar cualquier subclase como clave también
     public Stock() {
        productos = new HashMap<>();
        ultimaActualizacion = LocalDateTime.now();
    }
     /* Agrega un producto con una cantidad al stock.
    Si ya estaba ese producto, lo reemplaza con la nueva cantidad.
    Actualiza la fecha/hora de modificación. */
    public Stock(Producto p, int c){
        productos.put(p, c);
        ultimaActualizacion = LocalDateTime.now();
    }


    // le pasamos un producto para ver su cantidad, si no esta, devuelve 0
     public int obtenerCantidad(Producto producto) {
        return productos.getOrDefault(producto, 0);
    }
    //Devuelve la última fecha en que se tocó algo en el stock
    public LocalDateTime getFechaActualizacion() {
        return ultimaActualizacion;
    }
    //Devuelve el mapa completo con todos los productos y sus cantidades. 
     public Map<Producto, Integer> getProductos() {
        return productos;
    }

}