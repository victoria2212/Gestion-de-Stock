package com.victoria.Main;

import com.victoria.navegation.Navegador;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    // Conexión disponible para toda la app@Override
public void start(Stage stage) throws Exception {

    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/com/victoria/Interfaces/MainLayout.fxml")
    );

    Parent root = loader.load();

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.setTitle("Sistema de Gestión de Stock");
    stage.setMaximized(true);
    stage.show();
    
    Platform.runLater(() -> {
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneInicioSesion.fxml");
});

    /*  MOSTRAR PRIMERA VISTA
    Navegador.cambiarVista("/com/victoria/Interfaces/SceneInicioSesion.fxml");
    */
}
    
     public static void main(String[] args) {
        launch(args);  // Llama a start()
    }
    

    
   
}
