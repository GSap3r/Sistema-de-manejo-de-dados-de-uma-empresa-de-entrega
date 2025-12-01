package org.example.javafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.javafx.db.dals.CategoriaDAL;
import org.example.javafx.db.dals.MarcaDAL;
import org.example.javafx.db.dals.ProdutoDAL;
import org.example.javafx.db.entidades.Categoria;
import org.example.javafx.db.entidades.Marca;
import org.example.javafx.db.entidades.Produto;
import org.example.javafx.db.util.SingletonDB;
import org.example.javafx.util.MaskFieldUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class FormMarcaController implements Initializable {

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
        Marca marca = new Marca();
        marca.setNome(tfNome.getText());
        if (tfId.getText().isEmpty()) {
            if (!incluir(marca)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao incluir\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfNome.getScene().getWindow().hide();
        } else {
            marca.setId(Integer.parseInt(tfId.getText()));
            if (!alterar(marca)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfNome.getScene().getWindow().hide();
        }
    }

    private boolean incluir(Marca marca) {
        MarcaDAL dal = new MarcaDAL();
        return dal.gravar(marca);
    }

    private boolean alterar(Marca marca) {
        MarcaDAL dal = new MarcaDAL();
        return dal.alterar(marca);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Marca m = TabMarcaController.marca;
        if (m != null) {
            tfId.setText("" + m.getId());
            tfNome.setText(m.getNome());
        }
        Platform.runLater(() -> tfNome.requestFocus());
    }
}
