package com.biblioteca.persistence;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Pessoa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Responsável por salvar e carregar o estado da biblioteca em disco.
 *
 * Por padrão, os dados são gravados em um arquivo binário local
 * (biblioteca.dat), o que já atende ao RF08 sem depender de nenhum
 * serviço externo. Para usar um banco de dados relacional de verdade
 * (MySQL/PostgreSQL) e obter o ponto extra completo, veja os arquivos
 * de referência na pasta "bonus-banco-de-dados" do projeto.
 */
public class Persistencia {

    private static final String ARQUIVO = "biblioteca.dat";

    public static void salvar(List<Pessoa> usuarios, List<Livro> livros, List<Emprestimo> emprestimos) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            DadosBiblioteca dados = new DadosBiblioteca(usuarios, livros, emprestimos);
            out.writeObject(dados);
            System.out.println("Dados salvos com sucesso em \"" + ARQUIVO + "\".");
        } catch (IOException e) {
            System.out.println("Não foi possível salvar os dados: " + e.getMessage());
        }
    }

    public static DadosBiblioteca carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (DadosBiblioteca) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Não foi possível carregar os dados salvos: " + e.getMessage());
            return null;
        }
    }
}
