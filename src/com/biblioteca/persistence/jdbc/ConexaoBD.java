package com.biblioteca.persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão com o banco de dados PostgreSQL.
 *
 * Altere as constantes URL, USUARIO e SENHA para os valores
 * do seu ambiente antes de compilar.
 */
public class ConexaoBD {

    // -------------------------------------------------------
    // ⚙️  CONFIGURE AQUI OS DADOS DO SEU BANCO
    // -------------------------------------------------------
    private static final String URL      = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USUARIO  = "postgres";   // seu usuário do PostgreSQL
    private static final String SENHA    = "suasenha";  // sua senha do PostgreSQL
    // -------------------------------------------------------

    private ConexaoBD() {
        // impede instanciação — classe utilitária
    }

    /**
     * Abre e retorna uma conexão com o banco.
     * O chamador é responsável por fechar a conexão (use try-with-resources).
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
     * Testa a conexão e imprime o resultado no console.
     * Útil para verificar se tudo está configurado corretamente.
     */
    public static void testarConexao() {
        System.out.println("Testando conexão com o PostgreSQL...");
        try (Connection conn = conectar()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅  Conexão bem-sucedida! Banco: " + conn.getCatalog());
            }
        } catch (SQLException e) {
            System.out.println("❌  Erro ao conectar: " + e.getMessage());
            System.out.println("Verifique URL, usuário e senha em ConexaoBD.java");
        }
    }
}
