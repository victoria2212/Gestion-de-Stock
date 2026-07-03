package com.victoria.Interfaces;

import com.victoria.Clases.Empleado;
import com.victoria.Gestores.GestorEmpleado;
import com.victoria.navegation.Navegador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroEmpleado {

    public GestorEmpleado gestorEmpleado = GestorEmpleado.getInstance();

    @FXML private TextField campoDni;
    @FXML private TextField campoNombre;
    @FXML private TextField campoApellido;
    @FXML private TextField campoDireccion;
    @FXML private TextField campoContacto;
    @FXML private ComboBox<String> campoRol;
    @FXML private PasswordField campoContrasenaAdmin;
    @FXML private TextField campoContrasenaAdminVisible;
    @FXML private Button botonVerContrasena;

    @FXML
    private void initialize() {
        campoRol.setItems(FXCollections.observableArrayList("Administrador", "Empleado"));

        // Restricción: solo números en DNI
        campoDni.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campoDni.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Restricción: solo números en Contacto — VA ACÁ, en initialize()
        campoContacto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campoContacto.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void registrarEmpleado() {
        String apellido = campoApellido.getText().trim();
        String nombre = campoNombre.getText().trim();
        String dni = campoDni.getText().trim();
        String direc = campoDireccion.getText().trim();
        String contacto = campoContacto.getText().trim();
        String rol = campoRol.getValue();
        String contrasenaAdmin = campoContrasenaAdmin.isVisible()
                ? campoContrasenaAdmin.getText().trim()
                : campoContrasenaAdminVisible.getText().trim();

        if (nombre.isEmpty() || dni.isEmpty() || direc.isEmpty() || apellido.isEmpty()
                || contacto.isEmpty() || rol == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        Integer documento = Integer.parseInt(dni);

        if (contrasenaAdmin.isEmpty()) {
            mostrarAlerta("Ingrese la contraseña del administrador para registrar el nuevo empleado.");
            return;
        }

        String contraseñaCorrecta = "$BerterO$";
        if (!contrasenaAdmin.equals(contraseñaCorrecta)) {
            mostrarAlerta("Contraseña incorrecta. No puede registrar el empleado.");
            return;
        }

        if (!gestorEmpleado.existeEmpleado(documento)) {
            // Pasá contacto al constructor de Empleado si ya lo agregaste al modelo
            Empleado nuevoEmpleado = new Empleado(documento, nombre, apellido, direc, contacto);
            gestorEmpleado.agregarEmpleado(nuevoEmpleado);
            mostrarAlerta("Empleado registrado con éxito.");
            limpiarCampos();
            Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionEmpleados.fxml");
        } else {
            mostrarAlerta("Este empleado ya existe en el sistema.");
            limpiarCampos();
        }
    }

    @FXML
    private void toggleVerContrasena() {
        if (campoContrasenaAdmin.isVisible()) {
            campoContrasenaAdminVisible.setText(campoContrasenaAdmin.getText());
            campoContrasenaAdmin.setVisible(false);
            campoContrasenaAdmin.setManaged(false);
            campoContrasenaAdminVisible.setVisible(true);
            campoContrasenaAdminVisible.setManaged(true);
        } else {
            campoContrasenaAdmin.setText(campoContrasenaAdminVisible.getText());
            campoContrasenaAdminVisible.setVisible(false);
            campoContrasenaAdminVisible.setManaged(false);
            campoContrasenaAdmin.setVisible(true);
            campoContrasenaAdmin.setManaged(true);
        }
    }

    @FXML
    private void inicioSesion() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionEmpleados.fxml");
    }

    private void limpiarCampos() {
        campoDni.clear();
        campoNombre.clear();
        campoApellido.clear();
        campoDireccion.clear();
        campoContacto.clear();           // agregado
        campoRol.getSelectionModel().clearSelection();
        campoContrasenaAdmin.clear();
        campoContrasenaAdminVisible.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
