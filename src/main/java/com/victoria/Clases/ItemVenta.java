package com.victoria.Clases;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class ItemVenta {

    private final ObjectProperty<Producto> producto = new SimpleObjectProperty<>();
    private final DoubleProperty precioUnitario = new SimpleDoubleProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty(1);
    private final DoubleProperty subtotal = new SimpleDoubleProperty();

    public ItemVenta() {
        // el subtotal se recalcula solo, nunca lo carga el usuario a mano
        subtotal.bind(Bindings.createDoubleBinding(
                () -> precioUnitario.get() * cantidad.get(),
                precioUnitario, cantidad
        ));
    }

    public ObjectProperty<Producto> productoProperty() { return producto; }
    public DoubleProperty precioUnitarioProperty() { return precioUnitario; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty subtotalProperty() { return subtotal; }

    public Producto getProducto() { return producto.get(); }
    public void setProducto(Producto p) {
        producto.set(p);
        if (p != null) {
            precioUnitario.set(p.getPrecio()); // el campo real se llama "precio"
        }
    }

    public double getPrecioUnitario() { return precioUnitario.get(); }
    public void setPrecioUnitario(double v) { precioUnitario.set(v); }

    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int v) { cantidad.set(v); }

    public double getSubtotal() { return subtotal.get(); }
}
