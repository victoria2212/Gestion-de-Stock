package com.victoria.Interfaces;

import com.victoria.Clases.ItemVenta;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Venta;
import com.victoria.Gestores.GestorVenta;
import com.victoria.navegation.Navegador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialVentas {

    @FXML private ComboBox<String> cbVendedor;

    @FXML private ComboBox<String> cbProducto;      // ahora filtra por TIPO de producto (Ropa/Accesorio)

    @FXML private ComboBox<String> cbMedioPago;      // NUEVO

    @FXML private DatePicker dpFecha;

    @FXML private Button btnLimpiar;

    @FXML private void volverMenuPrincipal() {
        Navegador.cambiarVista(
                "/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }

    @FXML private VBox contenedorVentas;


    private final ObservableList<Venta> ventas =
            FXCollections.observableArrayList();

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    // ==========================================================
    // INICIALIZACIÓN
    // ==========================================================

    @FXML
    public void initialize() {

        cargarVentas();

        cargarFiltros();

        cbVendedor.setOnAction(e -> aplicarFiltros());
        cbProducto.setOnAction(e -> aplicarFiltros());
        cbMedioPago.setOnAction(e -> aplicarFiltros());   // NUEVO
        dpFecha.setOnAction(e -> aplicarFiltros());
        btnLimpiar.setOnAction(e -> limpiarFiltros());


        mostrarVentas(ventas);
    }


    // ==========================================================
    // CARGAR HISTORIAL
    // ==========================================================

    private void cargarVentas() {

        List<Venta> historial = GestorVenta.getInstance().obtenerHistorialVentas();

        historial.sort((v1, v2) -> v2.getFecha().compareTo(v1.getFecha()));

        ventas.setAll(historial);
        }


    // ==========================================================
    // CARGAR FILTROS
    // ==========================================================

    private void cargarFiltros() {

        // ------------------------------------------------------
        // VENDEDORES
        // ------------------------------------------------------

        List<String> vendedores = ventas.stream()
                .map(Venta::getVendedor)
                .filter(v -> v != null && !v.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cbVendedor.setItems(
                FXCollections.observableArrayList(vendedores)
        );


        // ------------------------------------------------------
        // TIPO DE PRODUCTO (Ropa / Accesorio)
        // ------------------------------------------------------

        List<String> tiposProducto = ventas.stream()
                .flatMap(venta -> venta.getItems().stream())
                .map(this::tipoProducto)
                .filter(tipo -> tipo != null && !tipo.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cbProducto.setItems(
                FXCollections.observableArrayList(tiposProducto)
        );


        // ------------------------------------------------------
        // MEDIO DE PAGO
        // ------------------------------------------------------

        List<String> mediosPago = ventas.stream()
                .map(Venta::getMedioPago)
                .filter(m -> m != null && !m.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cbMedioPago.setItems(
                FXCollections.observableArrayList(mediosPago)
        );
    }


    // ==========================================================
    // FILTROS
    // ==========================================================

    private void aplicarFiltros() {

        String vendedorSeleccionado =
                cbVendedor.getValue();

        String tipoSeleccionado =
                cbProducto.getValue();

        String medioPagoSeleccionado =
                cbMedioPago.getValue();

        LocalDate fechaSeleccionada =
                dpFecha.getValue();


        List<Venta> ventasFiltradas = ventas.stream()

                // ------------------------------------------------
                // VENDEDOR
                // ------------------------------------------------

                .filter(venta -> {

                    if (vendedorSeleccionado == null) {
                        return true;
                    }

                    return vendedorSeleccionado.equals(
                            venta.getVendedor()
                    );
                })


                // ------------------------------------------------
                // FECHA
                // ------------------------------------------------

                .filter(venta -> {

                    if (fechaSeleccionada == null) {
                        return true;
                    }

                    return fechaSeleccionada.equals(
                            venta.getFecha()
                    );
                })


                // ------------------------------------------------
                // TIPO DE PRODUCTO
                // ------------------------------------------------

                .filter(venta -> {

                    if (tipoSeleccionado == null) {
                        return true;
                    }

                    return venta.getItems()
                            .stream()
                            .anyMatch(item ->
                                    tipoSeleccionado.equals(
                                            tipoProducto(item)
                                    )
                            );
                })


                // ------------------------------------------------
                // MEDIO DE PAGO
                // ------------------------------------------------

                .filter(venta -> {

                    if (medioPagoSeleccionado == null) {
                        return true;
                    }

                    return medioPagoSeleccionado.equals(
                            venta.getMedioPago()
                    );
                })


                .collect(Collectors.toList());


        mostrarVentas(ventasFiltradas);
    }


    // ==========================================================
    // MOSTRAR VENTAS
    // ==========================================================

    private void mostrarVentas(List<Venta> listaVentas) {

        contenedorVentas.getChildren().clear();

        for (Venta venta : listaVentas) {

            TitledPane pane =
                    crearVentaPane(venta);

            contenedorVentas.getChildren().add(pane);
        }
    }


    // ==========================================================
    // CREAR VENTA
    // ==========================================================

    private TitledPane crearVentaPane(Venta venta) {

        TitledPane pane = new TitledPane();

        pane.setExpanded(false);

        pane.setMaxWidth(
                Double.MAX_VALUE
        );

        pane.getStyleClass().add("venta");


        // ======================================================
        // CABECERA
        // ======================================================

        HBox encabezado = new HBox();

        encabezado.setSpacing(10);

        encabezado.setAlignment(
                Pos.CENTER_LEFT
        );

        encabezado.setMaxWidth(
                Double.MAX_VALUE
        );


        // ------------------------------------------------------
        // FECHA
        // ------------------------------------------------------

        Label lblFecha = new Label();

        if (venta.getFecha() != null) {

            lblFecha.setText(
                    venta.getFecha()
                            .format(formatoFecha)
            );
        }

        lblFecha.setPrefWidth(110);

        lblFecha.getStyleClass()
                .add("dato-fecha");


        // ------------------------------------------------------
        // VENDEDOR
        // ------------------------------------------------------

        Label lblVendedor = new Label(
                venta.getVendedor() != null
                        ? venta.getVendedor()
                        : ""
        );

        lblVendedor.setPrefWidth(140);

        lblVendedor.getStyleClass()
                .add("dato-vendedor");


        // ------------------------------------------------------
        // MEDIO DE PAGO (NUEVO en el encabezado también)
        // ------------------------------------------------------

        Label lblMedioPago = new Label(
                venta.getMedioPago() != null
                        ? venta.getMedioPago()
                        : ""
        );

        lblMedioPago.setPrefWidth(130);

        lblMedioPago.getStyleClass()
                .add("dato-vendedor");


        // ------------------------------------------------------
        // CANTIDAD
        // ------------------------------------------------------

        Label lblCantidad = new Label(
                String.valueOf(
                        venta.getItems().size()
                )
        );

        lblCantidad.setPrefWidth(100);

        lblCantidad.getStyleClass()
                .add("dato-cantidad");


        // ------------------------------------------------------
        // TOTAL
        // ------------------------------------------------------

        Label lblTotal = new Label(
                "$ " + String.format(
                        "%,.0f",
                        venta.getTotal()
                )
        );

        lblTotal.getStyleClass()
                .add("dato-total");

        HBox.setHgrow(
                lblTotal,
                Priority.ALWAYS
        );


        encabezado.getChildren().addAll(
                lblFecha,
                lblVendedor,
                lblMedioPago,
                lblCantidad,
                lblTotal
        );


        pane.setGraphic(encabezado);


        // ======================================================
        // DETALLE
        // ======================================================

        pane.setContent(
                crearDetalleVenta(venta)
        );


        return pane;
    }


    // ==========================================================
    // DETALLE DE LA VENTA
    // ==========================================================

    private VBox crearDetalleVenta(Venta venta) {

        VBox detalle = new VBox();

        detalle.setSpacing(8);

        detalle.getStyleClass()
                .add("detalle-venta");


        // ======================================================
        // RESUMEN: MEDIO DE PAGO Y DESCUENTO
        // ======================================================

        HBox resumen = new HBox();

        resumen.setSpacing(30);

        resumen.getStyleClass()
                .add("resumen-detalle");


        Label lblMedioPago = new Label(
                "Medio de pago: "
                        + (venta.getMedioPago() != null
                                ? venta.getMedioPago()
                                : "-")
        );

        lblMedioPago.getStyleClass()
                .add("texto-detalle");


        Label lblDescuento = new Label(
                "Descuento: "
                        + String.format(
                                "%,.0f",
                                venta.getDescuentoPorcentaje()
                        )
                        + "%"
        );

        lblDescuento.getStyleClass()
                .add("texto-detalle");


        resumen.getChildren().addAll(
                lblMedioPago,
                lblDescuento
        );


        detalle.getChildren().add(
                resumen
        );


        // ======================================================
        // CABECERA DEL DETALLE
        // ======================================================

        HBox cabecera = new HBox();

        cabecera.setSpacing(10);

        cabecera.getStyleClass()
                .add("cabecera-detalle");


        Label lblProducto = new Label("Producto");
        lblProducto.setPrefWidth(280);


        Label lblPrecio =
                new Label("Precio unit.");

        lblPrecio.setPrefWidth(120);


        Label lblCantidad =
                new Label("Cant.");

        lblCantidad.setPrefWidth(80);


        Label lblSubtotal =
                new Label("Subtotal");

        HBox.setHgrow(
                lblSubtotal,
                Priority.ALWAYS
        );


        cabecera.getChildren().addAll(
                lblProducto,
                lblPrecio,
                lblCantidad,
                lblSubtotal
        );


        detalle.getChildren().add(
                cabecera
        );


        // ======================================================
        // ITEMS
        // ======================================================

        for (ItemVenta item : venta.getItems()) {

            detalle.getChildren().add(
                    crearFilaItem(item)
            );
        }


        return detalle;
    }


    // ==========================================================
    // FILA DE ITEM
    // ==========================================================

    private HBox crearFilaItem(ItemVenta item) {

        HBox fila = new HBox();

        fila.setSpacing(10);
        fila.setAlignment(Pos.TOP_LEFT);


        // ------------------------------------------------------
        // PRODUCTO
        // ------------------------------------------------------

        Label lblProducto = new Label(nombreProducto(item));

        lblProducto.setPrefWidth(280);
        lblProducto.setWrapText(true);


        // ------------------------------------------------------
        // PRECIO UNITARIO
        // ------------------------------------------------------

        Label lblPrecio =
                new Label(
                        "$ " + String.format(
                                "%,.0f",
                                item.getPrecioUnitario()
                        )
                );

        lblPrecio.setPrefWidth(120);


        // ------------------------------------------------------
        // CANTIDAD
        // ------------------------------------------------------

        Label lblCantidad =
                new Label(
                        String.valueOf(
                                item.getCantidad()
                        )
                );

        lblCantidad.setPrefWidth(80);


        // ------------------------------------------------------
        // SUBTOTAL
        // ------------------------------------------------------

        Label lblSubtotal =
                new Label(
                        "$ " + String.format(
                                "%,.0f",
                                item.getSubtotal()
                        )
                );

        HBox.setHgrow(
                lblSubtotal,
                Priority.ALWAYS
        );


        fila.getChildren().addAll(
                lblProducto,
                lblPrecio,
                lblCantidad,
                lblSubtotal
        );


        return fila;
    }


    // ==========================================================
    // NOMBRE DEL PRODUCTO
    // ==========================================================

    private String nombreProducto(ItemVenta item) {

        Producto producto =
                item.getProducto();

        if (producto == null) {
            return "";
        }

        return producto.getMarca()
                + " - "
                + producto.getDescripcion();
    }


    // ==========================================================
    // TIPO DE PRODUCTO (Ropa / Accesorio) — NUEVO
    // ==========================================================

    private String tipoProducto(ItemVenta item) {

        Producto producto =
                item.getProducto();

        if (producto == null) {
            return "";
        }

        return producto.getTipoProducto();
    }


    // ==========================================================
    // LIMPIAR FILTROS
    // ==========================================================

    @FXML
    private void limpiarFiltros() {

        cbVendedor.getSelectionModel()
                .clearSelection();

        cbProducto.getSelectionModel()
                .clearSelection();

        cbMedioPago.getSelectionModel()
                .clearSelection();

        dpFecha.setValue(null);

        mostrarVentas(ventas);
    }
}