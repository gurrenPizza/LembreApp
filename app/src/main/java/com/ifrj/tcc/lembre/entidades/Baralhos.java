package com.ifrj.tcc.lembre.entidades;

import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Baralhos {

    private String titulo;
    private String descricao;
    private String categoria;
    private String autor;
    private Integer quantCartas;

    public Baralhos() {    }

    public void salvar(){
        DatabaseReference referenciaDatabase = ConfiguracaoFirebase.getFirebase();
        referenciaDatabase.child("baralhos").child(String.valueOf(getTitulo())).setValue(this);
    }

    public Integer getQuantCartas() {
        return quantCartas;
    }

    public void setQuantCartas(Integer quantCartas) {
        this.quantCartas = quantCartas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
