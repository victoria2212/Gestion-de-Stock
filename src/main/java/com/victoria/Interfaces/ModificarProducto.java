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
       

       
    @FXML
    private void volverMenuPrincipal() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
}