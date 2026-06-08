package com.victoria.Interfaces;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.victoria.Clases.Producto;
import com.victoria.Dao.DaoProductoImp;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;
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

public class ModificarProducto {

    @FXML private ComboBox<String> cbTipoProducto;
    @FXML private ComboBox<String> cbTipoRopa;
    @FXML private ComboBox<String> cbTipoAccesorio;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTalle;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtColor;
    @FXML private TextField txtMarca;
    @FXML private Spinner<Integer> spCantidad;
    @FXML private ImageView imgPreview;
    @FXML private StackPane placeholderFoto;
    @FXML private Label lblSinImagen;
    @FXML private Label lblNombreFoto;
    @FXML private Button btnSeleccionarFoto;

    private GestorStock gestorStock = new GestorStock();
    private GestorProducto gestorProducto = new GestorProducto();
    private DaoProductoImp daoProducto = new DaoProductoImp();

    // null = no cambió la foto, se mantiene la de la BD
    private byte[] fotoNueva = null;

    Object producto = Navegador.getDato();

    @FXML
    public void initialize() {

        cbTipoRopa.getItems().addAll(
            "Remeras", "Musculosas", "Deportivo", "Pantalones",
            "Buzos", "Pulovers", "Chombas", "Boxer",
            "Campera", "Malla", "Bermuda", "Camisa", "Zapatillas");

        cbTipoAccesorio.getItems().addAll(
            "Gorra", "Gorro", "Medias", "Lentes",
            "Cinto", "Mochila", "Pulsera", "Collar");

        spCantidad.setEditable(true);

        if (producto instanceof RopaStockDTO ropa) {
            cbTipoProducto.setValue("ROPA");
            cbTipoRopa.setValue(ropa.getTipoRopa());
            txtDescripcion.setText(ropa.getDescripcion());
            txtTalle.setText(ropa.getTalle());
            txtPrecio.setText(String.valueOf(ropa.getPrecio()));
            txtColor.setText(ropa.getColor());
            txtMarca.setText(ropa.getMarca());
            spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, ropa.getCantidad()));
            cbTipoAccesorio.setDisable(true);

            // Cargar foto existente desde la BD
            cargarFotoExistente(ropa.getIdentificador());

        } else if (producto instanceof AccsStockDTO accs) {
            cbTipoProducto.setValue("ACCESORIO");
            cbTipoAccesorio.setValue(accs.getTipoAccs());
            txtDescripcion.setText(accs.getDescripcion());
            txtTalle.setText(accs.getTalle());
            txtPrecio.setText(String.valueOf(accs.getPrecio()));
            txtColor.setText(accs.getColor());
            txtMarca.setText(accs.getMarca());
            spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, accs.getCantidad()));
            cbTipoRopa.setDisable(true);

            // Cargar foto existente desde la BD
            cargarFotoExistente(accs.getIdentificador());
        }

        // Solo números en precio
        txtPrecio.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.,]*"))
                txtPrecio.setText(newValue.replaceAll("[^0-9.,]", ""));
        });

        // Talle en mayúscula
        txtTalle.textProperty().addListener((obs, oldValue, newValue) ->
            txtTalle.setText(newValue.toUpperCase()));

        // Color en mayúscula
        txtColor.textProperty().addListener((obs, oldValue, newValue) ->
            txtColor.setText(newValue.toUpperCase()));

        // Marca capitalizada
        txtMarca.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String capitalizado = capitalizar(newValue);
                if (!newValue.equals(capitalizado))
                    txtMarca.setText(capitalizado);
            }
        });
    }

    // Recupera la foto guardada en la BD y la muestra en el preview
    private void cargarFotoExistente(Integer idProducto) {
        byte[] foto = daoProducto.obtenerFoto(idProducto);
        if (foto != null && foto.length > 0) {
            Image imagen = new Image(new ByteArrayInputStream(foto));
            imgPreview.setImage(imagen);
            imgPreview.setVisible(true);
            lblSinImagen.setVisible(false);
            lblNombreFoto.setText("Foto actual");
        }
    }

    @FXML
    private void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar nueva foto del producto");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp")
        );

        Stage stage = (Stage) btnSeleccionarFoto.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            try {
                fotoNueva = Files.readAllBytes(archivo.toPath());

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
    private void guardarCambios() {
        try {
            if (producto instanceof RopaStockDTO ropa) {

                ropa.setDescripcion(txtDescripcion.getText());
                ropa.setTalle(txtTalle.getText());
                ropa.setPrecio(Double.parseDouble(txtPrecio.getText()));
                ropa.setColor(txtColor.getText());
                ropa.setMarca(txtMarca.getText());
                ropa.setTipoRopa(cbTipoRopa.getValue());
                ropa.setCantidad(spCantidad.getValue());

                Producto prod = new Producto();
                prod.setId_producto(ropa.getIdentificador());
                prod.setDescripcion(ropa.getDescripcion());
                prod.setTalle(ropa.getTalle());
                prod.setPrecio(ropa.getPrecio());
                prod.setColor(ropa.getColor());
                prod.setMarca(ropa.getMarca());
                prod.setTipo("Ropa");
                prod.setTipoProducto(ropa.getTipoRopa());
                // Si eligió foto nueva la seteamos, sino queda null
                // y el DAO no la sobreescribe
                prod.setFoto(fotoNueva);

                gestorProducto.actualizarProducto(prod);
                gestorStock.actualizarRopa(ropa.getIdentificador(), ropa.getCantidad());
                System.out.println("Ropa actualizada correctamente.");

            } else if (producto instanceof AccsStockDTO accs) {

                accs.setDescripcion(txtDescripcion.getText());
                accs.setTalle(txtTalle.getText());
                accs.setPrecio(Double.parseDouble(txtPrecio.getText()));
                accs.setColor(txtColor.getText());
                accs.setMarca(txtMarca.getText());
                accs.setTipoAccs(cbTipoAccesorio.getValue());
                accs.setCantidad(spCantidad.getValue());

                Producto prod = new Producto();
                prod.setId_producto(accs.getIdentificador());
                prod.setDescripcion(accs.getDescripcion());
                prod.setTalle(accs.getTalle());
                prod.setPrecio(accs.getPrecio());
                prod.setColor(accs.getColor());
                prod.setMarca(accs.getMarca());
                prod.setTipo("Accesorio");
                prod.setTipoProducto(accs.getTipoAccs());
                prod.setFoto(fotoNueva);

                gestorProducto.actualizarProducto(prod);
                gestorStock.actualizarAccs(accs.getIdentificador(), accs.getCantidad());
                System.out.println("Accesorio actualizado correctamente.");
            }

            Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void volverMenuPrincipal() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
}
