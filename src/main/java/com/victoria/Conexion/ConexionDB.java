package com.victoria.Conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();

        // Busca config.properties en la MISMA carpeta donde está el .exe
        File archivoConfig = new File("config.properties");

        try (FileInputStream fis = new FileInputStream(archivoConfig)) {
            props.load(fis);
        } catch (IOException e) {
            System.out.println("⚠️ No se encontró config.properties, usando valores por defecto.");
        }

        URL = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/gestion_stock");
        USER = props.getProperty("db.user", "postgres");
        PASSWORD = props.getProperty("db.password", "");
    }

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