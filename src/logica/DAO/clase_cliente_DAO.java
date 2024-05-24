package logica.DAO;

import dataaccess.BaseDatosFitZone;
import logica.dominio.Clase;
import logica.dominio.Cliente;
import logica.dominio.clase_cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Vero
 */
public class clase_cliente_DAO {

    public int registrarInscripcion(int idClase, int idCliente) throws SQLException {
        int filasAfectadas = 0;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "INSERT INTO clase_cliente (idClase, idCliente) VALUES (?, ?)")) {
            sentenciaPreparada.setInt(1, idClase);
            sentenciaPreparada.setInt(2, idCliente);
            filasAfectadas = sentenciaPreparada.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(clase_cliente_DAO.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return filasAfectadas;
    }

    public int eliminarInscripcion(int idClase, int idCliente) throws SQLException {
        int filasAfectadas = 0;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "DELETE FROM clase_cliente WHERE idClase=? AND idCliente=?")) {
            sentenciaPreparada.setInt(1, idClase);
            sentenciaPreparada.setInt(2, idCliente);
            filasAfectadas = sentenciaPreparada.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(clase_cliente_DAO.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return filasAfectadas;
    }

    public List<Cliente> obtenerClientesPorClase(int idClase) {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT c.* FROM Cliente c INNER JOIN clase_cliente cc ON c.idCliente = cc.idCliente WHERE cc.idClase = ?")) {
            sentenciaPreparada.setInt(1, idClase);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                while (resultSet.next()) {
                    Cliente cliente = new Cliente();
                    // Asignar valores del ResultSet al objeto Cliente
                    clientes.add(cliente);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(clase_cliente_DAO.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return clientes;
    }

    public List<Clase> obtenerClasesPorCliente(int idCliente) {
        List<Clase> clases = new ArrayList<>();

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT cl.* FROM Clase cl INNER JOIN clase_cliente cc ON cl.idClase = cc.idClase WHERE cc.idCliente = ?")) {
            sentenciaPreparada.setInt(1, idCliente);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                while (resultSet.next()) {
                    Clase clase = new Clase();
                    // Asignar valores del ResultSet al objeto Clase
                    clases.add(clase);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(clase_cliente_DAO.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return clases;
    }

    public boolean verificarInscripcion(int idClase, int idCliente) {
        boolean inscrito = false;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT COUNT(*) AS cantidad FROM clase_cliente WHERE idClase=? AND idCliente=?")) {
            sentenciaPreparada.setInt(1, idClase);
            sentenciaPreparada.setInt(2, idCliente);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    int cantidad = resultSet.getInt("cantidad");
                    inscrito = cantidad > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(clase_cliente_DAO.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        return inscrito;
    }
}
