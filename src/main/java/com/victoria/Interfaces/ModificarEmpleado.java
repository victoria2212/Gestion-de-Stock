package com.victoria.Interfaces;

import com.victoria.Dto.EmpleadoDTO;
import com.victoria.Gestores.GestorEmpleado;
import com.victoria.navegation.Navegador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ModificarEmpleado {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtDni;
    @FXML private TextField txtContacto;
    @FXML private ComboBox<String> cbRol;
    @FXML private DatePicker dpDiaAlta;

    private EmpleadoDTO empleado;

    private GestorEmpleado gestorEmpleado = GestorEmpleado.getInstance();

    Object dato = Navegador.getDato();

    @FXML
    public void initialize() {

        cbRol.setItems(FXCollections.observableArrayList("Administrador", "Empleado"));

        txtContacto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtContacto.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        if (dato instanceof EmpleadoDTO empleado) {

            this.empleado = empleado;

            txtNombre.setText(empleado.getNombre());
            txtApellido.setText(empleado.getApellido());
            txtDireccion.setText(empleado.getDireccion());
            txtDni.setText(String.valueOf(empleado.getDni()));
            txtContacto.setText(empleado.getContacto());
            cbRol.setValue(empleado.getRol());
            dpDiaAlta.setValue(empleado.getDia_de_alta());

            txtDni.setEditable(false);
            dpDiaAlta.setDisable(true);
        }
    }

    @FXML
    private void guardarCambios() {

        try {
            empleado.setNombre(txtNombre.getText());
            empleado.setApellido(txtApellido.getText());
            empleado.setDireccion(txtDireccion.getText());
            empleado.setContacto(txtContacto.getText());
            empleado.setRol(cbRol.getValue());

            gestorEmpleado.modificarEmpleado(empleado);

            System.out.println("Empleado actualizado correctamente.");

            Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionEmpleados.fxml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volverGestionEmpleado() {
        Navegador.cambiarVista("/com/victoria/Interfaces/SceneGestionEmpleados.fxml");
    }
}