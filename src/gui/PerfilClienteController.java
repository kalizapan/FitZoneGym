package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logica.dominio.Cliente;

import java.io.IOException;

public class PerfilClienteController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoPaternoField;
    @FXML
    private TextField apellidoMaternoField;
    @FXML
    private TextField correoField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField membresiaField;

    private Cliente cliente;
    private Stage stage;

    public void initialize() {
        cargarDatosCliente();
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        cargarDatosCliente();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void cargarDatosCliente() {
        if (cliente != null) {
            nombreField.setText(cliente.getNombre());
            apellidoPaternoField.setText(cliente.getApellidoPaterno());
            apellidoMaternoField.setText(cliente.getApellidoMaterno());
            correoField.setText(cliente.getCorreo());
            direccionField.setText(cliente.getDireccion());
            telefonoField.setText(cliente.getNumeroCelular());
            membresiaField.setText(String.valueOf(cliente.getIdMembresia())); 
        }
    }

    @FXML
    private void handleRegresar() {
        stage.close(); 
    }

    @FXML
    private void handleModificarInformacion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModificarPerfil.fxml"));
            Parent root = loader.load();

            ModificarPerfilController modificarPerfilController = loader.getController();
            modificarPerfilController.setCliente(cliente);
            modificarPerfilController.setStage(stage);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de modificar perfil.");
        }
    }

    @FXML
    private void handleEliminarCuenta() {
        cliente.setEstado(0);
        mostrarAlerta("Cuenta desactivada", "Su cuenta ha sido desactivada.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}