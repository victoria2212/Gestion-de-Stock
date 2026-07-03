package com.victoria.Interfaces;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;

import com.victoria.navegation.Navegador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuPrincipal {

    @FXML private Button btnAltaProductos;
    @FXML private Button btnEmpleados;
    @FXML private Button btnGestionStock;
    @FXML private Button btnVisualizacionStock;
    @FXML private Button btnVentas;

    @FXML
    public void initialize() {
        // ya no hace falta setOnAction manual: el FXML llama directo a los métodos vía onAction
    }

    @FXML
    private void abrirGestionEmpleado(ActionEvent event){
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionEmpleados.fxml");
    }

    @FXML
    private void abrirAltaProducto(ActionEvent event) {
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

    @FXML
    private void mostrarMenuGestionStock(ActionEvent event) {
        ContextMenu menu = new ContextMenu();

        MenuItem itemRopa = new MenuItem("Gestión de Ropa");
        itemRopa.setOnAction(this::abrirGestionRopa);

        MenuItem itemAccs = new MenuItem("Gestión de Accesorios");
        itemAccs.setOnAction(this::abrirGestionAccs);

        menu.getItems().addAll(itemRopa, itemAccs);
        menu.show(btnGestionStock, Side.BOTTOM, 0, 5);
    }

    @FXML
    private void mostrarMenuVisualizacion(ActionEvent event) {
        ContextMenu menu = new ContextMenu();

        MenuItem itemRopa = new MenuItem("Visualización de Ropa");
        itemRopa.setOnAction(this::abrirVisualizacionRopa);

        MenuItem itemAccs = new MenuItem("Visualización de Accesorios");
        itemAccs.setOnAction(this::abrirVisualizacionAccs);

        menu.getItems().addAll(itemRopa, itemAccs);
        menu.show(btnVisualizacionStock, Side.BOTTOM, 0, 5);
    }

    @FXML
    private void mostrarMenuVentas(ActionEvent event) {
        ContextMenu menu = new ContextMenu();

        MenuItem itemRegistrar = new MenuItem("Registrar Venta");
        itemRegistrar.setOnAction(e -> Navegador.cambiarVista("/com/victoria/Interfaces/SceneRegistrarVenta.fxml"));

        MenuItem itemHistorial = new MenuItem("Historial de Ventas");
        itemHistorial.setOnAction(e -> Navegador.cambiarVista("/com/victoria/Interfaces/SceneHistorialVentas.fxml"));

        menu.getItems().addAll(itemRegistrar, itemHistorial);
        menu.show(btnVentas, Side.BOTTOM, 0, 5);
    }
}
