package com.biblioteca.model;

import java.io.Serializable;

/**
 * Classe abstrata que representa uma pessoa cadastrada na biblioteca.
 * Serve de base para Aluno e Professor (HERANÇA).
 */
public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    // Atributos privados (ENCAPSULAMENTO)
    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    public Pessoa(String nome, String cpf, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Exibe os dados da pessoa de forma amigável (RNF03).
     */
    public void exibirDados() {
        System.out.println("Tipo.........: " + getTipo());
        System.out.println("Nome.........: " + nome);
        System.out.println("CPF..........: " + cpf);
        System.out.println("E-mail.......: " + email);
        System.out.println("Telefone.....: " + telefone);
        System.out.println("Limite livros: " + calcularLimiteEmprestimo());
    }

    /**
     * Método abstrato: cada subclasse define sua própria regra de limite.
     */
    public abstract int calcularLimiteEmprestimo();

    /**
     * Usado apenas para exibição amigável do tipo de usuário.
     */
    public abstract String getTipo();
}
