package com.biblioteca.persistence.jdbc;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Pessoa;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelos empréstimos na tabela "emprestimos".
 * A tabela relaciona usuários e livros pelas colunas usuario_id e livro_id.
 */
public class EmprestimoDAO {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LivroDAO   livroDAO   = new LivroDAO();

    /**
     * Insere um novo empréstimo no banco.
     * Busca os IDs de usuário e livro pelas colunas cpf/isbn.
     */
    public boolean salvar(Emprestimo emprestimo) {
        String sql = """
            INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, ativo)
            VALUES (
                (SELECT id FROM usuarios WHERE cpf  = ?),
                (SELECT id FROM livros   WHERE isbn = ?),
                ?, TRUE
            )
            """;

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emprestimo.getPessoa().getCpf());
            stmt.setString(2, emprestimo.getLivro().getIsbn());
            stmt.setDate  (3, Date.valueOf(emprestimo.getDataEmprestimo()));

            int linhas = stmt.executeUpdate();

            // captura o ID gerado pelo banco e atualiza o contador do objeto
            if (linhas > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        Emprestimo.ajustarProximoId(keys.getInt(1));
                    }
                }
            }

            return linhas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar empréstimo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra a devolução: atualiza data_devolucao e ativo = false.
     */
    public boolean registrarDevolucao(String isbn, String cpf) {
        String sql = """
            UPDATE emprestimos
            SET data_devolucao = CURRENT_DATE, ativo = FALSE
            WHERE ativo = TRUE
              AND livro_id   = (SELECT id FROM livros   WHERE isbn = ?)
              AND usuario_id = (SELECT id FROM usuarios WHERE cpf  = ?)
            """;

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            stmt.setString(2, cpf);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao registrar devolução: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retorna somente os empréstimos ativos.
     */
    public List<Emprestimo> listarAtivos() {
        return listar("WHERE e.ativo = TRUE");
    }

    /**
     * Retorna todos os empréstimos (histórico completo).
     */
    public List<Emprestimo> listarTodos() {
        return listar("");
    }

    // -------------------------------------------------------
    //  Auxiliares
    // -------------------------------------------------------

    private List<Emprestimo> listar(String filtro) {
        String sql = """
            SELECT e.id,
                   e.data_emprestimo,
                   e.data_devolucao,
                   e.ativo,
                   u.cpf  AS usuario_cpf,
                   l.isbn AS livro_isbn
            FROM emprestimos e
            JOIN usuarios u ON u.id = e.usuario_id
            JOIN livros   l ON l.id = e.livro_id
            """ + filtro + " ORDER BY e.id";

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Reutiliza os outros DAOs para hidratar os objetos
                Pessoa pessoa = usuarioDAO.buscarPorCpf(rs.getString("usuario_cpf"));
                Livro  livro  = livroDAO.buscarPorIsbn(rs.getString("livro_isbn"));

                if (pessoa == null || livro == null) continue;

                Emprestimo e = new Emprestimo(pessoa, livro);
                e.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());

                Date devData = rs.getDate("data_devolucao");
                if (devData != null) {
                    e.setDataDevolucao(devData.toLocalDate());
                }

                e.setAtivo(rs.getBoolean("ativo"));
                Emprestimo.ajustarProximoId(rs.getInt("id"));
                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
        }

        return lista;
    }
}
