package com.victoria.Interfaces;

import java.time.LocalDate;

import com.victoria.Dto.EmpleadoDTO;
import com.victoria.Gestores.GestorEmpleado;
import com.victoria.navegation.Navegador;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ModificarEmpleado {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtDni;
    @FXML private DatePicker dpDiaAlta;

    private EmpleadoDTO empleado;

    private GestorEmpleado gestorEmpleado =
        GestorEmpleado.getInstance();

    Object dato = Navegador.getDato();

    @FXML
    public void initialize() {

        if (dato instanceof EmpleadoDTO empleado) {

            this.empleado = empleado;

            // =========================
            // CARGAR DATOS
            // =========================

            txtNombre.setText(
                empleado.getNombre()
            );

            txtApellido.setText(
                empleado.getApellido()
            );

            txtDireccion.setText(
                empleado.getDireccion()
            );

            txtDni.setText(
                String.valueOf(
                    empleado.getDni()
                )
            );

            dpDiaAlta.setValue(
                empleado.getDia_de_alta()
            );

            // =========================
            // BLOQUEAR CAMPOS
            // =========================

            txtDni.setEditable(false);

            dpDiaAlta.setDisable(true);
        }
    }

    @FXML
    private void guardarCambios() {

        try {

            // =========================
            // ACTUALIZAR DTO
            // =========================

            empleado.setNombre(
                txtNombre.getText()
            );

            empleado.setApellido(
                txtApellido.getText()
            );

            empleado.setDireccion(
                txtDireccion.getText()
            );

            // =========================
            // ACTUALIZAR EN BD
            // =========================

            gestorEmpleado.modificarEmpleado(
                empleado
            );

            System.out.println(
                "Empleado actualizado correctamente."
            );

            // =========================
            // VOLVER
            // =========================

            Navegador.cambiarVista(
                "/com/victoria/Interfaces/SceneGestionEmpleados.fxml"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
 
   @FXML
    private void volverGestionEmpleado() {

        Navegador.cambiarVista(
            "/com/victoria/Interfaces/SceneGestionEmpleados.fxml"
        );
    }
}