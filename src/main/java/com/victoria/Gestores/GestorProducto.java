package com.victoria.Gestores;

import java.util.ArrayList;

import com.victoria.Clases.Producto;
import com.victoria.Dao.DaoProducto;
import com.victoria.Dao.DaoProductoImp;

public class GestorProducto {
    ArrayList<Producto> productos; // lista de productos
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

    public int altaProducto(Producto producto){

    int idGenerado = productoDao.altaProducto(producto);

    producto.setId_producto(idGenerado);

    productos.add(producto);

    return idGenerado;
}

    public void eliminarProducto(Integer id){
    productoDao.bajaProducto(id);
    }
    public Integer existeProducto(String descripcion, String marca, String color, String talle, String tipo, String tipoProducto){
    
    Integer id_existente = productoDao.existeProducto(descripcion, marca, color, talle, tipo, tipoProducto);
    return id_existente;
    
    }
    public void actualizarProducto(Producto producto){
        productoDao.modificarProducto(producto);
        
    }
   public byte[] obtenerFoto(Integer idProducto) {
    return productoDao.obtenerFoto(idProducto);
    }

}
