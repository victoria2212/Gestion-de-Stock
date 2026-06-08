package com.victoria.Interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import com.victoria.Clases.Accesorio;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Ropa;
import com.victoria.Clases.Stock;
import com.victoria.Gestores.GestorProducto;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

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

    private byte[] fotoBytes = null; // ← guardamos los bytes, no el File

    @FXML
    public void initialize() {

        cbTipoProducto.getItems().addAll("Ropa", "Accesorio");

        cbTipoRopa.getItems().addAll(
            "Remeras", "Musculosas", "Deportivo", "Pantalones", "Buzos",
            "Pulovers", "Chombas", "Boxer", "Campera", "Malla",
            "Bermuda", "Camisa", "Zapatillas"
        );

        cbTipoAccesorio.getItems().addAll(
            "Gorra", "Gorro", "Medias", "Lentes", "Cinto", "Mochila", "Pulsera", "Collar"
        );

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

        txtMarca.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String capitalizado = capitalizar(newValue);
                if (!newValue.equals(capitalizado))
                    txtMarca.setText(capitalizado);
            }
        });

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
            } else if ("Accesorio".equals(seleccion)) {
                cbTipoRopa.setDisable(true);
                cbTipoAccesorio.setDisable(false);
                cbTipoRopa.setValue(null);
            }
        });

        guardarButton.setOnAction(e -> guardarProducto());
    }

    @FXML
    private void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto del producto");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp")
        );

        Stage stage = (Stage) btnSeleccionarFoto.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            try {
                // Convertir el archivo a bytes para guardar en la BD
                fotoBytes = Files.readAllBytes(archivo.toPath());

                // Mostrar preview en la UI
                Image imagen = new Image(archivo.toURI().toString());
                imgPreview.setImage(imagen);
                imgPreview.setVisible(true);
                lblSinImagen.setVisible(false);

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
        if ("Ropa".equals(tipoProducto) && cbTipoRopa.getValue() == null) {
            mostrarAlerta("Seleccioná un tipo de ropa.");
            return;
        }
        if ("Accesorio".equals(tipoProducto) && cbTipoAccesorio.getValue() == null) {
            mostrarAlerta("Seleccioná un tipo de accesorio.");
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
            String tipoRopa = cbTipoRopa.getValue();
            Integer id_existe = gestorProducto.existeProducto(descripcion, marca, color, talle, "Ropa", tipoRopa);
            if (id_existe != null) {
                mostrarAlerta("El producto ya existe en el Sistema.");
            } else {
                Ropa ropa = new Ropa(descripcion, talle, precio, color, marca, tipoRopa);
                ropa.setFoto(fotoBytes); // ← asignamos los bytes
                producto = ropa;
                id = gestorProducto.altaProducto(producto);
                gestorStock.registrarStock(id, cantidad, LocalDateTime.now());
                mostrarAlerta("Producto guardado correctamente.");
            }
        } else if ("Accesorio".equals(tipoProducto)) {
            String tipoAccesorio = cbTipoAccesorio.getValue();
            Integer id_existe = gestorProducto.existeProducto(descripcion, marca, color, talle, "Accesorio", tipoAccesorio);
            if (id_existe != null) {
                mostrarAlerta("El producto ya existe en el Sistema.");
            } else {
                Accesorio accs = new Accesorio(descripcion, talle, precio, color, marca, tipoAccesorio);
                accs.setFoto(fotoBytes); // ← asignamos los bytes
                producto = accs;
                id = gestorProducto.altaProducto(producto);
                gestorStock.registrarStock(id, cantidad, LocalDateTime.now());
                mostrarAlerta("Producto guardado correctamente.");
            }
        }

        limpiarFormulario();
    }

    private void limpiarFormulario() {
        cbTipoProducto.setValue(null);
        cbTipoRopa.setValue(null);
        cbTipoAccesorio.setValue(null);
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

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}
