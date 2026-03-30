package com.victoria.Interfaces;


import com.victoria.Dto.RopaStockDTO;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class ModificarRopa {
    
    @FXML private ComboBox<String> cbTipoRopa;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTalle;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtColor;
    @FXML private TextField txtMarca;
    @FXML private Spinner<Integer> spCantidad;

    private RopaStockDTO ropa;
    private GestorStock gestorStock = new GestorStock();

    @FXML
    public void initialize() {
        Object producto = Navegador.getDato();
        
            this.ropa = (RopaStockDTO) producto;
            cbTipoRopa.setValue(ropa.getTipoRopa());
            txtDescripcion.setText(ropa.getDescripcion());
            txtTalle.setText(ropa.getTalle());
            txtPrecio.setText(String.valueOf(ropa.getPrecio()));
            txtColor.setText(ropa.getColor());
            txtMarca.setText(ropa.getMarca());
            
            spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, ropa.getCantidad())
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
