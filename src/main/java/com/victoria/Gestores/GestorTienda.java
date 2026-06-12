package com.victoria.Gestores;

import com.victoria.Dao.DaoTienda;
import com.victoria.Dao.DaoTiendaImp;

public class GestorTienda {
    private static GestorTienda gestorTienda;

    DaoTienda tiendaDao;

    public GestorTienda() {
        tiendaDao = new DaoTiendaImp();
    }

    public static GestorTienda getInstance() {

        if (gestorTienda == null) {
            gestorTienda = new GestorTienda();
        }

        return gestorTienda;
    }

    public boolean esOwner(Integer dni) {

        boolean b = false;

        b = tiendaDao.esOwner(dni);

        return b;
    }

}
