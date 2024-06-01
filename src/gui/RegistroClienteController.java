package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import logica.dominio.Cliente;
import logica.DAO.ClienteDAO;

public class RegistroClienteController implements Initializable {

    @FXML
    private Button buttonRegistrarse;

    @FXML
    private Button buttonRegresar;

    @FXML
    private TextField txtfieldNombre;

    @FXML
    private TextField txtfieldApellidoPaterno;

    @FXML
    private TextField txtfieldApellidoMaterno;

    @FXML
    private TextField txtfieldCorreo;
    
    @FXML
    private TextField txtfieldContrasena;

    @FXML
    private TextField txtfieldDireccion;

    @FXML
    private TextField txtfieldCelular;
    
    @FXML 
    private ComboBox<String> cBoxMembresia;

    private ClienteDAO clienteDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO = new ClienteDAO();
        cBoxMembresia.getItems().addAll("Mensual", "Anual", "Semestral");
    }

    @FXML
    private void handleRegistrarse(ActionEvent event) {
        try {
            String nombre = txtfieldNombre.getText();
            String apellidoPaterno = txtfieldApellidoPaterno.getText();
            String apellidoMaterno = txtfieldApellidoMaterno.getText();
            String correo = txtfieldCorreo.getText();
            String contrasena = txtfieldContrasena.getText();
            String direccion = txtfieldDireccion.getText();
            String numeroCelular = txtfieldCelular.getText();
            String membresia = cBoxMembresia.getValue();
            int estado =1;

            if (nombre.isEmpty() || apellidoPaterno.isEmpty() || correo.isEmpty()) {
                throw new IllegalArgumentException("Por favor, completa todos los campos obligatorios.");
            }
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setApellidoPaterno(apellidoPaterno);
            nuevoCliente.setApellidoMaterno(apellidoMaterno);
            nuevoCliente.setCorreo(correo);
            nuevoCliente.setContrasena(contrasena);
            nuevoCliente.setDireccion(direccion);
            nuevoCliente.setNumeroCelular(numeroCelular);
            nuevoCliente.setEstado(estado);
            nuevoCliente.setMembresia(membresia);

            int filasAfectadas = clienteDAO.registrarCliente(nuevoCliente);
            if (filasAfectadas > 0) {
                mostrarAlerta("Cliente registrado exitosamente.");
                vaciarCampos();
            } else {
                mostrarAlertaError("Error al registrar cliente.");
            }
        } catch (IllegalArgumentException ex) {
            mostrarAlertaError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarAlertaError("Error al registrar cliente: " + ex.getMessage());
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            Stage stageActual = (Stage) buttonRegresar.getScene().getWindow();
            stageActual.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InicioSesion.fxml"));
            AnchorPane root = loader.load();
            
            InicioSesionController controller = loader.getController();
            controller.setStage(stageActual);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (Exception e) {
            mostrarAlertaError("Error al cargar la ventana de inicio de sesión: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText("Información");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void vaciarCampos() {
        txtfieldNombre.clear();
        txtfieldApellidoPaterno.clear();
        txtfieldApellidoMaterno.clear();
        txtfieldCorreo.clear();
        txtfieldContrasena.clear();
        txtfieldDireccion.clear();
        txtfieldCelular.clear();
    }
}
