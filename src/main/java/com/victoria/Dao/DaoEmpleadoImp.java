package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.victoria.Clases.Empleado;
import com.victoria.Conexion.ConexionDB;
import com.victoria.Dto.EmpleadoDTO;

public class DaoEmpleadoImp implements DaoEmpleado {

@Override
public ArrayList<EmpleadoDTO> buscarEmpleados() {

        ArrayList<EmpleadoDTO> empleados = new ArrayList<>();

        String consulta =
            "SELECT dni, nombre, apellido, direccion, contacto, dia_de_alta, rol, ultima_conexion " +
            "FROM empleado " +
            "ORDER BY apellido";

        ConexionDB conexion = new ConexionDB();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = conexion.conectar();
            ps = cn.prepareStatement(consulta);
            rs = ps.executeQuery();

            while (rs.next()) {

                Integer dni = rs.getInt("dni");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String direccion = rs.getString("direccion");
                String contacto = rs.getString("contacto");
                LocalDate fechaAlta = rs.getObject("dia_de_alta", LocalDate.class);

                EmpleadoDTO emp = new EmpleadoDTO(dni, nombre, apellido, direccion, contacto, fechaAlta);

                emp.setRol(rs.getString("rol"));
                emp.setUltimaConexion(rs.getObject("ultima_conexion", java.time.LocalDateTime.class));

                empleados.add(emp);
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

        return empleados;
    }

   @Override
    public void crearEmpleado(Empleado empleado) {
        Connection cn = null;
        PreparedStatement cs = null;
        ResultSet rs = null;

        String consulta = "INSERT INTO empleado (dni, nombre, apellido, direccion, contacto, dia_de_alta, rol) "
                        + "VALUES (?,?,?,?,?,?,?);";
        ConexionDB conexion = new ConexionDB();
        try {
            cn = conexion.conectar();
            cs = cn.prepareStatement(consulta);

            cs.setInt(1, empleado.getDni());
            cs.setString(2, empleado.getNombre());
            cs.setString(3, empleado.getApellido());
            cs.setString(4, empleado.getDireccion());
            cs.setString(5, empleado.getContacto());
            cs.setObject(6, empleado.getDia_de_alta());
            cs.setString(7, empleado.getRol());

            cs.executeUpdate();
            System.out.println("Empleado creado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar en base:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (cn != null) cn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public boolean existeEmpleado(Integer dni) {
    boolean b = false;
    ConexionDB conexion = new ConexionDB();
    String consulta = "SELECT 1 FROM empleado WHERE dni = ?;";
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);
        ps.setInt(1, dni);  // seteás el DNI en la consulta
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
   @Override
    public void modificarEmpleado(EmpleadoDTO empleado) {

    String consulta =
        "UPDATE empleado SET " +
        "nombre = ?, apellido = ?, direccion = ?, contacto = ?, rol = ? " +
        "WHERE dni = ?;";

    ConexionDB conexion = new ConexionDB();
    Connection cn = null;
    PreparedStatement ps = null;

    try {
        cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);

        ps.setString(1, empleado.getNombre());
        ps.setString(2, empleado.getApellido());
        ps.setString(3, empleado.getDireccion());
        ps.setString(4, empleado.getContacto());
        ps.setString(5, empleado.getRol());
        ps.setInt(6, empleado.getDni());

        ps.executeUpdate();
        System.out.println("Empleado modificado correctamente.");
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
    public void eliminarEmpleado(Integer dni){
    String consulta = "DELETE FROM empleado WHERE dni = ?";

    ConexionDB conexion = new ConexionDB();

    Connection cn = null;
    PreparedStatement ps = null;

    try {

        cn = conexion.conectar();

        ps = cn.prepareStatement(consulta);

        ps.setInt(1, dni);

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
    public void actualizarUltimaConexion(Integer dni) {

    String consulta = "UPDATE empleado SET ultima_conexion = ? WHERE dni = ?;";

    ConexionDB conexion = new ConexionDB();
    Connection cn = null;
    PreparedStatement ps = null;

    try {
        cn = conexion.conectar();
        ps = cn.prepareStatement(consulta);
        ps.setTimestamp(1, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        ps.setInt(2, dni);
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
         
    


}
