package gui;

import logica.dominio.Entrenador;
import logica.DAO.EntrenadorDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegistroEntrenadorController implements Initializable {

    @FXML
    private Button buttonRegistrarEntrenador;

    @FXML
    private TextField txtFieldNombre;

    @FXML
    private TextField txtFieldApellidoPaterno;

    @FXML
    private TextField txtFieldApellidoMaterno;

    @FXML
    private TextField txtFieldEspecialidad;

    private EntrenadorDAO entrenadorDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entrenadorDAO = new EntrenadorDAO();
    }

    @FXML
    private void handleRegistrarEntrenador(ActionEvent event) {
        try {
            String nombre = txtFieldNombre.getText();
            String apellidoPaterno = txtFieldApellidoPaterno.getText();
            String apellidoMaterno = txtFieldApellidoMaterno.getText();
            String especialidad = txtFieldEspecialidad.getText();

            if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || especialidad.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            Entrenador nuevoEntrenador = new Entrenador();
            nuevoEntrenador.setNombre(nombre);
            nuevoEntrenador.setApellidoPaterno(apellidoPaterno);
            nuevoEntrenador.setApellidoMaterno(apellidoMaterno);
            nuevoEntrenador.setEspecialidad(especialidad);
            nuevoEntrenador.setEstado(true); // Asumimos que el entrenador está activo al registrarse

            int filasAfectadas = entrenadorDAO.registrarEntrenador(nuevoEntrenador);

            if (filasAfectadas > 0) {
                mostrarAlerta("Entrenador registrado exitosamente.");
                vaciarCampos();
            }

        } catch (IllegalArgumentException ex) {
            mostrarAlertaError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarAlertaError("Error al registrar entrenador: " + ex.getMessage());
        } catch (Exception ex) {
            mostrarAlertaError("Error inesperado: " + ex.getMessage());
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
        txtFieldNombre.clear();
        txtFieldApellidoPaterno.clear();
        txtFieldApellidoMaterno.clear();
        txtFieldEspecialidad.clear();
    }
}
