package org.example.candidavagasjavafx.entidades;

import java.sql.Date;

public class Vaga {
    private int idVaga;
    private String area;
    private int vagas;
    private Date dataAbertura;
    private Date dataEncerramento;
    private Double valor;
    private Date dataCadastro;
    private int inscritos;


    public Vaga() {
    }

    public Vaga(int idVaga, String area, int vagas,int inscritos, Date dataAbertura, Date dataEncerramento, Double valor , Date dataCadastro) {
        this.idVaga = idVaga;
        this.area = area;
        this.dataAbertura = dataAbertura;
        this.dataCadastro = dataCadastro;
        this.dataEncerramento = dataEncerramento;
        this.valor = valor;
        this.vagas = vagas;
        this.inscritos = inscritos;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public int getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(int idVaga) {
        this.idVaga = idVaga;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }
}
