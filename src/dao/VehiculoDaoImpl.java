package dao;

import config.DatabaseConnection;
import entities.Vehiculo;
import entities.SeguroVehicular;
import entities.Cobertura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Vehiculo
 * @author Arroquigarays
 */
public class VehiculoDaoImpl implements GenericDao<Vehiculo> {
    
    // ========================================
    // SQL Statements
    // ========================================
    
    private static final String INSERT_SQL =
        "INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
        "SELECT id, eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro " +
        "FROM vehiculos WHERE id = ?";

    private static final String SELECT_ALL_SQL =
        "SELECT id, eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro " +
        "FROM vehiculos";

    private static final String UPDATE_SQL =
        "UPDATE vehiculos SET eliminado = ?, dominio = ?, marca = ?, modelo = ?, anio = ?, " +
        "nro_chasis = ?, id_seguro = ? WHERE id = ?";

    private static final String DELETE_SQL =
        "DELETE FROM vehiculos WHERE id = ?";

    private static final String SELECT_WITH_SEGURO_SQL =
        "SELECT v.id AS v_id, v.eliminado AS v_eliminado, v.dominio, v.marca, v.modelo, v.anio, v.nro_chasis, v.id_seguro, " +
        "s.id AS s_id, s.eliminado AS s_eliminado, s.aseguradora, s.nro_poliza, s.cobertura, s.vencimiento " +
        "FROM vehiculos v " +
        "LEFT JOIN seguro_vehicular s ON v.id_seguro = s.id " +
        "WHERE v.id = ?";

    // ========================================
    // Métodos con conexión propia
    // ========================================

    @Override
    public Vehiculo crear(Vehiculo entidad) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(entidad, conn);
        }
    }

    @Override
    public Vehiculo leer(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leer(id, conn);
        }
    }

    @Override
    public List<Vehiculo> leerTodos() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leerTodos(conn);
        }
    }

    @Override
    public void actualizar(Vehiculo entidad) throws SQLException {
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
    // Método con JOIN (leer vehículo con seguro)
    // ========================================

    @Override
    public Vehiculo leerConDetalle(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_WITH_SEGURO_SQL)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVehiculoConSeguro(rs);
                }
            }
        }
        return null;
    }

    // ========================================
    // Métodos con conexión externa (transacciones)
    // ========================================

    @Override
    public Vehiculo crear(Vehiculo entidad, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, Boolean.TRUE.equals(entidad.getEliminado()));
            ps.setString(2, entidad.getDominio());
            ps.setString(3, entidad.getMarca());
            ps.setString(4, entidad.getModelo());

            // Manejo de campo nullable anio
            if (entidad.getAnio() != null) {
                ps.setInt(5, entidad.getAnio());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setString(6, entidad.getNro_chasis());

            // Manejo de campo nullable id_seguro
            if (entidad.getId_seguro() != null) {
                ps.setInt(7, entidad.getId_seguro());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

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
    public Vehiculo leer(long id, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVehiculo(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Vehiculo> leerTodos(Connection conn) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRowToVehiculo(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Vehiculo entidad, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setBoolean(1, Boolean.TRUE.equals(entidad.getEliminado()));
            ps.setString(2, entidad.getDominio());
            ps.setString(3, entidad.getMarca());
            ps.setString(4, entidad.getModelo());

            if (entidad.getAnio() != null) {
                ps.setInt(5, entidad.getAnio());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setString(6, entidad.getNro_chasis());

            if (entidad.getId_seguro() != null) {
                ps.setInt(7, entidad.getId_seguro());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setLong(8, entidad.getId());

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
    // Helpers para mapear ResultSet a objeto
    // ========================================

    /**
     * Mapea un ResultSet a un objeto Vehiculo (sin seguro)
     */
    private Vehiculo mapRowToVehiculo(ResultSet rs) throws SQLException {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(rs.getLong("id"));
        vehiculo.setEliminado(rs.getBoolean("eliminado"));
        vehiculo.setDominio(rs.getString("dominio"));
        vehiculo.setMarca(rs.getString("marca"));
        vehiculo.setModelo(rs.getString("modelo"));

        // Manejo de campo nullable anio
        int anio = rs.getInt("anio");
        vehiculo.setAnio(rs.wasNull() ? null : anio);

        vehiculo.setNro_chasis(rs.getString("nro_chasis"));

        // Manejo de campo nullable id_seguro
        int idSeguro = rs.getInt("id_seguro");
        vehiculo.setId_seguro(rs.wasNull() ? null : idSeguro);

        return vehiculo;
    }

    /**
     * Mapea un ResultSet a un objeto Vehiculo CON su SeguroVehicular (JOIN)
     */
    private Vehiculo mapRowToVehiculoConSeguro(ResultSet rs) throws SQLException {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(rs.getLong("v_id"));
        vehiculo.setEliminado(rs.getBoolean("v_eliminado"));
        vehiculo.setDominio(rs.getString("dominio"));
        vehiculo.setMarca(rs.getString("marca"));
        vehiculo.setModelo(rs.getString("modelo"));

        // Manejo de campo nullable anio
        int anio = rs.getInt("anio");
        vehiculo.setAnio(rs.wasNull() ? null : anio);

        vehiculo.setNro_chasis(rs.getString("nro_chasis"));

        // Manejo de campo nullable id_seguro
        int idSeguro = rs.getInt("id_seguro");
        vehiculo.setId_seguro(rs.wasNull() ? null : idSeguro);

        // Mapear el seguro si existe (LEFT JOIN puede devolver NULL)
        int seguroId = rs.getInt("s_id");
        if (!rs.wasNull()) {
            SeguroVehicular seguro = new SeguroVehicular();
            seguro.setId((long) seguroId);
            seguro.setEliminado(rs.getBoolean("s_eliminado"));
            seguro.setAseguradora(rs.getString("aseguradora"));
            seguro.setNro_poliza(rs.getString("nro_poliza"));
            seguro.setCobertura(Cobertura.valueOf(rs.getString("cobertura")));
            seguro.setVencimiento(rs.getDate("vencimiento"));

            // Asociar el seguro al vehículo
            vehiculo.setDetalle(seguro);
        }

        return vehiculo;
    }
}