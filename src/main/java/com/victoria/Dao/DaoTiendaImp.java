package com.victoria.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.victoria.Conexion.ConexionDB;

public class DaoTiendaImp implements DaoTienda{
     public boolean esOwner(Integer dni) {

        boolean b = false;

        ConexionDB conexion = new ConexionDB();

        String consulta = "SELECT 1 FROM tienda WHERE owner = ?;";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            cn = conexion.conectar();

            ps = cn.prepareStatement(consulta);

            ps.setInt(1, dni);

            rs = ps.executeQuery();

            if (rs.next()) {
                b = true;
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

        return b;
    }

}
