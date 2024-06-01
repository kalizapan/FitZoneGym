package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAO.ClaseDAO;
import logica.DAO.ClienteDAO;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Clase;
import logica.dominio.Cliente;
import logica.dominio.Entrenador;

public class AdministradorController implements Initializable {

    @FXML
    private Button btnClases;

    @FXML
    private Button btnMiembros;

    @FXML
    private Button btnEntrenadores;

    @FXML
    private Button btnAddClase;

    @FXML
    private Button btnAddEntrenador;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TableView<Cliente> tableMiembros;

    @FXML
    private TableView<Clase> tableClases;

    @FXML
    private TableView<Entrenador> tableEntrenadores;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ClaseDAO claseDAO = new ClaseDAO();
    private final EntrenadorDAO entrenadorDAO = new EntrenadorDAO();

    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnMiembros.setOnAction(event -> showTable("miembros"));
        btnClases.setOnAction(event -> showTable("clases"));
        btnEntrenadores.setOnAction(event -> showTable("entrenadores"));

        showTable("miembros");

        tableEntrenadores.setOnMouseClicked(this::mostrarDetallesEntrenador);
        tableMiembros.setOnMouseClicked(this::mostrarDetallesCliente);
        tableClases.setOnMouseClicked(this::mostrarDetallesClase);
    }

    private void showTable(String table) {
        tableMiembros.setVisible(false);
        tableClases.setVisible(false);
        tableEntrenadores.setVisible(false);
        btnAddClase.setVisible(false);
        btnAddEntrenador.setVisible(false);

        switch (table) {
            case "miembros":
                loadClientes();
                tableMiembros.setVisible(true);
                break;
            case "clases":
                loadClases();
                tableClases.setVisible(true);
                btnAddClase.setVisible(true);
                break;
            case "entrenadores":
                loadEntrenadores();
                tableEntrenadores.setVisible(true);
                btnAddEntrenador.setVisible(true);
                break;
        }
    }

    void loadClientes() {
        List<Cliente> clientesActivos = clienteDAO.listarClientesActivos();
        ObservableList<Cliente> data = FXCollections.observableArrayList(clientesActivos);
        tableMiembros.setItems(data);
    }

    private void loadClases() {
        List<Clase> clasesActivas = claseDAO.listarClasesActivas();
        ObservableList<Clase> data = FXCollections.observableArrayList(clasesActivas);
        tableClases.setItems(data);
    }

    void loadEntrenadores() {
        List<Entrenador> entrenadoresActivos = entrenadorDAO.listarEntrenadoresActivos();
        ObservableList<Entrenador> data = FXCollections.observableArrayList(entrenadoresActivos);
        tableEntrenadores.setItems(data);
    }

    @FXML
    private void showTableMiembros(ActionEvent event) {
        showTable("miembros");
    }

    @FXML
    private void showTableClases(ActionEvent event) {
        showTable("clases");
    }

    @FXML
    private void showTableEntrenadores(ActionEvent event) {
        showTable("entrenadores");
    }

    @FXML
    private void openRegistrarClase(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/RegistroClase.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Clase");
            stage.setScene(new Scene(root));
            stage.showAndWait(); 

            loadClases();
        } catch (IOException e) {
            mostrarAlertaError("Error al cargar la ventana de registro de clases: " + e.getMessage());
        }
    }

    @FXML
    private void openRegistrarEntrenador(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/RegistroEntrenador.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Entrenador");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadEntrenadores();
        } catch (IOException e) {
            mostrarAlertaError("Error al cargar la ventana de registro de entrenadores: " + e.getMessage());
        }
    }

    @FXML
    private void mostrarDetallesEntrenador(MouseEvent event) {
        if (event.getClickCount() == 1) {
            try {
                Entrenador entrenadorSeleccionado = tableEntrenadores.getSelectionModel().getSelectedItem();
                if (entrenadorSeleccionado != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ConsultarEntrenador.fxml"));
                    Parent root = loader.load();

                    ConsultarEntrenadorController controller = loader.getController();
                    controller.initData(entrenadorSeleccionado);
                    controller.setAdministradorController(this);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Detalles del Entrenador");
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(primaryStage);
                    controller.setDialogStage(stage);
                    stage.showAndWait();
                } else {
                    System.out.println("No se seleccionó ningún entrenador.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void mostrarDetallesCliente(MouseEvent event) {
        if (event.getClickCount() == 1) {
            try {
                Cliente clienteSeleccionado = tableMiembros.getSelectionModel().getSelectedItem();
                if (clienteSeleccionado != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ConsultaCliente.fxml"));
                    Parent root = loader.load();

                    ConsultaClienteController controller = loader.getController();
                    controller.initData(clienteSeleccionado);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Detalles del Cliente");
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(primaryStage);
                    controller.setDialogStage(stage);
                    stage.showAndWait();
                } else {
                    System.out.println("No se seleccionó ningún cliente.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlertaError("Error al cargar la ventana de detalles del cliente: " + e.getMessage());
            }
        }
    }

    @FXML
    private void mostrarDetallesClase(MouseEvent event) {
        if (event.getClickCount() == 1) {
            try {
                Clase claseSeleccionada = tableClases.getSelectionModel().getSelectedItem();
                if (claseSeleccionada != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ConsultaClase.fxml"));
                    Parent root = loader.load();

                    ConsultaClaseController controller = loader.getController();
                    controller.initData(claseSeleccionada);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Detalles de la Clase");
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(primaryStage);
                    controller.setDialogStage(stage);
                    stage.showAndWait();
                } else {
                    System.out.println("No se seleccionó ninguna clase.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InicioSesion.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            if (primaryStage != null) {
                primaryStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}

