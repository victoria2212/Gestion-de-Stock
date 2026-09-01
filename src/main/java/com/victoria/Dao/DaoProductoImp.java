package com.victoria.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.victoria.Clases.Producto;
import com.victoria.Conexion.ConexionDB;

public class DaoProductoImp implements DaoProducto {

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
            "(descripcion, color, marca, talle, precio, tipo, tipoproducto, codigo_producto, foto) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            cn = conexion.conectar();

            cs = cn.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);

            cs.setString(1, producto.getDescripcion());
            cs.setString(2, producto.getColor());
            cs.setString(3, producto.getMarca());
            cs.setString(4, producto.getTalle());
            cs.setDouble(5, producto.getPrecio());
            cs.setString(6, producto.getTipo());
            cs.setString(7, producto.getTipoProducto());
            cs.setString(8, ""); // codigo_producto temporal

            // FOTO: si es null guarda null en la BD, sin problema
            if (producto.getFoto() != null) {
                cs.setBytes(9, producto.getFoto());
            } else {
                cs.setNull(9, java.sql.Types.BINARY);
            }

            cs.executeUpdate();

            rs = cs.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

            // GENERAR Y GUARDAR CÓDIGO
            String tipoNorm = producto.getTipo().trim().toUpperCase().replaceAll("\\s+", "");
            String marcaNorm = producto.getMarca().trim().toUpperCase().replaceAll("\\s+", "");
            String ropaNorm = producto.getTipoProducto().trim().toUpperCase().replaceAll("\\s+", "");
            String colorNorm = producto.getColor().trim().toUpperCase().replaceAll("\\s+", "");
            String codigoProducto = tipoNorm + "-" + marcaNorm + "-" + ropaNorm + "-" + colorNorm + "-" + idGenerado;

            String update = "UPDATE producto SET codigo_producto = ? WHERE id = ?";
            csUpdate = cn.prepareStatement(update);
            csUpdate.setString(1, codigoProducto);
            csUpdate.setInt(2, idGenerado);
            csUpdate.executeUpdate();

            System.out.println("Producto creado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al guardar en base:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (csUpdate != null) csUpdate.close();
                if (cn != null) cn.close();
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
                if (ps != null) ps.close();
                if (cn != null) cn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<Producto> buscarProductos() {
        return new ArrayList<>();
    }

    @Override
    public Integer existeProducto(String descripcion, String marca, String color,
                                   String talle, String tipo, String tipoProducto) {

        Integer idProducto = null;
        ConexionDB conexion = new ConexionDB();
        String consulta =
            "SELECT id FROM producto " +
            "WHERE descripcion = ? AND marca = ? AND color = ? " +
            "AND talle = ? AND tipo = ? AND tipoproducto = ?;";

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
                idProducto = rs.getInt("id");
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
        return idProducto;
    }

    @Override
    public void modificarProducto(Producto producto) {

        // Si viene foto nueva la actualizamos, si no solo actualizamos los datos
        String consulta;
        if (producto.getFoto() != null) {
            consulta =
                "UPDATE producto SET " +
                "descripcion = ?, talle = ?, precio = ?, color = ?, " +
                "marca = ?, tipoproducto = ?, foto = ? " +
                "WHERE id = ?;";
        } else {
            consulta =
                "UPDATE producto SET " +
                "descripcion = ?, talle = ?, precio = ?, color = ?, " +
                "marca = ?, tipoproducto = ? " +
                "WHERE id = ?;";
        }

        ConexionDB conexion = new ConexionDB();
        Connection cn = null;
        PreparedStatement ps = null;
        PreparedStatement psUpdate = null;

        try {
            cn = conexion.conectar();
            ps = cn.prepareStatement(consulta);

            ps.setString(1, producto.getDescripcion());
            ps.setString(2, producto.getTalle());
            ps.setDouble(3, producto.getPrecio());
            ps.setString(4, producto.getColor());
            ps.setString(5, producto.getMarca());
            ps.setString(6, producto.getTipoProducto());

            if (producto.getFoto() != null) {
                ps.setBytes(7, producto.getFoto());
                ps.setInt(8, producto.getId_producto());
            } else {
                ps.setInt(7, producto.getId_producto());
            }

            ps.executeUpdate();

           // REGENERAR CÓDIGO
            String tipoNorm = producto.getTipo().trim().toUpperCase().replaceAll("\\s+", "");
            String marcaNorm = producto.getMarca().trim().toUpperCase().replaceAll("\\s+", "");
            String tipoProductoNorm = producto.getTipoProducto().trim().toUpperCase().replaceAll("\\s+", "");
            String colorNorm = producto.getColor().trim().toUpperCase().replaceAll("\\s+", "");
            String codigoProducto = tipoNorm + "-" + marcaNorm + "-" + tipoProductoNorm + "-" + colorNorm + "-" + producto.getId_producto();
            String updateCodigo = "UPDATE producto SET codigo_producto = ? WHERE id = ?";
            psUpdate = cn.prepareStatement(updateCodigo);
            psUpdate.setString(1, codigoProducto);
            psUpdate.setInt(2, producto.getId_producto());
            psUpdate.executeUpdate();

            System.out.println("Producto modificado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (psUpdate != null) psUpdate.close();
                if (cn != null) cn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // ── RECUPERAR FOTO ────────────────────────────────────────────────────────
    public byte[] obtenerFoto(Integer idProducto) {

        byte[] foto = null;
        ConexionDB conexion = new ConexionDB();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String consulta = "SELECT foto FROM producto WHERE id = ?";

        try {
            cn = conexion.conectar();
            ps = cn.prepareStatement(consulta);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                foto = rs.getBytes("foto");
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
        return foto;
    }
}

