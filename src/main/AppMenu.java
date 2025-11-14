package main;

import entities.Vehiculo;
import entities.SeguroVehicular;
import entities.Cobertura;
import service.VehiculoService;
import service.SeguroVehicularService;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Arroquigarays
 */
public class AppMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final VehiculoService vehiculoService = new VehiculoService();
    private final SeguroVehicularService seguroService = new SeguroVehicularService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            try {
                mostrarMenuPrincipal();
                String opcion = scanner.nextLine().trim().toUpperCase();

                switch (opcion) {
                    case "1":
                        menuVehiculos();
                        break;
                    case "2":
                        menuSeguros();
                        break;
                    case "X":
                        salir = true;
                        System.out.println("Saliendo de la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    // Menu principal
    private void mostrarMenuPrincipal() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1 - Gestión de Vehículos");
        System.out.println("2 - Gestión de Seguros");
        System.out.println("X - Salir");
        System.out.print("Seleccione una opción: ");
    }

    // ========================================
    // Menú Vehículos (A)
    // ========================================

    private void menuVehiculos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n----- MENÚ VEHÍCULOS -----");
            System.out.println("1 - Crear vehículo");
            System.out.println("2 - Leer vehículo por ID");
            System.out.println("3 - Listar vehículos");
            System.out.println("4 - Actualizar vehículo");
            System.out.println("5 - Eliminar lógico vehículo");
            System.out.println("6 - Buscar por dominio");
            System.out.println("7 - Crear vehículo con seguro nuevo");
            System.out.println("X - Volver");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine().trim().toUpperCase();

            try {
                switch (opcion) {
                    case "1":
                        crearVehiculo();
                        break;
                    case "2":
                        leerVehiculoPorId();
                        break;
                    case "3":
                        listarVehiculos();
                        break;
                    case "4":
                        actualizarVehiculo();
                        break;
                    case "5":
                        eliminarVehiculo();
                        break;
                    case "6":
                        buscarVehiculoPorDominio();
                        break;
                    case "7":
                        crearVehiculoConSeguroNuevo();
                        break;
                    case "X":
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
               System.out.println("Error de base de datos: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Entrada numérica inválida: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error de validación: " + e.getMessage());
            }
        }
    }

    private void crearVehiculo() throws SQLException {
        System.out.println("\n*** Crear vehículo ***");

        System.out.print("Dominio: ");
        String dominio = scanner.nextLine().trim().toUpperCase();

        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim().toUpperCase();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine().trim().toUpperCase();

        System.out.print("Año (ENTER si no sabe): ");
        String anioStr = scanner.nextLine().trim();
        Integer anio = null;
        if (!anioStr.isEmpty()) {
            anio = Integer.parseInt(anioStr);
        }

        System.out.print("Número de chasis: ");
        String nroChasis = scanner.nextLine().trim().toUpperCase();

        System.out.print("ID de seguro (ENTER si no tiene): ");
        String idSeguroStr = scanner.nextLine().trim();
        Integer idSeguro = null;
        if (!idSeguroStr.isEmpty()) {
            idSeguro = Integer.parseInt(idSeguroStr);
        }

        Vehiculo v = new Vehiculo();
        v.setEliminado(false);
        v.setDominio(dominio);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setAnio(anio);
        v.setNro_chasis(nroChasis);
        v.setId_seguro(idSeguro);

        try {
            v = vehiculoService.insertar(v);
            System.out.println("Vehículo creado con éxito. ID generado: " + v.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }
    
    private void crearVehiculoConSeguroNuevo() throws SQLException, ParseException {
        System.out.println("\n*** Crear vehículo con seguro nuevo (TRANSACCIONAL) ***");

        // ========================
        // 1) Pedir datos del seguro
        // ========================
        System.out.println("== Datos del seguro ==");
        System.out.print("Aseguradora: ");
        String aseguradora = scanner.nextLine().trim().toUpperCase();

        System.out.print("Nro póliza: ");
        String nroPoliza = scanner.nextLine().trim().toUpperCase();

        Cobertura cobertura = leerCoberturaDesdeConsola(null);

        System.out.print("Fecha de vencimiento (yyyy-MM-dd): ");
        String fechaStr = scanner.nextLine().trim();
        Date vencimiento = dateFormat.parse(fechaStr);

        SeguroVehicular seguro = new SeguroVehicular();
        seguro.setEliminado(false);
        seguro.setAseguradora(aseguradora);
        seguro.setNro_poliza(nroPoliza);
        seguro.setCobertura(cobertura);
        seguro.setVencimiento(vencimiento);

        // ========================
        // 2) Pedir datos del vehículo
        // ========================
        System.out.println("\n== Datos del vehículo ==");
        System.out.print("Dominio: ");
        String dominio = scanner.nextLine().trim().toUpperCase();

        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim().toUpperCase();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine().trim().toUpperCase();

        System.out.print("Año (ENTER si no sabe): ");
        String anioStr = scanner.nextLine().trim();
        Integer anio = null;
        if (!anioStr.isEmpty()) {
            anio = Integer.parseInt(anioStr);
        }

        System.out.print("Número de chasis: ");
        String nroChasis = scanner.nextLine().trim().toUpperCase();

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setEliminado(false);
        vehiculo.setDominio(dominio);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio(anio);
        vehiculo.setNro_chasis(nroChasis);

        // ========================
        // 3) Llamar al método transaccional
        // ========================
        try {
            vehiculo = vehiculoService.crearVehiculoConSeguro(vehiculo, seguro);
            System.out.println("Vehículo y seguro creados con éxito (misma transacción).");
            System.out.println("ID vehículo: " + vehiculo.getId());
            System.out.println("ID seguro:   " + vehiculo.getId_seguro());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private void leerVehiculoPorId() throws SQLException {
        System.out.print("\nIngrese ID de vehículo: ");
        String idStr = scanner.nextLine().trim();
        long id = Long.parseLong(idStr);

        Vehiculo v = vehiculoService.getById(id);
        if (v == null) {
            System.out.println("No se encontró vehículo con ID " + id);
        } else {
            System.out.println(v);
        }
    }

    private void listarVehiculos() throws SQLException {
        System.out.println("\n*** Listado de vehículos ***");
        List<Vehiculo> lista = vehiculoService.getAll();
        if (lista.isEmpty()) {
            System.out.println("No hay vehículos cargados.");
        } else {
            for (Vehiculo v : lista) {
                System.out.println(v);
            }
        }
    }

    private void actualizarVehiculo() throws SQLException {
        System.out.print("\nIngrese ID de vehículo a actualizar: ");
        String idStr = scanner.nextLine().trim();
        long id = Long.parseLong(idStr);

        Vehiculo existente = vehiculoService.getById(id);
        if (existente == null) {
            System.out.println("No existe vehículo con ID " + id);
            return;
        }

        System.out.println("Dejar vacío para mantener el valor actual.");

        System.out.print("Dominio (" + existente.getDominio() + "): ");
        String dominio = scanner.nextLine().trim();
        if (!dominio.isEmpty()) {
            existente.setDominio(dominio.toUpperCase());
        }

        System.out.print("Marca (" + existente.getMarca() + "): ");
        String marca = scanner.nextLine().trim();
        if (!marca.isEmpty()) {
            existente.setMarca(marca.toUpperCase());
        }

        System.out.print("Modelo (" + existente.getModelo() + "): ");
        String modelo = scanner.nextLine().trim();
        if (!modelo.isEmpty()) {
            existente.setModelo(modelo.toUpperCase());
        }

        System.out.print("Año (" + existente.getAnio() + "): ");
        String anioStr = scanner.nextLine().trim();
        if (!anioStr.isEmpty()) {
            existente.setAnio(Integer.parseInt(anioStr));
        }

        System.out.print("Nro chasis (" + existente.getNro_chasis() + "): ");
        String nroChasis = scanner.nextLine().trim();
        if (!nroChasis.isEmpty()) {
            existente.setNro_chasis(nroChasis.toUpperCase());
        }

        System.out.print("ID seguro (" + existente.getId_seguro() + ", ENTER para null): ");
        String idSeguroStr = scanner.nextLine().trim();
        if (!idSeguroStr.isEmpty()) {
            existente.setId_seguro(Integer.parseInt(idSeguroStr));
        } else {
            existente.setId_seguro(null);
        }

        try {
            vehiculoService.actualizar(existente);
            System.out.println("Vehículo actualizado correctamente.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private void eliminarVehiculo() throws SQLException {
        System.out.print("\nIngrese ID de vehículo a eliminar lógicamente: ");
        String idStr = scanner.nextLine().trim();
        long id = Long.parseLong(idStr);

        Vehiculo v = vehiculoService.getById(id);
        if (v == null) {
            System.out.println("No existe vehículo con ID " + id);
            return;
        }

        v.setEliminado(true);
        vehiculoService.actualizar(v);
        System.out.println("Vehículo marcado como eliminado.");
    }

    private void buscarVehiculoPorDominio() throws SQLException {
        System.out.print("\nIngrese dominio a buscar: ");
        String dominio = scanner.nextLine().trim().toUpperCase();

        List<Vehiculo> lista = vehiculoService.getAll();
        Vehiculo encontrado = null;
        for (Vehiculo v : lista) {
            if (v.getDominio() != null && v.getDominio().equalsIgnoreCase(dominio)) {
                encontrado = v;
                break;
            }
        }

        if (encontrado == null) {
            System.out.println("No se encontró vehículo con dominio " + dominio);
        } else {
            System.out.println("Vehículo encontrado:");
            System.out.println(encontrado);
        }
    }

    // ========================================
    // Menú Seguros (B)
    // ========================================

    private void menuSeguros() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n----- MENÚ SEGUROS -----");
            System.out.println("1 - Crear seguro");
            System.out.println("2 - Leer seguro por ID");
            System.out.println("3 - Listar seguros");
            System.out.println("4 - Actualizar seguro");
            System.out.println("5 - Eliminar lógico seguro");
            System.out.println("X - Volver");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine().trim().toUpperCase();

            try {
                switch (opcion) {
                    case "1":
                        crearSeguro();
                        break;
                    case "2":
                        leerSeguroPorId();
                        break;
                    case "3":
                        listarSeguros();
                        break;
                    case "4":
                        actualizarSeguro();
                        break;
                    case "5":
                        eliminarSeguro();
                        break;
                    case "X":
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Entrada numérica inválida: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
            } catch (IllegalArgumentException e) {
                // para errores de enum Cobertura.valueOf
                System.out.println("Valor de cobertura inválido: " + e.getMessage());
            }
        }
    }

    private void mostrarOpcionesCobertura() {
        System.out.println("Tipos de cobertura disponibles:");
        for (Cobertura c : Cobertura.values()) {
            System.out.println(" - " + c.name());
        }
    }

    private Cobertura leerCoberturaDesdeConsola(Cobertura actual) {
        if (actual == null) {
            mostrarOpcionesCobertura();
            System.out.print("Cobertura (escriba exactamente alguno de los valores): ");
        } else {
            System.out.println("Cobertura actual: " + actual.name());
            mostrarOpcionesCobertura();
            System.out.print("Nueva cobertura (ENTER para mantener actual): ");
        }

        String input = scanner.nextLine().trim().toUpperCase();
        if (input.isEmpty() && actual != null) {
            return actual; // mantener
        }
        if (input.isEmpty()) {
            return null; // creación y no cargó nada (el service después validará si es obligatorio)
        }
        return Cobertura.valueOf(input); // puede lanzar IllegalArgumentException si escribe mal
    }

    private void crearSeguro() throws SQLException, ParseException {
        System.out.println("\n*** Crear seguro ***");

        System.out.print("Aseguradora: ");
        String aseguradora = scanner.nextLine().trim().toUpperCase();

        System.out.print("Nro póliza: ");
        String nroPoliza = scanner.nextLine().trim().toUpperCase();

        Cobertura cobertura = leerCoberturaDesdeConsola(null);

        System.out.print("Fecha de vencimiento (yyyy-MM-dd): ");
        String fechaStr = scanner.nextLine().trim();
        Date vencimiento = dateFormat.parse(fechaStr);

        SeguroVehicular s = new SeguroVehicular();
        s.setEliminado(false);
        s.setAseguradora(aseguradora);
        s.setNro_poliza(nroPoliza);
        s.setCobertura(cobertura);
        s.setVencimiento(vencimiento);

        try {
            s = seguroService.insertar(s);
            System.out.println("Seguro creado con éxito. ID generado: " + s.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private void leerSeguroPorId() throws SQLException {
        System.out.print("\nIngrese ID de seguro: ");
        String idStr = scanner.nextLine().trim();
        long id = Long.parseLong(idStr);

        SeguroVehicular s = seguroService.getById(id);
        if (s == null) {
            System.out.println("No se encontró seguro con ID " + id);
        } else {
            System.out.println(s);
        }
    }

    private void listarSeguros() throws SQLException {
        System.out.println("\n*** Listado de seguros ***");
        List<SeguroVehicular> lista = seguroService.getAll();
        if (lista.isEmpty()) {
            System.out.println("No hay seguros cargados.");
        } else {
            for (SeguroVehicular s : lista) {
                System.out.println(s);
            }
        }
    }

    private void actualizarSeguro() throws SQLException, ParseException {
        System.out.print("\nIngrese ID de seguro a actualizar: ");
        String idStr = scanner.nextLine().trim();
        long id = Long.parseLong(idStr);

        SeguroVehicular existente = seguroService.getById(id);
        if (existente == null) {
            System.out.println("No existe seguro con ID " + id);
            return;
        }

        System.out.println("Dejar vacío para mantener el valor actual.");

        System.out.print("Aseguradora (" + existente.getAseguradora() + "): ");
        String aseguradora = scanner.nextLine().trim();
        if (!aseguradora.isEmpty()) {
            existente.setAseguradora(aseguradora.toUpperCase());
        }

        System.out.print("Nro póliza (" + existente.getNro_poliza() + "): ");
        String nroPoliza = scanner.nextLine().trim();
        if (!nroPoliza.isEmpty()) {
            existente.setNro_poliza(nroPoliza.toUpperCase());
        }

        // Cobertura con enum
        Cobertura nuevaCobertura = leerCoberturaDesdeConsola(existente.getCobertura());
        existente.setCobertura(nuevaCobertura);

        System.out.print("Vencimiento (" + dateFormat.format(existente.getVencimiento()) + ", formato yyyy-MM-dd): ");
        String fechaStr = scanner.nextLine().trim();
        if (!fechaStr.isEmpty()) {
            existente.setVencimiento(dateFormat.parse(fechaStr));
        }

        try {
            seguroService.actualizar(existente);
            System.out.println("Seguro actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private void eliminarSeguro() throws SQLException {
        System.out.print("\nIngrese ID de seguro a eliminar lógicamente: ");
        String idStr = scanner.nextLine().trim();
        long id = Long.parseLong(idStr);

        SeguroVehicular s = seguroService.getById(id);
        if (s == null) {
            System.out.println("No existe seguro con ID " + id);
            return;
        }

        s.setEliminado(true);
        seguroService.actualizar(s);
        System.out.println("Seguro marcado como eliminado.");
    }
}