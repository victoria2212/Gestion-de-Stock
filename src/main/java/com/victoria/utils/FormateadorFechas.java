package com.victoria.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormateadorFechas {
     private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Método para formatear Timestamp
    public static String formatear(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }

        LocalDateTime fecha = timestamp.toLocalDateTime();
        return fecha.format(FORMATTER);
    }

    // (Opcional) Método si en algún momento usás LocalDateTime directo
    public static String formatear(LocalDateTime fecha) {
        if (fecha == null) {
            return "";
        }

        return fecha.format(FORMATTER);
    }
    
}
