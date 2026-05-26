package com.victoria.Interfaces;

import java.util.List;
import java.util.Optional;

import com.victoria.Dto.RopaStockDTO;
import com.victoria.Gestores.GestorProducto;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;
import com.victoria.utils.FormateadorFechas;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionStockRopa {
    public GestorStock gestorStock = GestorStock.getInstance();
    public GestorProducto gestorProducto = GestorProducto.getInstance();
    // Tabla y columnas
    @FXML private TableView<RopaStockDTO> tablaStock;
    @FXML private TableColumn<RopaStockDTO, String> colTipoRopa;
    @FXML private TableColumn<RopaStockDTO, String> colDescripcion;
    @FXML private TableColumn<RopaStockDTO, String> colTalle;
    @FXML private TableColumn<RopaStockDTO, Double> colPrecio;
    @FXML private TableColumn<RopaStockDTO, String> colColor;
    @FXML private TableColumn<RopaStockDTO, String> colMarca;
    @FXML private TableColumn<RopaStockDTO, Integer> colCantidad;
    @FXML private TableColumn<RopaStockDTO, String> colCodigo;
    @FXML private TableColumn<RopaStockDTO, String> colFechaActualizacion;
    @FXML private TableColumn<RopaStockDTO, Void> colModificar;
    @FXML private TableColumn<RopaStockDTO, Void> colEliminar;
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
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colFechaActualizacion.setCellValueFactory(cellData ->
        new SimpleStringProperty(FormateadorFechas.formatear(cellData.getValue().getFechaActualizacion())));
        
        // ===== CENTRAR COLUMNAS =====

        colTipoRopa.setStyle("-fx-alignment: CENTER;");
        colDescripcion.setStyle("-fx-alignment: CENTER;");
        colTalle.setStyle("-fx-alignment: CENTER;");
        colPrecio.setStyle("-fx-alignment: CENTER;");
        colColor.setStyle("-fx-alignment: CENTER;");
        colMarca.setStyle("-fx-alignment: CENTER;");
        colCantidad.setStyle("-fx-alignment: CENTER;");
        colCodigo.setStyle("-fx-alignment: CENTER;");
        colFechaActualizacion.setStyle("-fx-alignment: CENTER;");

        // Centrar botones
        colModificar.setStyle("-fx-alignment: CENTER;");
        colEliminar.setStyle("-fx-alignment: CENTER;");

        // ============================

        agregarBotonesModificar();
        agregarBotonesEliminar();

        cargarDatos();
    }
    // Método para volver al menú principal
    @FXML
    private void volverMenuPrincipal() {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }

    //BOTON DE MODIFICAR
    private void agregarBotonesModificar() {
    colModificar.setCellFactory(col -> new TableCell<>() {
        private final Button btn = new Button("Modificar");

        {
        btn.setOnAction(e -> {
                
            RopaStockDTO item = getTableView().getItems().get(getIndex());
            Navegador.setDato(item);
            Navegador.cambiarVista("/com/victoria/Interfaces/SceneModificarProducto.fxml");
        });

            setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : btn);
        }
    });
}
   
   
    private void agregarBotonesEliminar() {
        colEliminar.setCellFactory(col -> new TableCell<>() {
        private final Button btn = new Button("Eliminar");

        {
            btn.setOnAction(e -> {
                RopaStockDTO item = getTableView().getItems().get(getIndex());
                Integer idProducto = item.getIdentificador();
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

                alerta.setTitle("Confirmar eliminación");

                alerta.setHeaderText("Eliminar producto");

                alerta.setContentText(
                    "¿Estás seguro que quieres eliminar el producto: " + item.getTipoRopa() + " "
                    + item.getDescripcion() +  "?"
                );

                Optional<ButtonType> resultado = alerta.showAndWait();

                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                            gestorStock.eliminarProductoStock(idProducto);
                            gestorProducto.eliminarProducto(idProducto);

                            cargarDatos();
                }
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
    List<RopaStockDTO> listaDTO = gestorStock.obtenerStockRopa();
    ObservableList<RopaStockDTO> listaRopaStock = FXCollections.observableArrayList(listaDTO);
    tablaStock.setItems(listaRopaStock);
    }
}
