package com.victoria.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.victoria.Conexion.ConexionDB;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;

public class DaoStockImp implements DaoStock{

    @Override
    public void registrarStock(String id, int cantidad, LocalDateTime actualizacion) {
        
        String consulta = "INSERT INTO stock (id_producto, cantidad, ultimaactualizacion)" + "VALUES (?, ?, ?);";
        
        ConexionDB conexion = new ConexionDB();
		Connection cn = null; // para conectar a la bd
		ResultSet rs = null;
		PreparedStatement cs = null;//para hacer las consultas SQL

        try {
        // Convertir LocalDateTime a java.sql.Timestamp
        Timestamp timestamp = Timestamp.valueOf(actualizacion);
			cn = conexion.conectar();
			cs = cn.prepareStatement(consulta);
			//INCORPORAMOS PARAMETROS DE ARRIBA (los values)
			cs.setString(1, id);
			cs.setInt(2, cantidad);
			cs.setTimestamp(3, timestamp);

			cs.executeUpdate();
            System.out.println("Stock actualizado correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Para liberar recursos
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (cn != null) {
					cn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
        

    }
	public List<RopaStockDTO> obtenerStockRopa() {
	List<RopaStockDTO> lista = new ArrayList<>();
	ConexionDB conexion = new ConexionDB();
	Connection cn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
    String consulta = "SELECT p.id_producto, p.tipoProducto, p.descripcion, p.talle, p.precio, " +
                  "p.color, p.marca, s.cantidad, s.ultimaActualizacion " +
                  "FROM producto p " +
                  "JOIN stock s ON p.id_producto = s.id_producto " +
                  "WHERE p.tipo = 'Ropa'" + "ORDER BY 1" ;

    try {
		cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);
        //ps.setString(7, "Ropa");  // seteás el Tipo Ropa en la consulta
        ps = cn.prepareStatement(consulta);
        rs = ps.executeQuery();
		while (rs.next()) {
	
				RopaStockDTO dto = new RopaStockDTO(
				rs.getString("tipoProducto"),
				rs.getString("descripcion"),
				rs.getString("talle"),
				rs.getDouble("precio"),
				rs.getString("color"),
				rs.getString("marca"),
				rs.getInt("cantidad"),
				rs.getString("id_producto"), // suponiendo que es el identificador
				rs.getObject("ultimaActualizacion", LocalDateTime.class)
				);
    		lista.add(dto);
		}} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
		}
	@Override
	public List<AccsStockDTO> obtenerStockAccs() {
	List<AccsStockDTO> lista = new ArrayList<>();
	ConexionDB conexion = new ConexionDB();
	Connection cn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
    String consulta = "SELECT p.id_producto, p.tipoProducto, p.descripcion, p.talle, p.precio, " +
                  "p.color, p.marca, s.cantidad, s.ultimaActualizacion " +
                  "FROM producto p " +
                  "JOIN stock s ON p.id_producto = s.id_producto " +
                  "WHERE p.tipo = 'Accesorio'" + "ORDER BY 1" ;
	try {
		cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);
        //ps.setString(7, "Accesorio");  // seteás el Tipo Accs en la consulta
        ps = cn.prepareStatement(consulta);
        rs = ps.executeQuery();
		while (rs.next()) {
	
				AccsStockDTO dto = new AccsStockDTO(
				rs.getString("tipoProducto"),
				rs.getString("descripcion"),
				rs.getString("talle"),
				rs.getDouble("precio"),
				rs.getString("color"),
				rs.getString("marca"),
				rs.getInt("cantidad"),
				rs.getString("id_producto"), // suponiendo que es el identificador
				rs.getObject("ultimaActualizacion", LocalDateTime.class)
				);
    		lista.add(dto);
		}} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
	public void bajaProducto(String id_producto){
	}
	public void actualizarAccs(AccsStockDTO accs){

	}
	public void actualizarRopa(RopaStockDTO ropa){
		
	}
	
	
}


