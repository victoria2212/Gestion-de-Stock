package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.util.ArrayList;

import com.victoria.Clases.Empleado;

public interface DaoEmpleado {

    public ArrayList<Empleado> buscarEmpleados();
    public void crearEmpleado(Empleado empleado);
    public boolean existeEmpleado(Integer dni);

}
