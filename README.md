# Desafio DX - Sistema de Gestão de Times

## Descrição do Projeto:

Sistema para gerenciar escalações de times (esportes tradicionais ou eSports). Permite cadastro de integrantes, criação de times e consultas sobre os dados. Desenvolvido em Java com Spring Boot, banco de dados H2 e Thymeleaf.

## Funcionalidades Implementadas

### Gerenciamento de Integrantes:
Cadastro de Integrantes:
Cadastro (nome, franquia e função).
Exclusão permanente.
Busca pelo integrante mais usado ou do time mais comum em um período.


### Gerenciamento de Times

Cadastro de Times:
Cadastro de times associando uma data e os integrantes selecionados.
Exclusão de Times:
Exclusão permanente de um time diretamente da lista de times.
Busca de Time por Data:
Busca detalhada do time escalado em uma data específica.

### Interface
Interface amigável para interação com o sistema, utilizando Thymeleaf.
Design funcional com componentes HTML intuitivos.

### Java: Linguagem principal.
Spring Boot: Framework para desenvolvimento da aplicação.
Thymeleaf: Motor de template para renderização de páginas HTML.
H2 Database: Banco de dados em memória para testes.
Maven: Gerenciador de dependências.
HTML/CSS: Interface do usuário.

### Configurações do Banco de Dados
O projeto utiliza o banco de dados em memória H2 para testes. O console do H2 está disponível em http://localhost:8080/h2-console com as seguintes credenciais:
URL JDBC: jdbc:h2:mem:testdb
Usuário: sa
Senha: (vazia)


### Configurações para Execução
JDK 17+
Maven 3.8+

Clonar repositorio git clone https://github.com/matheus464/desafioDX.git
cd desafioDX

em gitbash: mvn clean install , mvn spring-boot:run ( ou rodar arquivo DuxusdesafioApplication)
port tradicional: localhost:8080/rotas do endpoint FrontendController

#### utilizado IntelliJ IDEA 2024.2.4

#### Obs: Até o momento fiz algumas funcionalidades para testes e meu html bem simples, desse modo informo que nao finalizei todo o projeto. Fico à disposição!
