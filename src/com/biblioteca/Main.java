package com.biblioteca;

import com.biblioteca.model.Aluno;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Pessoa;
import com.biblioteca.model.Professor;
import com.biblioteca.persistence.DadosBiblioteca;
import com.biblioteca.persistence.Persistencia;
import com.biblioteca.service.Biblioteca;
import com.biblioteca.util.Validador;

import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada do sistema: menu interativo via console (RNF01).
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Biblioteca biblioteca;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("    BIBLIOTECA INTELIGENTE - SISTEMA POO   ");
        System.out.println("==========================================");

        // Escolha do modo de persistência ao iniciar
        System.out.println();
        System.out.println("Selecione o modo de persistência:");
        System.out.println("  1 - Arquivo local (biblioteca.dat)");
        System.out.println("  2 - Banco de dados PostgreSQL");
        int modoPersistencia = lerOpcaoEntre("Modo: ", 1, 2);

        if (modoPersistencia == 2) {
            biblioteca = new Biblioteca(Biblioteca.Modo.BANCO);
            com.biblioteca.persistence.jdbc.ConexaoBD.testarConexao();
        } else {
            biblioteca = new Biblioteca(Biblioteca.Modo.ARQUIVO);
        }

        carregarDadosIniciais();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            processarOpcao(opcao);
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("----------- MENU PRINCIPAL -----------");
        System.out.println("1 - Cadastrar usuário");
        System.out.println("2 - Listar usuários");
        System.out.println("3 - Cadastrar livro");
        System.out.println("4 - Listar livros");
        System.out.println("5 - Realizar empréstimo");
        System.out.println("6 - Registrar devolução");
        System.out.println("7 - Listar empréstimos ativos");
        System.out.println("8 - Listar histórico de empréstimos");
        System.out.println("9 - Salvar dados em arquivo");
        System.out.println("0 - Sair");
        System.out.println("---------------------------------------");
    }

    private static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                cadastrarUsuario();
                break;
            case 2:
                listarUsuarios();
                break;
            case 3:
                cadastrarLivro();
                break;
            case 4:
                listarLivros();
                break;
            case 5:
                realizarEmprestimo();
                break;
            case 6:
                registrarDevolucao();
                break;
            case 7:
                listarEmprestimosAtivos();
                break;
            case 8:
                listarHistorico();
                break;
            case 9:
                salvarDados();
                break;
            case 0:
                salvarDados();
                System.out.println("Dados salvos. Até logo!");
                break;
            default:
                System.out.println("Opção inválida! Escolha um número do menu.");
        }
    }

    // ---------------- Usuários ----------------

    private static void cadastrarUsuario() {
        System.out.println();
        System.out.println("--- Cadastro de Usuário ---");

        String nome = lerTexto("Nome: ");
        String cpf = lerCpf("CPF (somente números): ");

        if (biblioteca.buscarUsuarioPorCpf(cpf) != null) {
            System.out.println("Já existe um usuário cadastrado com esse CPF.");
            return;
        }

        String email = lerEmail("E-mail: ");
        String telefone = lerTexto("Telefone: ");
        int tipo = lerOpcaoEntre("Tipo de usuário (1-Aluno / 2-Professor): ", 1, 2);

        Pessoa pessoa;
        if (tipo == 1) {
            pessoa = new Aluno(nome, cpf, email, telefone);
        } else {
            pessoa = new Professor(nome, cpf, email, telefone);
        }

        biblioteca.cadastrarUsuario(pessoa);
        System.out.println("Usuário \"" + nome + "\" cadastrado com sucesso!");
    }

    private static void listarUsuarios() {
        System.out.println();
        System.out.println("--- Usuários Cadastrados ---");
        List<Pessoa> usuarios = biblioteca.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado ainda.");
            return;
        }

        for (Pessoa p : usuarios) {
            System.out.println("---------------------------");
            p.exibirDados();
        }
    }

    // ---------------- Livros ----------------

    private static void cadastrarLivro() {
        System.out.println();
        System.out.println("--- Cadastro de Livro ---");

        String titulo = lerTexto("Título: ");
        String autor = lerTexto("Autor: ");
        String isbn = lerTexto("ISBN: ");

        if (biblioteca.buscarLivroPorIsbn(isbn) != null) {
            System.out.println("Já existe um livro cadastrado com esse ISBN.");
            return;
        }

        int ano = lerAno("Ano de publicação: ");

        Livro livro = new Livro(titulo, autor, isbn, ano);
        biblioteca.cadastrarLivro(livro);
        System.out.println("Livro \"" + titulo + "\" cadastrado com sucesso!");
    }

    private static void listarLivros() {
        System.out.println();
        System.out.println("--- Livros Cadastrados ---");
        List<Livro> livros = biblioteca.listarLivros();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado ainda.");
            return;
        }

        for (Livro l : livros) {
            System.out.println(l);
        }
    }

    // ---------------- Empréstimos ----------------

    private static void realizarEmprestimo() {
        System.out.println();
        System.out.println("--- Realizar Empréstimo ---");

        if (biblioteca.listarUsuarios().isEmpty() || biblioteca.listarLivros().isEmpty()) {
            System.out.println("É necessário ter ao menos um usuário e um livro cadastrados.");
            return;
        }

        String cpf = lerCpf("CPF do usuário: ");
        String isbn = lerTexto("ISBN do livro: ");

        System.out.println(biblioteca.realizarEmprestimo(cpf, isbn));
    }

    private static void registrarDevolucao() {
        System.out.println();
        System.out.println("--- Registrar Devolução ---");

        String isbn = lerTexto("ISBN do livro: ");
        String cpf = lerCpf("CPF do usuário: ");

        System.out.println(biblioteca.registrarDevolucao(isbn, cpf));
    }

    private static void listarEmprestimosAtivos() {
        System.out.println();
        System.out.println("--- Empréstimos Ativos ---");
        List<Emprestimo> ativos = biblioteca.listarEmprestimosAtivos();

        if (ativos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
            return;
        }

        for (Emprestimo e : ativos) {
            System.out.println(e);
        }
    }

    private static void listarHistorico() {
        System.out.println();
        System.out.println("--- Histórico de Empréstimos ---");
        List<Emprestimo> todos = biblioteca.listarTodosEmprestimos();

        if (todos.isEmpty()) {
            System.out.println("Nenhum empréstimo foi registrado ainda.");
            return;
        }

        for (Emprestimo e : todos) {
            System.out.println(e);
        }
    }

    // ---------------- Persistência ----------------

    private static void salvarDados() {
        if (biblioteca.getModo() == Biblioteca.Modo.BANCO) {
            System.out.println("Modo banco: dados já são persistidos automaticamente a cada operação.");
        } else {
            Persistencia.salvar(
                    biblioteca.listarUsuarios(),
                    biblioteca.listarLivros(),
                    biblioteca.listarTodosEmprestimos()
            );
        }
    }

    private static void carregarDadosIniciais() {
        if (biblioteca.getModo() == Biblioteca.Modo.BANCO) {
            System.out.println("Modo banco: dados carregados diretamente do PostgreSQL.");
            return;
        }
        DadosBiblioteca dados = Persistencia.carregar();
        if (dados != null) {
            biblioteca.carregarDados(dados);
            System.out.println("Dados anteriores carregados a partir de \"biblioteca.dat\".");
        }
    }

    // ---------------- Entrada validada (RNF04) ----------------

    private static String lerTexto(String mensagem) {
        String valor;
        do {
            System.out.print(mensagem);
            valor = scanner.nextLine().trim();
            if (!Validador.textoNaoVazio(valor)) {
                System.out.println("Entrada inválida: o campo não pode ficar vazio.");
            }
        } while (!Validador.textoNaoVazio(valor));
        return valor;
    }

    private static String lerCpf(String mensagem) {
        String cpf;
        do {
            System.out.print(mensagem);
            cpf = scanner.nextLine().trim().replaceAll("[^0-9]", "");
            if (!Validador.cpfValido(cpf)) {
                System.out.println("CPF inválido: digite exatamente 11 números.");
            }
        } while (!Validador.cpfValido(cpf));
        return cpf;
    }

    private static String lerEmail(String mensagem) {
        String email;
        do {
            System.out.print(mensagem);
            email = scanner.nextLine().trim();
            if (!Validador.emailValido(email)) {
                System.out.println("E-mail inválido. Exemplo válido: nome@dominio.com");
            }
        } while (!Validador.emailValido(email));
        return email;
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: digite um número inteiro.");
            }
        }
    }

    private static int lerOpcaoEntre(String mensagem, int min, int max) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("Opção inválida: escolha um número entre " + min + " e " + max + ".");
        }
    }

    private static int lerAno(String mensagem) {
        while (true) {
            int ano = lerInteiro(mensagem);
            if (Validador.anoValido(ano)) {
                return ano;
            }
            System.out.println("Ano inválido: digite um valor entre 1 e o ano atual.");
        }
    }
}
