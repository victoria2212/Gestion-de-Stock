package com.victoria.Interfaces;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import com.victoria.utils.FormateadorFechas;
import com.victoria.Dao.DaoProductoImp;
import com.victoria.Dto.AccsStockDTO;
import com.victoria.Gestores.GestorProducto;
import com.victoria.Gestores.GestorStock;
import com.victoria.navegation.Navegador;

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
import java.io.ByteArrayInputStream;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GestionStockAccs {
    
    public GestorStock gestorStock = GestorStock.getInstance();
    public GestorProducto gestorProducto = GestorProducto.getInstance();
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
    @FXML private TableColumn<AccsStockDTO, Void> colModificar;
    @FXML private TableColumn<AccsStockDTO, Void> colEliminar;
    
    // Inicializador del controlador

    @FXML
        private void initialize() {
        tablaStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        agregarColumnaImagen();
        // Centrar botones
        colModificar.setStyle("-fx-alignment: CENTER;");
        colEliminar.setStyle("-fx-alignment: CENTER;");

// ============================    

        agregarBotonesModificar();
        agregarBotonesEliminar();
        
        cargarDatos();
        configurarBuscador();
    }
    // Método para volver al menú principal
    @FXML
    private void volverMenuPrincipal() {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }
      private void agregarColumnaImagen() {

    colImagen.setCellFactory(col -> new TableCell<>() {

        private final ImageView imageView = new ImageView();

        {
            imageView.setFitWidth(90);
            imageView.setFitHeight(90);
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

                Image imagen =
                        new Image(new ByteArrayInputStream(foto));

                imageView.setImage(imagen);
                setGraphic(imageView);

            } else {
                setGraphic(null);
                }
            }
        });
    }
  
    private void agregarBotonesModificar() {
    colModificar.setCellFactory(col -> new TableCell<>() {
        private final Button btn = new Button("Modificar");
        {
            btn.getStyleClass().add("btn-modificar"); // <- agregá esta línea
            btn.setOnAction(e -> {
                AccsStockDTO item = getTableView().getItems().get(getIndex());
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
            btn.getStyleClass().add("btn-eliminar"); // <- agregá esta línea
            btn.setOnAction(e -> {
                AccsStockDTO item = getTableView().getItems().get(getIndex());
                Integer idProducto = item.getIdentificador();
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Confirmar eliminación");
                alerta.setHeaderText("Eliminar producto");
                alerta.setContentText("¿Estás seguro que quieres eliminar el producto: "
                    + item.getTipoAccs() + " " + item.getDescripcion() + "?");
                Optional<ButtonType> resultado = alerta.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    gestorStock.eliminarProductoStock(idProducto);
                    gestorProducto.eliminarProducto(idProducto);
                    cargarDatos();
                }
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
    private void cargarDatos() {
    List<AccsStockDTO> listaDTO = gestorStock.obtenerStockAccs();

    listaOriginal = FXCollections.observableArrayList(listaDTO);

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

            String texto = newValue.toLowerCase();

            return producto.getCodigoProducto().toLowerCase().contains(texto)
                || producto.getDescripcion().toLowerCase().contains(texto)
                || producto.getMarca().toLowerCase().contains(texto)
                || producto.getColor().toLowerCase().contains(texto)
                || producto.getTipoAccs().toLowerCase().contains(texto);
        });
    });

    tablaStock.setItems(filtrada);
    }

}
