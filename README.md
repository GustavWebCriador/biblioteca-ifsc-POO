# Biblioteca Inteligente

Sistema de gerenciamento de biblioteca em Java, desenvolvido com
Programação Orientada a Objetos, seguindo o levantamento de requisitos
do projeto.

## Como compilar e executar

Pelo terminal, a partir da pasta `biblioteca-inteligente`:

```bash
# compilar
javac -cp "lib\postgresql-42.7.11.jar" -d bin $(Get-ChildItem -Recurse src -Filter *.java | % {$_.FullName})

# executar
java -cp "bin;lib\postgresql-42.7.11.jar" com.biblioteca.Main
```

No Windows (cmd), troque `$(find src -name "*.java")` por listar os
arquivos manualmente ou use uma IDE (IntelliJ, Eclipse, NetBeans, VS
Code com extensão Java) — basta abrir a pasta `src` como projeto e
rodar `Main.java`.

Ao usar a opção **9 - Salvar dados** ou **0 - Sair**, o sistema grava
tudo em um arquivo `biblioteca.dat` na pasta onde o programa foi
executado. Da próxima vez que rodar, os dados são carregados
automaticamente.

## Estrutura do projeto

```
src/com/biblioteca/
├── Main.java                       Menu interativo (ponto de entrada)
├── model/
│   ├── Pessoa.java                 Classe abstrata
│   ├── Aluno.java                  Herda de Pessoa
│   ├── Professor.java              Herda de Pessoa
│   ├── Emprestavel.java            Interface
│   ├── Livro.java                  Implementa Emprestavel
│   └── Emprestimo.java             Associa Pessoa e Livro
├── service/
│   └── Biblioteca.java             Regras de negócio (cadastro, empréstimo, devolução)
├── persistence/
│   ├── Persistencia.java           Salvar/carregar em arquivo
│   └── DadosBiblioteca.java        Estrutura agrupando o estado salvo
└── util/
    └── Validador.java              Validações de entrada (CPF, e-mail, ano)

bonus-banco-de-dados/                Material de referência para trocar
                                      o arquivo por MySQL/PostgreSQL
```

## Como cada requisito foi atendido

| Exigência                   | Onde está no código                                             |
| ---------------------------- | ----------------------------------------------------------------|
| Entrada de dados interativa  | `Main.java` — menu numerado em loop, com `Scanner`               |
| Encapsulamento                | Todos os atributos de `Pessoa`, `Livro`, `Emprestimo` são `private`, com getters/setters |
| Classes                       | `Pessoa`, `Aluno`, `Professor`, `Livro`, `Emprestimo`, `Biblioteca` |
| Herança                       | `Aluno extends Pessoa`, `Professor extends Pessoa`               |
| Associação                    | `Emprestimo` guarda uma referência a `Pessoa` e a `Livro`         |
| Classe abstrata               | `Pessoa` (método `calcularLimiteEmprestimo()` é abstrato)        |
| Método abstrato               | `Pessoa.calcularLimiteEmprestimo()` — implementado de forma diferente em `Aluno` (3) e `Professor` (10) |
| Polimorfismo                  | `Biblioteca.realizarEmprestimo()` chama `pessoa.calcularLimiteEmprestimo()` sem saber se é Aluno ou Professor |
| Interface                     | `Emprestavel` (`emprestar()`, `devolver()`)                      |
| Implementação de interface    | `Livro implements Emprestavel`                                   |
| Validação de entradas (RNF04) | `Validador.java` + métodos `lerCpf`, `lerEmail`, `lerAno` em `Main.java` |
| Mensagens amigáveis (RNF03)   | Todas as saídas de `Main.java` e `Biblioteca.java`                |
| Persistência (RF08, extra)    | `Persistencia.java` (arquivo, funciona pronto) + `bonus-banco-de-dados/` (caminho para MySQL/PostgreSQL) |

## Regras de negócio implementadas

- Um **Aluno** pode ter até **3** empréstimos ativos ao mesmo tempo.
- Um **Professor** pode ter até **10** empréstimos ativos ao mesmo tempo.
- Um livro só pode ser emprestado se estiver **disponível**.
- Não é possível cadastrar dois usuários com o mesmo CPF, nem dois
  livros com o mesmo ISBN.
- A devolução marca o livro como disponível novamente e fecha o
  empréstimo (registra a data de devolução).


