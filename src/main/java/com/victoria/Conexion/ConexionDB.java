package com.victoria.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionDB {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_stock";
    private static final String USER = "postgres";
    private static final String PASSWORD = "victoria22:)";
    /*
     *  Como vamos a acceder a la conexión desde otras clases 
     * (como el controlador del FXML), 
     * lo mejor es usar el método public static Connection conectar()
     */
    //conexion a la bd
    public Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conectado a PostgreSQL");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión");
            e.printStackTrace();
            return null;
        }
    }
   
}

