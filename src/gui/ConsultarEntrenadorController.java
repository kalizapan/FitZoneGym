package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Entrenador;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConsultarEntrenadorController implements Initializable {
    private AdministradorController administradorController;

    public void setAdministradorController(AdministradorController administradorController) {
        this.administradorController = administradorController;
    }

    @FXML
    private TextField txtFieldNombre;
    @FXML
    private TextField txtFieldApellidoPaterno;
    @FXML
    private TextField txtFieldApellidoMaterno;
    @FXML
    private TextField txtFieldEspecialidad;
    @FXML
    private Button buttonActualizar;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonDarBaja;

    private Stage dialogStage;
    private Entrenador entrenador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No es necesario hacer nada aquí en este momento
    }

    public void initData(Entrenador entrenador) {
        this.entrenador = entrenador;
        this.administradorController = administradorController;
        mostrarDetallesEntrenador();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
        mostrarDetallesEntrenador();
    }

    private void mostrarDetallesEntrenador() {
        if (entrenador != null) {
            txtFieldNombre.setText(entrenador.getNombre());
            txtFieldApellidoPaterno.setText(entrenador.getApellidoPaterno());
            txtFieldApellidoMaterno.setText(entrenador.getApellidoMaterno());
            txtFieldEspecialidad.setText(entrenador.getEspecialidad());
        } else {
            System.out.println("Error: El entrenador es nulo en mostrarDetallesEntrenador.");
        }
    }

    @FXML
    private void mostrarBotonAceptar() {
        buttonAceptar.setVisible(true);
        txtFieldNombre.setEditable(true);
        txtFieldApellidoPaterno.setEditable(true);
        txtFieldApellidoMaterno.setEditable(true);
        txtFieldEspecialidad.setEditable(true);
    }

    @FXML
    private void actualizarEntrenador() {
        if (entrenador == null) {
            System.out.println("Error: Entrenador es nulo en handleActualizarEntrenador.");
            return;
        }

        try {
            entrenador.setNombre(txtFieldNombre.getText().trim());
            entrenador.setApellidoPaterno(txtFieldApellidoPaterno.getText().trim());
            entrenador.setApellidoMaterno(txtFieldApellidoMaterno.getText().trim());
            entrenador.setEspecialidad(txtFieldEspecialidad.getText().trim());

            EntrenadorDAO entrenadorDAO = new EntrenadorDAO();
            int filasAfectadas = entrenadorDAO.actualizarEntrenador(entrenador);

            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Entrenador actualizado exitosamente.");
                administradorController.loadEntrenadores(); // Aquí se carga nuevamente la tabla de entrenadores
            } else {
                mostrarAlertaError("Error al actualizar entrenador.");
            }
            // Ocultar botón de aceptar y hacer no editables los campos después de la actualización
            buttonAceptar.setVisible(false);
            txtFieldNombre.setEditable(false);
            txtFieldApellidoPaterno.setEditable(false);
            txtFieldApellidoMaterno.setEditable(false);
            txtFieldEspecialidad.setEditable(false);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al actualizar entrenador: " + e.getMessage());
        }
    }

    @FXML
    private void darBajaEntrenador() throws SQLException {
        if (entrenador == null) {
            System.out.println("Error: Entrenador es nulo en darBajaEntrenador.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Seguro que desea dar de baja a este entrenador?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            EntrenadorDAO entrenadorDAO = new EntrenadorDAO();
            int idEntrenador = entrenador.getIdEntrenador();
            int filasAfectadas = entrenadorDAO.desactivarEntrenador(idEntrenador);
            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Entrenador dado de baja exitosamente.");
                administradorController.loadEntrenadores(); // Aquí se carga nuevamente la tabla de entrenadores
                dialogStage.close();
            } else {
                mostrarAlertaError("Error al dar de baja entrenador.");
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
