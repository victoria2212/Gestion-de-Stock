package com.victoria.Interfaces;

import java.util.List;
import java.util.Optional;

import com.victoria.Dto.EmpleadoDTO;
import com.victoria.Gestores.GestorEmpleado;
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

public class GestionEmpleado {
    private GestorEmpleado gestorEmpleado = GestorEmpleado.getInstance();

    @FXML private TableView<EmpleadoDTO> tablaEmpleados;

    @FXML private TableColumn<EmpleadoDTO, String> colDni;
    @FXML private TableColumn<EmpleadoDTO, String> colNombre;
    @FXML private TableColumn<EmpleadoDTO, String> colApellido;
    @FXML private TableColumn<EmpleadoDTO, String> colDireccion;
    @FXML private TableColumn<EmpleadoDTO, String> colContacto;
    @FXML private TableColumn<EmpleadoDTO, String> colFechaAlta;

    @FXML private TableColumn<EmpleadoDTO, Void> colModificar;
    @FXML private TableColumn<EmpleadoDTO, Void> colEliminar;

    @FXML
    private void initialize() {

        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));

        colFechaAlta.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                FormateadorFechas.formatear(
                    cellData.getValue().getFechaInicio()
                )
            )
        );
        // ===== CENTRAR COLUMNAS =====

        colDni.setStyle("-fx-alignment: CENTER;");
        colNombre.setStyle("-fx-alignment: CENTER;");
        colApellido.setStyle("-fx-alignment: CENTER;");
        colDireccion.setStyle("-fx-alignment: CENTER;");
        colContacto.setStyle("-fx-alignment: CENTER;");
        colFechaAlta.setStyle("-fx-alignment: CENTER;");

        colModificar.setStyle("-fx-alignment: CENTER;");
        colEliminar.setStyle("-fx-alignment: CENTER;");

    // =============================

        agregarBotonesModificar();
        agregarBotonesEliminar();

        cargarDatos();
    }
    @FXML
    private void irARegistroEmpleado() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneRegistroEmpleado.fxml");
    }

    @FXML
    private void volverMenuPrincipal() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");
    }

    // BOTÓN MODIFICAR
    private void agregarBotonesModificar() {

        colModificar.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Modificar");

            {
                btn.setOnAction(e -> {

                    EmpleadoDTO empleado =
                        getTableView().getItems().get(getIndex());

                    Navegador.setDato(empleado);

                    Navegador.cambiarVista(
                        "/com/victoria/Interfaces/SceneModificarEmpleado.fxml"
                    );
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

    // BOTÓN ELIMINAR
    private void agregarBotonesEliminar() {

        colEliminar.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Eliminar");

            {
                btn.setOnAction(e -> {

                    EmpleadoDTO empleado =
                        getTableView().getItems().get(getIndex());

                    Alert alerta =
                        new Alert(Alert.AlertType.CONFIRMATION);

                    alerta.setTitle("Confirmar eliminación");

                    alerta.setHeaderText("Eliminar empleado");

                    alerta.setContentText(
                        "¿Deseas eliminar al empleado: "
                        + empleado.getNombre()
                        + " "
                        + empleado.getApellido()
                        + "?"
                    );

                    Optional<ButtonType> resultado =
                        alerta.showAndWait();

                    if (resultado.isPresent()
                        && resultado.get() == ButtonType.OK) {
                        
                        gestorEmpleado.eliminarEmpleado(
                            empleado.getDni()
                        );
                         
                        

                        cargarDatos();
                    }
                });

                setAlignment(Pos.CENTER);
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

        List<EmpleadoDTO> lista =
            gestorEmpleado.obtenerEmpleados();

        ObservableList<EmpleadoDTO> empleados =
            FXCollections.observableArrayList(lista);

        tablaEmpleados.setItems(empleados);
    }

}
