package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.victoria.Clases.ItemVenta;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Venta;
import com.victoria.Conexion.ConexionDB;

public class DaoVentaImp implements DaoVenta {

    /**
     * Inserta la cabecera en "ventas" y cada fila en "detalle_venta", todo dentro
     * de una misma transacción: si falla el insert de un item, se hace rollback
     * de la venta entera (no queda una venta a medias con solo algunos productos).
     */
    @Override
    public int altaVenta(Venta venta) {

        String insertVenta = "INSERT INTO ventas (fecha, vendedor, total) VALUES (?, ?, ?);";
        String insertDetalle = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) " +
                "VALUES (?, ?, ?, ?, ?);";

        ConexionDB conexion = new ConexionDB();
        Connection cn = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        ResultSet rsKeys = null;
        int idVentaGenerado = -1;

        try {
            cn = conexion.conectar();
            cn.setAutoCommit(false); // arranca la transacción

            psVenta = cn.prepareStatement(insertVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setDate(1, java.sql.Date.valueOf(venta.getFecha()));
            psVenta.setString(2, venta.getVendedor());
            psVenta.setDouble(3, venta.getTotal());
            psVenta.executeUpdate();

            rsKeys = psVenta.getGeneratedKeys();
            if (rsKeys.next()) {
                idVentaGenerado = rsKeys.getInt(1);
            }

            psDetalle = cn.prepareStatement(insertDetalle);
            for (ItemVenta item : venta.getItems()) {
                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, item.getProducto().getId_producto());
                psDetalle.setInt(3, item.getCantidad());
                psDetalle.setDouble(4, item.getPrecioUnitario());
                psDetalle.setDouble(5, item.getSubtotal());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            cn.commit();
            System.out.println("Venta registrada correctamente.");

        } catch (SQLException e) {
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psDetalle != null) psDetalle.close();
                if (psVenta != null) psVenta.close();
                if (cn != null) {
                    cn.setAutoCommit(true);
                    cn.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return idVentaGenerado;
    }

    @Override
    public List<Venta> obtenerHistorialVentas() {
        String consulta =
                "SELECT v.id_venta, v.fecha, v.vendedor, v.total, " +
                "d.cantidad, d.precio_unitario, d.subtotal, " +
                "p.id AS producto_id, p.tipoProducto, p.descripcion, p.talle, p.precio, " +
                "p.color, p.marca, p.codigo_producto " +
                "FROM ventas v " +
                "JOIN detalle_venta d ON d.venta_id = v.id_venta " +
                "JOIN producto p ON p.id = d.producto_id " +
                "ORDER BY v.id_venta;";

        return ejecutarConsultaAgrupada(consulta, null, null);
    }

    @Override
    public List<Venta> obtenerVentasPorFecha(LocalDate desde, LocalDate hasta) {
        String consulta =
                "SELECT v.id_venta, v.fecha, v.vendedor, v.total, " +
                "d.cantidad, d.precio_unitario, d.subtotal, " +
                "p.id AS producto_id, p.tipoProducto, p.descripcion, p.talle, p.precio, " +
                "p.color, p.marca, p.codigo_producto " +
                "FROM ventas v " +
                "JOIN detalle_venta d ON d.venta_id = v.id_venta " +
                "JOIN producto p ON p.id = d.producto_id " +
                "WHERE v.fecha BETWEEN ? AND ? " +
                "ORDER BY v.id_venta;";

        return ejecutarConsultaAgrupada(consulta, desde, hasta);
    }

    /**
     * Arma la List<Venta> agrupando filas consecutivas por id_venta: cada vez
     * que cambia el id, cierra la Venta anterior y arranca una nueva.
     */
    private List<Venta> ejecutarConsultaAgrupada(String consulta, LocalDate desde, LocalDate hasta) {
        List<Venta> ventas = new ArrayList<>();
        ConexionDB conexion = new ConexionDB();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = conexion.conectar();
            ps = cn.prepareStatement(consulta);
            if (desde != null && hasta != null) {
                ps.setDate(1, java.sql.Date.valueOf(desde));
                ps.setDate(2, java.sql.Date.valueOf(hasta));
            }
            rs = ps.executeQuery();

            Venta ventaActual = null;
            Integer idVentaActual = null;

            while (rs.next()) {
                int idVentaFila = rs.getInt("id_venta");

                if (!Integer.valueOf(idVentaFila).equals(idVentaActual)) {
                    ventaActual = new Venta(rs.getDate("fecha").toLocalDate(), rs.getString("vendedor"));
                    ventaActual.setId_venta(idVentaFila);
                    ventas.add(ventaActual);
                    idVentaActual = idVentaFila;
                }

                Producto producto = new Producto(
                        rs.getString("descripcion"),
                        rs.getString("talle"),
                        rs.getDouble("precio"),
                        rs.getString("color"),
                        rs.getString("marca"),
                        rs.getString("tipoProducto"),
                        rs.getString("tipoProducto")
                );
                producto.setId_producto(rs.getInt("producto_id"));
                producto.setCodigoProducto(rs.getString("codigo_producto"));

                ItemVenta item = new ItemVenta();
                item.setProducto(producto);
                item.setPrecioUnitario(rs.getDouble("precio_unitario"));
                item.setCantidad(rs.getInt("cantidad"));

                ventaActual.agregarItem(item);
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

        return ventas;
    }
}
