package org.example.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.db.dals.PedidoDAL;
import org.example.javafx.db.entidades.Pedido;
import org.example.javafx.reports.PedidoReports;

import java.util.List;

public class MenuController {

    @FXML
    void onCadCategoria(ActionEvent event) {
        openModal("form-categoria-view.fxml", "Cadastro de categorias");

    }

    @FXML
    void onCadMarca(ActionEvent event) {
        openModal("form-marca-view.fxml", "Cadastro de marcas");

    }

    @FXML
    void onCadProduto(ActionEvent event) {
        openModal("tab-produto-view.fxml", "Gerenciamento de produtos");

    }



    @FXML
    void onNovoPedido(ActionEvent event) {
        openModal("novo-pedido-view.fxml", "Cadastro de pedidos");

    }

    @FXML
    void onSair(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setHeaderText("Confirmar saída");
        alert.setContentText("Deseja sair do aplicativo?");
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                Platform.exit();
            }
        });

    }

    @FXML
    void onSobre(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre...");
        alert.setHeaderText("ManeDeliveryFX");
        alert.setContentText("Desenvolvido por:\nGuilherme Sales\nIgor\nLais\n\nFIPP, 2025");
        alert.showAndWait();

    }

    @FXML
    public void onCadTipoPagamento(ActionEvent actionEvent) {
        openModal("form-tipopagamento-view.fxml", "Cadastro Tipo de Pagamento");
    }

    @FXML
    public void onGerenciarPedido(ActionEvent actionEvent) {
        openModal("gerenciar-pedido-view.fxml", "Gerenciar pedidos");
    }

    @FXML
    public void onRelatorio(ActionEvent actionEvent) {
        openModal("relatorio-view.fxml", "Relatórios");
    }

    private void openModal(String resource, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource(resource));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showException("Erro ao abrir janela", "Não foi possível abrir: " + resource, e);
        }
    }

    private void showException(String header, String content, Exception e) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(header);
            alert.setContentText(content + "\n" + e.getMessage());

            // expandable Exception details
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement el : e.getStackTrace()) {
                sb.append(el.toString()).append("\n");
            }
            javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea(sb.toString());
            ta.setEditable(false);
            ta.setWrapText(true);
            ta.setMaxWidth(Double.MAX_VALUE);
            ta.setMaxHeight(Double.MAX_VALUE);
            javafx.scene.layout.GridPane expContent = new javafx.scene.layout.GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(ta, 0, 0);
            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
