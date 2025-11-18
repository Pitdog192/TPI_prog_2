package service;

import config.DatabaseConnection;
import dao.VehiculoDaoImpl;
import dao.SeguroVehicularDaoImpl;
import entities.Vehiculo;
import entities.SeguroVehicular;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestionar Vehículos
 * @author Arroquigarays
 */
public class VehiculoService implements GenericService<Vehiculo> {
    
    private final VehiculoDaoImpl vehiculoDao;
    private final SeguroVehicularDaoImpl seguroDao;
    private final SeguroVehicularService seguroService;

    public VehiculoService() {
        this.vehiculoDao = new VehiculoDaoImpl();
        this.seguroDao = new SeguroVehicularDaoImpl();
        this.seguroService = new SeguroVehicularService();
    }

    // =========================
    // Métodos CRUD básicos
    // =========================

    @Override
    public Vehiculo insertar(Vehiculo entidad) throws SQLException {
        validar(entidad);
        
        // Regla 1→1: si viene con id_seguro, verificar que no esté ya usado
        if (entidad.getId_seguro() != null) {
            verificarSeguroNoAsociado(entidad.getId_seguro());
        }
        
        return vehiculoDao.crear(entidad);
    }

    @Override
    public Vehiculo actualizar(Vehiculo entidad) throws SQLException {
        if (entidad.getId() == null || entidad.getId() == 0L) {
            throw new IllegalArgumentException("El ID del vehículo no puede ser null o 0 para actualizar");
        }
        
        validar(entidad);
        
        if (entidad.getId_seguro() != null) {
            verificarSeguroNoAsociadoEnOtroVehiculo(entidad.getId(), entidad.getId_seguro());
        }
        
        vehiculoDao.actualizar(entidad);
        return entidad;
    }

    @Override
    public void eliminar(long id) throws SQLException {
        vehiculoDao.eliminar(id);
    }

    @Override
    public Vehiculo getById(long id) throws SQLException {
        return vehiculoDao.leer(id);
    }

    @Override
    public List<Vehiculo> getAll() throws SQLException {
        return vehiculoDao.leerTodos();
    }

    // =========================
    // 👉 Método con JOIN (corregido)
    // =========================

    @Override
    public Vehiculo getByIdConDetalle(long id) throws SQLException {
        return vehiculoDao.leerConDetalle(id);
    }

    // =========================
    // Validaciones de dominio
    // =========================

    private void validar(Vehiculo v) {
        if (v == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null");
        }
        if (v.getDominio() == null || v.getDominio().isBlank()) {
            throw new IllegalArgumentException("El dominio es obligatorio");
        }
        if (v.getMarca() == null || v.getMarca().isBlank()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }
        if (v.getModelo() == null || v.getModelo().isBlank()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }
        if (v.getNro_chasis() == null || v.getNro_chasis().isBlank()) {
            throw new IllegalArgumentException("El número de chasis es obligatorio");
        }
    }

    // =========================
    // Regla 1 → 1 Seguro-Vehículo
    // =========================

    /**
     * Verifica que un seguro no esté ya asociado a ningún vehículo
     */
    private void verificarSeguroNoAsociado(Integer idSeguro) throws SQLException {
        List<Vehiculo> vehiculos = vehiculoDao.leerTodos();
        
        for (Vehiculo v : vehiculos) {
            if (v.getId_seguro() != null && v.getId_seguro().equals(idSeguro)) {
                throw new IllegalStateException(
                    "El seguro con id=" + idSeguro + " ya está asociado al vehículo con id=" + v.getId()
                );
            }
        }
    }

    /**
     * Verifica que un seguro no esté asociado a otro vehículo (distinto al actual)
     */
    private void verificarSeguroNoAsociadoEnOtroVehiculo(Long idVehiculoActual, Integer idSeguro) throws SQLException {
        List<Vehiculo> vehiculos = vehiculoDao.leerTodos();
        
        for (Vehiculo v : vehiculos) {
            if (!v.getId().equals(idVehiculoActual) &&
                v.getId_seguro() != null &&
                v.getId_seguro().equals(idSeguro)) {
                throw new IllegalStateException(
                    "El seguro con id=" + idSeguro + " ya está asociado al vehículo con id=" + v.getId()
                );
            }
        }
    }

    // =========================
    // Método compuesto con transacción:
    // Crear Seguro + Vehículo asociado
    // =========================

    /**
     * Crea un vehículo junto con su seguro en una única transacción
     * @param vehiculo El vehículo a crear (sin id_seguro)
     * @param seguro El seguro a crear y asociar
     * @return El vehículo creado con su seguro asociado
     * @throws SQLException Si hay error en la transacción
     */
    public Vehiculo crearVehiculoConSeguro(Vehiculo vehiculo, SeguroVehicular seguro) throws SQLException {
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Validar seguro
            seguroService.validarSeguro(seguro);
            
            // 2. Insertar seguro usando la MISMA conexión
            seguro = seguroDao.crear(seguro, conn);

            // 3. Asociar el seguro al vehículo (1→1)
            vehiculo.setId_seguro(seguro.getId().intValue());
            
            // 4. Validar vehículo
            validar(vehiculo);

            // 5. Crear vehículo usando la misma conexión
            vehiculo = vehiculoDao.crear(vehiculo, conn);

            // 6. Commit si todo OK
            conn.commit();
            
            return vehiculo;

        } catch (Exception e) {
            // Rollback en caso de error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error al crear vehículo con seguro en transacción: " + e.getMessage(), e);
            
        } finally {
            // Restablecer y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}