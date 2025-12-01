package org.example.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.db.dals.PedidoDAL;
import org.example.javafx.db.entidades.Pedido;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarPedidoController implements Initializable {

    @FXML
    private TableView<Pedido> tvPedidos;

    @FXML
    private TableColumn<Pedido, String> colId;

    @FXML
    private TableColumn<Pedido, String> colCliente;

    @FXML
    private TableColumn<Pedido, String> colData;

    @FXML
    private TableColumn<Pedido, String> colTotal;

    @FXML
    private TableColumn<Pedido, Void> colAcoes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getId())));
        colCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNomeCliente()));
        colData.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getData().toString()));
        colTotal.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f", cell.getValue().getTotal())));

        // action buttons column
        colAcoes.setCellFactory(col -> new TableCell<>(){
            private final Button btAlterar = new Button("Alterar");
            private final Button btEntregue = new Button("Entregue");
            private final Button btCancelar = new Button("Cancelar");
            private final HBox box = new HBox(6, btAlterar, btEntregue, btCancelar);

            {
                btAlterar.setOnAction(e -> onAlterar(getIndex()));
                btEntregue.setOnAction(e -> onMarcarEntregue(getIndex()));
                btCancelar.setOnAction(e -> onMarcarCancelado(getIndex()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= tvPedidos.getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });

        // initial load
        loadPedidos();

        // listen for updates
        PedidoNotifier.addListener(this::loadPedidos);
    }

    private void onAlterar(int index) {
        if (index < 0 || index >= tvPedidos.getItems().size()) return;
        Pedido p = tvPedidos.getItems().get(index);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/novo-pedido-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            // pass the pedido to the controller
            Object ctrl = loader.getController();
            if (ctrl instanceof NovoPedidoController) {
                ((NovoPedidoController) ctrl).setPedido(p);
            }
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onMarcarEntregue(int index) {
        if (index < 0 || index >= tvPedidos.getItems().size()) return;
        Pedido p = tvPedidos.getItems().get(index);
        p.setEntregue("S");
        new PedidoDAL().alterar(p);
        PedidoNotifier.notifyListeners();
        tvPedidos.getItems().remove(index);
    }

    private void onMarcarCancelado(int index) {
        if (index < 0 || index >= tvPedidos.getItems().size()) return;
        Pedido p = tvPedidos.getItems().get(index);
        p.setEntregue("C");
        new PedidoDAL().alterar(p);
        PedidoNotifier.notifyListeners();
        tvPedidos.getItems().remove(index);
    }

    private void loadPedidos() {
        try {
            List<Pedido> pedidos = new PedidoDAL().get("ped_entregue='N'");
            tvPedidos.setItems(FXCollections.observableArrayList(pedidos));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onAbrir(ActionEvent event) {
        Pedido p = tvPedidos.getSelectionModel().getSelectedItem();
        if (p != null) {
            onAlterar(tvPedidos.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void onFechar(ActionEvent event) {
        Stage stage = (Stage) ((Node) ((javafx.scene.control.Control) event.getSource())).getScene().getWindow();
        stage.close();
    }
}
