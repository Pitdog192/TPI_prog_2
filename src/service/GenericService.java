package service;

import java.sql.SQLException;
import java.util.List;

public interface GenericService<T> {

    T insertar(T entidad) throws SQLException;
    T actualizar(T entidad) throws SQLException;
    void eliminar(long id) throws SQLException;
    T getById(long id) throws SQLException;
    List<T> getAll() throws SQLException;

    //Método opcional para leer con detalle (JOIN)
    default T getByIdConDetalle(long id) throws SQLException {
        throw new UnsupportedOperationException("getByIdConDetalle no implementado en este servicio");
    }
}