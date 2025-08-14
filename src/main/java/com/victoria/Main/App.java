package com.victoria.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    // Conexión disponible para toda la app

    @Override
    public void start(Stage stage) throws Exception {

    // Cargamos la interfaz inicial (FXML)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/victoria/Interfaces/SceneInicioSesion.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión de Stock");
        stage.show();
    }
     public static void main(String[] args) {
        launch(args);  // Llama a start()
    }
   
}
