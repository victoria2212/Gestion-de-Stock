package com.victoria.utils;

import java.io.ByteArrayInputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VisorImagen {
      public static void mostrar(byte[] foto) {

        if (foto == null || foto.length == 0) {
            return;
        }

        Image imagenCompleta = new Image(new ByteArrayInputStream(foto));

        ImageView imageView = new ImageView(imagenCompleta);
        imageView.setPreserveRatio(true);

        // Ajusta el tamaño máximo de la vista ampliada
        imageView.setFitWidth(Math.min(imagenCompleta.getWidth(), 800));
        imageView.setFitHeight(Math.min(imagenCompleta.getHeight(), 800));

        StackPane contenedor = new StackPane(imageView);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setStyle("-fx-background-color: black; -fx-padding: 10;");

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Stage stage = new Stage();
        stage.setTitle("Vista previa de imagen");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(scroll, 850, 850));
        stage.show();
    }

}
