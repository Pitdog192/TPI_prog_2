/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Arroquigarays
 */
public class DataBaseConnection {
        
    // Datos de conexión - Se configuran directamente en el código
    private static final String URL = "jdbc:mysql://localhost:3306/prueba_instalacion";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            // Carga del driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Se lanza una excepción en caso de que el driver no esté disponible o falle
            throw new RuntimeException("Error: No se encontró el driver JDBC.", e);
        }
    }

    /**
     * Valida que los parámetros de configuración sean válidos.
     * Llamado una sola vez desde el bloque static.
     *
     * Reglas:
     * - URL y USER no pueden ser null ni estar vacíos
     * - PASSWORD puede ser vacío (común en MySQL local root sin password)
     * - PASSWORD no puede ser null
     *
     * @throws IllegalStateException Si la configuración es inválida
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
