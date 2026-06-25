package com.biblioteca.persistence;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Pessoa;

import java.io.Serializable;
import java.util.List;

/**
 * Estrutura simples que agrupa todo o estado da biblioteca
 * para ser gravado/lido de uma só vez (RF08 - persistência).
 */
public class DadosBiblioteca implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Pessoa> usuarios;
    private final List<Livro> livros;
    private final List<Emprestimo> emprestimos;

    public DadosBiblioteca(List<Pessoa> usuarios, List<Livro> livros, List<Emprestimo> emprestimos) {
        this.usuarios = usuarios;
        this.livros = livros;
        this.emprestimos = emprestimos;
    }

    public List<Pessoa> getUsuarios() {
        return usuarios;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}
