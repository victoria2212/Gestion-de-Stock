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
        "SELECT dni, nombre, apellido, direccion, dia_de_alta " +
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

            LocalDate fechaAlta =
                rs.getObject("dia_de_alta", LocalDate.class);

            EmpleadoDTO emp = new EmpleadoDTO(
                dni,
                nombre,
                apellido,
                direccion,
                fechaAlta
            );

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
        Connection cn = null; //para conectar a la bd
		PreparedStatement cs = null;//para hacer las consultas SQL
		ResultSet rs = null; 
        // DOY DE ALTA A UN EMPLEADO/ CREO UN EMPLEADO
        String consulta = "INSERT INTO empleado (dni, nombre, apellido, direccion, dia_de_alta)" 
                        + "VALUES (?,?,?,?,?);";
        ConexionDB conexion = new ConexionDB();
        try {
			cn = conexion.conectar();
			cs = cn.prepareStatement(consulta);
			
			//INCORPORAMOS PARAMETROS DE ARRIBA (los values)
			cs.setInt(1, empleado.getDni());
			cs.setString(2, empleado.getNombre());
			cs.setString(3, empleado.getApellido());
            cs.setString(4, empleado.getDireccion());
            cs.setObject(5, empleado.getDia_de_alta());

			cs.executeUpdate();
            System.out.println("Empleado creado correctamente.");
            System.out.println("Se ejecutó el INSERT correctamente.");
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
         
    


}
