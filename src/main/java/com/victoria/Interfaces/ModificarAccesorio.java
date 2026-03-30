package com.victoria.Interfaces;

import com.victoria.Dto.AccsStockDTO;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class ModificarAccesorio {
    @FXML private ComboBox<String> cbTipoAccesorio;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTalle;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtColor;
    @FXML private TextField txtMarca;
    @FXML private Spinner<Integer> spCantidad;
    
    private GestorStock gestorStock = new GestorStock();
    private AccsStockDTO accs;

    @FXML
    public void initialize() {
        Object producto = Navegador.getDato();
        this.accs = (AccsStockDTO) producto;

        cbTipoAccesorio.setValue(accs.getTipoAccs());
        txtDescripcion.setText(accs.getDescripcion());
        txtTalle.setText(accs.getTalle());
        txtPrecio.setText(String.valueOf(accs.getPrecio()));
        txtColor.setText(accs.getColor());
        txtMarca.setText(accs.getMarca());
        spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, accs.getCantidad())
            );

    }

    @FXML
    private void guardarCambios() {
        /*
         //ropa.setTipoRopa(txtTipoRopa.getText());
        ropa.setDescripcion(txtDescripcion.getText());
        ropa.setTalle(txtTalle.getText());
        ropa.setPrecio(Double.parseDouble(txtPrecio.getText()));
        ropa.setColor(txtColor.getText());
        ropa.setMarca(txtMarca.getText());
        ropa.setCantidad(spCantidad.getValue());
        if (ropa != null) {
        gestorStock.actualizarRopa(ropa);
        } else if (accs != null) {
        gestorStock.actualizarAccs(accs);
        }

        /*aca un condicional si es ropa o accesorio
        gestorStock.actualizarRopa(ropa);
        gestorStock.actualizarAccs(accs);

        Navegador.limpiarDato();
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionStockRopa.fxml");
         */

       
    }
    @FXML
    private void volverMenuPrincipal() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }

}
