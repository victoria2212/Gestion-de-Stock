package com.victoria.Interfaces;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dto.AccsStockDTO;
import com.victoria.Gestores.GestorStock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class VisualizarStockAccs {
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
    // Inicializador del controlador

    @FXML
        private void initialize() {
        /*
           Usar PropertyValueFactory para vincular los nombres de las columnas con los getters de RopaStockDTO
         * El string que le pasas a PropertyValueFactory debe coincidir exactamente con el nombre de la propiedad en el DTO (sin el prefijo get).
         */
        colTipoAccs.setCellValueFactory(new PropertyValueFactory<>("tipoAccs"));
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
    private void cargarDatos() {
    List<AccsStockDTO> listaDTO = gestorStock.obtenerStockAccs();
    ObservableList<AccsStockDTO> listaAccsStock = FXCollections.observableArrayList(listaDTO);
    tablaStock.setItems(listaAccsStock);
    }

}
