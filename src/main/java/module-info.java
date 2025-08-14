/*module com.victoria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.victoria to javafx.fxml;
    exports com.victoria;
}*/

module com.victoria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    //opens com.victoria.Controllers to javafx.fxml;

    opens com.victoria.Interfaces to javafx.fxml;
    opens com.victoria.Clases to javafx.fxml;
    opens com.victoria.Conexion to javafx.fxml;
    opens com.victoria.Main to javafx.fxml;
     opens com.victoria.Dto to javafx.base;  // acceso reflexivo a JavaFX para se pueda cargar en la tabla de visualizacion 

    //exports com.victoria.Controllers;
    exports com.victoria.Main;
    exports com.victoria.Clases;
    exports com.victoria.Conexion;
    
}