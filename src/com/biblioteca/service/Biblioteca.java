package com.biblioteca.service;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Pessoa;
import com.biblioteca.persistence.DadosBiblioteca;
import com.biblioteca.persistence.jdbc.EmprestimoDAO;
import com.biblioteca.persistence.jdbc.LivroDAO;
import com.biblioteca.persistence.jdbc.UsuarioDAO;

import java.util.List;

/**
 * Classe central do sistema: concentra as regras de negócio (RF01 a RF07).
 *
 * Pode operar em dois modos:
 *   - ARQUIVO  → dados ficam em memória + gravados em biblioteca.dat
 *   - BANCO    → cada operação lê/escreve diretamente no PostgreSQL via DAOs
 *
 * O modo é escolhido no construtor (ver Main.java).
 */
public class Biblioteca {

    public enum Modo { ARQUIVO, BANCO }

    private final Modo modo;

    // Listas usadas somente no modo ARQUIVO
    private java.util.List<Pessoa>    usuarios   = new java.util.ArrayList<>();
    private java.util.List<Livro>     livros     = new java.util.ArrayList<>();
    private java.util.List<Emprestimo> emprestimos = new java.util.ArrayList<>();

    // DAOs usados somente no modo BANCO
    private final UsuarioDAO   usuarioDAO;
    private final LivroDAO     livroDAO;
    private final EmprestimoDAO emprestimoDAO;

    /** Construtor padrão — inicia no modo ARQUIVO (sem banco). */
    public Biblioteca() {
        this(Modo.ARQUIVO);
    }

    /** Construtor com escolha de modo. */
    public Biblioteca(Modo modo) {
        this.modo = modo;
        if (modo == Modo.BANCO) {
            usuarioDAO    = new UsuarioDAO();
            livroDAO      = new LivroDAO();
            emprestimoDAO = new EmprestimoDAO();
        } else {
            usuarioDAO    = null;
            livroDAO      = null;
            emprestimoDAO = null;
        }
    }

    // ================================================================
    //  Usuários (RF01, RF02)
    // ================================================================

    public void cadastrarUsuario(Pessoa pessoa) {
        if (modo == Modo.BANCO) {
            usuarioDAO.salvar(pessoa);
        } else {
            usuarios.add(pessoa);
        }
    }

    public List<Pessoa> listarUsuarios() {
        return modo == Modo.BANCO ? usuarioDAO.listarTodos() : usuarios;
    }

    public Pessoa buscarUsuarioPorCpf(String cpf) {
        if (modo == Modo.BANCO) {
            return usuarioDAO.buscarPorCpf(cpf);
        }
        for (Pessoa p : usuarios) {
            if (p.getCpf().equalsIgnoreCase(cpf)) return p;
        }
        return null;
    }

    // ================================================================
    //  Livros (RF03, RF04)
    // ================================================================

    public void cadastrarLivro(Livro livro) {
        if (modo == Modo.BANCO) {
            livroDAO.salvar(livro);
        } else {
            livros.add(livro);
        }
    }

    public List<Livro> listarLivros() {
        return modo == Modo.BANCO ? livroDAO.listarTodos() : livros;
    }

    public Livro buscarLivroPorIsbn(String isbn) {
        if (modo == Modo.BANCO) {
            return livroDAO.buscarPorIsbn(isbn);
        }
        for (Livro l : livros) {
            if (l.getIsbn().equalsIgnoreCase(isbn)) return l;
        }
        return null;
    }

    // ================================================================
    //  Empréstimos (RF05, RF06, RF07)
    // ================================================================

    public String realizarEmprestimo(String cpf, String isbn) {
        Pessoa pessoa = buscarUsuarioPorCpf(cpf);
        if (pessoa == null) return "Usuário não encontrado. Verifique o CPF informado.";

        Livro livro = buscarLivroPorIsbn(isbn);
        if (livro == null) return "Livro não encontrado. Verifique o ISBN informado.";

        if (!livro.isDisponivel()) return "Este livro já está emprestado e não pode ser retirado.";

        // contagem de empréstimos ativos do usuário
        long ativos;
        if (modo == Modo.BANCO) {
            ativos = emprestimoDAO.listarAtivos().stream()
                    .filter(e -> e.getPessoa().getCpf().equalsIgnoreCase(cpf))
                    .count();
        } else {
            ativos = emprestimos.stream()
                    .filter(e -> e.isAtivo() && e.getPessoa().getCpf().equalsIgnoreCase(cpf))
                    .count();
        }

        int limite = pessoa.calcularLimiteEmprestimo();  // polimorfismo: Aluno=3, Professor=10
        if (ativos >= limite) {
            return "Limite atingido para " + pessoa.getNome()
                    + " (máximo de " + limite + " livro(s) simultâneos).";
        }

        livro.emprestar();
        Emprestimo novo = new Emprestimo(pessoa, livro);

        if (modo == Modo.BANCO) {
            emprestimoDAO.salvar(novo);
            livroDAO.atualizarDisponibilidade(isbn, false);
        } else {
            emprestimos.add(novo);
        }

        return "Empréstimo realizado com sucesso!\n" + novo;
    }

    public String registrarDevolucao(String isbn, String cpf) {
        if (modo == Modo.BANCO) {
            boolean ok = emprestimoDAO.registrarDevolucao(isbn, cpf);
            if (!ok) return "Nenhum empréstimo ativo foi encontrado para esse usuário e livro.";

            livroDAO.atualizarDisponibilidade(isbn, true);

            Pessoa pessoa = buscarUsuarioPorCpf(cpf);
            String nome = pessoa != null ? pessoa.getNome() : "usuário";
            return "Devolução registrada com sucesso! Obrigado, " + nome + ".";
        }

        for (Emprestimo e : emprestimos) {
            if (e.isAtivo()
                    && e.getLivro().getIsbn().equalsIgnoreCase(isbn)
                    && e.getPessoa().getCpf().equalsIgnoreCase(cpf)) {
                e.finalizar();
                e.getLivro().devolver();
                return "Devolução registrada com sucesso! Obrigado, " + e.getPessoa().getNome() + ".";
            }
        }
        return "Nenhum empréstimo ativo foi encontrado para esse usuário e livro.";
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return modo == Modo.BANCO ? emprestimoDAO.listarAtivos() : emprestimos.stream()
                .filter(Emprestimo::isAtivo).collect(java.util.stream.Collectors.toList());
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return modo == Modo.BANCO ? emprestimoDAO.listarTodos() : emprestimos;
    }

    // ================================================================
    //  Persistência em arquivo (só modo ARQUIVO)
    // ================================================================

    public void carregarDados(DadosBiblioteca dados) {
        this.usuarios    = dados.getUsuarios();
        this.livros      = dados.getLivros();
        this.emprestimos = dados.getEmprestimos();
        for (Emprestimo e : this.emprestimos) {
            Emprestimo.ajustarProximoId(e.getId());
        }
    }

    public Modo getModo() { return modo; }
}
