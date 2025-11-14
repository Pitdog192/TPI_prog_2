package dao;

import config.DatabaseConnection;
import entities.SeguroVehicular;
import entities.Cobertura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arroquigarays
 */
public class SeguroVehicularDaoImpl implements GenericDao<SeguroVehicular> {
    // SQL Statements
    private static final String INSERT_SQL =
        "INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
        "SELECT id, eliminado, aseguradora, nro_poliza, cobertura, vencimiento " +
        "FROM seguro_vehicular WHERE id = ?";

    private static final String SELECT_ALL_SQL =
        "SELECT id, eliminado, aseguradora, nro_poliza, cobertura, vencimiento " +
        "FROM seguro_vehicular";

    private static final String UPDATE_SQL =
        "UPDATE seguro_vehicular SET eliminado = ?, aseguradora = ?, nro_poliza = ?, " +
        "cobertura = ?, vencimiento = ? WHERE id = ?";

    private static final String DELETE_SQL =
        "DELETE FROM seguro_vehicular WHERE id = ?";

    // ========================================
    // Métodos con conexión propia
    // ========================================

    @Override
    public SeguroVehicular crear(SeguroVehicular entidad) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(entidad, conn);
        }
    }

    @Override
    public SeguroVehicular leer(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leer(id, conn);
        }
    }

    @Override
    public List<SeguroVehicular> leerTodos() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leerTodos(conn);
        }
    }

    @Override
    public void actualizar(SeguroVehicular entidad) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            actualizar(entidad, conn);
        }
    }

    @Override
    public void eliminar(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            eliminar(id, conn);
        }
    }

    // ========================================
    // Métodos con conexión externa (transacciones)
    // ========================================

    @Override
    public SeguroVehicular crear(SeguroVehicular entidad, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, Boolean.TRUE.equals(entidad.getEliminado()));
            ps.setString(2, entidad.getAseguradora());
            ps.setString(3, entidad.getNro_poliza());
            ps.setString(4,
                    entidad.getCobertura() != null
                            ? entidad.getCobertura().name()
                            : null);
            ps.setDate(5, new java.sql.Date(entidad.getVencimiento().getTime()));

            ps.executeUpdate();

            // Recuperar el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
                }
            }
        }
        return entidad;
    }

    @Override
    public SeguroVehicular leer(long id, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeguro(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<SeguroVehicular> leerTodos(Connection conn) throws SQLException {
        List<SeguroVehicular> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowToSeguro(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(SeguroVehicular entidad, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setBoolean(1, Boolean.TRUE.equals(entidad.getEliminado()));
            ps.setString(2, entidad.getAseguradora());
            ps.setString(3, entidad.getNro_poliza());
            ps.setString(4,
                    entidad.getCobertura() != null
                            ? entidad.getCobertura().name()
                            : null);
            ps.setDate(5, new java.sql.Date(entidad.getVencimiento().getTime()));
            ps.setLong(6, entidad.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(long id, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // ========================================
    // Helper para mapear ResultSet a objeto
    // ========================================

    private SeguroVehicular mapRowToSeguro(ResultSet rs) throws SQLException {
        SeguroVehicular seguro = new SeguroVehicular();
        seguro.setId(rs.getLong("id"));
        seguro.setEliminado(rs.getBoolean("eliminado"));
        seguro.setAseguradora(rs.getString("aseguradora"));
        seguro.setNro_poliza(rs.getString("nro_poliza"));

        String cobStr = rs.getString("cobertura");
        if (cobStr != null) {
            seguro.setCobertura(Cobertura.valueOf(cobStr));
        } else {
            seguro.setCobertura(null);
        }

        seguro.setVencimiento(rs.getDate("vencimiento"));
        return seguro;
    }
}