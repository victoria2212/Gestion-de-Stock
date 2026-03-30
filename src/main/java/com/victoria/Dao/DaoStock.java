package com.victoria.Dao;

import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;

public interface DaoStock {
    public void registrarStock(String id, int cantidad, LocalDateTime actualizacion);
    public List<RopaStockDTO> obtenerStockRopa();
    public List<AccsStockDTO> obtenerStockAccs();
    public void bajaProducto(String id_producto);
    public void actualizarAccs(AccsStockDTO accesorio);
    public void actualizarRopa(RopaStockDTO ropa);

}
