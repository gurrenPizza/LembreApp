package com.ifrj.tcc.lembre.entidades;

public class Cartas {

    private String frente;
    private String verso;

    public Cartas() {    }

    public Cartas(String frente) {
        this.frente = frente;
    }

    public String getFrente() {
        return frente;
    }

    public void setFrente(String frente) {
        this.frente = frente;
    }

    public String getVerso() {
        return verso;
    }

    public void setVerso(String verso) {
        this.verso = verso;
    }
}
