package com.biblioteca.persistence.jdbc;

import com.biblioteca.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável por salvar e carregar livros na tabela "livros".
 */
public class LivroDAO {

    /**
     * Insere um novo livro no banco.
     */
    public boolean salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, isbn, ano_publicacao, disponivel) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt   (4, livro.getAnoPublicacao());
            stmt.setBoolean(5, livro.isDisponivel());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza apenas a disponibilidade de um livro
     * (chamado ao emprestar ou devolver).
     */
    public boolean atualizarDisponibilidade(String isbn, boolean disponivel) {
        String sql = "UPDATE livros SET disponivel = ? WHERE isbn = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, disponivel);
            stmt.setString (2, isbn);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar disponibilidade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um livro pelo ISBN.
     */
    public Livro buscarPorIsbn(String isbn) {
        String sql = "SELECT * FROM livros WHERE isbn = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearLivro(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retorna todos os livros cadastrados.
     */
    public List<Livro> listarTodos() {
        String sql = "SELECT * FROM livros ORDER BY titulo";
        List<Livro> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar livros: " + e.getMessage());
        }

        return lista;
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("isbn"),
            rs.getInt   ("ano_publicacao")
        );
        livro.setDisponivel(rs.getBoolean("disponivel"));
        return livro;
    }
}
