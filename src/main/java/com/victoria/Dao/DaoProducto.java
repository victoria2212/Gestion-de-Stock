package com.victoria.Dao;

import java.util.ArrayList;

import com.victoria.Clases.Producto;

public interface DaoProducto {
    public void altaProducto(Producto producto);
    public void bajaProducto(String id_producto);
    public ArrayList<Producto> buscarProductos();
    public boolean existeProducto(String id);
    
}
