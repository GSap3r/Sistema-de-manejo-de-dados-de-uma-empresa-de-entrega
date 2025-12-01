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
import org.example.javafx.db.dals.ProdutoDAL;
import org.example.javafx.db.entidades.Categoria;
import org.example.javafx.db.entidades.Produto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TabProdutoController implements Initializable {
    public static Produto produto=null;
    @FXML
    private TextField TFiltro;

    @FXML
    private TableColumn<Produto, Categoria> coCat;

    @FXML
    private TableColumn<Produto, Integer> coId;

    @FXML
    private TableColumn<Produto, String> coNome;

    @FXML
    private TableView<Produto> tableView;

    @FXML
    private void onFechar(ActionEvent event) {
        // Obtém o Stage (janela atual) a partir do botão
        ((Stage)((Button)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onFiltrar(KeyEvent event) {
       carregarTabela("upper(pro_nome) LIKE '%"+TFiltro.getText().toUpperCase()+"%'");
    }

    @FXML
    void onNovoProduto(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-produto-view.fxml"));
            Stage stage=new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Gerenciamento de produtos");
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
        coCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        carregarTabela("");
    }

    private void carregarTabela(String filtro) {
        ProdutoDAL dal = new ProdutoDAL();
        List<Produto> produtoList=dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(produtoList));
    }

    public void onAlterar(ActionEvent actionEvent) throws Exception{
        if(tableView.getSelectionModel().getSelectedIndex()!=-1) {
            produto=tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("form-produto-view.fxml"));
            Stage stage=new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Gerenciamento de produtos");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarTabela("");
            produto=null;
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if(tableView.getSelectionModel().getSelectedIndex()!=-1) {
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Posso apagar o produto?");
            if(alert.showAndWait().get()== ButtonType.OK) {
                Produto produto = tableView.getSelectionModel().getSelectedItem();
                ProdutoDAL dal = new ProdutoDAL();
                dal.apagar(produto);
                carregarTabela("");
            }
        }
    }
}
