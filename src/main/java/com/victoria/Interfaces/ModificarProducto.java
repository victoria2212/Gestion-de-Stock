package com.victoria.Interfaces;

import com.victoria.Clases.Producto;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;
import com.victoria.Gestores.GestorProducto;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ModificarProducto {
    @FXML private ComboBox<String> cbTipoProducto;
    @FXML private ComboBox<String> cbTipoRopa;
    @FXML private TextField txtNuevoTipoRopa;
    @FXML private Button btnAddTipoRopa;
    @FXML private ComboBox<String> cbTipoAccesorio;
    @FXML private TextField txtNuevoTipoAccesorio;
    @FXML private Button btnAddTipoAccesorio;

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTalle;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtColor;
    @FXML private TextField txtMarca;
    @FXML private Spinner<Integer> spCantidad;

    private RopaStockDTO ropa;
    private GestorStock gestorStock = new GestorStock();
    private GestorProducto gestorProducto = new GestorProducto();
    private AccsStockDTO accs;

    Object producto = Navegador.getDato();
    
    @FXML
    public void initialize() {
    
        cbTipoRopa.getItems().addAll(
            "Remeras", "Musculosas", "Deportivo", "Pantalones",
            "Buzos", "Pulovers", "Chombas", "Boxer",
            "Campera", "Malla", "Bermuda", "Camisa",
            "Zapatillas");

        cbTipoAccesorio.getItems().addAll(
            "Gorra", "Gorro", "Medias", "Lentes",
            "Cinto", "Mochila", "Pulsera", "Collar");
        
        spCantidad.setEditable(true);
        
        if (producto instanceof RopaStockDTO ropa) {
            this.ropa = (RopaStockDTO) producto;
            cbTipoProducto.setValue("ROPA");
            cbTipoRopa.setValue(ropa.getTipoRopa());
            txtDescripcion.setText(ropa.getDescripcion());
            txtTalle.setText(ropa.getTalle());
            txtPrecio.setText(String.valueOf(ropa.getPrecio()));
            txtColor.setText(ropa.getColor());
            txtMarca.setText(ropa.getMarca());
            

            spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, ropa.getCantidad())
            );

            cbTipoAccesorio.setDisable(true);
            txtNuevoTipoAccesorio.setDisable(true);
            btnAddTipoAccesorio.setDisable(true);
        }
        else if (producto instanceof AccsStockDTO accs) {
            this.accs = (AccsStockDTO) producto;
            cbTipoProducto.setValue("ACCESORIO");
            cbTipoAccesorio.setValue(accs.getTipoAccs());
            txtDescripcion.setText(accs.getDescripcion());
            txtTalle.setText(accs.getTalle());
            txtPrecio.setText(String.valueOf(accs.getPrecio()));
            txtColor.setText(accs.getColor());
            txtMarca.setText(accs.getMarca());
           

            spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, accs.getCantidad())
            );

            cbTipoRopa.setDisable(true);
            txtNuevoTipoRopa.setDisable(true);
            btnAddTipoRopa.setDisable(true);
        }
        // SOLO NUMEROS 
        txtPrecio.textProperty().addListener((obs, oldValue, newValue) -> {

        if (!newValue.matches("[0-9.,]*")) {
            txtPrecio.setText(
                newValue.replaceAll("[^0-9.,]", "")
            );
        }});

        // TALLE EN MAYUSC
        txtTalle.textProperty().addListener((obs, oldValue, newValue) -> {
        txtTalle.setText(newValue.toUpperCase());});

        // COLOR EN MAYUSC
        txtColor.textProperty().addListener((obs, oldValue, newValue) -> {
        txtColor.setText(newValue.toUpperCase());});

        // MARCA QUE EMPIEZA CON LETRA MAYUSC
        txtMarca.textProperty().addListener((obs, oldValue, newValue) -> {

        if (!newValue.isEmpty()) {

            String capitalizado = capitalizar(newValue);

            if (!newValue.equals(capitalizado)) {
                txtMarca.setText(capitalizado);
            }
        }});
    }

    @FXML
    private void guardarCambios() {

     try {

        // =========================
        // SI ES ROPA
        // =========================
        if (producto instanceof RopaStockDTO ropa) {

            // ACTUALIZAR DTO
            ropa.setDescripcion(
                txtDescripcion.getText()
            );

            ropa.setTalle(
                txtTalle.getText()
            );

            ropa.setPrecio(
                Double.parseDouble(
                    txtPrecio.getText()
                )
            );

            ropa.setColor(
                txtColor.getText()
            );

            ropa.setMarca(
                txtMarca.getText()
            );
            ropa.setTipoRopa(
                cbTipoRopa.getValue()
            );
            ropa.setCantidad(
                spCantidad.getValue()
            );

            // CREAR PRODUCTO
            Producto prod = new Producto();

            prod.setId_producto(
                ropa.getIdentificador()
            );

            prod.setDescripcion(
                ropa.getDescripcion()
            );

            prod.setTalle(
                ropa.getTalle()
            );

            prod.setPrecio(
                ropa.getPrecio()
            );

            prod.setColor(
                ropa.getColor()
            );

            prod.setMarca(
                ropa.getMarca()
            );
            prod.setTipoProducto(
                ropa.getTipoRopa()
            );
            
            // ACTUALIZAR TABLA PRODUCTO
            gestorProducto.actualizarProducto(prod);

            // ACTUALIZAR STOCK
            gestorStock.actualizarRopa(
                ropa.getIdentificador(),
                ropa.getCantidad()
            );

            System.out.println(
                "Ropa actualizada correctamente."
            );
        }

        // =========================
        // SI ES ACCESORIO
        // =========================
        else if (producto instanceof AccsStockDTO accs) {

            // ACTUALIZAR DTO
            accs.setDescripcion(
                txtDescripcion.getText()
            );

            accs.setTalle(
                txtTalle.getText()
            );

            accs.setPrecio(
                Double.parseDouble(
                    txtPrecio.getText()
                )
            );

            accs.setColor(
                txtColor.getText()
            );

            accs.setMarca(
                txtMarca.getText()
            );
            accs.setTipoAccs(
                cbTipoAccesorio.getValue()
            );
            accs.setCantidad(
                spCantidad.getValue()
            );

            // CREAR PRODUCTO
            Producto prod = new Producto();

            prod.setId_producto(
                accs.getIdentificador()
            );

            prod.setDescripcion(
                accs.getDescripcion()
            );

            prod.setTalle(
                accs.getTalle()
            );

            prod.setPrecio(
                accs.getPrecio()
            );

            prod.setColor(
                accs.getColor()
            );

            prod.setMarca(
                accs.getMarca()
            );
            prod.setTipoProducto(
                accs.getTipoAccs()
            );
           

            // ACTUALIZAR TABLA PRODUCTO
            gestorProducto.actualizarProducto(prod);

            // ACTUALIZAR STOCK
            gestorStock.actualizarAccs(
                accs.getIdentificador(),
                accs.getCantidad()
            );

            System.out.println(
                "Accesorio actualizado correctamente."
            );
        }

        // VOLVER
        Navegador.cambiarVista(
            "/com/victoria/Interfaces/SceneMenuPrincipal.fxml"
        );

    } catch (Exception e) {

        e.printStackTrace();

    }

    }
    private String capitalizar(String texto) {

        if (texto == null || texto.isEmpty()) {
            return texto;
        }
    return texto.substring(0,1).toUpperCase() +
           texto.substring(1).toLowerCase();}       
    
    @FXML
    private void volverMenuPrincipal() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
}