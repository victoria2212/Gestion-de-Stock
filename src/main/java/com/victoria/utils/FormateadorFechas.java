package com.victoria.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormateadorFechas {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final DateTimeFormatter FORMATTER_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Timestamp
    public static String formatear(Timestamp timestamp) {

        if (timestamp == null) {
            return "";
        }

        LocalDateTime fecha = timestamp.toLocalDateTime();

        return fecha.format(FORMATTER);
    }

    // LocalDateTime
    public static String formatear(LocalDateTime fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.format(FORMATTER);
    }

    // LocalDate
    public static String formatear(LocalDate fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.format(FORMATTER_FECHA);
    }
}
