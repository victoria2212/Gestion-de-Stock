package com.victoria.Interfaces;

import java.io.IOException;

import com.victoria.navegation.Navegador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuPrincipal {

    @FXML private Button btnAltaProductos;
    @FXML private Button btnEmpleados;
    @FXML private MenuItem menuItemGestionRopa;
    @FXML private MenuItem menuItemGestionAccs;
    @FXML private MenuItem menuItemVisualizacionRopa;
    @FXML private MenuItem menuItemVisualizacionAccs;

    @FXML
    public void initialize() {
        btnAltaProductos.setOnAction(e -> abrirAltaProducto());
        btnEmpleados.setOnAction(e -> mostrarMensaje("Empleados clickeado"));

        menuItemGestionRopa.setOnAction(this::abrirGestionRopa);
        menuItemGestionAccs.setOnAction(this::abrirGestionAccs);
        
        menuItemVisualizacionRopa.setOnAction(this::abrirVisualizacionRopa);
        menuItemVisualizacionAccs.setOnAction(this::abrirVisualizacionAccs);
    }
    @FXML
    private void abrirAltaProducto() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneAltaProducto.fxml");
    }
  
    @FXML
    private void abrirGestionRopa(ActionEvent event) {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionStockRopa.fxml");
    }
    @FXML
    private void abrirGestionAccs(ActionEvent event) {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionStockAccs.fxml");
    }
    @FXML
    private void abrirVisualizacionRopa(ActionEvent event) {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneVisualizacionStockRopa.fxml");
    }
    @FXML
    private void abrirVisualizacionAccs(ActionEvent event) {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneVisualizacionStockAccs.fxml");
    }

    private void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
        // Si quieres mostrar alerta visual:
        // Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        // alert.showAndWait();
    }

}