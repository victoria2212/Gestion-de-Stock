package com.victoria.Dao;
// el DAO es quien habla con la BD
import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;

public interface DaoStock {
    public void registrarStock(Integer id, int cantidad, LocalDateTime actualizacion);
    public List<RopaStockDTO> obtenerStockRopa();
    public List<AccsStockDTO> obtenerStockAccs();
    public void bajaProducto(Integer id_producto);
    public void actualizarAccs(Integer idProducto, int cantidad);
    public void actualizarRopa(Integer idProducto, int cantidad);

}
