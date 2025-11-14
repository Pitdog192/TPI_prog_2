package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Arroquigarays
 * @param <T>
 */
public interface GenericDao<T> {
    // Usando conexión propia (abre/cierra)
    T crear(T entidad) throws SQLException;
    T leer(long id) throws SQLException;
    List<T> leerTodos() throws SQLException;
    void actualizar(T entidad) throws SQLException;
    void eliminar(long id) throws SQLException;

    // Usando conexión externa
    T crear(T entidad, Connection conn) throws SQLException;
    T leer(long id, Connection conn) throws SQLException;
    List<T> leerTodos(Connection conn) throws SQLException;
    void actualizar(T entidad, Connection conn) throws SQLException;
    void eliminar(long id, Connection conn) throws SQLException;
}