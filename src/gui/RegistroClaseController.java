package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import logica.DAO.ClaseDAO;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Clase;
import logica.dominio.Entrenador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;

public class RegistroClaseController implements Initializable {

    @FXML
    private Button buttonRegistrar;

    @FXML
    private Button buttonRegresar;

    @FXML
    private TextField txtfieldNombre;

    @FXML
    private ChoiceBox<String> chboxTipo;

    @FXML
    private DatePicker dtpickerFechaDeInicio;

    @FXML
    private TextField txtfieldCapacidad;

    @FXML
    private TextField txtfieldHoraInicio;

    @FXML
    private TextField txtfieldHoraFin;

    @FXML
    private TextField txtfieldPrecio;

    @FXML
    private ChoiceBox<String> chboxEntrenador;

    private ClaseDAO claseDAO;
    private EntrenadorDAO entrenadorDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        claseDAO = new ClaseDAO();
        entrenadorDAO = new EntrenadorDAO();
        chboxTipo.getItems().addAll("Pilates", "Cardio", "Spinning", "Yoga");

        cargarNombresEntrenadores();
    }

    private void cargarNombresEntrenadores() {
        chboxEntrenador.getItems().clear(); 
        for (Entrenador entrenador : entrenadorDAO.listarEntrenadoresActivos()) {
            chboxEntrenador.getItems().add(entrenador.getNombre());
        }
    }

    @FXML
    private void handleRegistrarClase(ActionEvent event) throws SQLException{
            String nombre = txtfieldNombre.getText();
            String capacidad = txtfieldCapacidad.getText();
            String horaInicio = txtfieldHoraInicio.getText();
            String horaFin = txtfieldHoraFin.getText();
            String precio = txtfieldPrecio.getText();
            String nombreEntrenador = chboxEntrenador.getValue();
            LocalDate fechaInicio = dtpickerFechaDeInicio.getValue();
            String tipo = chboxTipo.getValue();

            if (nombre.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty() || nombreEntrenador == null ||
                    tipo == null || fechaInicio == null || capacidad.isEmpty() || precio.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            Entrenador entrenador = null;
        try {
            entrenador = entrenadorDAO.obtenerEntrenadorPorNombre(nombreEntrenador);
        } catch (SQLException ex) {
            Logger.getLogger(RegistroClaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
            if (entrenador == null) {
                throw new IllegalArgumentException("No se encontró un entrenador con ese nombre.");
            }
            int idEntrenador = entrenador.getIdEntrenador();
            
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fechaInicioString = fechaInicio.format(formato);

            int capacidadInt = Integer.parseInt(capacidad);
            int precioInt = Integer.parseInt(precio);

            Clase nuevaClase = new Clase(nombre, tipo, capacidadInt, fechaInicioString, horaInicio, horaFin, precioInt, 1, idEntrenador);
            int filasAfectadas = 0;
        try {
            filasAfectadas = claseDAO.registrarClase(nuevaClase);
        } catch (SQLException ex) {
            Logger.getLogger(RegistroClaseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(RegistroClaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

            if (filasAfectadas > 0) {
                mostrarAlerta("Clase registrada exitosamente.");
                vaciarCampos();
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
        txtfieldCapacidad.clear();
        txtfieldHoraInicio.clear();
        txtfieldHoraFin.clear();
        txtfieldPrecio.clear();
        chboxEntrenador.getSelectionModel().clearSelection();
        chboxTipo.getSelectionModel().clearSelection();
        dtpickerFechaDeInicio.getEditor().clear();
    }
}
