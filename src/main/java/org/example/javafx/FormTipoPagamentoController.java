package org.example.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.javafx.db.dals.TipoPagamentoDAL;
import org.example.javafx.db.entidades.TipoPagamento;
import org.example.javafx.db.util.SingletonDB;

import java.net.URL;
import java.util.ResourceBundle;

public class FormTipoPagamentoController implements Initializable {

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
        TipoPagamento t = new TipoPagamento();
        t.setNome(tfNome.getText());
        if (tfId.getText().isEmpty()) {
            if (!incluir(t)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao incluir\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfNome.getScene().getWindow().hide();
        } else {
            t.setId(Integer.parseInt(tfId.getText()));
            if (!alterar(t)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfNome.getScene().getWindow().hide();
        }
    }

    private boolean incluir(TipoPagamento t) {
        TipoPagamentoDAL dal = new TipoPagamentoDAL();
        return dal.gravar(t);
    }

    private boolean alterar(TipoPagamento t) {
        TipoPagamentoDAL dal = new TipoPagamentoDAL();
        return dal.alterar(t);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TipoPagamento t = TabTipoPagamentoController.tipoPagamento;
        if (t != null) {
            tfId.setText("" + t.getId());
            tfNome.setText(t.getNome());
        }
        Platform.runLater(() -> tfNome.requestFocus());
    }
}
