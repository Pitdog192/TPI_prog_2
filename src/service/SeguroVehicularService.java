package service;

import dao.SeguroVehicularDaoImpl;
import entities.SeguroVehicular;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Servicio para gestionar Seguros Vehiculares
 * @author Arroquigarays
 */
public class SeguroVehicularService implements GenericService<SeguroVehicular> {
    
    private final SeguroVehicularDaoImpl seguroDao;

    public SeguroVehicularService() {
        this.seguroDao = new SeguroVehicularDaoImpl();
    }

    // =========================
    // Métodos CRUD básicos
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
        
        validarParaActualizacion(entidad);
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

    /**
     * Validación estricta para inserción (no permite seguros vencidos)
     */
    private void validar(SeguroVehicular s) {
        validarCamposObligatorios(s);
        
        // Al crear, no permitir seguros ya vencidos
        if (s.getVencimiento().before(new Date())) {
            throw new IllegalArgumentException("No se puede crear un seguro con fecha de vencimiento pasada");
        }
    }

    /**
     * Validación flexible para actualización (permite seguros vencidos)
     */
    private void validarParaActualizacion(SeguroVehicular s) {
        validarCamposObligatorios(s);
        // Al actualizar, SÍ permitimos modificar seguros vencidos
    }

    /**
     * Validación de campos obligatorios (usada internamente y desde VehiculoService)
     */
    public void validarSeguro(SeguroVehicular s) {
        validarCamposObligatorios(s);
    }

    private void validarCamposObligatorios(SeguroVehicular s) {
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
        if (s.getCobertura() == null) {
            throw new IllegalArgumentException("La cobertura es obligatoria");
        }
    }

    /**
     * Verifica si un seguro está vencido
     */
    public boolean estaVencido(SeguroVehicular seguro) {
        return seguro.getVencimiento().before(new Date());
    }

    /**
     * Verifica si un seguro está vencido por ID
     */
    public boolean estaVencido(long id) throws SQLException {
        SeguroVehicular seguro = getById(id);
        return seguro != null && estaVencido(seguro);
    }
}