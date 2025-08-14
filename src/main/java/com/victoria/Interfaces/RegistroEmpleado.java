package com.victoria.Interfaces;

import java.io.IOException;

import com.victoria.Clases.Empleado;
import com.victoria.Gestores.GestorEmpleado;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroEmpleado {

    public GestorEmpleado gestorEmpleado = GestorEmpleado.getInstance();

    @FXML private TextField campoDni;
    @FXML private TextField campoNombre;
    @FXML private TextField campoApellido;
    @FXML private TextField campoDireccion;
    @FXML private ComboBox<String> campoRol;
    @FXML private PasswordField campoContrasenaAdmin;
    @FXML private TextField campoContrasenaAdminVisible;
    @FXML private Button botonVerContrasena;
    

    @FXML
    private void initialize() {
        campoRol.setItems(FXCollections.observableArrayList("Administrador", "Empleado"));

        // Esto hace que solo se pueda ingresar números en campoDni
        campoDni.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campoDni.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void registrarEmpleado() {
        String apellido = campoApellido.getText().trim();
        String nombre = campoNombre.getText().trim();
        String dni = campoDni.getText().trim();
        String direc = campoDireccion.getText().trim();
        String rol = campoRol.getValue();
        String contrasenaAdmin = campoContrasenaAdmin.getText().trim();

        // en un futuro puedo hacer las validaciones por separado de cada campo asi muestro una alerta personalizada
        if (nombre.isEmpty() || dni.isEmpty() || direc.isEmpty() || apellido.isEmpty() || rol == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }
        Integer documento;
        documento = Integer.parseInt(dni);
        
        if (contrasenaAdmin.isEmpty()) {
            mostrarAlerta("Ingrese la contraseña del administrador para registrar el nuevo empleado.");
            return;
        }
        
        //aca tengo que cambiarlo para que saque la contraseña de la base de datos
        String contraseñaCorrecta = "$BerterO$";  
        if (!contrasenaAdmin.equals(contraseñaCorrecta)) {
            mostrarAlerta("Contraseña incorrecta. No puede registrar el empleado.");
            return;
        }


        if (!gestorEmpleado.existeEmpleado(documento)) {
            Empleado nuevoEmpleado = new Empleado(documento, nombre, apellido, direc);
            gestorEmpleado.agregarEmpleado(nuevoEmpleado);
            //ACA TENGO QUE VER SI EN SERIO SE GUARDO CORRECTAMENTE, PORQUE PUEDE QUE TIRE EL MENSAJE PERO NO SE HAYA GUARDADO
            mostrarAlerta("Empleado registrado con éxito.");
            limpiarCampos();
           // VOLVEMOS a la pantalla de inicio de sesión en la misma ventana
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneInicioSesion.fxml"));
            Parent root = loader.load();
            Stage stageActual = (Stage) campoDni.getScene().getWindow();
            stageActual.setScene(new Scene(root));
            stageActual.setTitle("Inicio de Sesión");
        }  catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar Inicio de Sesión.");
        }
        } else {
            mostrarAlerta("Este empleado ya existe en el sistema.");
            campoDni.clear();
            campoNombre.clear();
            campoApellido.clear();
            campoDireccion.clear();
            campoRol.setValue(null);
            campoContrasenaAdmin.clear();
        }
    }
    @FXML
        private void toggleVerContrasena() {
            if (campoContrasenaAdmin.isVisible()) {
                // Mostrar el campo visible y ocultar el PasswordField
                campoContrasenaAdminVisible.setText(campoContrasenaAdmin.getText());
                campoContrasenaAdmin.setVisible(false);
                campoContrasenaAdmin.setManaged(false);
                campoContrasenaAdminVisible.setVisible(true);
                campoContrasenaAdminVisible.setManaged(true);
            } else {
                // Volver al PasswordField
                campoContrasenaAdmin.setText(campoContrasenaAdminVisible.getText());
                campoContrasenaAdminVisible.setVisible(false);
                campoContrasenaAdminVisible.setManaged(false);
                campoContrasenaAdmin.setVisible(true);
                campoContrasenaAdmin.setManaged(true);
            }
    }
    @FXML private void inicioSesion(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneInicioSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) campoDni.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        campoDni.clear();
        campoNombre.clear();
        campoApellido.clear();
        campoDireccion.clear();
        campoRol.getSelectionModel().clearSelection();
        campoContrasenaAdmin.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

