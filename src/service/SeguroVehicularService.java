package service;

import dao.SeguroVehicularDaoImpl;
import entities.SeguroVehicular;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Arroquigarays
 */
public class SeguroVehicularService implements GenericService<SeguroVehicular> {
    private final SeguroVehicularDaoImpl seguroDao = new SeguroVehicularDaoImpl();

    // =========================
    // Métodos simples (sin transacción compuesta)
    // =========================

    @Override
    public SeguroVehicular insertar(SeguroVehicular entidad) throws SQLException {
        validar(entidad);
        return seguroDao.crear(entidad);
    }

    @Override
    public SeguroVehicular actualizar(SeguroVehicular entidad) throws SQLException {
        if (entidad.getId() == null || entidad.getId() == 0L) {
            throw new IllegalArgumentException("El ID del seguro no puede ser null o 0 para actualizar");
        }
        validar(entidad);
        seguroDao.actualizar(entidad);
        return entidad;
    }

    @Override
    public void eliminar(long id) throws SQLException {
        seguroDao.eliminar(id);
    }

    @Override
    public SeguroVehicular getById(long id) throws SQLException {
        return seguroDao.leer(id);
    }

    @Override
    public List<SeguroVehicular> getAll() throws SQLException {
        return seguroDao.leerTodos();
    }

    // =========================
    // Validaciones de dominio
    // =========================

    private void validar(SeguroVehicular s) {
        if (s == null) {
            throw new IllegalArgumentException("El seguro no puede ser null");
        }
        if (s.getAseguradora() == null || s.getAseguradora().isBlank()) {
            throw new IllegalArgumentException("La aseguradora es obligatoria");
        }
        if (s.getNro_poliza() == null || s.getNro_poliza().isBlank()) {
            throw new IllegalArgumentException("El número de póliza es obligatorio");
        }
        if (s.getVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria");
        }
        if (s.getVencimiento().before(new Date())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede estar vencida");
        }
        if (s.getCobertura() == null) {
            throw new IllegalArgumentException("La cobertura es obligatoria");
        }
    }
}