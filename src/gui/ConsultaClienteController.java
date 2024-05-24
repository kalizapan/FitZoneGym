package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAO.ClienteDAO;
import logica.dominio.Cliente;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConsultaClienteController implements Initializable {
    private AdministradorController administradorController;

    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField apellidoPaternoTextField;
    @FXML
    private TextField apellidoMaternoTextField;
    @FXML
    private TextField correoTextField;
    @FXML
    private TextField direccionTextField;
    @FXML
    private TextField celularTextField;
    @FXML
    private TextField membresiaTextField;
    @FXML
    private Button buttonActualizar;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonDarBaja;

    private Stage dialogStage;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initData(Cliente cliente) {
        this.cliente = cliente;
        mostrarDetallesCliente();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void mostrarDetallesCliente() {
        if (cliente != null) {
            nombreTextField.setText(cliente.getNombre());
            apellidoPaternoTextField.setText(cliente.getApellidoPaterno());
            apellidoMaternoTextField.setText(cliente.getApellidoMaterno());
            correoTextField.setText(cliente.getCorreo());
            direccionTextField.setText(cliente.getDireccion());
            celularTextField.setText(cliente.getNumeroCelular());
            membresiaTextField.setText(String.valueOf(cliente.getIdMembresia()));
        } else {
            System.out.println("Error: El cliente es nulo en mostrarDetallesCliente.");
        }
    }

    @FXML
    private void mostrarBotonAceptar() {
        buttonAceptar.setVisible(true);
        nombreTextField.setEditable(true);
        apellidoPaternoTextField.setEditable(true);
        apellidoMaternoTextField.setEditable(true);
        correoTextField.setEditable(true);
        direccionTextField.setEditable(true);
        celularTextField.setEditable(true);
        membresiaTextField.setEditable(true);
    }

    @FXML
    private void actualizarCliente() {
        if (cliente == null) {
            System.out.println("Error: Cliente es nulo en handleActualizarCliente.");
            return;
        }

        try {
            cliente.setNombre(nombreTextField.getText().trim());
            cliente.setApellidoPaterno(apellidoPaternoTextField.getText().trim());
            cliente.setApellidoMaterno(apellidoMaternoTextField.getText().trim());
            cliente.setCorreo(correoTextField.getText().trim());
            cliente.setDireccion(direccionTextField.getText().trim());
            cliente.setNumeroCelular(celularTextField.getText().trim());
            cliente.setIdMembresia(Integer.parseInt(membresiaTextField.getText().trim()));

            ClienteDAO clienteDAO = new ClienteDAO();
            int filasAfectadas = clienteDAO.actualizarCliente(cliente);

            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Cliente actualizado exitosamente.");
                administradorController.loadClientes();
            } else {
                mostrarAlertaError("Error al actualizar cliente.");
            }
            buttonAceptar.setVisible(false);
            nombreTextField.setEditable(false);
            apellidoPaternoTextField.setEditable(false);
            apellidoMaternoTextField.setEditable(false);
            correoTextField.setEditable(false);
            direccionTextField.setEditable(false);
            celularTextField.setEditable(false);
            membresiaTextField.setEditable(false);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al actualizar cliente: " + e.getMessage());
        }
    }

    @FXML
    private void darBajaCliente() throws SQLException {
        if (cliente == null) {
            System.out.println("Error: Cliente es nulo en darBajaCliente.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Seguro que desea dar de baja a este cliente?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ClienteDAO clienteDAO = new ClienteDAO();
            int idCliente = cliente.getIdCliente();
            int filasAfectadas = clienteDAO.desactivarCliente(idCliente);
            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Cliente dado de baja exitosamente.");
                administradorController.loadClientes();
                dialogStage.close();
            } else {
                mostrarAlertaError("Error al dar de baja cliente.");
            }
        }
    }

    private void mostrarAlertaInformacion(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
