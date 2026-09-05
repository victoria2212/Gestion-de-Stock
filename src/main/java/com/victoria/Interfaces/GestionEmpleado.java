package com.victoria.Interfaces;

import java.util.List;
import java.util.Optional;

import com.victoria.Dto.EmpleadoDTO;
import com.victoria.Gestores.GestorEmpleado;
import com.victoria.navegation.Navegador;
import com.victoria.utils.FormateadorFechas;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class GestionEmpleado {

    private GestorEmpleado gestorEmpleado = GestorEmpleado.getInstance();

    @FXML private FlowPane contenedorEmpleados;
    @FXML private Label lblCantidadEmpleados;

    @FXML
    private void initialize() {
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

    private void actualizarContador(int cantidad) {

        String texto = cantidad == 1
                ? "1 empleado"
                : cantidad + " empleados";

        lblCantidadEmpleados.setText(texto);
    }

    private void cargarDatos() {

        List<EmpleadoDTO> lista = gestorEmpleado.obtenerEmpleados();

        contenedorEmpleados.getChildren().clear();

        for (EmpleadoDTO empleado : lista) {
            contenedorEmpleados.getChildren().add(crearTarjetaEmpleado(empleado));
        }

        actualizarContador(lista.size());
    }

    // =========================================================
    // CREAR TARJETA DE EMPLEADO
    // =========================================================

   private VBox crearTarjetaEmpleado(EmpleadoDTO empleado) {

    VBox tarjeta = new VBox(10);
    tarjeta.getStyleClass().add("tarjeta-empleado");

    // ------------------------------------------------------
    // ENCABEZADO: avatar + nombre + DNI + badge de rol
    // ------------------------------------------------------

    HBox encabezado = new HBox(10);
    encabezado.setAlignment(Pos.CENTER_LEFT);

    String iniciales = obtenerIniciales(empleado.getNombre(), empleado.getApellido());

    javafx.scene.layout.StackPane avatar = new javafx.scene.layout.StackPane();
    avatar.getStyleClass().add("avatar-empleado");
    Label lblIniciales = new Label(iniciales);
    avatar.getChildren().add(lblIniciales);

    VBox nombreDni = new VBox(2);
    Label lblNombre = new Label(empleado.getNombre() + " " + empleado.getApellido());
    lblNombre.getStyleClass().add("nombre-empleado");
    lblNombre.setWrapText(true);
    lblNombre.setMaxWidth(150);
    Label lblDni = new Label("DNI " + empleado.getDni());
    lblDni.getStyleClass().add("dni-empleado");
    nombreDni.getChildren().addAll(lblNombre, lblDni);

    Region espacio = new Region();
    HBox.setHgrow(espacio, Priority.ALWAYS);

    String rol = (empleado.getRol() != null) ? empleado.getRol() : "Empleado";
    Label lblRol = new Label(rol);
    lblRol.getStyleClass().add(
            "Administrador".equals(rol) ? "badge-rol-admin" : "badge-rol-empleado"
    );
    lblRol.setMinWidth(Region.USE_PREF_SIZE); 

    encabezado.getChildren().addAll(avatar, nombreDni, espacio, lblRol);

    // ------------------------------------------------------
    // DATOS: dirección, contacto, fecha de alta
    // ------------------------------------------------------

    VBox datos = new VBox(4);

    Label lblDireccion = new Label(empleado.getDireccion());
    lblDireccion.getStyleClass().add("dato-empleado");

    Label lblContacto = new Label(empleado.getContacto());
    lblContacto.getStyleClass().add("dato-empleado");

    Label lblFecha = new Label(
            "Desde " + FormateadorFechas.formatear(empleado.getFechaInicio())
    );
    lblFecha.getStyleClass().add("dato-empleado");

    String textoConexion = (empleado.getUltimaConexion() != null)
            ? "Última conexión: " + FormateadorFechas.formatear(empleado.getUltimaConexion())
            : "Aún no se conectó";
    Label lblConexion = new Label(textoConexion);
    lblConexion.getStyleClass().add("dato-empleado");

    int ventas = com.victoria.Gestores.GestorVenta.getInstance()
            .contarVentasPorVendedor(empleado.getNombreCompleto());
    Label lblVentas = new Label(
            ventas == 1 ? "1 venta realizada" : ventas + " ventas realizadas"
    );
    lblVentas.getStyleClass().add("dato-empleado-destacado");

    datos.getChildren().addAll(lblDireccion, lblContacto, lblFecha, lblConexion, lblVentas);

    // ------------------------------------------------------
    // BOTONES: Modificar / Eliminar
    // ------------------------------------------------------

    Button btnModificar = new Button("Modificar");
    btnModificar.getStyleClass().add("btn-modificar");
    HBox.setHgrow(btnModificar, Priority.ALWAYS);
    btnModificar.setOnAction(e -> {
        Navegador.setDato(empleado);
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneModificarEmpleado.fxml");
    });

    Button btnEliminar = new Button("Eliminar");
    btnEliminar.getStyleClass().add("btn-eliminar");
    HBox.setHgrow(btnEliminar, Priority.ALWAYS);
    btnEliminar.setOnAction(e -> {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminación");
        alerta.setHeaderText("Eliminar empleado");
        alerta.setContentText(
                "¿Deseas eliminar al empleado: "
                + empleado.getNombre() + " " + empleado.getApellido() + "?"
        );

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestorEmpleado.eliminarEmpleado(empleado.getDni());
            cargarDatos();
        }
    });

    HBox botones = new HBox(8, btnModificar, btnEliminar);

    tarjeta.getChildren().addAll(encabezado, datos, botones);

        return tarjeta;
    }
    private String obtenerIniciales(String nombre, String apellido) {

        String inicialNombre = (nombre != null && !nombre.isBlank())
                ? nombre.trim().substring(0, 1).toUpperCase()
                : "";

        String inicialApellido = (apellido != null && !apellido.isBlank())
                ? apellido.trim().substring(0, 1).toUpperCase()
                : "";

        return inicialNombre + inicialApellido;
    }
}