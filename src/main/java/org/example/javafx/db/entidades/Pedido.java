package org.example.javafx.db.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    //inner class (record replaced by equivalent class for broader Java compatibility)
    public static class Item {
        private final Produto produto;
        private final int quantidade;
        private final double valor;

        public Item(Produto produto, int quantidade, double valor) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.valor = valor;
        }

        public Produto produto() { return produto; }
        public int quantidade() { return quantidade; }
        public double valor() { return valor; }
    }
    private int id;
    private LocalDate data;
    private String nomeCliente;
    private String foneCliente;
    private double total;
    private String entregue;
    private TipoPagamento tipoPagamento;
    private String local;
    private String numero;
    private List<Item> itens;

    public Pedido(int id, LocalDate data, String nomeCliente, String foneCliente, double total, String entregue, TipoPagamento tipoPagamento, String local, String numero) {
        this.id = id;
        this.data = data;
        this.nomeCliente = nomeCliente;
        this.foneCliente = foneCliente;
        this.total = total;
        this.entregue = entregue;
        this.tipoPagamento = tipoPagamento;
        this.local = local;
        this.numero = numero;
        itens=new ArrayList<>();
    }

    public Pedido(LocalDate data, String nomeCliente, String foneCliente, double total, String entregue, TipoPagamento tipoPagamento, String local, String numero) {
        this(0,data,nomeCliente,foneCliente,total,entregue,tipoPagamento,local,numero);
    }

    public Pedido() {
        this(0,LocalDate.now(),"","",0,"N",null,"","");
    }

    public boolean addItem(Produto produto, int quantidade){
        Item item=new Item(produto,quantidade,quantidade * produto.getPreco());
        if (itens.add(item)) {
            totalizarItens();
            return true;
        }
        return false;
    }

    private void totalizarItens(){
        double total=0;
        for(Item item : itens)
            total+=item.valor();
        this.total=total;
    }

    public List<Item> getItens() {
        return itens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getFoneCliente() {
        return foneCliente;
    }

    public void setFoneCliente(String foneCliente) {
        this.foneCliente = foneCliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEntregue() {
        return entregue;
    }

    public void setEntregue(String entregue) {
        this.entregue = entregue;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
