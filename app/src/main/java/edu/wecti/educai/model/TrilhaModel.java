package edu.wecti.educai.model;

import java.util.ArrayList;

public class TrilhaModel {
    private String nome;
    private ArrayList<AssuntoModel> assuntoModels;

    public TrilhaModel() {
    }

    public TrilhaModel(String nome, ArrayList<AssuntoModel> assuntoModels) {
        this.nome = nome;
        this.assuntoModels = assuntoModels;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<AssuntoModel> getAssuntoModels() {
        return assuntoModels;
    }

    public void setAssuntoModels(ArrayList<AssuntoModel> assuntoModels) {
        this.assuntoModels = assuntoModels;
    }

    @Override
    public String toString() {
        return "TrilhaModel{" +
                "nome='" + nome + '\'' +
                ", assuntoModels=" + assuntoModels.toString() +
                '}';
    }
}
