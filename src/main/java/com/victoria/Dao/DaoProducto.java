package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.util.ArrayList;

import com.victoria.Clases.Producto;


public interface DaoProducto {
    public int altaProducto(Producto producto);
    public void bajaProducto(Integer id_producto);
    public ArrayList<Producto> buscarProductos();
    public Integer existeProducto(String descripcion, String marca, String color, String talle, String tipo, String tipoProducto);
    public void modificarProducto(Producto producto);
    
}


 