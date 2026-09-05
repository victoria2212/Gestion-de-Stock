package com.victoria.Interfaces;


import com.victoria.Gestores.GestorEmpleado;
import com.victoria.Gestores.GestorTienda;
import com.victoria.navegation.Navegador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;


public class InicioSesion {

    @FXML private TextField campoNombre;  // Campo de texto del FXML
    @FXML private TextField campoDni;
    
    @FXML
    private void initialize() {
    // Esto hace que solo se pueda ingresar números en campoDni
            campoDni.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    campoDni.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
    }

    // Este método se ejecuta cuando el usuario hace clic en el botón "Ingresar"
    @FXML
    private void handleIngresar() {

    String dniTexto = campoDni.getText().trim();

    if (dniTexto.isEmpty()) {
        mostrarAlerta("Debe ingresar su DNI.");
        return;
    }

    int dni = Integer.parseInt(dniTexto);

    GestorEmpleado gestor = GestorEmpleado.getInstance();
    GestorTienda gestorTienda = GestorTienda.getInstance();

    if (gestor.existeEmpleado(dni)) {

        gestor.actualizarUltimaConexion(dni);   // ← NUEVO
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");

    } else if (gestorTienda.esOwner(dni)) {

        Navegador.cambiarVista("/com/victoria/Interfaces/SceneMenuPrincipal.fxml");

    } else {
        mostrarAlerta("El DNI ingresado no se encuentra registrado.");
        }
    }

    // Método para mostrar mensajes en pantalla
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
    

