package com.victoria.Gestores;

import java.util.ArrayList;
import java.util.List;

import com.victoria.Clases.Empleado;
import com.victoria.Dao.DaoEmpleado;
import com.victoria.Dao.DaoEmpleadoImp;
import com.victoria.Dto.EmpleadoDTO;

public class GestorEmpleado {

    /*
    ¿Qué es un Gestor?
    El gestor (también llamado servicio o controlador de lógica de negocio) es una capa que:
    - Usa los DAOs para acceder a los datos.
    - Contiene la lógica de negocio (validaciones, reglas, cálculos).
    - Orquesta operaciones más complejas que involucran múltiples DAOs o decisiones.
     */

     private static GestorEmpleado gestorEmp;
     DaoEmpleado empleadoDao;

    //constructor
    public GestorEmpleado(){
        empleadoDao = new DaoEmpleadoImp();
        }
    public static GestorEmpleado getInstance() {
		if (gestorEmp == null) {
			gestorEmp = new GestorEmpleado();
		}
		return gestorEmp;
	}
    public void agregarEmpleado(Empleado e) {
		empleadoDao.crearEmpleado(e);
	}
    public Empleado crearEmpleado(Integer dni, String nombre,String apellido, String direccion){
        return new Empleado(dni,nombre, apellido, direccion);
    }

    public boolean existeEmpleado(Integer dni){
    boolean b=false;
    b=empleadoDao.existeEmpleado(dni);
    return b;
    }
   
   public List<EmpleadoDTO> obtenerEmpleados() {
        return empleadoDao.buscarEmpleados();
        
    }
    
}
