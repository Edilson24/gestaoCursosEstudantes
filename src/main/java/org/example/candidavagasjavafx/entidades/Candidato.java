package org.example.candidavagasjavafx.entidades;

public class Candidato {
    private int idCandidato;
    private String nome;
    private int idade;
    private String curso;
    private float pagamento;

    public Candidato() {
    }

    public Candidato(int idCandidato, String nome, int idade, String curso, float pagamento) {
        this.idCandidato = idCandidato;
        this.nome = nome;
        this.idade = idade;
        this.curso = curso;
        this.pagamento = pagamento;
    }

    public int getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(int idCandidato) {
        this.idCandidato = idCandidato;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPagamento() {
        return pagamento;
    }

    public void setPagamento(float pagamento) {
        this.pagamento = pagamento;
    }
}
