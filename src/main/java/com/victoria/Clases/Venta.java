package com.victoria.Clases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private Integer id_venta;
    private LocalDate fecha;
    private String vendedor;
    private String medioPago;              // NUEVO
    private double descuentoPorcentaje;    // NUEVO
    private List<ItemVenta> items;
    private double total;

    public Venta() {
        items = new ArrayList<>();
    }

    public Venta(LocalDate fecha, String vendedor) {
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.items = new ArrayList<>();
    }

    public void agregarItem(ItemVenta item) {
        items.add(item);
        calcularTotal();
    }

    // =========================================================
    // AHORA calcularTotal APLICA EL DESCUENTO
    // =========================================================
    public void calcularTotal() {
        double totalSinDescuento =
                items.stream().mapToDouble(ItemVenta::getSubtotal).sum();

        double descuento = totalSinDescuento * (descuentoPorcentaje / 100.0);

        total = totalSinDescuento - descuento;
    }

    public Integer getId_venta() { return id_venta; }
    public void setId_venta(Integer id_venta) { this.id_venta = id_venta; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public double getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(double descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
        calcularTotal(); // recalcula el total si cambia el descuento
    }

    public List<ItemVenta> getItems() { return items; }
    public void setItems(List<ItemVenta> items) {
        this.items = items;
        calcularTotal();
    }
    public void setTotal(double total) { this.total = total; }

    public double getTotal() { return total; }
}