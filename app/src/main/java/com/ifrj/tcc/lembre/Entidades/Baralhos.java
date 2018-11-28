package com.ifrj.tcc.lembre.Entidades;

import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Baralhos {

    private String titulo;
    private String descricao;
    private String categoria;
    private String img;
    private String autor;
    private ArrayList<Cartas> listaCartas;

    public Baralhos() {    }

    public void salvar(){
        DatabaseReference referenciaDatabase = ConfiguracaoFirebase.getFirebase();
        referenciaDatabase.child("baralhos").child(String.valueOf(getTitulo())).setValue(this);
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<Cartas> getListaCartas() {
        return listaCartas;
    }

    public void setListaCartas(ArrayList<Cartas> listaCartas) {
        this.listaCartas = listaCartas;
    }
}
