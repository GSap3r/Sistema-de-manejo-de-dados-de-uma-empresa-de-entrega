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
import org.example.javafx.db.dals.CategoriaDAL;
import org.example.javafx.db.entidades.Categoria;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TabCategoriaController implements Initializable {
    public static Categoria categoria = null;

    @FXML
    private TextField TFiltro;

    @FXML
    private TableColumn<Categoria, Integer> coId;

    @FXML
    private TableColumn<Categoria, String> coNome;

    @FXML
    private TableView<Categoria> tableView;

    @FXML
    private void onFechar(ActionEvent event) {
        // Obtém o Stage (janela atual) a partir do botão
        ((Stage)((Button)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onFiltrar(KeyEvent event) {
        carregarTabela("upper(cat_nome) LIKE '%" + TFiltro.getText().toUpperCase() + "%'");
    }

    @FXML
    void onNovoCategoria(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-categoria-create-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Categoria");
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
        CategoriaDAL dal = new CategoriaDAL();
        List<Categoria> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent) throws Exception {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            categoria = tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-categoria-create-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Alterar Categoria");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarTabela("");
            categoria = null;
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Posso apagar a categoria?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                Categoria c = tableView.getSelectionModel().getSelectedItem();
                CategoriaDAL dal = new CategoriaDAL();
                dal.apagar(c);
                carregarTabela("");
            }
        }
    }
}
