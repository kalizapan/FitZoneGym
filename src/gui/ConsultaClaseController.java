package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAO.ClaseDAO;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Clase;
import logica.dominio.Entrenador;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConsultaClaseController implements Initializable {
    @FXML
    private TextField txtFieldTipo;
    @FXML
    private TextField txtFieldCapacidad;
    @FXML
    private TextField txtFieldHoraInicio;
    @FXML
    private TextField txtFieldHoraFin;
    @FXML
    private TextField txtFieldPrecio;
    @FXML
    private ComboBox<String> cBoxEntrenador;
    
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnCancelar;

    private Stage dialogStage;
    private Clase clase;
    private ClaseDAO claseDAO;
    private EntrenadorDAO entrenadorDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        claseDAO = new ClaseDAO();
        entrenadorDAO = new EntrenadorDAO();
        cargarEntrenador();
    }

    private void cargarEntrenador() {
    List<Entrenador> entrenadorActivo = entrenadorDAO.listarEntrenadoresActivos();
    ObservableList<String> nombresEntrenadores = FXCollections.observableArrayList();
    
    for (Entrenador entrenador : entrenadorActivo) {
        nombresEntrenadores.add(entrenador.getNombre());
    }
    
    cBoxEntrenador.setItems(nombresEntrenadores);
}


    public void initData(Clase clase) {
        this.clase = clase;
        mostrarDetallesClase();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void actualizarClase() {
        if (clase == null) {
            System.out.println("Error: Clase es nula en actualizarClase.");
            return;
        }

        try {
            clase.setTipo(txtFieldTipo.getText().trim());
            clase.setCapacidad(Integer.parseInt(txtFieldCapacidad.getText().trim()));
            clase.setHoraInicio(txtFieldHoraInicio.getText().trim());
            clase.setHoraFin(txtFieldHoraFin.getText().trim());
            clase.setPrecio(Float.parseFloat(txtFieldPrecio.getText().trim()));
            String nombreEntrenadorSeleccionado = cBoxEntrenador.getValue();

            int filasAfectadas = claseDAO.actualizarClase(clase);

            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Clase actualizada exitosamente.");
                // Implementa lógica adicional según sea necesario
            } else {
                mostrarAlertaError("Error al actualizar clase.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al actualizar clase: " + e.getMessage());
        }
    }

    @FXML
    private void cancelarClase() {
        dialogStage.close();
    }

    private void mostrarDetallesClase() {
        if (clase != null) {
            txtFieldTipo.setText(clase.getTipo());
            txtFieldCapacidad.setText(String.valueOf(clase.getCapacidad()));
            txtFieldHoraInicio.setText(clase.getHoraInicio());
            txtFieldHoraFin.setText(clase.getHoraFin());
            txtFieldPrecio.setText(String.valueOf(clase.getPrecio()));
            String nombreEntrenadorSeleccionado = cBoxEntrenador.getValue();
        } else {
            System.out.println("Error: Clase es nula en mostrarDetallesClase.");
        }
    }

    private void mostrarAlertaInformacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}