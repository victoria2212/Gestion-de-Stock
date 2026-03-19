package com.victoria.Interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dto.RopaStockDTO;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

public class VisualizarStockRopa {
    public GestorStock gestorStock = GestorStock.getInstance();
    // Tabla y columnas
    @FXML private TableView<RopaStockDTO> tablaStock;
    @FXML private TableColumn<RopaStockDTO, String> colTipoRopa;
    @FXML private TableColumn<RopaStockDTO, String> colDescripcion;
    @FXML private TableColumn<RopaStockDTO, String> colTalle;
    @FXML private TableColumn<RopaStockDTO, Double> colPrecio;
    @FXML private TableColumn<RopaStockDTO, String> colColor;
    @FXML private TableColumn<RopaStockDTO, String> colMarca;
    @FXML private TableColumn<RopaStockDTO, Integer> colCantidad;
    @FXML private TableColumn<RopaStockDTO, String> colIdentificador;
    @FXML private TableColumn<RopaStockDTO, LocalDateTime> colFechaActualizacion;

    // Inicializador del controlador

    @FXML
        private void initialize() {
        /*
           Usar PropertyValueFactory para vincular los nombres de las columnas con los getters de RopaStockDTO
         * El string que le pasas a PropertyValueFactory debe coincidir exactamente con el nombre de la propiedad en el DTO (sin el prefijo get).
         */
        colTipoRopa.setCellValueFactory(new PropertyValueFactory<>("tipoRopa"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTalle.setCellValueFactory(new PropertyValueFactory<>("talle"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colIdentificador.setCellValueFactory(new PropertyValueFactory<>("identificador"));
        colFechaActualizacion.setCellValueFactory(new PropertyValueFactory<>("fechaActualizacion"));

        cargarDatos();
    }
    // Método para volver al menú principal
    @FXML
    private void volverMenuPrincipal() {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
    /*@FXML
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
    }*/
    private void cargarDatos() {
    List<RopaStockDTO> listaDTO = gestorStock.obtenerStockRopa();
    ObservableList<RopaStockDTO> listaRopaStock = FXCollections.observableArrayList(listaDTO);
    tablaStock.setItems(listaRopaStock);
    }


}



