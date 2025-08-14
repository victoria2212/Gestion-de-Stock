package com.victoria.Dao;

import java.util.ArrayList;

import com.victoria.Clases.Empleado;

public interface DaoEmpleado {
    public ArrayList<Empleado> buscarEmpleados();
    public void crearEmpleado(Empleado empleado);
    public boolean existeEmpleado(Integer dni);

}
