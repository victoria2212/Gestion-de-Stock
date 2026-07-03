package com.victoria.Dao;
import java.util.ArrayList;
// el DAO es quien habla con la BD
import java.util.List;

import com.victoria.Clases.Empleado;
import com.victoria.Dto.EmpleadoDTO;

public interface DaoEmpleado {

    public ArrayList<EmpleadoDTO> buscarEmpleados();
    public void crearEmpleado(Empleado empleado);
    public boolean existeEmpleado(Integer dni);
    void modificarEmpleado(EmpleadoDTO empleado);
    void eliminarEmpleado(Integer dni);


}
