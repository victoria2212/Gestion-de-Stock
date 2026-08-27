package com.victoria.Interfaces;

import com.victoria.Clases.ItemVenta;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Venta;
import com.victoria.Dto.RopaStockDTO;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.EmpleadoDTO;
import com.victoria.Gestores.GestorEmpleado;
import com.victoria.Gestores.GestorStock;
import com.victoria.Gestores.GestorVenta;
import com.victoria.navegation.Navegador;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RegistrarItemVenta {


    // =========================================================
    // CAMPOS DEL FXML
    // =========================================================

    @FXML
    private DatePicker dpFecha;

    @FXML
    private ComboBox<String> cbVendedor;

    @FXML
    private ComboBox<String> cbMedioPago;

    @FXML
    private ComboBox<String> cbDescuento;

    @FXML
    private TableView<ItemVenta> tablaItems;

    @FXML
    private TableColumn<ItemVenta, Producto> colProducto;

    @FXML
    private TableColumn<ItemVenta, Integer> colCantidad;

    @FXML
    private TableColumn<ItemVenta, Double> colPrecioUnitario;

    @FXML
    private TableColumn<ItemVenta, Double> colSubtotal;

    @FXML
    private TableColumn<ItemVenta, Void> colEliminar;

    @FXML
    private Label lblCantidadItems;

    @FXML
    private Label lblDescuentoAplicado;

    @FXML
    private Label lblTotal;


    // =========================================================
    // LISTAS
    // =========================================================

    private final ObservableList<ItemVenta> items =
            FXCollections.observableArrayList();


    private final Map<Producto, Integer> stockDisponible =
            new HashMap<>();


    private final ObservableList<Producto> stockCompleto =
            FXCollections.observableArrayList();


    // =========================================================
    // INICIALIZACIÓN
    // =========================================================

    @FXML
    public void initialize() {

        // =====================================================
        // FORMATO DEL PRECIO UNITARIO
        // =====================================================

        colPrecioUnitario.setCellFactory(col -> new TableCell<>() {

            @Override
            protected void updateItem(
                    Double precio,
                    boolean empty
            ) {

                super.updateItem(precio, empty);

                setText(
                        empty || precio == null
                                ? null
                                : "$ " + String.format(
                                        "%,.0f",
                                        precio
                                )
                );
            }
        });


        // =====================================================
        // FORMATO DEL SUBTOTAL
        // =====================================================

        colSubtotal.setCellFactory(col -> new TableCell<>() {

            @Override
            protected void updateItem(
                    Double subtotal,
                    boolean empty
            ) {

                super.updateItem(subtotal, empty);

                setText(
                        empty || subtotal == null
                                ? null
                                : "$ " + String.format(
                                        "%,.0f",
                                        subtotal
                                )
                );
            }
        });


        // =====================================================
        // FECHA ACTUAL
        // =====================================================

        dpFecha.setValue(
                LocalDate.now()
        );


        // =====================================================
        // MEDIOS DE PAGO
        // =====================================================

        cbMedioPago.setItems(
                FXCollections.observableArrayList(
                        "Efectivo",
                        "Transferencia",
                        "Tarjeta de Débito",
                        "Tarjeta de Crédito"
                )
        );


        // =====================================================
        // DESCUENTOS
        // =====================================================

        cbDescuento.setItems(
                FXCollections.observableArrayList(
                        "0%",
                        "5%",
                        "10%",
                        "15%",
                        "20%",
                        "25%",
                        "30%",
                        "40%",
                        "50%"
                )
        );


        // El descuento inicial es 0%.
        cbDescuento.setValue("0%");


        /*
         * El ComboBox es editable.
         *
         * Por eso el vendedor también puede escribir
         * manualmente un porcentaje.
         *
         * Ejemplo:
         *
         * 7%
         * 12%
         * 35%
         */

        cbDescuento.getEditor()
                .setPromptText("Ej: 10%");


        /*
         * Cuando el vendedor cambia el descuento
         * seleccionándolo de la lista, actualizamos
         * el total.
         */

        cbDescuento.setOnAction(
                e -> actualizarTotal()
        );


        /*
         * Cuando el vendedor escribe manualmente
         * un porcentaje, también actualizamos el total.
         */

        cbDescuento.getEditor()
                .textProperty()
                .addListener(
                        (observable,
                         textoAnterior,
                         textoNuevo) -> {

                            actualizarTotal();
                        }
                );


        // =====================================================
        // CONFIGURACIÓN DE TABLA
        // =====================================================

        tablaItems.setItems(items);

        tablaItems.setEditable(true);


        // =====================================================
        // CONFIGURACIÓN DE COLUMNAS
        // =====================================================

        colPrecioUnitario.setCellValueFactory(
                new PropertyValueFactory<>(
                        "precioUnitario"
                )
        );


        colCantidad.setCellValueFactory(
                new PropertyValueFactory<>(
                        "cantidad"
                )
        );


        colSubtotal.setCellValueFactory(
                new PropertyValueFactory<>(
                        "subtotal"
                )
        );


        // =====================================================
        // CONFIGURAR COLUMNAS
        // =====================================================

        configurarColumnaProducto();

        configurarColumnaCantidad();

        configurarColumnaEliminar();


        // =====================================================
        // CONFIGURAR TOTALES
        // =====================================================

        configurarTotales();


        // =====================================================
        // CARGAR VENDEDORES
        // =====================================================

        cargarVendedores();


        // =====================================================
        // CARGAR STOCK
        // =====================================================

        cargarStockDisponibleAsync();
        configurarDescuentoPorMedioPago();
    }


    // =========================================================
    // CARGAR STOCK DISPONIBLE
    // =========================================================

    private void cargarStockDisponibleAsync() {

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {

                GestorStock gestorStock =
                        GestorStock.getInstance();


                List<RopaStockDTO> ropa =
                        gestorStock.obtenerStockRopa();


                List<AccsStockDTO> accs =
                        gestorStock.obtenerStockAccs();


                stockDisponible.clear();


                // =================================================
                // ROPA
                // =================================================

                ropa.forEach(dto -> {

                    Producto producto =
                            new Producto(
                                    dto.getDescripcion(),
                                    dto.getTalle(),
                                    dto.getPrecio(),
                                    dto.getColor(),
                                    dto.getMarca(),
                                    dto.getTipoRopa(),
                                    "Ropa"
                            );


                    producto.setId_producto(
                            dto.getIdentificador()
                    );


                    producto.setCodigoProducto(
                            dto.getCodigoProducto()
                    );


                    stockDisponible.put(
                            producto,
                            dto.getCantidad()
                    );
                });


                // =================================================
                // ACCESORIOS
                // =================================================

                accs.forEach(dto -> {

                    Producto producto =
                            new Producto(
                                    dto.getDescripcion(),
                                    dto.getTalle(),
                                    dto.getPrecio(),
                                    dto.getColor(),
                                    dto.getMarca(),
                                    dto.getTipoAccs(),
                                    "Accesorio"
                            );


                    producto.setId_producto(
                            dto.getIdentificador()
                    );


                    producto.setCodigoProducto(
                            dto.getCodigoProducto()
                    );


                    stockDisponible.put(
                            producto,
                            dto.getCantidad()
                    );
                });


                return null;
            }
        };


        task.setOnSucceeded(
                e -> stockCompleto.setAll(

                        stockDisponible.entrySet()
                                .stream()

                                // Solo productos con stock
                                .filter(
                                        entry ->
                                                entry.getValue() > 0
                                )

                                .map(
                                        Map.Entry::getKey
                                )

                                .collect(
                                        Collectors.toList()
                                )
                )
        );


        task.setOnFailed(
                e -> mostrarError(
                        "No se pudo cargar el stock disponible."
                )
        );


        new Thread(task).start();
    }


    // =========================================================
    // NOMBRE DEL PRODUCTO
    // =========================================================

    private String nombreProducto(
            Producto producto
    ) {

        return producto.getMarca()
                + " - "
                + producto.getDescripcion();
    }


    // =========================================================
    // CONFIGURAR COLUMNA PRODUCTO
    // =========================================================

    private void configurarColumnaProducto() {

        colProducto.setCellValueFactory(
                new PropertyValueFactory<>(
                        "producto"
                )
        );


        colProducto.setCellFactory(
                col -> new TableCell<ItemVenta, Producto>() {

                    private final ComboBox<Producto> combo =
                            new ComboBox<>();


                    {

                        combo.getStyleClass()
                                .add("combo-producto");


                        combo.setEditable(true);


                        combo.setMaxWidth(
                                Double.MAX_VALUE
                        );


                        configurarAutocompletado(
                                combo
                        );


                        combo.setOnAction(
                                e -> {

                                    Producto seleccionado =
                                            combo.getValue();


                                    if (
                                            seleccionado == null
                                    ) {

                                        return;
                                    }


                                    ItemVenta fila =
                                            getTableView()
                                                    .getItems()
                                                    .get(getIndex());


                                    // =================================
                                    // EVITAR PRODUCTOS REPETIDOS
                                    // =================================

                                    boolean repetido =
                                            items.stream()

                                                    .filter(
                                                            item ->
                                                                    item != fila
                                                    )

                                                    .anyMatch(
                                                            item ->

                                                                    item.getProducto() != null
                                                                            && item.getProducto()
                                                                            .getId_producto()
                                                                            .equals(
                                                                                    seleccionado
                                                                                            .getId_producto()
                                                                            )
                                                    );


                                    if (repetido) {

                                        mostrarError(
                                                "Ese producto ya fue agregado."
                                        );


                                        combo.setValue(
                                                null
                                        );


                                        return;
                                    }


                                    // =================================
                                    // ASIGNAR PRODUCTO
                                    // =================================

                                    fila.setProducto(
                                            seleccionado
                                    );


                                    actualizarTotal();


                                    tablaItems.refresh();
                                }
                        );
                    }


                    @Override
                    protected void updateItem(
                            Producto producto,
                            boolean empty
                    ) {

                        super.updateItem(
                                producto,
                                empty
                        );


                        if (empty) {

                            setGraphic(
                                    null
                            );

                        } else {

                            combo.setValue(
                                    producto
                            );


                            setGraphic(
                                    combo
                            );
                        }
                    }
                }
        );
    }


    // =========================================================
    // AUTOCOMPLETADO DE PRODUCTOS
    // =========================================================

    private void configurarAutocompletado(
            ComboBox<Producto> combo
    ) {

        combo.setItems(
                stockCompleto
        );


        combo.setConverter(
                new StringConverter<Producto>() {

                    @Override
                    public String toString(
                            Producto producto
                    ) {

                        return producto == null
                                ? ""
                                : nombreProducto(
                                        producto
                                );
                    }


                    @Override
                    public Producto fromString(
                            String texto
                    ) {

                        return stockCompleto
                                .stream()

                                .filter(
                                        producto ->

                                                nombreProducto(
                                                        producto
                                                )
                                                        .equalsIgnoreCase(
                                                                texto
                                                        )
                                )

                                .findFirst()

                                .orElse(null);
                    }
                }
        );


        combo.setCellFactory(
                lv -> new ListCell<Producto>() {

                    @Override
                    protected void updateItem(
                            Producto producto,
                            boolean empty
                    ) {

                        super.updateItem(
                                producto,
                                empty
                        );


                        if (
                                empty
                                        || producto == null
                        ) {

                            setGraphic(
                                    null
                            );

                            setText(
                                    null
                            );

                        } else {

                            Label nombre =
                                    new Label(
                                            nombreProducto(
                                                    producto
                                            )
                                                    + " (talle "
                                                    + producto.getTalle()
                                                    + ")"
                                    );


                            nombre.setStyle(
                                    "-fx-font-size: 13px; "
                                            + "-fx-text-fill: #1B2A4A;"
                            );


                            int disponible =
                                    stockDisponible.getOrDefault(
                                            producto,
                                            0
                                    );


                            Label detalle =
                                    new Label(
                                            "Stock: "
                                                    + disponible
                                                    + " · $"
                                                    + producto.getPrecio()
                                    );


                            detalle.setStyle(
                                    "-fx-font-size: 11px; "
                                            + "-fx-text-fill: #7C8AA0;"
                            );


                            setGraphic(
                                    new VBox(
                                            nombre,
                                            detalle
                                    )
                            );
                        }
                    }
                }
        );


        combo.getEditor()
                .textProperty()
                .addListener(
                        (
                                observable,
                                textoAnterior,
                                textoNuevo
                        ) -> {

                            if (
                                    textoNuevo == null
                                            || textoNuevo.isEmpty()
                            ) {

                                combo.setItems(
                                        stockCompleto
                                );

                                combo.show();

                                return;
                            }


                            if (
                                    combo.getValue() != null
                                            && nombreProducto(
                                            combo.getValue()
                                    ).equals(
                                            textoNuevo
                                    )
                            ) {

                                return;
                            }


                            List<Producto> filtrados =
                                    stockCompleto
                                            .stream()

                                            .filter(
                                                    producto ->

                                                            nombreProducto(
                                                                    producto
                                                            )
                                                                    .toLowerCase()
                                                                    .contains(
                                                                            textoNuevo
                                                                                    .toLowerCase()
                                                                    )

                                                                    ||

                                                            producto.getTalle()
                                                                    .toLowerCase()
                                                                    .contains(
                                                                            textoNuevo
                                                                                    .toLowerCase()
                                                                    )
                                            )

                                            .collect(
                                                    Collectors.toList()
                                            );


                            combo.setItems(
                                    FXCollections.observableArrayList(
                                            filtrados
                                    )
                            );


                            combo.show();
                        }
                );
    }


    // =========================================================
    // CONFIGURAR COLUMNA CANTIDAD
    // =========================================================

    private void configurarColumnaCantidad() {

        colCantidad.setCellFactory(
                col -> new TableCell<>() {

                    private final Spinner<Integer> spinner =
                            new Spinner<>();


                    {

                        spinner.setEditable(
                                true
                        );


                        spinner.valueProperty()
                                .addListener(
                                        (
                                                observable,
                                                cantidadAnterior,
                                                cantidadNueva
                                        ) -> {

                                            if (
                                                    cantidadNueva == null
                                            ) {

                                                return;
                                            }


                                            ItemVenta item =
                                                    getTableView()
                                                            .getItems()
                                                            .get(getIndex());


                                            if (
                                                    item.getProducto()
                                                            == null
                                            ) {

                                                return;
                                            }


                                            int stock =
                                                    stockDisponible
                                                            .getOrDefault(
                                                                    item.getProducto(),
                                                                    0
                                                            );


                                            // =================================
                                            // CONTROLAR STOCK
                                            // =================================

                                            if (
                                                    cantidadNueva > stock
                                            ) {

                                                spinner
                                                        .getValueFactory()
                                                        .setValue(
                                                                stock
                                                        );


                                                item.setCantidad(
                                                        stock
                                                );

                                            } else {

                                                item.setCantidad(
                                                        cantidadNueva
                                                );
                                            }


                                            actualizarTotal();
                                        }
                                );
                    }


                    @Override
                    protected void updateItem(
                            Integer cantidad,
                            boolean empty
                    ) {

                        super.updateItem(
                                cantidad,
                                empty
                        );


                        if (empty) {

                            setGraphic(
                                    null
                            );

                            return;
                        }


                        ItemVenta item =
                                getTableView()
                                        .getItems()
                                        .get(getIndex());


                        int max = 1;


                        if (
                                item.getProducto() != null
                        ) {

                            max =
                                    stockDisponible
                                            .getOrDefault(
                                                    item.getProducto(),
                                                    1
                                            );
                        }


                        spinner.setValueFactory(
                                new SpinnerValueFactory
                                        .IntegerSpinnerValueFactory(
                                                1,
                                                max,
                                                item.getCantidad()
                                        )
                        );


                        setGraphic(
                                spinner
                        );
                    }
                }
        );
    }


    private void cargarVendedores() {

        List<String> vendedores =
                new ArrayList<>();


        // Dueño fijo
        vendedores.add(
                "Mariano Bertero"
        );


        // Empleados de la base de datos
        List<EmpleadoDTO> empleados =
                GestorEmpleado
                        .getInstance()
                        .obtenerEmpleados();


        for (
                EmpleadoDTO empleado :
                empleados
        ) {

            vendedores.add(
                    empleado.getNombreCompleto()
            );
        }


        cbVendedor.setItems(
                FXCollections.observableArrayList(
                        vendedores
                )
        );
    }



    private void configurarColumnaEliminar() {

        colEliminar.setCellFactory(
                col -> new TableCell<ItemVenta, Void>() {

                    private final Button btn =
                            new Button("✕");


                    {

                        btn.getStyleClass()
                                .add(
                                        "btn-eliminar-fila"
                                );


                        btn.setOnAction(
                                e -> {

                                    ItemVenta fila =
                                            getTableView()
                                                    .getItems()
                                                    .get(getIndex());


                                    items.remove(
                                            fila
                                    );


                                    actualizarTotal();
                                }
                        );
                    }


                    @Override
                    protected void updateItem(
                            Void valor,
                            boolean empty
                    ) {

                        super.updateItem(
                                valor,
                                empty
                        );


                        setGraphic(
                                empty
                                        ? null
                                        : btn
                        );
                    }
                }
        );
    }


    // =========================================================
    // CONFIGURAR TOTALES
    // =========================================================

    private void configurarTotales() {

        lblCantidadItems.textProperty()
                .bind(
                        Bindings.size(
                                items
                        ).asString()
                );


        items.addListener(
                (javafx.collections.ListChangeListener<ItemVenta>)
                        cambio -> actualizarTotal()
        );


        actualizarTotal();
    }


    // =========================================================
    // OBTENER PORCENTAJE DE DESCUENTO
    // =========================================================

    private double obtenerPorcentajeDescuento() {

        String textoDescuento =
                cbDescuento.getEditor()
                        .getText();


        if (
                textoDescuento == null
                        || textoDescuento.trim().isEmpty()
        ) {

            return 0;
        }


        try {

            /*
             * Eliminamos el símbolo %.
             *
             * Ejemplo:
             *
             * "10%" -> "10"
             */

            textoDescuento =
                    textoDescuento
                            .replace(
                                    "%",
                                    ""
                            )
                            .trim();


            double porcentajeDescuento =
                    Double.parseDouble(
                            textoDescuento
                    );


            return porcentajeDescuento;

        } catch (NumberFormatException e) {

            return -1;
        }
    }


    // =========================================================
    // ACTUALIZAR TOTAL
    // =========================================================

    private void actualizarTotal() {

        // =====================================================
        // TOTAL SIN DESCUENTO
        // =====================================================

        double totalSinDescuento =
                items.stream()
                        .mapToDouble(
                                ItemVenta::getSubtotal
                        )
                        .sum();


        // =====================================================
        // OBTENER PORCENTAJE DE DESCUENTO
        // =====================================================

        double porcentajeDescuento =
                obtenerPorcentajeDescuento();


        /*
         * Si el usuario escribió algo inválido,
         * por ejemplo "hola", no aplicamos descuento.
         */

        if (
                porcentajeDescuento < 0
        ) {

            lblDescuentoAplicado.setText(
                    "$0"
            );


            lblTotal.setText(
                    "$"
                            + String.format(
                            "%,.0f",
                            totalSinDescuento
                    )
            );


            return;
        }


        // =====================================================
        // VALIDAR QUE ESTÉ ENTRE 0 Y 100
        // =====================================================

        if (
                porcentajeDescuento > 100
        ) {

            lblDescuentoAplicado.setText(
                    "$0"
            );


            lblTotal.setText(
                    "$"
                            + String.format(
                            "%,.0f",
                            totalSinDescuento
                    )
            );


            return;
        }


        // =====================================================
        // CALCULAR DINERO DEL DESCUENTO
        // =====================================================

        double descuentoAplicado =
                totalSinDescuento
                        * porcentajeDescuento
                        / 100.0;


        // =====================================================
        // CALCULAR TOTAL FINAL
        // =====================================================

        double totalConDescuento =
                totalSinDescuento
                        - descuentoAplicado;


        // =====================================================
        // MOSTRAR DESCUENTO APLICADO
        // =====================================================

        lblDescuentoAplicado.setText(
                "$"
                        + String.format(
                        "%,.0f",
                        descuentoAplicado
                )
        );


        // =====================================================
        // MOSTRAR TOTAL FINAL
        // =====================================================

        lblTotal.setText(
                "$"
                        + String.format(
                        "%,.0f",
                        totalConDescuento
                )
        );
    }


    // =========================================================
    // AGREGAR PRODUCTO
    // =========================================================

    @FXML
    private void onAgregarProducto() {

        items.add(
                new ItemVenta()
        );
    }


    // =========================================================
    // CANCELAR
    // =========================================================

    @FXML
    private void onCancelar() {

        Navegador.cambiarVista(
                "/com/victoria/Interfaces/SceneMenuPrincipal.fxml"
        );
    }



    @FXML
    private void onGuardarVenta() {


        if (
                items.isEmpty()
        ) {

            mostrarError(
                    "Debe agregar al menos un producto."
            );

            return;
        }


        for (
                ItemVenta item :
                items
        ) {

            if (
                    item.getProducto() == null
            ) {

                mostrarError(
                        "Hay productos sin seleccionar."
                );

                return;
            }
        }



        if (
                cbVendedor.getValue() == null
        ) {

            mostrarError(
                    "Seleccione un vendedor."
            );

            return;
        }


        if (cbMedioPago.getValue() == null) { 
            mostrarError("Seleccione un medio de pago.");
            return;
        }
       

        // =====================================================
        // VALIDAR DESCUENTO
        // =====================================================

        double porcentajeDescuento = obtenerPorcentajeDescuento();


        if (
                porcentajeDescuento < 0
                        || porcentajeDescuento > 100
        ) {

            mostrarError(
                    "El descuento debe ser un porcentaje "
                            + "entre 0% y 100%."
            );

            return;
        }


        for (
                ItemVenta item :
                items
        ) {

            int stock =
                    stockDisponible.getOrDefault(
                            item.getProducto(),
                            0
                    );


            if (
                    item.getCantidad() > stock
            ) {

                mostrarError(
                        "No hay stock suficiente para "
                                + nombreProducto(
                                item.getProducto()
                        )
                );

                return;
            }
        }


        Venta venta = new Venta();

        venta.setFecha(dpFecha.getValue());
        venta.setVendedor(cbVendedor.getValue());
        venta.setMedioPago(cbMedioPago.getValue());             
        venta.setDescuentoPorcentaje(porcentajeDescuento);        

        for (ItemVenta item : items) {
        venta.agregarItem(item);
        }


        for (
                ItemVenta item :
                items
        ) {

            venta.agregarItem(
                    item
            );
        }


        GestorVenta
                .getInstance()
                .registrarVenta(
                        venta
                );

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setHeaderText(
                null
        );


        alert.setTitle(
                "Venta"
        );


        alert.setContentText(
                "La venta se registró correctamente."
        );

        alert.showAndWait();
        limpiarFormulario();
    }

    private void mostrarError(
            String mensaje
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setHeaderText(
                null
        );


        alert.setTitle(
                "Error"
        );


        alert.setContentText(
                mensaje
        );


        alert.showAndWait();
    }
    private void configurarDescuentoPorMedioPago() {

    cbMedioPago.valueProperty()
            .addListener(
                    (observable, valorAnterior, valorNuevo) -> {

                        if (valorNuevo == null) {
                            return;
                        }

                        if (valorNuevo.equals("Efectivo")) {

                            cbDescuento.setValue("15%");
                            cbDescuento.getEditor().setText("15%");

                        } else if (valorNuevo.equals("Transferencia")) {

                            cbDescuento.setValue("10%");
                            cbDescuento.getEditor().setText("10%");
                        }

                        actualizarTotal();
                    }
            );}

    private void limpiarFormulario() {

        items.clear();
        actualizarTotal();
        dpFecha.setValue(LocalDate.now());

        cbVendedor
                .getSelectionModel()
                .clearSelection();

        cbMedioPago
                .getSelectionModel()
                .clearSelection();

        cbDescuento.setValue("0%");

        cbDescuento.getEditor()
                .setText( "0%");


        lblDescuentoAplicado.setText("$0");
        lblTotal.setText("$0");

        cargarStockDisponibleAsync();
    }
}