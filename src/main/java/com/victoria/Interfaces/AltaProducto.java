package com.victoria.Interfaces;
import java.io.IOException;
import java.time.LocalDateTime;

import com.victoria.Clases.Accesorio;
import com.victoria.Clases.Producto;
import com.victoria.Clases.Ropa;
import com.victoria.Clases.Stock;
import com.victoria.Gestores.GestorProducto;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AltaProducto {

    public GestorProducto gestorProducto = GestorProducto.getInstance();
    public GestorStock gestorStock = GestorStock.getInstance();
    Stock stock;

    @FXML private ComboBox<String> cbTipoProducto;
    @FXML private ComboBox<String> cbTipoRopa;
    @FXML private ComboBox<String> cbTipoAccesorio;
    @FXML private TextField txtNuevoTipoRopa;
    @FXML private TextField txtNuevoTipoAccesorio;
    @FXML private Button btnAddTipoRopa;
    @FXML private Button btnAddTipoAccesorio;
    @FXML private Button guardarButton;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTalle;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtColor;
    @FXML private TextField txtMarca;
    @FXML private Spinner<Integer> spCantidad;
    @FXML private Button btnVolver;

    @FXML
    public void initialize() {

        // Opciones iniciales
        cbTipoProducto.getItems().addAll("Ropa", "Accesorio");

        cbTipoRopa.getItems().addAll(
            "Remeras", "Musculosas", "Deportivo", "Pantalones", "Buzos",
            "Pulovers", "Chombas", "Boxer", "Campera", "Malla",
            "Bermuda", "Camisa", "Zapatillas"
        );

        cbTipoAccesorio.getItems().addAll(
            "Gorras", "Gorros", "Medias", "Lentes", "Cintos", "Mochilas"
        );
        // Cantidad de Producto -> lo usamos para el stock
        // Crear el ValueFactory con rango y valor inicial
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0);
        spCantidad.setValueFactory(valueFactory);
        // Permitir ingreso manual por teclado
        spCantidad.setEditable(true);

        // Agregar un listener para validar el texto ingresado y actualizar el valor del Spinner
           spCantidad.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Si el campo está vacío, ponemos valor mínimo (0)
                spCantidad.getValueFactory().setValue(0);
            } else {
                try {
                    int valor = Integer.parseInt(newValue);
                    if (valor >= 0 && valor <= 1000) {
                        spCantidad.getValueFactory().setValue(valor);
                    } else {
                        // Si fuera de rango, revertimos
                        spCantidad.getEditor().setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    // Si no es número válido, revertimos
                    spCantidad.getEditor().setText(oldValue);
                }
            }
        });

        // Deshabilitar combos al inicio
        cbTipoRopa.setDisable(true);
        cbTipoAccesorio.setDisable(true);

        // Lógica de habilitación según tipo de producto
        cbTipoProducto.setOnAction(e -> {
            String seleccion = cbTipoProducto.getValue();
            if ("Ropa".equals(seleccion)) {
                cbTipoRopa.setDisable(false);
                cbTipoAccesorio.setDisable(true);
            } else if ("Accesorio".equals(seleccion)) {
                cbTipoRopa.setDisable(true);
                cbTipoAccesorio.setDisable(false);
            }
        });

        // Añadir nuevo tipo de ropa
        btnAddTipoRopa.setOnAction(e -> {
            String nuevo = txtNuevoTipoRopa.getText().trim();
            if (!nuevo.isEmpty() && !cbTipoRopa.getItems().contains(nuevo)) {
                cbTipoRopa.getItems().add(nuevo);
                txtNuevoTipoRopa.clear();
            }
        });

        // Añadir nuevo tipo de accesorio
        btnAddTipoAccesorio.setOnAction(e -> {
            String nuevo = txtNuevoTipoAccesorio.getText().trim();
            if (!nuevo.isEmpty() && !cbTipoAccesorio.getItems().contains(nuevo)) {
                cbTipoAccesorio.getItems().add(nuevo);
                txtNuevoTipoAccesorio.clear();
            }
        });
        guardarButton.setOnAction(e -> guardarProducto());

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
    // Validación adicional: precio numérico
    double precio;
   
    try {
        precio = parsePrecio(precioStr);
    } catch (NumberFormatException ex) {
        mostrarAlerta("El precio debe ser un número válido.");
        return;
    }
    // Guardar según tipo de producto
    if ("Ropa".equals(tipoProducto)) {
        String tipoRopa = cbTipoRopa.getValue();
        if (tipoRopa == null || tipoRopa.isEmpty()) {
            mostrarAlerta("Seleccioná un tipo de ropa.");
            return;
        }
     
            // antes de guardarlo vemos si existe el producto
           boolean b=false;
           Integer id_existe = gestorProducto.existeProducto(descripcion, marca, color, talle, "Ropa", tipoRopa);
           if (id_existe != null) b= true;
            if(b) {mostrarAlerta("El producto ya existe en el Sistema.");}
            else{
                Ropa ropa = new Ropa(descripcion, talle, precio, color, marca,tipoRopa);
                producto= ropa;
                id = gestorProducto.altaProducto(producto);
                System.out.println("ID PRODUCTO: " + id);
                LocalDateTime now = LocalDateTime.now();
                gestorStock.registrarStock(id, cantidad, now);
                mostrarAlerta("Producto guardado correctamente.");
            }
            
        } else if ("Accesorio".equals(tipoProducto)) {
        String tipoAccesorio = cbTipoAccesorio.getValue();
        if (tipoAccesorio == null || tipoAccesorio.isEmpty()) {
            mostrarAlerta("Seleccioná un tipo de accesorio.");
            return; }
            // antes de guardarlo vemos si existe el producto
            boolean b=false;
            Integer id_existe = gestorProducto.existeProducto(descripcion,marca,color, talle, "Accesorio", tipoAccesorio);
            if (id_existe != null) b= true;
            if(b) {
                mostrarAlerta("El producto ya existe en el Sistema.");}
            else{
                Accesorio accs = new Accesorio(descripcion, talle, precio, color, marca, tipoAccesorio);
                producto= accs;
                id = gestorProducto.altaProducto(producto);
                LocalDateTime now = LocalDateTime.now();
                gestorStock.registrarStock(id,cantidad, now);
                mostrarAlerta("Producto guardado correctamente.");
                }
            } 
        cbTipoProducto.setValue(null);
        cbTipoRopa.setValue(null);
        cbTipoAccesorio.setValue(null);
        txtDescripcion.clear();
        txtTalle.clear();
        txtPrecio.clear();
        txtColor.clear();
        txtMarca.clear();
        spCantidad.getValueFactory().setValue(0);
    }
    @FXML
    private void volverMenuPrincipal() {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
   
    private double parsePrecio(String precioStr) throws NumberFormatException {
        if (precioStr == null || precioStr.trim().isEmpty()) {
            throw new NumberFormatException("Precio vacío");
        }
        // Quitar separador de miles y cambiar coma por punto
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
