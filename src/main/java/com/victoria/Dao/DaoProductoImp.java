package com.victoria.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.victoria.Clases.Producto;
import com.victoria.Conexion.ConexionDB;

public class DaoProductoImp implements DaoProducto{

    @Override
    public void altaProducto(Producto producto) {
    Connection cn = null; //para conectar a la bd
	PreparedStatement cs = null;//para hacer las consultas SQL
	ResultSet rs = null; 
             
         
        String consulta = "INSERT INTO producto (id_producto, descripcion, color, marca, talle, precio, tipo, tipoproducto)" 
                        + "VALUES (?,?,?,?,?,?,?,?);";
        
        ConexionDB conexion = new ConexionDB();
       
        try {
			cn = conexion.conectar();
			cs = cn.prepareStatement(consulta);
			//INCORPORAMOS PARAMETROS DE ARRIBA (los values)
			cs.setString(1, producto.getId_producto());
			cs.setString(2, producto.getDescripcion());
			cs.setString(3, producto.getColor());
           	cs.setString(4, producto.getMarca());
			cs.setString(5, producto.getTalle());
			cs.setDouble(6, producto.getPrecio());
			cs.setString(7, producto.getTipo());
			cs.setString(8, producto.getTipoProducto());

			//EJECUTAMOS
			//System.out.println("antes del execute");
			cs.executeUpdate();
            System.out.println("Producto creado correctamente.");
		}catch (SQLException e) {
            System.out.println("Error al guardar en base:");
			e.printStackTrace();
		}finally {
			//Para liberar recursos
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.close();
				}
				if(cn != null) {
					cn.close();
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
        System.out.println("Se ejecutó el INSERT correctamente.");

    }

    @Override
    public void bajaProducto(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bajaProducto'");
    }

    @Override
    public ArrayList<Producto> buscarProductos() {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        return productos;
    }
	public boolean existeProducto(String id_producto) {
    boolean b = false;
    ConexionDB conexion = new ConexionDB();
    String consulta = "SELECT 1 FROM producto WHERE id_producto = ?;";
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);
        ps.setString(1, id_producto);  // seteás el DNI en la consulta
        rs = ps.executeQuery();

        if (rs.next()) {
            b = true; // se encontró un empleado con ese DNI
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (cn != null) cn.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
        return b; // devuelve true si existe, false si no
    }

}
