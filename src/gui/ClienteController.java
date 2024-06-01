package gui;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import logica.DAO.ClaseDAO;
import logica.dominio.Clase;

public class ClienteController {

    @FXML
    private TableView<Clase> tableClases;

    @FXML
    private Label labelTitulo;

    @FXML
    private Button btnClases;

    @FXML
    private Button btnPerfil;
    
    @FXML
    private Button buttonCerrarSesion;
    
    private final ClaseDAO claseDAO = new ClaseDAO();
    private Stage stage;
    
    public void setStage(Stage stage) {
    this.stage = stage;
}
    
     private void loadClases() {
        List<Clase> clasesActivas = claseDAO.listarClasesActivas();
        ObservableList<Clase> data = FXCollections.observableArrayList(clasesActivas);
        tableClases.setItems(data);
    }

    @FXML
    void handleClases(ActionEvent event) {
        loadClases();
        tableClases.setVisible(true);
        labelTitulo.setText("¡Diviértete con nuestras clases!");
    }
   

    @FXML
    void handlePerfil(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PerfilCliente.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Perfil del Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InicioSesion.fxml"));
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