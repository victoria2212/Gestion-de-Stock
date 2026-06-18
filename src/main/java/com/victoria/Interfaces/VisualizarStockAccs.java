package com.victoria.Interfaces;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.victoria.Dto.AccsStockDTO;

import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;
import com.victoria.utils.FormateadorFechas;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class VisualizarStockAccs {
    public GestorStock gestorStock = GestorStock.getInstance();
    private ObservableList<AccsStockDTO> listaOriginal;
    // Tabla y columnas
    @FXML private TextField txtBuscar;
    @FXML private TableView<AccsStockDTO> tablaStock;
    @FXML private TableColumn<AccsStockDTO, String> colTipoAccs;
    @FXML private TableColumn<AccsStockDTO, String> colDescripcion;
    @FXML private TableColumn<AccsStockDTO, String> colTalle;
    @FXML private TableColumn<AccsStockDTO, Double> colPrecio;
    @FXML private TableColumn<AccsStockDTO, String> colColor;
    @FXML private TableColumn<AccsStockDTO, String> colMarca;
    @FXML private TableColumn<AccsStockDTO, Integer> colCantidad;
    @FXML private TableColumn<AccsStockDTO, String> colCodigo;
    @FXML private TableColumn<AccsStockDTO, String> colFechaActualizacion;
    @FXML private TableColumn<AccsStockDTO, Void> colImagen;

    // Inicializador del controlador

    @FXML
        private void initialize() {
        tablaStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);    
        /*
           Usar PropertyValueFactory para vincular los nombres de las columnas con los getters de AccsStockDTO
         * El string que le pasas a PropertyValueFactory debe coincidir exactamente con el nombre de la propiedad en el DTO (sin el prefijo get).
         */
        colTipoAccs.setCellValueFactory(new PropertyValueFactory<>("tipoAccs"));
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

        colTipoAccs.setStyle("-fx-alignment: CENTER;");
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

    {
        setAlignment(Pos.CENTER);

        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            return;
        }

        AccsStockDTO producto =
                getTableView().getItems().get(getIndex());

        byte[] foto = producto.getImagen();

        if (foto != null && foto.length > 0) {

            imageView.setImage(
                new Image(new ByteArrayInputStream(foto))
            );

            setGraphic(imageView);

        } else {
            setGraphic(null);
            }
        }
    });
    }
    // Método para volver al menú principal
    @FXML
    private void volverMenuPrincipal() {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
  
    private void cargarDatos() {
        List<AccsStockDTO> listaDTO =
            gestorStock.obtenerStockAccs();

    listaOriginal =
            FXCollections.observableArrayList(listaDTO);

    tablaStock.setItems(listaOriginal);
    }
    private void configurarBuscador() {

    FilteredList<AccsStockDTO> filtrada =
            new FilteredList<>(listaOriginal, p -> true);

    txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {

        filtrada.setPredicate(producto -> {

            if (newValue == null || newValue.isBlank()) {
                return true;
            }

            String filtro = newValue.toLowerCase();

            return producto.getTipoAccs().toLowerCase().contains(filtro)
                    || producto.getDescripcion().toLowerCase().contains(filtro)
                    || producto.getMarca().toLowerCase().contains(filtro)
                    || producto.getColor().toLowerCase().contains(filtro)
                    || producto.getCodigoProducto().toLowerCase().contains(filtro);
        });
    });

    SortedList<AccsStockDTO> ordenada =
            new SortedList<>(filtrada);

    ordenada.comparatorProperty()
            .bind(tablaStock.comparatorProperty());

    tablaStock.setItems(ordenada);
}

}
