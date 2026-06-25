package com.biblioteca.persistence.jdbc;

import com.biblioteca.model.Aluno;
import com.biblioteca.model.Pessoa;
import com.biblioteca.model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável por salvar e carregar
 * usuários (Aluno / Professor) na tabela "usuarios" do PostgreSQL.
 */
public class UsuarioDAO {

    /**
     * Insere um novo usuário no banco.
     * Retorna true se a inserção foi bem-sucedida.
     */
    public boolean salvar(Pessoa pessoa) {
        String sql = "INSERT INTO usuarios (tipo, nome, cpf, email, telefone) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getTipo().toUpperCase()); // "ALUNO" ou "PROFESSOR"
            stmt.setString(2, pessoa.getNome());
            stmt.setString(3, pessoa.getCpf());
            stmt.setString(4, pessoa.getEmail());
            stmt.setString(5, pessoa.getTelefone());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um usuário pelo CPF.
     * Retorna null se não encontrar.
     */
    public Pessoa buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPessoa(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retorna todos os usuários cadastrados.
     */
    public List<Pessoa> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        List<Pessoa> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPessoa(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Converte uma linha do ResultSet em um objeto Pessoa
     * (Aluno ou Professor, dependendo da coluna "tipo").
     */
    private Pessoa mapearPessoa(ResultSet rs) throws SQLException {
        String tipo     = rs.getString("tipo");
        String nome     = rs.getString("nome");
        String cpf      = rs.getString("cpf");
        String email    = rs.getString("email");
        String telefone = rs.getString("telefone");

        if ("PROFESSOR".equalsIgnoreCase(tipo)) {
            return new Professor(nome, cpf, email, telefone);
        } else {
            return new Aluno(nome, cpf, email, telefone);
        }
    }
}
