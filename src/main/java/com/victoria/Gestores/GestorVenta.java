package com.victoria.Gestores;

import java.time.LocalDate;
import java.util.List;

import com.victoria.Clases.ItemVenta;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Venta;
import com.victoria.Dao.DaoVenta;
import com.victoria.Dao.DaoVentaImp;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;

public class GestorVenta {

    private static GestorVenta gestorVenta;
    DaoVenta ventaDao;

    // constructor
    public GestorVenta() {
        ventaDao = new DaoVentaImp();
    }

    public static GestorVenta getInstance() {
        if (gestorVenta == null) {
            gestorVenta = new GestorVenta();
        }
        return gestorVenta;
    }

    /**
     * Registra la venta completa (cabecera + items) y descuenta el stock vendido.
     * Es el único punto de entrada para guardar una venta: el controller nunca
     * inserta directo ni toca stock por su cuenta.
     */
    public int registrarVenta(Venta venta) {
        int idGenerado = ventaDao.altaVenta(venta);
        venta.setId_venta(idGenerado);

        descontarStockVendido(venta);

        return idGenerado;
    }

    /**
     * Por cada item vendido, le pregunta a GestorStock cuánto hay disponible
     * ahora mismo y actualiza con la cantidad restante. GestorVenta nunca toca
     * la tabla de stock directamente: siempre pasa por GestorStock.
     */
   

    private int buscarCantidadActual(List<RopaStockDTO> stock, Integer idProducto) {
        return stock.stream()
                .filter(dto -> dto.getIdentificador().equals(idProducto))
                .findFirst()
                .map(RopaStockDTO::getCantidad)
                .orElse(0);
    }

    private int buscarCantidadActualAccs(List<AccsStockDTO> stock, Integer idProducto) {
        return stock.stream()
                .filter(dto -> dto.getIdentificador().equals(idProducto))
                .findFirst()
                .map(AccsStockDTO::getCantidad)
                .orElse(0);
    }

    public List<Venta> obtenerHistorialVentas() {
        return ventaDao.obtenerHistorialVentas();
    }

    public List<Venta> obtenerVentasPorFecha(LocalDate desde, LocalDate hasta) {
        return ventaDao.obtenerVentasPorFecha(desde, hasta);
    }
    private void descontarStockVendido(Venta venta) {

        GestorStock gestorStock = GestorStock.getInstance();

        List<RopaStockDTO> stockRopa = gestorStock.obtenerStockRopa();
        List<AccsStockDTO> stockAccs = gestorStock.obtenerStockAccs();

        for (ItemVenta item : venta.getItems()) {

            Producto producto = item.getProducto();
            Integer idProducto = producto.getId_producto();
            int cantidadVendida = item.getCantidad();

            boolean esRopa =
                    "Ropa".equalsIgnoreCase(producto.getTipoProducto());

            int actual = esRopa
                    ? buscarCantidadActual(stockRopa, idProducto)
                    : buscarCantidadActualAccs(stockAccs, idProducto);

            // Nunca dejar un stock negativo. La variable restante para ver cuanta cantidad queda de stock
            int restante = Math.max(actual - cantidadVendida, 0);

            if (esRopa) {
                gestorStock.actualizarRopa(idProducto, restante);
            } else {
                gestorStock.actualizarAccs(idProducto, restante);
            }
        }
    }
}
