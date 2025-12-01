package org.example.javafx.db.entidades;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int volume;
    private Categoria categoria;
    private Marca marca;

    public Produto(int id, String nome, double preco, int volume, Categoria categoria, Marca marca) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.volume = volume;
        this.categoria = categoria;
        this.marca = marca;
    }

    public Produto(String nome, double preco, int volume, Categoria categoria, Marca marca) {
        this(0, nome, preco, volume, categoria, marca);
    }

    public Produto() {
        this(0, "", 0, 0, null, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return nome;
    }
}
