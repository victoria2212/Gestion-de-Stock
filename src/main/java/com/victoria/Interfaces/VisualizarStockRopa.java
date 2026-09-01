package com.victoria.Interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.victoria.Dto.AccsStockDTO;
import com.victoria.Dto.RopaStockDTO;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;
import com.victoria.utils.FormateadorFechas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import com.victoria.utils.VisorImagen;
import javafx.scene.Cursor;

public class VisualizarStockRopa {
    public GestorStock gestorStock = GestorStock.getInstance();
    private ObservableList<RopaStockDTO> listaOriginal;
    // Tabla y columnas
    @FXML private TextField txtBuscar;
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
    @FXML private TableColumn<RopaStockDTO, Void> colImagen;

    // Inicializador del controlador

    @FXML
        private void initialize() {
        tablaStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

    // ============================

        agregarColumnaImagen();
        cargarDatos();
        configurarBuscador();
    }
    private void agregarColumnaImagen() {
    colImagen.setCellFactory(col -> new TableCell<>() {

        private final ImageView imageView = new ImageView();
        private final StackPane contenedor = new StackPane(imageView);

        {
            setAlignment(Pos.CENTER);

            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);

            contenedor.setCursor(Cursor.HAND);

            contenedor.setOnMouseClicked(event -> {
               // System.out.println(">>> CLICK DETECTADO EN CELDA, index=" + getIndex());

                if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                  //  System.out.println(">>> Index invalido, saliendo.");
                    return;
                }

                RopaStockDTO producto = getTableView().getItems().get(getIndex());
               /*  System.out.println(">>> Producto: " + producto.getCodigoProducto());
                System.out.println(">>> Foto = " +
                    (producto.getImagen() == null ? "null" : producto.getImagen().length + " bytes"));*/

                VisorImagen.mostrar(producto.getImagen());
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                return;
            }

            RopaStockDTO producto = getTableView().getItems().get(getIndex());
            byte[] foto = producto.getImagen();

            if (foto != null && foto.length > 0) {
                imageView.setImage(new Image(new ByteArrayInputStream(foto)));
            } else {
                imageView.setImage(null);
            }

            setGraphic(contenedor);
        }
    });
}
    // Método para volver al menú principal
    @FXML
    private void volverMenuPrincipal() {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
 
    private void cargarDatos() {
    
    List<RopaStockDTO> listaDTO =
        gestorStock.obtenerStockRopa();

    listaOriginal =
        FXCollections.observableArrayList(listaDTO);

    tablaStock.setItems(listaOriginal);
    }
    private void configurarBuscador() {

    FilteredList<RopaStockDTO> filtrada =
        new FilteredList<>(listaOriginal, p -> true);

    txtBuscar.textProperty().addListener(
        (obs, oldValue, newValue) -> {

        filtrada.setPredicate(producto -> {

            if (newValue == null || newValue.isBlank()) {
                return true;
            }

            String filtro =
                newValue.toLowerCase();

            return producto.getTipoRopa()
                    .toLowerCase()
                    .contains(filtro)

                || producto.getDescripcion()
                    .toLowerCase()
                    .contains(filtro)

                || producto.getMarca()
                    .toLowerCase()
                    .contains(filtro)

                || producto.getColor()
                    .toLowerCase()
                    .contains(filtro)

                || producto.getCodigoProducto()
                    .toLowerCase()
                    .contains(filtro);
        });
    });

    SortedList<RopaStockDTO> ordenada =
        new SortedList<>(filtrada);

        ordenada.comparatorProperty().bind(tablaStock.comparatorProperty());

        tablaStock.setItems(ordenada);
    }


}



