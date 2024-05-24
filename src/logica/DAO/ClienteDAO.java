package logica.DAO;

import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import dataaccess.BaseDatosFitZone;
import java.util.ArrayList;
import logica.dominio.Cliente;

public class ClienteDAO {

    public List<Cliente> listarClientesActivos() {
    List<Cliente> clientesActivos = new ArrayList<>();

    try (Connection conexion = BaseDatosFitZone.getConnection();
         PreparedStatement statement = conexion.prepareStatement("SELECT * FROM Cliente WHERE estado = ?")) {
        
        statement.setInt(1, 1);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Cliente cliente = new Cliente(
                        resultSet.getInt("idCliente"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellidoPaterno"),
                        resultSet.getString("apellidoMaterno"),
                        resultSet.getString("direccion"),
                        resultSet.getString("correo"),
                        resultSet.getString("contrasena"),
                        resultSet.getString("numeroCelular"),
                        resultSet.getInt("estado"),
                        resultSet.getInt("idMembresia")
                );
                clientesActivos.add(cliente);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
    }

    return clientesActivos;
}


    public boolean existeCliente(String correo, String numeroCelular) throws SQLException {
        boolean existe = false;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT COUNT(*) AS cantidad FROM Cliente WHERE correo = ? AND numeroCelular = ?")) {
            sentenciaPreparada.setString(1, correo);
            sentenciaPreparada.setString(2, numeroCelular);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    int cantidad = resultSet.getInt("cantidad");
                    existe = cantidad > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return existe;
    }

    public boolean validarCredenciales(String correo, String contrasena) {
        boolean credencialesValidas = false;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = BaseDatosFitZone.getConnection();
            PreparedStatement consulta = conexion.prepareStatement("SELECT contrasena FROM Cliente WHERE correo = ?");
            consulta.setString(1, correo);
            resultado = consulta.executeQuery();

            if (resultado.next()) {
                String contrasenaAlmacenada = resultado.getString("contrasena");
                credencialesValidas = contrasena.equals(contrasenaAlmacenada);
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            BaseDatosFitZone.cerrarConexion(conexion);
        }

        return credencialesValidas;
    }

    public boolean verificarEstadoCliente(String correo) {
        boolean clienteActivo = false;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = BaseDatosFitZone.getConnection();
            PreparedStatement consulta = conexion.prepareStatement("SELECT estado FROM Cliente WHERE correo = ?");
            consulta.setString(1, correo);
            resultado = consulta.executeQuery();

            if (resultado.next()) {
                int estadoCliente = resultado.getInt("estado");
                clienteActivo = (estadoCliente == 1); 
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            BaseDatosFitZone.cerrarConexion(conexion);
        }

        return clienteActivo;
    }

    public int registrarCliente(Cliente cliente) throws SQLException, IllegalArgumentException {
        if (validarCliente(cliente)) {
            if (!existeCliente(cliente.getCorreo(), cliente.getNumeroCelular())) {
                int filasAfectadas = 0;
                try (Connection conexion = BaseDatosFitZone.getConnection();
                     PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                             "INSERT INTO Cliente (nombre, apellidoPaterno, apellidoMaterno, direccion, correo, contrasena, numeroCelular, estado, idMembresia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                    sentenciaPreparada.setString(1, cliente.getNombre());
                    sentenciaPreparada.setString(2, cliente.getApellidoPaterno());
                    sentenciaPreparada.setString(3, cliente.getApellidoMaterno());
                    sentenciaPreparada.setString(4, cliente.getDireccion());
                    sentenciaPreparada.setString(5, cliente.getCorreo());
                    sentenciaPreparada.setString(6, cliente.getContrasena());
                    sentenciaPreparada.setString(7, cliente.getNumeroCelular());
                    sentenciaPreparada.setInt(8, 1);
                    sentenciaPreparada.setInt(9, 1);

                    filasAfectadas = sentenciaPreparada.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                return filasAfectadas;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("Datos del cliente no válidos.");
        }
    }

    public boolean validarCliente(Cliente cliente) {
        return !(cliente.getNombre() == null || cliente.getApellidoPaterno() == null
                || cliente.getApellidoMaterno() == null || cliente.getDireccion() == null
                || cliente.getCorreo() == null || cliente.getContrasena() == null || cliente.getNumeroCelular() == null);
    }

    public int actualizarCliente(Cliente cliente) throws SQLException {
        if (validarCliente(cliente)) {
            int filasAfectadas = 0;
            try (Connection conexion = BaseDatosFitZone.getConnection();
                 PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                         "UPDATE Cliente SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, direccion=?, correo=?, contrasena=?, numeroCelular=?, estado=?, idMembresia=? WHERE idCliente=?")) {
                sentenciaPreparada.setString(1, cliente.getNombre());
                sentenciaPreparada.setString(2, cliente.getApellidoPaterno());
                sentenciaPreparada.setString(3, cliente.getApellidoMaterno());
                sentenciaPreparada.setString(4, cliente.getDireccion());
                sentenciaPreparada.setString(5, cliente.getCorreo());
                sentenciaPreparada.setString(6, cliente.getContrasena());
                sentenciaPreparada.setString(7, cliente.getNumeroCelular());
                sentenciaPreparada.setInt(8, cliente.getEstado());
                sentenciaPreparada.setInt(9, cliente.getIdMembresia());
                sentenciaPreparada.setInt(10, cliente.getIdCliente());

                filasAfectadas = sentenciaPreparada.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            return filasAfectadas;
        } else {
            throw new IllegalArgumentException("Datos del cliente no válidos.");
        }
    }

    public int desactivarCliente(int idCliente) throws SQLException {
        int filasAfectadas = 0;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "UPDATE Cliente SET estado=0 WHERE idCliente=?")) {
            sentenciaPreparada.setInt(1, idCliente);
            filasAfectadas = sentenciaPreparada.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return filasAfectadas;
    }

    public Cliente obtenerClientePorId(int idCliente) throws SQLException {
        Cliente cliente = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT * FROM Cliente WHERE idCliente = ?")) {
            sentenciaPreparada.setInt(1, idCliente);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    cliente = new Cliente(
                            resultSet.getInt("idCliente"),
                            resultSet.getString("nombre"),
                            resultSet.getString("apellidoPaterno"),
                            resultSet.getString("apellidoMaterno"),
                            resultSet.getString("direccion"),
                            resultSet.getString("correo"),
                            resultSet.getString("contrasena"),
                            resultSet.getString("numeroCelular"),
                            resultSet.getInt("estado"),
                            resultSet.getInt("idMembresia")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return cliente;
    }
}
