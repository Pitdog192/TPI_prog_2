/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.Scanner;

/**
 *
 * @author Arroquigarays
 */
public class AppMenu {
        /**
     * Opciones de Vehiculos (1-4):
     * 1. Crear Vehiculo: Permite crear vehiculo
     * 2. Listar Vehiculo: Lista todos los vehiculos
     * 3. Actualizar Vehiculo: Actualiza datos de un vehiculo
     * 4. Eliminar Vehiculo: Soft delete de vehiculo (Agrega fecha de eliminacion tipo flag)
     *
     * Opciones de Seguro (5-10):
     * 5. Crear Seguro: Crea domicilio independiente (sin asociar a persona)
     * 6. Listar Seguro: Lista todos los domicilios activos
     * 7. Actualizar Seguro por ID: Actualiza domicilio directamente (afecta a TODAS las personas)
     * 8. Eliminar Seguro por ID: Elimina un seguro dejando en null el id en la tabla de vehiculos
     * 9. Actualizar Seguro por ID de Vehiculo: Busca vehiculo primero, luego actualiza su seguro
     * 10. Eliminar Seguro por ID de Vehiculo: Se actualiza la FK primero en null, y luego luego elimina el seguro
     *
     * Opción de salida:
     * 0. Salir: Termina la aplicación
     */
    public static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENU =========");
        System.out.println("1. Crear Vehiculo");
        System.out.println("2. Listar Vehiculos");
        System.out.println("3. Actualizar Vehiculo");
        System.out.println("4. Eliminar Vehiculo");
        System.out.println("5. Crear Seguro");
        System.out.println("6. Listar Seguros");
        System.out.println("7. Actualizar Seguro por ID");
        System.out.println("8. Eliminar Seguro por ID");
        System.out.println("9. Actualizar Seguro por ID de Vehiculo");
        System.out.println("10. Eliminar Seguro por ID de Vehiculo");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opcion: ");
    }
}
