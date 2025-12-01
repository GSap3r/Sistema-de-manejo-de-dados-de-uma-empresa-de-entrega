package org.example.javafx.db.dals;



import org.example.javafx.db.entidades.Produto;
import org.example.javafx.db.util.IDAL;
import org.example.javafx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdutoDAL implements IDAL<Produto> {
    @Override
    public boolean gravar(Produto entidade) {
        String sql= """
                INSERT INTO produto(
                	pro_nome, pro_preco, cat_id, pro_volume, mar_id)
                	VALUES ('#1', #2, #3, #4, #5);
                """;
        sql=sql.replace("#1",entidade.getNome());
        sql=sql.replace("#2",""+String.format(Locale.US,"%.2f",entidade.getPreco()));
        sql=sql.replace("#3",""+entidade.getCategoria().getId());
        sql=sql.replace("#4",""+entidade.getVolume());
        sql=sql.replace("#5",""+entidade.getMarca().getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Produto entidade) {
        String sql= """
                UPDATE produto 
                	SET pro_nome='#1', pro_preco=#2, cat_id=#3, pro_volume=#4, mar_id=#5
                	WHERE pro_id=#6
                """;
        sql=sql.replace("#1",entidade.getNome());
        sql=sql.replace("#2",""+String.format(Locale.US,"%.2f",entidade.getPreco()));
        sql=sql.replace("#3",""+entidade.getCategoria().getId());
        sql=sql.replace("#4",""+entidade.getVolume());
        sql=sql.replace("#5",""+entidade.getMarca().getId());
        sql=sql.replace("#6",""+entidade.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Produto entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM produto WHERE pro_id="+entidade.getId());
    }

    @Override
    public Produto get(int id) {
        Produto produto=null;
        try {
            String sql = "SELECT * FROM produto WHERE pro_id=" + id;
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                produto = new Produto(rs.getInt("pro_id"), rs.getString("pro_nome"),
                        rs.getDouble("pro_preco"),rs.getInt("pro_volume"),
                        new CategoriaDAL().get(rs.getInt("cat_id")),
                        new MarcaDAL().get(rs.getInt("mar_id")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produto;
    }

    @Override
    public List<Produto> get(String filtro) {
        List <Produto> produtoList=new ArrayList<>();
        try {
            String sql = "SELECT * FROM produto";
            if(!filtro.isEmpty())
                sql+=" WHERE "+filtro;
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                Produto produto = new Produto(rs.getInt("pro_id"), rs.getString("pro_nome"),
                        rs.getDouble("pro_preco"),rs.getInt("pro_volume"),
                        new CategoriaDAL().get(rs.getInt("cat_id")),
                        new MarcaDAL().get(rs.getInt("mar_id")));
                produtoList.add(produto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produtoList;
    }
}
