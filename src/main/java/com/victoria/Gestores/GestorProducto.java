package com.victoria.Gestores;

import java.util.ArrayList;

import com.victoria.Clases.Producto;
import com.victoria.Dao.DaoProducto;
import com.victoria.Dao.DaoProductoImp;

public class GestorProducto {
    ArrayList<Producto> productos;
    private static GestorProducto gestorProducto;
    DaoProducto productoDao;
    //constructor
    public GestorProducto(){
        productoDao = new DaoProductoImp();
        productos = new ArrayList<>(productoDao.buscarProductos());
    }
    public static GestorProducto getInstance() {
		if (gestorProducto == null) {
			gestorProducto = new GestorProducto();
		}
		return gestorProducto;
	}
    public void altaProducto(Producto producto){
    productoDao.altaProducto(producto);
    productos.add(producto);
    }
    public boolean existeProducto(String id){
    boolean b =productoDao.existeProducto(id);
    return b;
    }

}
