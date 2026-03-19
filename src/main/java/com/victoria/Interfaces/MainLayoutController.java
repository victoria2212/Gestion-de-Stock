package com.victoria.Interfaces;

import com.victoria.navegation.*;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainLayoutController {
     @FXML
    private BorderPane root;

    @FXML
    public void initialize() {
        Navegador.setRoot(root);
    }
    
}
