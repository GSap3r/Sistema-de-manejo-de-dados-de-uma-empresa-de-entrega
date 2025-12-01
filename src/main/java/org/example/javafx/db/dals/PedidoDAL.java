package org.example.javafx.db.dals;



import org.example.javafx.db.entidades.Pedido;
import org.example.javafx.db.util.IDAL;
import org.example.javafx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidoDAL implements IDAL<Pedido> {
    @Override
    public boolean gravar(Pedido entidade) {
        boolean ok=true;
        String sql= """
                INSERT INTO pedido(
                	ped_data, ped_clinome, ped_clifone, ped_total, ped_entregue, tpg_id, ped_local, ped_numero_res)
                	VALUES ('#1', '#2', '#3', #4, '#5', #6, '#7', '#8');
                """;
        sql=sql.replace("#1",entidade.getData().toString());
        sql=sql.replace("#2",entidade.getNomeCliente());
        sql=sql.replace("#3",entidade.getFoneCliente());
        sql=sql.replace("#4",String.format(Locale.US,"%.2f",entidade.getTotal()));
        sql=sql.replace("#5",entidade.getEntregue());
        sql=sql.replace("#6",""+entidade.getTipoPagamento().getId());
        sql=sql.replace("#7",entidade.getLocal());
        sql=sql.replace("#8",entidade.getNumero());
        try {
            SingletonDB.getConexao().getConnect().setAutoCommit(false);
            if (SingletonDB.getConexao().manipular(sql)) {
                int id = SingletonDB.getConexao().getMaxPK("pedido", "ped_id");
                //gravar os itens do pedido
                for (Pedido.Item item : entidade.getItens()) {
                    String sql2 = "INSERT INTO item (pro_id, ped_id, it_quant, it_valor) VALUES (#1, #2, #3, #4)";
                    sql2 = sql2.replace("#1", "" + item.produto().getId());
                    sql2 = sql2.replace("#2", "" + id);
                    sql2 = sql2.replace("#3", "" + item.quantidade());
                    sql2 = sql2.replace("#4", String.format(Locale.US, "%.2f", item.valor()));
                    if (!SingletonDB.getConexao().manipular(sql2))
                        ok = false;
                }
            } else ok = false;
            if(!ok) SingletonDB.getConexao().getConnect().rollback();
            else SingletonDB.getConexao().getConnect().commit();
            SingletonDB.getConexao().getConnect().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ok;
    }

    @Override
    public boolean alterar(Pedido entidade) {
        //apagar todos os itens da tabela item referente ao pedido
        SingletonDB.getConexao().manipular("DELETE FROM item WHERE ped_id="+entidade.getId());
        //fazer update no pedido
        boolean ok=true;
        String sql= """
                UPDATE pedido SET
                	ped_data='#1', ped_clinome='#2', ped_clifone='#3', ped_total=#4, ped_entregue='#5', tpg_id=#6, ped_local='#7', ped_numero_res='#8'
                	WHERE ped_id=#9;
                """;
        sql=sql.replace("#1",entidade.getData().toString());
        sql=sql.replace("#2",entidade.getNomeCliente());
        sql=sql.replace("#3",entidade.getFoneCliente());
        sql=sql.replace("#4",String.format(Locale.US,"%.2f",entidade.getTotal()));
        sql=sql.replace("#5",entidade.getEntregue());
        sql=sql.replace("#6",""+entidade.getTipoPagamento().getId());
        sql=sql.replace("#7",entidade.getLocal());
        sql=sql.replace("#8",entidade.getNumero());
        sql=sql.replace("#9",""+entidade.getId());
        try {
            SingletonDB.getConexao().getConnect().setAutoCommit(false);
            if (SingletonDB.getConexao().manipular(sql)) {
                //gravar novamente os itens na tabela item
                int id = SingletonDB.getConexao().getMaxPK("pedido", "ped_id");
                //gravar os itens do pedido
                for (Pedido.Item item : entidade.getItens()) {
                    String sql2 = "INSERT INTO item (pro_id, ped_id, it_quant, it_valor) VALUES (#1, #2, #3, #4)";
                    sql2 = sql2.replace("#1", "" + item.produto().getId());
                    sql2 = sql2.replace("#2", "" + id);
                    sql2 = sql2.replace("#3", "" + item.quantidade());
                    sql2 = sql2.replace("#4", String.format(Locale.US, "%.2f", item.valor()));
                    if (!SingletonDB.getConexao().manipular(sql2))
                        ok = false;
                }
            } else ok = false;
            if(!ok) SingletonDB.getConexao().getConnect().rollback();
            else SingletonDB.getConexao().getConnect().commit();
            SingletonDB.getConexao().getConnect().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ok;
    }

    @Override
    public boolean apagar(Pedido entidade) {
        //apagar todos os itens da tabela item referente ao pedido
        SingletonDB.getConexao().manipular("DELETE FROM item WHERE ped_id="+entidade.getId());
        //apagar o pedido
        return SingletonDB.getConexao().manipular("DELETE FROM pedido WHERE ped_id="+entidade.getId());
    }

    @Override
    public Pedido get(int id) {
        Pedido pedido=null;

        try {
            String sql = "SELECT * FROM pedido WHERE ped_id=" + id;
            ResultSet rs=SingletonDB.getConexao().consultar(sql);
            if (rs.next()){
                pedido=new Pedido(rs.getInt("ped_id"),rs.getDate("ped_data").toLocalDate(),
                        rs.getString("ped_clinome"),rs.getString("ped_clifone"),
                        rs.getDouble("ped_total"),rs.getString("ped_entregue"),
                        new TipoPagamentoDAL().get(rs.getInt("tpg_id")),rs.getString("ped_local"),
                        rs.getString("ped_numero_res"));
            }
            String sql2="SELECT * FROM item WHERE ped_id="+pedido.getId();
            ResultSet rs2=SingletonDB.getConexao().consultar(sql2);
            while (rs2.next())
                pedido.addItem(new ProdutoDAL().get(rs2.getInt("pro_id")),rs2.getInt("it_quant"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pedido;
    }

    @Override
    public List<Pedido> get(String filtro) {
        List<Pedido> pedidoList=new ArrayList<>();

        try {
            String sql = "SELECT * FROM pedido";
            if(!filtro.isEmpty())
                sql+=" WHERE " + filtro;
            ResultSet rs=SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                Pedido pedido = new Pedido(rs.getInt("ped_id"), rs.getDate("ped_data").toLocalDate(),
                        rs.getString("ped_clinome"), rs.getString("ped_clifone"),
                        rs.getDouble("ped_total"), rs.getString("ped_entregue"),
                        new TipoPagamentoDAL().get(rs.getInt("tpg_id")), rs.getString("ped_local"),
                        rs.getString("ped_numero_res"));

                String sql2 = "SELECT * FROM item WHERE ped_id=" + pedido.getId();
                ResultSet rs2 = SingletonDB.getConexao().consultar(sql2);
                while (rs2.next())
                    pedido.addItem(new ProdutoDAL().get(rs2.getInt("pro_id")), rs2.getInt("it_quant"));
                pedidoList.add(pedido);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pedidoList;
    }
}
