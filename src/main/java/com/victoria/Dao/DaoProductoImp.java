package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.victoria.Clases.Producto;
import com.victoria.Conexion.ConexionDB;

public class DaoProductoImp implements DaoProducto{

    @Override
    public int altaProducto(Producto producto) {
    
     int idGenerado = 0;

    Connection cn = null;
    PreparedStatement cs = null;
    PreparedStatement csUpdate = null;
    ResultSet rs = null;

    ConexionDB conexion = new ConexionDB();

    String consulta =
        "INSERT INTO producto " +
        "(descripcion, color, marca, talle, precio, tipo, tipoproducto, codigo_producto) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    try {

        cn = conexion.conectar();

        // IMPORTANTE:
        // RETURN_GENERATED_KEYS devuelve el ID autogenerado
        cs = cn.prepareStatement(
                consulta,
                Statement.RETURN_GENERATED_KEYS
        );

        // INSERT
        cs.setString(1, producto.getDescripcion());
        cs.setString(2, producto.getColor());
        cs.setString(3, producto.getMarca());
        cs.setString(4, producto.getTalle());
        cs.setDouble(5, producto.getPrecio());
        cs.setString(6, producto.getTipo());
        cs.setString(7, producto.getTipoProducto());

        // temporalmente vacío
        cs.setString(8, "");

        cs.executeUpdate();

        // RECUPERAR ID AUTOGENERADO
        rs = cs.getGeneratedKeys();

        if(rs.next()){

            idGenerado = rs.getInt(1);

        }

        // NORMALIZAR TEXTO
        String tipoNorm = producto.getTipo()
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", "");

        String marcaNorm = producto.getMarca()
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", "");

        String ropaNorm = producto.getTipoProducto()
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", "");

        // GENERAR CODIGO
        String codigoProducto =
                tipoNorm + "-" +
                marcaNorm + "-" +
                ropaNorm + "-" +
                idGenerado;

        // GUARDAR CODIGO GENERADO
        String update =
            "UPDATE producto " +
            "SET codigo_producto = ? " +
            "WHERE id = ?";

        csUpdate = cn.prepareStatement(update);

        csUpdate.setString(1, codigoProducto);
        csUpdate.setInt(2, idGenerado);

        csUpdate.executeUpdate();

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
        return idGenerado;
    }

    @Override
    public void bajaProducto(Integer id) {
    
    String consulta = "DELETE FROM producto WHERE id = ?";

    ConexionDB conexion = new ConexionDB();

    Connection cn = null;
    PreparedStatement ps = null;

    try {

        cn = conexion.conectar();

        ps = cn.prepareStatement(consulta);

        ps.setInt(1, id);

        ps.executeUpdate();

    } catch (SQLException e) {

        e.printStackTrace();

    } finally {

        try {

            if(ps != null) ps.close();

            if(cn != null) cn.close();

        } catch (Exception e2) {

            e2.printStackTrace();
        }
    }
    }

    @Override
    public ArrayList<Producto> buscarProductos() {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        return productos;
    }
	public Integer existeProducto( String descripcion,
        String marca,
        String color,
        String talle,
        String tipo,
        String tipoProducto) {
    
    Integer idProducto = null;
    ConexionDB conexion = new ConexionDB();
     String consulta =
        "SELECT id FROM producto " +
        "WHERE descripcion = ? " +
        "AND marca = ? " +
        "AND color = ? " +
        "AND talle = ? " +
        "AND tipo = ? " +
        "AND tipoproducto = ?;";
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);
        ps.setString(1, descripcion);
        ps.setString(2, marca);
        ps.setString(3, color);
        ps.setString(4, talle);
        ps.setString(5, tipo);
        ps.setString(6, tipoProducto);
        rs = ps.executeQuery();

        if (rs.next()) {
           idProducto = rs.getInt("id"); // si existe devuelve un numero, sino devuelve null
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
        return idProducto; // devuelve un numero si existe, sino -> null
    }
   @Override
public void modificarProducto(Producto producto) {

    String consulta =
        "UPDATE producto SET " +
        "descripcion = ?, " +
        "talle = ?, " +
        "precio = ?, " +
        "color = ?, " +
        "marca = ?, " +
        "tipoproducto = ? " +
        "WHERE id = ?;";

    ConexionDB conexion = new ConexionDB();

    Connection cn = null;
    PreparedStatement ps = null;
    PreparedStatement psUpdate = null; // ← declarada acá afuera

    try {

        cn = conexion.conectar();

        ps = cn.prepareStatement(consulta);

        ps.setString(1, producto.getDescripcion());
        ps.setString(2, producto.getTalle());
        ps.setDouble(3, producto.getPrecio());
        ps.setString(4, producto.getColor());
        ps.setString(5, producto.getMarca());
        ps.setString(6, producto.getTipoProducto());
        ps.setInt(7, producto.getId_producto());

        ps.executeUpdate();

        // ── REGENERAR CÓDIGO PRODUCTO ──────────────────────────
        String tipoNorm = producto.getTipo()
                .trim().toUpperCase().replaceAll("\\s+", "");

        String marcaNorm = producto.getMarca()
                .trim().toUpperCase().replaceAll("\\s+", "");

        String tipoProductoNorm = producto.getTipoProducto()
                .trim().toUpperCase().replaceAll("\\s+", "");

        String codigoProducto =
                tipoNorm + "-" +
                marcaNorm + "-" +
                tipoProductoNorm + "-" +
                producto.getId_producto();

        String updateCodigo =
            "UPDATE producto SET codigo_producto = ? WHERE id = ?";

        psUpdate = cn.prepareStatement(updateCodigo); // ← asignada acá adentro
        psUpdate.setString(1, codigoProducto);
        psUpdate.setInt(2, producto.getId_producto());
        psUpdate.executeUpdate();
        // ──────────────────────────────────────────────────────

        System.out.println("Producto modificado correctamente.");

    } catch (SQLException e) {

        e.printStackTrace();

    } finally {

        try {

            if (ps != null) ps.close();
            if (psUpdate != null) psUpdate.close(); // ← ahora el finally la ve
            if (cn != null) cn.close();

        } catch (Exception e2) {

            e2.printStackTrace();

        }
    }
}
    

}

