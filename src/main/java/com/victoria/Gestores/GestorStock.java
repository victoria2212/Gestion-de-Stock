package com.victoria.Gestores;

import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dao.DaoStock;
import com.victoria.Dao.DaoStockImp;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;

public class GestorStock {
    
    private static GestorStock gestorStock;
    DaoStock stockDao;
    //constructor
    public GestorStock(){
        stockDao = new DaoStockImp();
    }
    public static GestorStock getInstance() {
		if (gestorStock == null) {
			gestorStock = new GestorStock();
		}
		return gestorStock;
	}
    public void registrarStock(String id, int cantidad, LocalDateTime actualizacion){
    stockDao.registrarStock(id,cantidad,actualizacion);
    }
    public List<RopaStockDTO> obtenerStockRopa() {
        return stockDao.obtenerStockRopa(); // este método ya lo hiciste
    }
    public List<AccsStockDTO> obtenerStockAccs(){
        return stockDao.obtenerStockAccs();
    }
    public void actualizarRopa(RopaStockDTO ropa) {
    stockDao.actualizarRopa(ropa);
    }

    public void actualizarAccs(AccsStockDTO accs) {
    stockDao.actualizarAccs(accs);
    }

    
}
