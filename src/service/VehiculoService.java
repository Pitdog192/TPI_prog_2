package service;

import config.DatabaseConnection;
import dao.VehiculoDaoImpl;
import entities.Vehiculo;
import entities.SeguroVehicular;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Arroquigarays
 */
public class VehiculoService implements GenericService<Vehiculo> {
    private final VehiculoDaoImpl vehiculoDao = new VehiculoDaoImpl();

    // =========================
    // Métodos simples (sin operaciones compuestas)
    // =========================

    @Override
    public Vehiculo insertar(Vehiculo entidad) throws SQLException {
        validar(entidad);
        // regla 1→1: si viene con id_seguro, chequear que no esté ya usado
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
    }

    // =========================
    // Regla 1 → 1 Seguro-Vehículo (a alto nivel)
    // =========================

    private void verificarSeguroNoAsociado(Integer idSeguro) throws SQLException {
        for (Vehiculo v : vehiculoDao.leerTodos()) {
            if (v.getId_seguro() != null && v.getId_seguro().equals(idSeguro)) {
                throw new IllegalStateException("El seguro con id=" + idSeguro + " ya está asociado a un vehículo");
            }
        }
    }

    private void verificarSeguroNoAsociadoEnOtroVehiculo(Long idVehiculoActual, Integer idSeguro) throws SQLException {
        for (Vehiculo v : vehiculoDao.leerTodos()) {
            if (!v.getId().equals(idVehiculoActual) &&
                v.getId_seguro() != null &&
                v.getId_seguro().equals(idSeguro)) {
                throw new IllegalStateException("El seguro con id=" + idSeguro + " ya está asociado a otro vehículo");
            }
        }
    }

    // =========================
    // Método compuesto con transacción:
    // crear B (Seguro) y luego A (Vehículo) asociado
    // =========================

    public Vehiculo crearVehiculoConSeguro(Vehiculo vehiculo, SeguroVehicular seguro) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Abrir transacción

            // Insertar seguro usando la MISMA conexión
            seguro = seguroServiceInsertTransaccional(seguro, conn);

            // Asociar el seguro al vehículo (1→1)
            vehiculo.setId_seguro(seguro.getId().intValue());
            validar(vehiculo);
            verificarSeguroNoAsociado(vehiculo.getId_seguro());

            // Crear vehículo usando la misma conexión
            vehiculo = vehiculoDao.crear(vehiculo, conn);

            conn.commit(); // Si todo OK
            return vehiculo;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir todo si algo falla
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error al crear vehículo con seguro en una transacción", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restablecer auto-commit
                    conn.close();             // Cerrar conexión
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Pequeño helper para usar el DAO de seguro con la misma Connection
    private SeguroVehicular seguroServiceInsertTransaccional(SeguroVehicular seguro, Connection conn) throws SQLException {
        SeguroVehicularService seguroService = new SeguroVehicularService();
        // Validamos con la lógica de dominio
        seguroServiceInsertValidando(seguro);
        // Insertamos usando la conexión externa
        return new dao.SeguroVehicularDaoImpl().crear(seguro, conn);
    }

    private void seguroServiceInsertValidando(SeguroVehicular seguro) {
        if (seguro == null) {
            throw new IllegalArgumentException("El seguro no puede ser null");
        }
        if (seguro.getAseguradora() == null || seguro.getAseguradora().isBlank()) {
            throw new IllegalArgumentException("La aseguradora es obligatoria");
        }
        if (seguro.getNro_poliza() == null || seguro.getNro_poliza().isBlank()) {
            throw new IllegalArgumentException("El número de póliza es obligatorio");
        }
        if (seguro.getVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria");
        }
    }
}