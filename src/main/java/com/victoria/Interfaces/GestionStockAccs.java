package com.victoria.Interfaces;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;
import com.victoria.Gestores.GestorStock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class GestionStockAccs {
    
    public GestorStock gestorStock = GestorStock.getInstance();
    // Tabla y columnas
    @FXML private TableView<AccsStockDTO> tablaStock;
    @FXML private TableColumn<AccsStockDTO, String> colTipoAccs;
    @FXML private TableColumn<AccsStockDTO, String> colDescripcion;
    @FXML private TableColumn<AccsStockDTO, String> colTalle;
    @FXML private TableColumn<AccsStockDTO, Double> colPrecio;
    @FXML private TableColumn<AccsStockDTO, String> colColor;
    @FXML private TableColumn<AccsStockDTO, String> colMarca;
    @FXML private TableColumn<AccsStockDTO, Integer> colCantidad;
    @FXML private TableColumn<AccsStockDTO, String> colIdentificador;
    @FXML private TableColumn<AccsStockDTO, LocalDateTime> colFechaActualizacion;
    @FXML private TableColumn<RopaStockDTO, Void> colModificar;
    @FXML private TableColumn<RopaStockDTO, Void> colEliminar;
    
    // Inicializador del controlador

    @FXML
        private void initialize() {
        
        colTipoAccs.setCellValueFactory(new PropertyValueFactory<>("tipoAccs"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTalle.setCellValueFactory(new PropertyValueFactory<>("talle"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colIdentificador.setCellValueFactory(new PropertyValueFactory<>("identificador"));
        colFechaActualizacion.setCellValueFactory(new PropertyValueFactory<>("fechaActualizacion"));

        agregarBotonesModificar();
        agregarBotonesEliminar();

        cargarDatos();
    }
    // Método para volver al menú principal
    @FXML
    private void volverMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneMenuPrincipal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tablaStock.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void agregarBotonesModificar() {
        colModificar.setCellFactory(col -> new TableCell<>() {
        private final Button btn = new Button("Modificar");
        {
            btn.setOnAction(e -> {
                RopaStockDTO item = getTableView().getItems().get(getIndex());
                // Aquí iría tu lógica de gestor para modificar
                System.out.println("Modificar: " + item.getIdentificador());
            });
            setAlignment(Pos.CENTER); // Centrar contenido
        }
        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(btn);
                }
            }
        });
    }
     private void agregarBotonesEliminar() {
        colEliminar.setCellFactory(col -> new TableCell<>() {
        private final Button btn = new Button("Eliminar");

        {
            btn.setOnAction(e -> {
                RopaStockDTO item = getTableView().getItems().get(getIndex());
                // Aquí iría tu lógica de gestor para eliminar
                System.out.println("Eliminar: " + item.getIdentificador());
            });
            setAlignment(Pos.CENTER); // Centrar contenido
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(btn);
                }
            }
        });
    }
    private void cargarDatos() {
    List<AccsStockDTO> listaDTO = gestorStock.obtenerStockAccs();
    ObservableList<AccsStockDTO> listaAccsStock = FXCollections.observableArrayList(listaDTO);
    tablaStock.setItems(listaAccsStock);
    }

}
