package com.victoria.Clases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private Integer id_venta;
    private LocalDate fecha;
    private String vendedor;
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

    public void calcularTotal() {
        total = items.stream().mapToDouble(ItemVenta::getSubtotal).sum();
    }

    public Integer getId_venta() { return id_venta; }
    public void setId_venta(Integer id_venta) { this.id_venta = id_venta; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }

    public List<ItemVenta> getItems() { return items; }
    public void setItems(List<ItemVenta> items) {
        this.items = items;
        calcularTotal();
    }

    public double getTotal() { return total; }
}
