package com.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Empréstimo associa uma Pessoa a um Livro (ASSOCIAÇÃO ENTRE CLASSES).
 */
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static int proximoId = 1;

    private int id;
    private Pessoa pessoa;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean ativo;

    public Emprestimo(Pessoa pessoa, Livro livro) {
        this.id = proximoId++;
        this.pessoa = pessoa;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = null;
        this.ativo = true;
    }

    public int getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Marca o empréstimo como devolvido, registrando a data de hoje.
     */
    public void finalizar() {
        this.dataDevolucao = LocalDate.now();
        this.ativo = false;
    }

    /**
     * Garante que o contador de IDs continue de onde parou após
     * carregar empréstimos salvos em arquivo (evita IDs repetidos).
     */
    public static void ajustarProximoId(int idCarregado) {
        if (idCarregado >= proximoId) {
            proximoId = idCarregado + 1;
        }
    }

    @Override
    public String toString() {
        String devolucaoStr = dataDevolucao != null ? dataDevolucao.format(FORMATADOR) : "Pendente";
        String status = ativo ? "ATIVO" : "FINALIZADO";
        return "Empréstimo #" + id
                + " | Livro: " + livro.getTitulo() + " (" + livro.getIsbn() + ")"
                + " | Usuário: " + pessoa.getNome() + " (" + pessoa.getCpf() + ")"
                + " | Emprestado em: " + dataEmprestimo.format(FORMATADOR)
                + " | Devolvido em: " + devolucaoStr
                + " | Status: " + status;
    }
}
