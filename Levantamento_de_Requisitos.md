# Levantamento de Requisitos

## Projeto: Biblioteca Inteligente

### Disciplina

Programação Orientada a Objetos (POO)

### Objetivo do Sistema

O sistema Biblioteca Inteligente tem como objetivo realizar o gerenciamento de livros, usuários e empréstimos de uma biblioteca, permitindo o cadastro, consulta, empréstimo e devolução de livros de forma interativa.

---

# 1. Requisitos Funcionais

## RF01 - Cadastro de Usuários

O sistema deverá permitir o cadastro de usuários contendo as seguintes informações:

* Nome
* CPF
* E-mail
* Telefone

## RF02 - Visualização de Usuários

O sistema deverá permitir a visualização de todos os usuários cadastrados.

## RF03 - Cadastro de Livros

O sistema deverá permitir o cadastro de livros contendo:

* Título
* Autor
* ISBN
* Ano de publicação

## RF04 - Visualização de Livros

O sistema deverá exibir todos os livros cadastrados.

## RF05 - Realização de Empréstimos

O sistema deverá permitir que um usuário realize empréstimos de livros disponíveis.

## RF06 - Registro de Devoluções

O sistema deverá permitir registrar a devolução de livros emprestados.

## RF07 - Consulta de Empréstimos

O sistema deverá exibir todos os empréstimos ativos e seu respectivo status.

## RF08 - Persistência de Dados

O sistema poderá armazenar os dados em banco de dados relacional para obtenção da pontuação extra prevista na atividade.

---

# 2. Requisitos Não Funcionais

## RNF01 - Interface Interativa

O sistema deverá possuir um menu interativo para entrada e visualização dos dados.

## RNF02 - Programação Orientada a Objetos

Todo o sistema deverá ser desenvolvido utilizando os conceitos de Programação Orientada a Objetos.

## RNF03 - Usabilidade

As mensagens exibidas ao usuário deverão ser claras e objetivas.

## RNF04 - Validação de Dados

O sistema deverá validar informações inválidas antes de realizar operações.

---

# 3. Requisitos de Programação Orientada a Objetos

O sistema deverá contemplar os seguintes conceitos obrigatórios:

* Encapsulamento
* Classes
* Herança
* Associação
* Classe Abstrata
* Método Abstrato
* Interface

---

# 4. Modelagem Orientada a Objetos

## 4.1 Classe Abstrata

### Pessoa

Atributos:

* nome
* cpf

Métodos:

* exibirDados()

Método Abstrato:

* calcularLimiteEmprestimo()

---

## 4.2 Herança

### Aluno

Classe derivada de Pessoa.

Implementa:

* calcularLimiteEmprestimo()

### Professor

Classe derivada de Pessoa.

Implementa:

* calcularLimiteEmprestimo()

---

## 4.3 Interface

### Emprestavel

Métodos:

* emprestar()
* devolver()

---

## 4.4 Implementação da Interface

### Livro

Implementa a interface Emprestavel.

Atributos:

* titulo
* autor
* isbn
* disponivel

Métodos:

* emprestar()
* devolver()

---

## 4.5 Associação entre Classes

### Emprestimo

Representa a associação entre:

* Pessoa
* Livro

Atributos:

* dataEmprestimo
* dataDevolucao

---

# 5. Encapsulamento

Todos os atributos das classes deverão ser privados e acessados por meio de métodos Getters e Setters.

Exemplo:

```java
private String nome;

public String getNome() {
    return nome;
}

public void setNome(String nome) {
    this.nome = nome;
}
```

---

# 6. Estrutura das Classes

```text
Pessoa (Abstrata)
│
├── Aluno
│
└── Professor

Emprestavel (Interface)
│
└── Livro

Emprestimo
│
├── Pessoa
└── Livro

Biblioteca
```

---

# 7. Diagrama de Classes

```text
                <<abstract>>
                    Pessoa
            -------------------
            - nome : String
            - cpf  : String
            -------------------
            + exibirDados()
            + calcularLimiteEmprestimo()
                      ^
                      |
       ----------------------------------
       |                                |
     Aluno                        Professor


              <<interface>>
               Emprestavel
            ----------------
            + emprestar()
            + devolver()
                     ^
                     |
                   Livro
            ----------------
            - titulo
            - autor
            - isbn
            - disponivel
            ----------------

                Emprestimo
            ----------------
            - dataEmprestimo
            - dataDevolucao
            ----------------
            Pessoa
            Livro
```

---

# 8. Atendimento aos Critérios da Avaliação

| Critério                                            | Implementação                            |
| --------------------------------------------------- | ---------------------------------------- |
| Entrada e visualização de dados de forma interativa | Menu de cadastro e consulta              |
| Encapsulamento                                      | Atributos privados com Getters e Setters |
| Classes                                             | Pessoa, Livro, Empréstimo e Biblioteca   |
| Herança                                             | Pessoa → Aluno e Professor               |
| Associação                                          | Empréstimo possui Pessoa e Livro         |
| Classe Abstrata                                     | Pessoa                                   |
| Método Abstrato                                     | calcularLimiteEmprestimo()               |
| Interface                                           | Emprestavel                              |
| Banco de Dados (Extra)                              | PostgreSQL                               |

---

# Integrantes

* Gustavo Luiz Medeiros Nunes de Souza


---

# Data

25/06/2026
