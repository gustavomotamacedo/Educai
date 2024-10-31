package edu.wecti.educai.model;

public class AssuntoModel {
    private String nome;
    private String resumo;
    private String videoLink;
    private String artigoLink;
    private boolean completado;

    public AssuntoModel() {
    }

    public AssuntoModel(String nome, String resumo, String videoLink, String artigoLink, boolean completado) {
        this.nome = nome;
        this.resumo = resumo;
        this.videoLink = videoLink;
        this.artigoLink = artigoLink;
        this.completado = completado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getArtigoLink() {
        return artigoLink;
    }

    public void setArtigoLink(String artigoLink) {
        this.artigoLink = artigoLink;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    @Override
    public String toString() {
        return "AssuntoModel{" +
                "nome='" + nome + '\'' +
                ", resumo='" + resumo + '\'' +
                ", videoLink='" + videoLink + '\'' +
                ", artigoLink='" + artigoLink + '\'' +
                ", completado=" + completado +
                '}';
    }
}
