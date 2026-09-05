package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.time.LocalDate;
import java.util.List;

import com.victoria.Clases.Venta;

public interface DaoVenta {
    public int altaVenta(Venta venta);
    public List<Venta> obtenerHistorialVentas();
    public List<Venta> obtenerVentasPorFecha(LocalDate desde, LocalDate hasta);
    int contarVentasPorVendedor(String nombreCompleto);
}
