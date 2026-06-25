package com.biblioteca.model;

/**
 * Aluno é uma especialização de Pessoa (HERANÇA).
 * Limite de empréstimo: 3 livros.
 */
public class Aluno extends Pessoa {
    private static final long serialVersionUID = 1L;

    public Aluno(String nome, String cpf, String email, String telefone) {
        super(nome, cpf, email, telefone);
    }

    @Override
    public int calcularLimiteEmprestimo() {
        return 3;
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }
}
