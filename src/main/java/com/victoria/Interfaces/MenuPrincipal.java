package com.victoria.Interfaces;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuPrincipal {

    @FXML private Button btnAltaProductos;
    @FXML private Button btnEmpleados;
    @FXML private MenuItem menuItemGestionRopa;
    @FXML private MenuItem menuItemGestionAccs;
    @FXML private MenuItem menuItemVisualizacionRopa;
    @FXML private MenuItem menuItemVisualizacionAccs;

    @FXML
    public void initialize() {
        btnAltaProductos.setOnAction(e -> abrirAltaProducto());
        btnEmpleados.setOnAction(e -> mostrarMensaje("Empleados clickeado"));

        menuItemGestionRopa.setOnAction(this::abrirGestionRopa);
        menuItemGestionAccs.setOnAction(this::abrirGestionAccs);
        
        menuItemVisualizacionRopa.setOnAction(this::abrirVisualizacionRopa);
        menuItemVisualizacionAccs.setOnAction(this::abrirVisualizacionAccs);
        //menuItemVisualizacionRopa.setOnAction(e -> cambiarEscena("SceneVisualizacionStockRopa.fxml", "Stock de Ropa"));
    }
    private void abrirAltaProducto() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneAltaProducto.fxml"));
        Parent root = loader.load();

        Stage stageActual = (Stage) btnAltaProductos.getScene().getWindow();
        stageActual.setScene(new Scene(root));
        stageActual.setTitle("Alta de Producto");

    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Error al abrir la escena de Alta de Producto");
        }
    }

    private void abrirGestionRopa(ActionEvent event) {   
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneGestionStockRopa.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnAltaProductos.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Gestionar Stock de Ropa");
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir Gestion de Stock de Ropa");
        }
        
    }

    private void abrirGestionAccs(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneGestionStockAccs.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnAltaProductos.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Gestionar Stock de Accesorios");
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir Gestion de Stock de Accesorios");
        }
    }
    private void abrirVisualizacionRopa(ActionEvent event) {  
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneVisualizacionStockRopa.fxml"));
        Parent root = loader.load();

        // Usamos un nodo visible para obtener el Stage actual
        /*
         * Esta línea obtiene la ventana actual (el Stage) usando cualquier nodo que ya esté en pantalla. En este caso, se usa el botón btnAltaProductos porque:
            - Ya está cargado en la escena.
            - Es un Node, y los Node tienen acceso a su Scene.
            - La Scene tiene acceso al Window, que es el Stage.
            ¿Por qué no usamos el MenuItem directamente?
        Porque los MenuItem no son nodos gráficos en JavaFX. No tienen una Scene, ni están en el árbol de nodos. Por eso, no podés hacer:
            ((MenuItem) event.getSource()).getScene().getWindow(); // ❌ Esto no funciona
        Entonces, ¿por qué usar btnAltaProductos?
        Porque es una forma segura de acceder al Stage actual, sin importar desde qué parte del controlador se llama el método. Aunque estés en el método abrirVisualizacionRopa, el botón btnAltaProductos sigue existiendo en la escena, y por eso podés usarlo como "puerta de entrada" al Stage.
        ¿Hay una forma más limpia?
        Sí, podés usar cualquier otro nodo visible, como un MenuBar, un Pane, o incluso guardar el Stage en una variable global cuando se inicializa la escena. Pero si ya tenés un botón visible y accesible, usarlo es totalmente válido.
         */
        Stage stage = (Stage) btnAltaProductos.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Stock de Ropa");
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la escena de Stock de Ropa");
        }
    }
    private void abrirVisualizacionAccs(ActionEvent event) {  
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneVisualizacionStockAccs.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnAltaProductos.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Stock de Accesorios");
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la escena de Stock de Accesorios");
        }
    }

    // PARAMETRICE EL METODO PARA UN CODIGO MAS LIMPIO
   /*private void cambiarEscena(ActionEvent event, String nombreFXML, String tituloVentana) {
    try {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/victoria/Interfaces/" + nombreFXML));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreFXML));
        Parent root = loader.load();

        // Obtiene la ventana desde el nodo que disparó el evento
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.setScene(new Scene(root));
        stageActual.setTitle(tituloVentana);

    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Error al abrir la escena: " + nombreFXML);
    }
    }*/

    private void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
        // Si quieres mostrar alerta visual:
        // Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        // alert.showAndWait();
    }

}