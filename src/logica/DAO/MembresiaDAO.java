package logica.DAO;

import dataaccess.BaseDatosFitZone;
import logica.dominio.Membresia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MembresiaDAO {

    public List<Membresia> listarMembresiasActivas() {
        List<Membresia> membresiasActivas = new LinkedList<>();

        try (Connection conexion = BaseDatosFitZone.getConnection(); PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                "SELECT * FROM Membresia WHERE estado = ?")) {
            sentenciaPreparada.setString(1, "Activa");

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                while (resultSet.next()) {
                    Membresia membresia = new Membresia(
                            resultSet.getInt("idMembresia"),
                            resultSet.getString("nombreMembresia"),
                            resultSet.getString("duracion"),
                            resultSet.getInt("estado")
                    );
                    membresiasActivas.add(membresia);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembresiaDAO.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return membresiasActivas;
    }

    public boolean existeMembresia(String nombreMembresia) throws SQLException {
        boolean existe = false;

        try (Connection conexion = BaseDatosFitZone.getConnection();
                PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                        "SELECT COUNT(*) AS cantidad FROM Membresia "
                        + "WHERE nombreMembresia = ?")) {
            sentenciaPreparada.setString(1, nombreMembresia);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    int cantidad = resultSet.getInt("cantidad");
                    existe = cantidad > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembresiaDAO.class.getName()).log(
                    Level.SEVERE, null, ex);
            throw ex;
        }
        return existe;
    }

    public int registrarMembresia(Membresia membresia) throws SQLException, IllegalArgumentException {
        if (validarMembresia(membresia)) {
            if (!existeMembresia(membresia.getNombreMembresia())) {
                int filasAfectadas = 0;
                try (Connection conexion = BaseDatosFitZone.getConnection();
                        PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                                "INSERT INTO Membresia (nombreMembresia, duracion, estado) VALUES (?, ?, ?)")) {

                    sentenciaPreparada.setString(1, membresia.getNombreMembresia());
                    sentenciaPreparada.setString(2, membresia.getDuracion());
                    sentenciaPreparada.setInt(3, membresia.getEstado());

                    filasAfectadas = sentenciaPreparada.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(MembresiaDAO.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
                return filasAfectadas;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("Datos de la membresía no válidos.");
        }
    }

    public boolean validarMembresia(Membresia membresia) {
        return !(membresia.getNombreMembresia() == null || membresia.getDuracion() == null
                || membresia.getEstado() < 0l);
    }

    public int actualizarMembresia(Membresia membresia) throws SQLException {
        if (validarMembresia(membresia)) {
            int filasAfectadas = 0;
            try (Connection conexion = BaseDatosFitZone.getConnection();
                    PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                            "UPDATE Membresia "
                            + "SET nombreMembresia=?, duracion=?, estado=? "
                            + "WHERE idMembresia=?")) {
                sentenciaPreparada.setString(1, membresia.getNombreMembresia());
                sentenciaPreparada.setString(2, membresia.getDuracion());
                sentenciaPreparada.setInt(3, membresia.getEstado());
                sentenciaPreparada.setInt(4, membresia.getIdMembresia());

                filasAfectadas = sentenciaPreparada.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(MembresiaDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            return filasAfectadas;
        } else {
            throw new IllegalArgumentException("Datos de la membresía no válidos.");
        }
    }

    public int desactivarMembresia(int idMembresia) throws SQLException {
    int filasAfectadas = 0;

    try (Connection conexion = BaseDatosFitZone.getConnection();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                    "UPDATE Membresia SET estado=0 WHERE idMembresia=?")) {
        sentenciaPreparada.setInt(1, idMembresia);
        filasAfectadas = sentenciaPreparada.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(MembresiaDAO.class.getName()).log(
                Level.SEVERE, null, ex);
    }

    return filasAfectadas;
}

    public Membresia obtenerMembresiaPorId(int idMembresia) throws SQLException {
        if (idMembresia <= 0) {
            throw new IllegalArgumentException("ID de la membresía no válido.");
        }

        Membresia membresia = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
                PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                        "SELECT * FROM Membresia WHERE idMembresia=?")) {
            sentenciaPreparada.setInt(1, idMembresia);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    membresia = new Membresia(
                            resultSet.getInt("idMembresia"),
                            resultSet.getString("nombreMembresia"),
                            resultSet.getString("duracion"),
                            resultSet.getInt("estado")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MembresiaDAO.class.getName()).log(
                    Level.SEVERE, null, ex);
            throw ex;
        }

        return membresia;
    }
}
