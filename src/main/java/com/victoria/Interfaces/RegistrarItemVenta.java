package com.victoria.Interfaces;

import com.victoria.Clases.ItemVenta;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Venta;
import com.victoria.Dto.RopaStockDTO;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Gestores.GestorStock;
import com.victoria.Gestores.GestorVenta;
import com.victoria.navegation.Navegador;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrarItemVenta{

    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbVendedor;
    @FXML private TableView<ItemVenta> tablaItems;
    @FXML private TableColumn<ItemVenta, Producto> colProducto;
    @FXML private TableColumn<ItemVenta, Integer> colCantidad;
    @FXML private TableColumn<ItemVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<ItemVenta, Double> colSubtotal;
    
    @FXML private TableColumn<ItemVenta, Void> colEliminar;
    

    @FXML private Label lblCantidadItems;
    @FXML private Label lblTotal;

    private final ObservableList<ItemVenta> items = FXCollections.observableArrayList();

    // Producto -> cantidad disponible, para validar y mostrar en el combo
    private final java.util.Map<Producto, Integer> stockDisponible = new java.util.HashMap<>();
    private ObservableList<Producto> stockCompleto = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colSubtotal.setCellFactory(col -> new TableCell<>() {

    @Override
    protected void updateItem(Double subtotal, boolean empty) {

        super.updateItem(subtotal, empty);

        if(empty || subtotal==null){

            setText(null);

        }else{

            setText("$ " + String.format("%,.0f", subtotal));

         }

        }

    });
    colPrecioUnitario.setCellFactory(col -> new TableCell<>() {
    @Override
    protected void updateItem(Double precio, boolean empty) {

        super.updateItem(precio, empty);

        if(empty || precio==null){

            setText(null);

        }else{

            setText("$ " + String.format("%,.0f", precio));

            }

        }

    });
        dpFecha.setValue(LocalDate.now());
        cbVendedor.setItems(FXCollections.observableArrayList("Victoria", "Otro vendedor"));

        cargarStockDisponible();

        tablaItems.setItems(items);
        tablaItems.setEditable(true);

        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        configurarColumnaProducto();
        configurarColumnaCantidad();
        configurarColumnaEliminar();
        configurarTotales();
        
    }

    /**
     * Trae el stock de ropa y accesorios vía GestorStock y arma, a partir de los
     * campos sueltos del DTO, los Producto correspondientes (el DTO no envuelve
     * un Producto, viene "aplanado" desde la consulta SQL).
     *
     * OJO: para RopaStockDTO asumí un campo "tipoRopa" análogo a "tipoAccs" de
     * AccsStockDTO -- si el getter se llama distinto, avisame y cambio esa línea.
     */
    private void cargarStockDisponible() {
        GestorStock gestorStock = GestorStock.getInstance();

        List<RopaStockDTO> ropa = gestorStock.obtenerStockRopa();
        List<AccsStockDTO> accs = gestorStock.obtenerStockAccs();

        ropa.forEach(dto -> {
            Producto p = new Producto(dto.getDescripcion(), dto.getTalle(), dto.getPrecio(),
                    dto.getColor(), dto.getMarca(), dto.getTipoRopa(), "Ropa");
            p.setId_producto(dto.getIdentificador());
            p.setCodigoProducto(dto.getCodigoProducto());
            stockDisponible.put(p, dto.getCantidad());
        });

        accs.forEach(dto -> {
            Producto p = new Producto(dto.getDescripcion(), dto.getTalle(), dto.getPrecio(),
                    dto.getColor(), dto.getMarca(), dto.getTipoAccs(), "Accesorio");
            p.setId_producto(dto.getIdentificador());
            p.setCodigoProducto(dto.getCodigoProducto());
            stockDisponible.put(p, dto.getCantidad());
        });

        // Solo se puede vender lo que tiene cantidad disponible > 0
        stockCompleto.setAll(
                stockDisponible.entrySet().stream()
                        .filter(e -> e.getValue() > 0)
                        .map(java.util.Map.Entry::getKey)
                        .collect(Collectors.toList())
        );
    }

    private String nombreProducto(Producto p) {
        // Producto no tiene "nombre": lo armamos con marca + tipo + descripción
        return p.getMarca() + " - " + p.getDescripcion();
    }

    private void configurarColumnaProducto() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));

        colProducto.setCellFactory(col -> new TableCell<ItemVenta, Producto>() {
            private final ComboBox<Producto> combo = new ComboBox<>();

            {
                combo.getStyleClass().add("combo-producto");
                combo.setEditable(true);
                combo.setMaxWidth(Double.MAX_VALUE);
                configurarAutocompletado(combo);

                combo.setOnAction(e -> {
                Producto seleccionado = combo.getValue();
                ItemVenta fila = getTableView().getItems().get(getIndex());
                boolean repetido = items.stream()

                        .filter(i -> i != fila)

                        .anyMatch(i -> i.getProducto()!=null
                                && i.getProducto().getId_producto()
                                .equals(seleccionado.getId_producto()));

                if(repetido){

                    mostrarError("Ese producto ya fue agregado.");

                    combo.setValue(null);

                    return;

                }
                    fila.setProducto(seleccionado);
                    actualizarTotal();
                });
            }

            @Override
            protected void updateItem(Producto producto, boolean empty) {
                super.updateItem(producto, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    combo.setValue(producto);
                    setGraphic(combo);
                }
            }
        });
    }

    private void configurarAutocompletado(ComboBox<Producto> combo) {
        combo.setItems(stockCompleto);

        combo.setConverter(new StringConverter<Producto>() {
            @Override
            public String toString(Producto p) {
                return p == null ? "" : nombreProducto(p);
            }

            @Override
            public Producto fromString(String texto) {
                return stockCompleto.stream()
                        .filter(p -> nombreProducto(p).equalsIgnoreCase(texto))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Cada opción del dropdown: marca/descripción + stock disponible + precio
        combo.setCellFactory(lv -> new ListCell<Producto>() {
            @Override
            protected void updateItem(Producto p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label nombre = new Label(nombreProducto(p) + " (talle " + p.getTalle() + ")");
                    nombre.setStyle("-fx-font-size: 13px; -fx-text-fill: #1B2A4A;");
                    int disponible = stockDisponible.getOrDefault(p, 0);
                    Label detalle = new Label("Stock: " + disponible + " · $" + p.getPrecio());
                    detalle.setStyle("-fx-font-size: 11px; -fx-text-fill: #7C8AA0;");
                    setGraphic(new VBox(nombre, detalle));
                }
            }
        });

        combo.getEditor().textProperty().addListener((obs, textoAnterior, textoNuevo) -> {
            if (combo.getValue() != null && nombreProducto(combo.getValue()).equals(textoNuevo)) {
                return; // evita refiltrar apenas se selecciona un producto
            }
            List<Producto> filtrados = stockCompleto.stream()
                    .filter(p -> nombreProducto(p).toLowerCase().contains(textoNuevo.toLowerCase())
                              || p.getTalle().toLowerCase().contains(textoNuevo.toLowerCase()))
                    .collect(Collectors.toList());
            combo.setItems(FXCollections.observableArrayList(filtrados));
            combo.show();
        });
    }
    private void configurarColumnaCantidad() {

    colCantidad.setCellFactory(col -> new TableCell<>() {

        private final Spinner<Integer> spinner = new Spinner<>();

        {
            spinner.setEditable(true);

            spinner.valueProperty().addListener((obs, anterior, nuevo) -> {

                ItemVenta item = getTableView().getItems().get(getIndex());

                if (item.getProducto() == null)
                    return;

                int stock = stockDisponible.getOrDefault(item.getProducto(), 0);

                if (nuevo > stock) {
                    spinner.getValueFactory().setValue(stock);
                    item.setCantidad(stock);
                } else {
                    item.setCantidad(nuevo);
                    actualizarTotal();
                }

                tablaItems.refresh();
            });
        }

        @Override
        protected void updateItem(Integer cantidad, boolean empty) {

            super.updateItem(cantidad, empty);

            if (empty) {
                setGraphic(null);
                return;
            }

            ItemVenta item = getTableView().getItems().get(getIndex());

            int max = 1;

            if (item.getProducto() != null) {
                max = stockDisponible.getOrDefault(item.getProducto(), 1);
            }

            spinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(
                            1,
                            max,
                            item.getCantidad()
                    ));

            setGraphic(spinner);
            }
        });

    }

    private void configurarColumnaEliminar() {
        colEliminar.setCellFactory(col -> new TableCell<ItemVenta, Void>() {
            private final Button btn = new Button("✕");

            {
                btn.getStyleClass().add("btn-eliminar-fila");
                btn.setOnAction(e -> {
                    ItemVenta fila = getTableView().getItems().get(getIndex());
                    items.remove(fila);
                });
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

   private void configurarTotales() {

        lblCantidadItems.textProperty().bind(Bindings.size(items).asString());

        items.addListener((javafx.collections.ListChangeListener<ItemVenta>) c -> actualizarTotal());

        actualizarTotal();
    }
private void actualizarTotal() {

    double total = items.stream()
            .mapToDouble(ItemVenta::getSubtotal)
            .sum();

        lblTotal.setText("$" + String.format("%,.0f", total));

    }

    @FXML
    private void onAgregarProducto() {
        items.add(new ItemVenta());
    }

    @FXML
    private void onCancelar() {
    Navegador.cambiarVista("/com/victoria/Interfaces/MenuPrincipal.fxml");
    }

   @FXML
private void onGuardarVenta() {

    if (items.isEmpty()) {
        mostrarError("Debe agregar al menos un producto.");
        return;
    }

    if (cbVendedor.getValue() == null) {
        mostrarError("Seleccione un vendedor.");
        return;
    }

    Venta venta = new Venta();
    venta.setFecha(dpFecha.getValue());
    venta.setVendedor(cbVendedor.getValue());

    for (ItemVenta item : items) {

        if (item.getProducto() == null) {
            mostrarError("Hay productos sin seleccionar.");
            return;
        }

        int stock = stockDisponible.getOrDefault(item.getProducto(), 0);

        if (item.getCantidad() > stock) {
            mostrarError(
                    "No hay stock suficiente para "
                            + nombreProducto(item.getProducto()));
            return;
        }

        venta.agregarItem(item);
    }
    
        GestorVenta.getInstance().registrarVenta(venta);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Venta");
        alert.setContentText("La venta se registró correctamente.");
        alert.showAndWait();

        limpiarFormulario();
    }
    private void mostrarError(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();

    }
    private void limpiarFormulario() {
        tablaItems.refresh();

        actualizarTotal();

        items.clear();

        dpFecha.setValue(LocalDate.now());

        cbVendedor.getSelectionModel().clearSelection();

        lblTotal.setText("$0");
    }
}
