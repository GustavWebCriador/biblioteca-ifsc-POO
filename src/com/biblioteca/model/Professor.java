package com.biblioteca.model;

/**
 * Professor é uma especialização de Pessoa (HERANÇA).
 * Limite de empréstimo: 10 livros.
 */
public class Professor extends Pessoa {
    private static final long serialVersionUID = 1L;

    public Professor(String nome, String cpf, String email, String telefone) {
        super(nome, cpf, email, telefone);
    }

    @Override
    public int calcularLimiteEmprestimo() {
        return 10;
    }

    @Override
    public String getTipo() {
        return "Professor";
    }
}
