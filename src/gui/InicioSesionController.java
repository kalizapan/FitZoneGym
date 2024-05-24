package gui;

import cuentas.ConfigCuentas;
import logica.DAO.ClienteDAO;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InicioSesionController {

    @FXML
    private TextField correoTextField;

    @FXML
    private TextField contraseñaTextField;

    @FXML
    private Button ingresarButton;

    @FXML
    private Button crearCuentaButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {

        ingresarButton.setOnAction(event -> handleIniciarSesion());
        crearCuentaButton.setOnAction(event -> handleRegistrarse());
    }

    @FXML
    private void handleIniciarSesionAdministrativo() throws SQLException {
        String correo = correoTextField.getText();
        String contrasenia = contraseñaTextField.getText();

        ConfigCuentas configUsuarios = new ConfigCuentas();
        String correoAdmin = configUsuarios.obtenerCorreoAdministrador();
        String contraseniaAdmin = configUsuarios.obtenerContraseniaAdministrador();

        if (correo.equals(correoAdmin) && contrasenia.equals(contraseniaAdmin)) {
            abrirVentanaAdministrador();
        } else {
            mostrarAlerta("Correo o contraseña incorrectos para acceso administrativo.");
        }
    }

    @FXML
    private void handleIniciarSesion() {
        String correo = correoTextField.getText().trim();
        String contrasenia = contraseñaTextField.getText().trim();

        ClienteDAO clienteDAO = new ClienteDAO();

        if (clienteDAO.validarCredenciales(correo, contrasenia)) {
            if (clienteDAO.verificarEstadoCliente(correo)) {
                abrirVentanaCliente();
            } else {
                mostrarAlerta("La cuenta está inactiva. Contacta al administrador.");
            }
        } else {
            mostrarAlerta("Correo o contraseña incorrectos");
        }
    }

    private void abrirVentanaCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cliente.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentanaAdministrador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Administrador.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleRegistrarse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/RegistroCliente.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
