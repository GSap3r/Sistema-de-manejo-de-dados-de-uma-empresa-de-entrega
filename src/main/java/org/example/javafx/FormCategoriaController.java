package org.example.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.javafx.db.dals.CategoriaDAL;
import org.example.javafx.db.entidades.Categoria;
import org.example.javafx.db.util.SingletonDB;


import java.net.URL;
import java.util.ResourceBundle;

public class FormCategoriaController implements Initializable {

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;



    @FXML
    void onCanc(ActionEvent event) {
        tfNome.getScene().getWindow().hide();
    }

    @FXML
    void onConfir(ActionEvent event) {
        Categoria categoria = new Categoria();
        categoria.setNome(tfNome.getText());
        if (tfId.getText().isEmpty()) {
            if (!incluir(categoria)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao incluir\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfNome.getScene().getWindow().hide();
        } else {
            categoria.setId(Integer.parseInt(tfId.getText()));
            if (!alterar(categoria)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfNome.getScene().getWindow().hide();
        }
    }

    private boolean incluir(Categoria categoria) {
        CategoriaDAL dal = new CategoriaDAL();
        return dal.gravar(categoria);
    }

    private boolean alterar(Categoria categoria) {
        CategoriaDAL dal = new CategoriaDAL();
        return dal.alterar(categoria);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Categoria c = TabCategoriaController.categoria;
        if (c != null) {
            tfId.setText("" + c.getId());
            tfNome.setText(c.getNome());
        }
        Platform.runLater(() -> tfNome.requestFocus());
    }
}
