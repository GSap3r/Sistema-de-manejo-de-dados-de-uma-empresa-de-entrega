package org.example.javafx.db.dals;



import org.example.javafx.db.entidades.Marca;
import org.example.javafx.db.util.IDAL;
import org.example.javafx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAL implements IDAL<Marca> {
    @Override
    public boolean gravar(Marca entidade) {
        String sql="INSERT INTO marca (mar_nome) VALUES ('#1')";
        sql=sql.replace("#1",entidade.getNome());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Marca entidade) {
        String sql="UPDATE marca SET mar_nome='#1' WHERE mar_id=#2";
        sql=sql.replace("#1",entidade.getNome());
        sql=sql.replace("#2",""+entidade.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Marca entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM marca WHERE mar_id="+entidade.getId());
    }

    @Override
    public Marca get(int id) {
        Marca marca=null;
        try {
            String sql = "SELECT * FROM marca WHERE mar_id=" + id;
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                marca = new Marca(rs.getInt("mar_id"), rs.getString("mar_nome"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return marca;
    }

    @Override
    public List<Marca> get(String filtro) {
        List<Marca> MarcaList=new ArrayList<>();
        try {
            String sql = "SELECT * FROM marca";
            if(!filtro.isEmpty())
                sql+=" WHERE "+filtro;
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                Marca marca = new Marca(rs.getInt("mar_id"), rs.getString("mar_nome"));
                MarcaList.add(marca);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return MarcaList;
    }
}
