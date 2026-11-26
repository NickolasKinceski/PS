# BiblioTech

Sistema de emprestimo de livros para a biblioteca do campus.

## 1. O projeto

(escreva aqui, com suaspalavras: quem é o cliente, qual problema o sistema resolve e para quem)

    1. Quem é o cliente?
    R: Alunos, professores e funcionários do Campus.

    2. Qual problema o sistema resolve?
    R: O problema da falta de controle da saída e entrada dos livros, além da disponibilidade.

    3. Para quem?
    R: Para os alunos, professores e funcionários do Campus.

## 2. Historias de usuario

| # | Historia de usuario |
|---|---|
| HU01 | Como leitor, quero consultar a disponibilidade de um livro, para saber se posso pega-lo emprestado sem ir ate o balcao. |
| HU02 | Como leitor, quero devolver um livro, para nao ficar com pendencia na biblioteca. |
| HU03 | Como bibliotecaria, quero registrar um emprestimo, para saber quem esta com cada exemplar. |
| HU04 | Como bibliotecaria, quero cadastrar um livro novo, para que ele possa ser encontrado no sistema. |
| HU05 | Como bibliotecaria, quero ver os emprestimos atrasados, para cobrar a devolucao. |

## 3. Requesitos

### Requesitos funcionais

| # | Requisito funcional | Veio da |
|---|---|---|
| RF01 | O sistema deve permitir que a bibliotecaria cadastre um livro no acervo. | HU04 |
| RF02 | O sistema deve permitir que a bibliotecaria cadastre um leitor. | regra de acesso: so quem tem cadastro leva livro |
| RF03 | O sistema deve permitir que o leitor consulte a disponibilidade de um livro. | HU01 |
| RF04 | O sistema deve permitir que a bibliotecaria registre a devolucao de um livro. | HU02 |
| RF05 | O sistema deve permitir que a bibliotecaria registre o emprestimo de um livro. | HU03 |
| RF06 | (o seu: escreva aqui) | (de onde veio) |
​
### Requisitos nao funcionais
​
| # | Requisito funcional | Veio da |
|---|---|---|
| RF01 | O sistema deve permitir que a bibliotecaria cadastre um livro no acervo. | HU04 |
| RF02 | O sistema deve permitir que a bibliotecaria cadastre um leitor. | regra de acesso: so quem tem cadastro leva livro |
| RF03 | O sistema deve permitir que o leitor consulte a disponibilidade de um livro. | HU01 |
| RF04 | O sistema deve permitir que a bibliotecaria registre a devolucao de um livro. | HU02 |
| RF05 | O sistema deve permitir que a bibliotecaria registre o emprestimo de um livro. | HU03 |
| RF06 | (o seu: escreva aqui) | (de onde veio) |
​
### Requisitos nao funcionais
​
| # | Requisito funcional | Veio da |
|---|---|---|
| RF01 | O sistema deve permitir que a bibliotecaria cadastre um livro no acervo. | HU04 |
| RF02 | O sistema deve permitir que a bibliotecaria cadastre um leitor. | regra de acesso: so quem tem cadastro leva livro |
| RF03 | O sistema deve permitir que o leitor consulte a disponibilidade de um livro. | HU01 |
| RF04 | O sistema deve permitir que a bibliotecaria registre a devolucao de um livro. | HU02 |
| RF05 | O sistema deve permitir que a bibliotecaria registre o emprestimo de um livro. | HU03 |
| RF06 | (o seu: escreva aqui) | (de onde veio) |

### Requisitos nao funcionais

| # | Requisito nao funcional |
|---|---|
| RNF01 | A consulta de disponibilidade deve responder em menos de 3 segundos. |
| RNF02 | Somente usuarios identificados como bibliotecarios podem alterar o acervo. |

## 4. Diagramas (feitos em APS)
​
### Casos de uso
​
![Diagrama de casos de uso do BiblioTech](docs/casos-de-uso.svg)
​
### Classes
​
![Diagrama de classes do BiblioTech](docs/classes.svg)

## 5. Como recriar o banco de dados

Para recriar a estrutura do banco bibliotech em um ambiente limpo:

1. Inicie o servico do MariaDB: sudo service mariadb start
2. Execute o script de estrutura como administrador: sudo mariadb < banco.sql
3. Crie o usuario da aplicacao com menor privilegio (SELECT, INSERT, UPDATE, DELETE):
   CREATE USER IF NOT EXISTS 'bibliotech'@'localhost' IDENTIFIED BY 'sua_senha';
   GRANT SELECT, INSERT, UPDATE, DELETE ON bibliotech.* TO 'bibliotech'@'localhost';
4. Configure DB_USER e DB_PASSWORD no terminal antes de compilar e rodar o sistema.

## 6. Manutencao e correcoes (Aula 47)

| # | Sintoma | Causa | Correcao | Prova |
|---|---|---|---|---|
| P1 | Livro carregado do banco aparecia como emprestado. | Construtor com id nao inicializava disponivel (valor padrao false). | Definicao de this.disponivel = true; no construtor com id em Livro.java. | TesteCorrecoes: P1 passou. |
| P2 | ALTER TABLE recusado com erro 1142 para o usuario bibliotech. | Usuario da aplicacao possui apenas SELECT, INSERT, UPDATE e DELETE. | Criacao do banco.sql com IF NOT EXISTS executado como administrador. | DESCRIBE livros; e execucao dupla de banco.sql sem erro. |
| P3 | Cadastro de livro nao compilava (required: String,String,int; found: Livro). | Chamada passava objeto Livro em vez de tres parametros. | Criacao de TelaCadastroLivro.java e botao Novo livro na TelaLivros.java. | Validacoes na tela e TesteCorrecoes P3 passou. |
| P4 | Buscas por ID e titulo diziam "nao encontrado" com MariaDB parado. | LivroDAO retornava null/vazio no catch de SQLException. | Lancamento de PersistenciaException no LivroDAO e catch na TelaLivros. | Caixa de erro de conexao com tabela preservada. |
| P5 | Operacoes de escrita precisavam separar erro de banco de registro inexistente. | SQLException precisava propagar PersistenciaException separado de false. | Propagacao de PersistenciaException nos metodos de escrita e TelaCadastroLivro. | TesteCorrecoes: P5 passou (6/6). |
