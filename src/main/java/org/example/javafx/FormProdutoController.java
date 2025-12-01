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

public class FormProdutoController implements Initializable {

    public TextField tfPreco;
    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfVolume;

    @FXML
    void onCanc(ActionEvent event) {
        tfVolume.getScene().getWindow().hide();

    }

    @FXML
    void onConfir(ActionEvent event) {
        Produto produto=new Produto();
        produto.setNome(tfNome.getText());
        produto.setVolume(Integer.parseInt(tfVolume.getText()));
        produto.setPreco(Double.parseDouble(tfPreco.getText().replace(",",".")));
        produto.setCategoria(cbCategoria.getValue());
        produto.setMarca(cbMarca.getValue());
        if(tfId.getText().isEmpty()) {
            if (!incluir(produto)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao incluir\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
            else tfVolume.getScene().getWindow().hide();
        }
        else {
            produto.setId(Integer.parseInt(tfId.getText()));
            if (!alterar(produto)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            } else tfVolume.getScene().getWindow().hide();
        }
    }

    private boolean incluir(Produto produto) {
        ProdutoDAL dal=new ProdutoDAL();
        return dal.gravar(produto);
    }

    private boolean alterar(Produto produto) {
        ProdutoDAL dal=new ProdutoDAL();
        return dal.alterar(produto);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MaskFieldUtil.numericField(tfVolume);
        MaskFieldUtil.monetaryField(tfPreco);
        carregarCB();
        Produto produto=TabProdutoController.produto;
        if(produto!=null) {
            tfId.setText(""+produto.getId());
            tfNome.setText(produto.getNome());
            tfVolume.setText(""+produto.getVolume());
            tfPreco.setText(String.format("%.2f",produto.getPreco()));
            cbMarca.setValue(produto.getMarca());
            cbCategoria.setValue(produto.getCategoria());
        }
        Platform.runLater(()->tfNome.requestFocus());
    }

    private void carregarCB() {
        CategoriaDAL categoriaDAL=new CategoriaDAL();
        MarcaDAL marcaDAL=new MarcaDAL();
        List<Categoria> categoriaList=categoriaDAL.get("");
        //carregar todas as categorias no combobox
        cbCategoria.setItems(FXCollections.observableArrayList(categoriaList));
        //carregar todas as marcas no combobox
        cbMarca.setItems(FXCollections.observableArrayList(marcaDAL.get("")));
    }
}
