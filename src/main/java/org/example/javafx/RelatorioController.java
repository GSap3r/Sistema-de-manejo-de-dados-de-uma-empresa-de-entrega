package org.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.example.javafx.db.dals.PedidoDAL;
import org.example.javafx.db.entidades.Pedido;
import org.example.javafx.reports.PedidoReports;

import java.time.LocalDate;
import java.util.List;

public class RelatorioController {

    @FXML
    private DatePicker dpInicio;

    @FXML
    private DatePicker dpFim;

    @FXML
    private Button btnGerar;

    @FXML
    private Button btnCancelar;

    @FXML
    private void onGerar(ActionEvent event){
        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();
        if (inicio == null || fim == null) return;
        if (fim.isBefore(inicio)) {
            LocalDate t = inicio; inicio = fim; fim = t;
        }

        String filtro = "ped_data BETWEEN '" + inicio.toString() + "' AND '" + fim.toString() + "'";
        List<Pedido> pedidos = new PedidoDAL().get(filtro);

        PedidoReports.pedidosPeriodo(pedidos, inicio, fim);

        // fechar a janela do relatório
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelar(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
