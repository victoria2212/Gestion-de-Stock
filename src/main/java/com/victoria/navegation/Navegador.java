package com.victoria.navegation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class Navegador {
     private static BorderPane root;

    public static void setRoot(BorderPane rootPane) {
        root = rootPane;
    }    
     public static void cambiarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                Navegador.class.getResource(fxml)
            );
            Parent vista = loader.load();

            root.setCenter(vista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
