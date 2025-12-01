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
import org.example.javafx.db.dals.TipoPagamentoDAL;
import org.example.javafx.db.entidades.TipoPagamento;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TabTipoPagamentoController implements Initializable {
    public static TipoPagamento tipoPagamento = null;

    @FXML
    private TextField TFiltro;

    @FXML
    private TableColumn<TipoPagamento, Integer> coId;

    @FXML
    private TableColumn<TipoPagamento, String> coNome;

    @FXML
    private TableView<TipoPagamento> tableView;

    @FXML
    private void onFechar(ActionEvent event) {
        // Obtém o Stage (janela atual) a partir do botão
        ((Stage)((Button)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onFiltrar(KeyEvent event) {
        carregarTabela("upper(tpg_nome) LIKE '%" + TFiltro.getText().toUpperCase() + "%'");
    }

    @FXML
    void onNovoTipo(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-tipopagamento-create-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro Tipo Pagamento");
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
        TipoPagamentoDAL dal = new TipoPagamentoDAL();
        List<TipoPagamento> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent) throws Exception {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            tipoPagamento = tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-tipopagamento-create-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Alterar Tipo Pagamento");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarTabela("");
            tipoPagamento = null;
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Posso apagar o tipo de pagamento?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                TipoPagamento t = tableView.getSelectionModel().getSelectedItem();
                TipoPagamentoDAL dal = new TipoPagamentoDAL();
                dal.apagar(t);
                carregarTabela("");
            }
        }
    }
}
