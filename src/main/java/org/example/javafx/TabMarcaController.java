package org.example.javafx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.db.dals.MarcaDAL;
import org.example.javafx.db.entidades.Marca;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TabMarcaController implements Initializable {
    public static Marca marca = null;

    @FXML
    private TextField TFiltro;

    @FXML
    private TableColumn<Marca, Integer> coId;

    @FXML
    private TableColumn<Marca, String> coNome;

    @FXML
    private TableView<Marca> tableView;

    @FXML
    private void onFechar(ActionEvent event) {
        // Obtém o Stage (janela atual) a partir do botão
        ((Stage)((Button)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onFiltrar(KeyEvent event) {
        carregarTabela("upper(mar_nome) LIKE '%" + TFiltro.getText().toUpperCase() + "%'");
    }

    @FXML
    void onNovoMarca(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-marca-create-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Marca");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarTabela("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coId.setCellValueFactory(new PropertyValueFactory<>("id"));
        coNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        carregarTabela("");
    }

    private void carregarTabela(String filtro) {
        MarcaDAL dal = new MarcaDAL();
        List<Marca> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent) throws Exception {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            marca = tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-marca-create-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Alterar Marca");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarTabela("");
            marca = null;
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Posso apagar a marca?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                Marca m = tableView.getSelectionModel().getSelectedItem();
                MarcaDAL dal = new MarcaDAL();
                dal.apagar(m);
                carregarTabela("");
            }
        }
    }
}
