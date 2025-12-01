package org.example.javafx;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.db.dals.PedidoDAL;
import org.example.javafx.db.dals.ProdutoDAL;
import org.example.javafx.db.dals.TipoPagamentoDAL;
import org.example.javafx.db.entidades.Pedido;
import org.example.javafx.db.entidades.Produto;
import org.example.javafx.db.entidades.TipoPagamento;
import org.example.javafx.db.util.SingletonDB;
import org.example.javafx.util.MaskFieldUtil;
import org.example.javafx.util.ModalTable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class NovoPedidoController implements Initializable {

    private Produto produto;
    private double totalPedido=0;
    private Pedido editingPedido = null;


    @FXML
    public TableView <Pedido.Item>tableView;

    @FXML
    private Button btProduto;

    @FXML
    private ComboBox<TipoPagamento> cbTipoPagamento;

    @FXML
    private TableColumn<Pedido.Item, String> coProduto;

    @FXML
    private TableColumn<Pedido.Item, String> coQuant;

    @FXML
    private TableColumn<Pedido.Item, String> coValor;

    @FXML
    private Label lbTotal;

    @FXML
    private Spinner<Integer> spQuant;

    @FXML
    private TextField tfCep;

    @FXML
    private TextField tfCliente;

    @FXML
    private TextField tfEndereco;

    @FXML
    private TextField tfNumero;

    @FXML
    private TextField tfTelefone;



    @FXML
    void onBuscarCep(KeyEvent event) {
        if(tfCep.getText().length()==9){
            String sjson=consultaCep(tfCep.getText(),"json");
            JSONObject json = new JSONObject(sjson);
            tfEndereco.setText(json.getString("logradouro")+", "+
                    json.getString("bairro")+", "+json.getString("localidade")+"/"+
                    json.getString("uf"));
            Platform.runLater(()->tfNumero.requestFocus()); //coloca o cursor no textfield de número
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        if(totalPedido>0) {
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Atenção");
            alert.setContentText("Deseja realmente abandonar o Pedido?");
            if(alert.showAndWait().get()==ButtonType.OK)
                lbTotal.getScene().getWindow().hide(); //fechando a janela
        }
        else
            lbTotal.getScene().getWindow().hide(); //fechando a janela
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        // antes, verifique se todas as informações foram preenchidas
        if (tfCliente.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Dados incompletos");
            a.setContentText("Preencha o nome do cliente.");
            a.showAndWait();
            return;
        }
        if (tfTelefone.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Dados incompletos");
            a.setContentText("Preencha o telefone do cliente.");
            a.showAndWait();
            return;
        }
        if (tfEndereco.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Dados incompletos");
            a.setContentText("Preencha o endereço do cliente.");
            a.showAndWait();
            return;
        }
        if (cbTipoPagamento.getValue() == null) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Dados incompletos");
            a.setContentText("Selecione uma forma de pagamento.");
            a.showAndWait();
            return;
        }
        if (tableView.getItems().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Dados incompletos");
            a.setContentText("Inclua ao menos 1 item no pedido.");
            a.showAndWait();
            return;
        }

        Pedido pedido=new Pedido();
        pedido.setNomeCliente(tfCliente.getText());
        pedido.setFoneCliente(tfTelefone.getText());
        pedido.setLocal(tfEndereco.getText());
        pedido.setNumero(tfNumero.getText());
        pedido.setTipoPagamento(cbTipoPagamento.getValue());
        for(Pedido.Item item : tableView.getItems())
            pedido.addItem(item.produto(), item.quantidade());
        PedidoDAL dal=new PedidoDAL();
        boolean ok;
        if (editingPedido != null) {
            // preserve id and date and entregue flag
            pedido.setId(editingPedido.getId());
            pedido.setData(editingPedido.getData());
            pedido.setEntregue(editingPedido.getEntregue());
            ok = dal.alterar(pedido);
        } else {
            ok = dal.gravar(pedido);
        }
        if(!ok) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao criar o Pedido");
            alert.setContentText(SingletonDB.getConexao().getMensagemErro());
        }
        else {
            // notify other screens that pedidos changed
            org.example.javafx.PedidoNotifier.notifyListeners();
            lbTotal.getScene().getWindow().hide(); //fechando a janela
        }
    }
    @FXML
    void onApagarProduto(ActionEvent event) {
        Pedido.Item item=tableView.getSelectionModel().getSelectedItem();
        if(item!=null) {  //tem item selecionado
            tableView.getItems().remove(item);
        }
    }

    @FXML
    void onIncluirProduto(ActionEvent event) {
        if (produto == null) return;

        int quant = spQuant.getValue();

        // check for existing item with same product -> sum quantities
        Pedido.Item existing = null;
        for (Pedido.Item it : tableView.getItems()) {
            if (it.produto().getId() == produto.getId()) {
                existing = it;
                break;
            }
        }

        if (existing != null) {
            int newQuant = existing.quantidade() + quant;
            // remove and re-add combined item
            tableView.getItems().remove(existing);
            Pedido.Item combined = new Pedido.Item(produto, newQuant, newQuant * produto.getPreco());
            tableView.getItems().add(combined);
        } else {
            Pedido.Item item=new Pedido.Item(produto, quant, produto.getPreco() * quant);
            tableView.getItems().add(item);
        }

        // recalcular total
        totalPedido = 0;
        for (Pedido.Item it : tableView.getItems()) totalPedido += it.valor();
        lbTotal.setText(String.format("%.2f", totalPedido));

        // reset produto selection and spinner (restore original prompt)
        produto = null;
        btProduto.setText("Selecione o produto");
        spQuant.getValueFactory().setValue(1);
    }

    @FXML
    void onSelProduto(ActionEvent event) {
        ModalTable mt=new ModalTable(new ProdutoDAL().get(""),new String[]{"id","nome","preco"},"nome");
        Stage stage=new Stage();
        stage.setScene(new Scene(mt));
        stage.setWidth(600); stage.setHeight(480); stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        produto = (Produto)mt.getSelecionado();
        if (produto!=null) {
            btProduto.setText(produto.getNome());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spQuant.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1));
        MaskFieldUtil.cepField(tfCep);
        MaskFieldUtil.foneField(tfTelefone);
        //mapear as colunas
        coProduto.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().produto().getNome()));
        coQuant.setCellValueFactory(cellData->new SimpleStringProperty(""+cellData.getValue().quantidade()));
        coValor.setCellValueFactory(cellData->new SimpleStringProperty(""+cellData.getValue().valor()));
        carregarCB();
    }

    private void carregarCB() {
        TipoPagamentoDAL dal=new TipoPagamentoDAL();
        cbTipoPagamento.setItems(FXCollections.observableArrayList(dal.get("")));
    }

    public void setPedido(Pedido p) {
        this.editingPedido = p;
        if (p == null) return;
        tfCliente.setText(p.getNomeCliente());
        tfTelefone.setText(p.getFoneCliente());
        tfEndereco.setText(p.getLocal());
        tfNumero.setText(p.getNumero());
        // select tipo pagamento if available
        if (p.getTipoPagamento() != null) {
            for (TipoPagamento t : cbTipoPagamento.getItems()) {
                if (t.getId() == p.getTipoPagamento().getId()) {
                    cbTipoPagamento.setValue(t);
                    break;
                }
            }
        }
        tableView.getItems().clear();
        for (Pedido.Item it : p.getItens())
            tableView.getItems().add(it);
        totalPedido = p.getTotal();
        lbTotal.setText(String.format("%.2f", totalPedido));
    }

    public static String consultaCep(String cep, String formato)
    {
        StringBuffer dados = new StringBuffer();
        try {
            URL url = new URL("https://viacep.com.br/ws/"+ cep + "/"+formato+"/");
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false);
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s = "";
            while (null != (s = br.readLine()))
                dados.append(s);
            br.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dados.toString();
    }
}

