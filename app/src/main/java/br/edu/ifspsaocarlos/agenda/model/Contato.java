package br.edu.ifspsaocarlos.agenda.model;

import java.io.Serializable;

public class Contato implements Serializable{
    private static final long serialVersionUID = 1L;
    private long id;
    private String nome;
    private String fone;
    private String fone2; //item adicionado para a v3
    private String email;
    private int favorito; //item adicionado para marcar contato como favorito
    private String diaAniversario; //item adicionado para a v4
    private String mesAniversario; //item adicionado para a v4

    public Contato()
    {
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getFone() {
        return fone;
    }
    public void setFone(String fone) {
        this.fone = fone;
    }

    //Get e Set para o atributo fone2 da v3
    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //Get e Set para o atributo favorito
    public int getFavorito() {
        return favorito;
    }

    public void setFavorito(int favorito) {
        this.favorito = favorito;
    }

    //Get e Set para o atributo dia e mês de aniversário da v4
    public String getDiaAniversario() {
        return diaAniversario;
    }

    public void setDiaAniversario(String diaAniversario) {
        this.diaAniversario = diaAniversario;
    }


    public String getMesAniversario() {
        return mesAniversario;
    }

    public void setMesAniversario(String mesAniversario) {
        this.mesAniversario = mesAniversario;
    }
}

