package com.victoria.Interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

import com.victoria.Clases.Accesorio;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Ropa;
import com.victoria.Clases.Stock;
import com.victoria.Gestores.GestorProducto;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AltaProducto {

    public GestorProducto gestorProducto = GestorProducto.getInstance();
    public GestorStock gestorStock = GestorStock.getInstance();
    Stock stock;

    @FXML private ComboBox<String> cbTipoProducto;
    @FXML private ComboBox<String> cbTipoRopa;
    @FXML private ComboBox<String> cbTipoAccesorio;
    @FXML private Button guardarButton;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTalle;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtColor;
    @FXML private TextField txtMarca;
    @FXML private Spinner<Integer> spCantidad;
    @FXML private Button btnVolver;
    @FXML private ImageView imgPreview;
    @FXML private StackPane placeholderFoto;
    @FXML private Button btnSeleccionarFoto;
    @FXML private Label lblNombreFoto;
    @FXML private Label lblSinImagen;

    private byte[] fotoBytes = null;

    // Listas completas (sugerencias base + lo ya cargado en la BD)
    private final ObservableList<String> tiposRopaCompleto = FXCollections.observableArrayList();
    private final ObservableList<String> tiposAccesorioCompleto = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        cbTipoProducto.getItems().addAll("Ropa", "Accesorio");

        cargarTiposRopa();
        cargarTiposAccesorio();

        configurarComboEditable(cbTipoRopa, tiposRopaCompleto);
        configurarComboEditable(cbTipoAccesorio, tiposAccesorioCompleto);

        SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0);
        spCantidad.setValueFactory(valueFactory);
        spCantidad.setEditable(true);

        txtPrecio.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.,]*"))
                txtPrecio.setText(newValue.replaceAll("[^0-9.,]", ""));
        });

        txtTalle.textProperty().addListener((obs, oldValue, newValue) ->
            txtTalle.setText(newValue.toUpperCase()));

        txtColor.textProperty().addListener((obs, oldValue, newValue) ->
            txtColor.setText(newValue.toUpperCase()));

        txtMarca.textProperty().addListener((obs, oldValue, newValue) ->
            txtMarca.setText(newValue.toUpperCase()));

        spCantidad.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                spCantidad.getValueFactory().setValue(0);
            } else {
                try {
                    int valor = Integer.parseInt(newValue);
                    if (valor >= 0 && valor <= 1000)
                        spCantidad.getValueFactory().setValue(valor);
                    else
                        spCantidad.getEditor().setText(oldValue);
                } catch (NumberFormatException e) {
                    spCantidad.getEditor().setText(oldValue);
                }
            }
        });

        cbTipoRopa.setDisable(true);
        cbTipoAccesorio.setDisable(true);

        cbTipoProducto.setOnAction(e -> {
            String seleccion = cbTipoProducto.getValue();
            if ("Ropa".equals(seleccion)) {
                cbTipoRopa.setDisable(false);
                cbTipoAccesorio.setDisable(true);
                cbTipoAccesorio.setValue(null);
                cbTipoAccesorio.getEditor().clear();
            } else if ("Accesorio".equals(seleccion)) {
                cbTipoRopa.setDisable(true);
                cbTipoAccesorio.setDisable(false);
                cbTipoRopa.setValue(null);
                cbTipoRopa.getEditor().clear();
            }
        });

        guardarButton.setOnAction(e -> guardarProducto());
    }

    // =========================================================
    // CARGAR TIPOS DE ROPA (sugerencias base + ya cargados en BD)
    // =========================================================

    private void cargarTiposRopa() {

        LinkedHashSet<String> combinados = new LinkedHashSet<>();

        combinados.addAll(List.of(
            "Remeras", "Musculosas", "Deportivo", "Pantalones", "Buzos",
            "Pulovers", "Chombas", "Boxer", "Campera", "Malla",
            "Bermuda", "Camisa", "Zapatillas"
        ));

        combinados.addAll(gestorProducto.obtenerTiposExistentes("Ropa"));

        tiposRopaCompleto.setAll(combinados);
    }

    // =========================================================
    // CARGAR TIPOS DE ACCESORIO (sugerencias base + ya cargados en BD)
    // =========================================================

    private void cargarTiposAccesorio() {

        LinkedHashSet<String> combinados = new LinkedHashSet<>();

        combinados.addAll(List.of(
            "Gorra", "Gorro", "Medias", "Lentes", "Cinto", "Mochila", "Pulsera", "Collar"
        ));

        combinados.addAll(gestorProducto.obtenerTiposExistentes("Accesorio"));

        tiposAccesorioCompleto.setAll(combinados);
    }

    // =========================================================
    // CONFIGURAR COMBO EDITABLE CON AUTOCOMPLETADO
    // =========================================================

    private void configurarComboEditable(ComboBox<String> combo, ObservableList<String> opcionesCompletas) {

        combo.setEditable(true);
        combo.setItems(opcionesCompletas);

        combo.getEditor().textProperty().addListener((observable, textoAnterior, textoNuevo) -> {

            if (!combo.isFocused()) {
                return;
            }

            if (textoNuevo == null || textoNuevo.isEmpty()) {
                combo.setItems(opcionesCompletas);
                combo.show();
                return;
            }

            List<String> filtrados = opcionesCompletas.stream()
                    .filter(tipo -> tipo.toLowerCase().contains(textoNuevo.toLowerCase()))
                    .toList();

            combo.setItems(FXCollections.observableArrayList(filtrados));
            combo.show();
        });
    }

    // =========================================================
    // OBTENER EL VALOR ESCRITO/SELECCIONADO DE UN COMBO EDITABLE
    // =========================================================

    private String obtenerValorCombo(ComboBox<String> combo) {

        String texto = combo.getEditor().getText();

        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        return texto.trim();
    }

    @FXML
    private void seleccionarFoto() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto del producto");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) btnSeleccionarFoto.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            try {
                fotoBytes = Files.readAllBytes(archivo.toPath());

                Image imagen = new Image(archivo.toURI().toString());

                imgPreview.setImage(imagen);
                imgPreview.setVisible(true);
                imgPreview.setManaged(true);
                imgPreview.toFront();

                lblSinImagen.setVisible(false);
                lblSinImagen.setManaged(false);

                String nombre = archivo.getName();
                lblNombreFoto.setText(nombre.length() > 22 ? nombre.substring(0, 20) + "..." : nombre);

            } catch (IOException ex) {
                mostrarAlerta("No se pudo leer la imagen seleccionada.");
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void guardarProducto() {
        Producto producto;
        Integer id;
        String tipoProducto = cbTipoProducto.getValue();
        String descripcion = txtDescripcion.getText().trim();
        String talle = txtTalle.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String color = txtColor.getText().trim();
        String marca = txtMarca.getText().trim();
        int cantidad = spCantidad.getValue();

        if (tipoProducto == null || descripcion.isEmpty() || talle.isEmpty() ||
            precioStr.isEmpty() || color.isEmpty() || marca.isEmpty()) {
            mostrarAlerta("Por favor, completá todos los campos obligatorios.");
            return;
        }

        String tipoRopa = obtenerValorCombo(cbTipoRopa);
        String tipoAccesorio = obtenerValorCombo(cbTipoAccesorio);

        if ("Ropa".equals(tipoProducto) && tipoRopa == null) {
            mostrarAlerta("Seleccioná o escribí un tipo de ropa.");
            return;
        }
        if ("Accesorio".equals(tipoProducto) && tipoAccesorio == null) {
            mostrarAlerta("Seleccioná o escribí un tipo de accesorio.");
            return;
        }

        double precio;
        try {
            precio = parsePrecio(precioStr);
        } catch (NumberFormatException ex) {
            mostrarAlerta("El precio debe ser un número válido.");
            return;
        }

        if ("Ropa".equals(tipoProducto)) {
            Integer id_existe = gestorProducto.existeProducto(descripcion, marca, color, talle, "Ropa", tipoRopa);
            if (id_existe != null) {
                mostrarAlerta("El producto ya existe en el Sistema.");
            } else {
                Ropa ropa = new Ropa(descripcion, talle, precio, color, marca, tipoRopa);
                ropa.setFoto(fotoBytes);
                producto = ropa;
                id = gestorProducto.altaProducto(producto);
                gestorStock.registrarStock(id, cantidad, LocalDateTime.now());
                mostrarAlerta("Producto guardado correctamente.");
            }
        } else if ("Accesorio".equals(tipoProducto)) {
            Integer id_existe = gestorProducto.existeProducto(descripcion, marca, color, talle, "Accesorio", tipoAccesorio);
            if (id_existe != null) {
                mostrarAlerta("El producto ya existe en el Sistema.");
            } else {
                Accesorio accs = new Accesorio(descripcion, talle, precio, color, marca, tipoAccesorio);
                accs.setFoto(fotoBytes);
                producto = accs;
                id = gestorProducto.altaProducto(producto);
                gestorStock.registrarStock(id, cantidad, LocalDateTime.now());
                mostrarAlerta("Producto guardado correctamente.");
            }
        }

        // Refrescar las listas para que el nuevo tipo (si era nuevo) quede disponible la próxima vez
        cargarTiposRopa();
        cargarTiposAccesorio();

        limpiarFormulario();
    }

    private void limpiarFormulario() {
        cbTipoProducto.setValue(null);
        cbTipoRopa.setValue(null);
        cbTipoRopa.getEditor().clear();
        cbTipoAccesorio.setValue(null);
        cbTipoAccesorio.getEditor().clear();
        cbTipoRopa.setDisable(true);
        cbTipoAccesorio.setDisable(true);
        txtDescripcion.clear();
        txtTalle.clear();
        txtPrecio.clear();
        txtColor.clear();
        txtMarca.clear();
        spCantidad.getValueFactory().setValue(0);
        fotoBytes = null;
        imgPreview.setImage(null);
        imgPreview.setVisible(false);
        lblSinImagen.setVisible(true);
        lblNombreFoto.setText("");
    }

    @FXML
    private void volverMenuPrincipal() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }

    private double parsePrecio(String precioStr) throws NumberFormatException {
        if (precioStr == null || precioStr.trim().isEmpty())
            throw new NumberFormatException("Precio vacío");
        precioStr = precioStr.replace(".", "").replace(",", ".");
        return Double.parseDouble(precioStr);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}