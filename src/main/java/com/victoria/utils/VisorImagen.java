package com.victoria.utils;

import java.io.ByteArrayInputStream;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class VisorImagen {

    public static void mostrar(byte[] foto) {

        if (foto == null || foto.length == 0) {
            return;
        }

        Image imagenCompleta = new Image(new ByteArrayInputStream(foto));

        // =====================================================
        // OBTENER EL TAMAÑO REAL DE LA PANTALLA ACTUAL
        // =====================================================

        Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();

        // Usamos como máximo el 80% del ancho/alto disponible,
        // así siempre queda margen para ver los botones de la ventana.
        double maxAnchoVentana = pantalla.getWidth() * 0.8;
        double maxAltoVentana = pantalla.getHeight() * 0.8;

        // La imagen en sí ocupa un poco menos que la ventana,
        // para dejar lugar al padding y al ScrollPane.
        double maxAnchoImagen = maxAnchoVentana - 60;
        double maxAltoImagen = maxAltoVentana - 60;

        ImageView imageView = new ImageView(imagenCompleta);
        imageView.setPreserveRatio(true);

        // Ajusta el tamaño máximo de la vista ampliada,
        // sin superar ni la imagen original ni el espacio disponible
        imageView.setFitWidth(Math.min(imagenCompleta.getWidth(), maxAnchoImagen));
        imageView.setFitHeight(Math.min(imagenCompleta.getHeight(), maxAltoImagen));

        StackPane contenedor = new StackPane(imageView);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setStyle("-fx-background-color: black; -fx-padding: 10;");

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Stage stage = new Stage();
        stage.setTitle("Vista previa de imagen");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(scroll, maxAnchoVentana, maxAltoVentana));

        // Centra la ventana en la pantalla, asegurando que
        // el borde superior (con los botones) sea visible
        stage.centerOnScreen();

        stage.show();
    }

}