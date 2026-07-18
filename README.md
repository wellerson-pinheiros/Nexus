# 🎮 Game Store API

[![Java CI with Maven](https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO/actions/workflows/ci.yml/badge.svg)](https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO/actions/workflows/ci.yml)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?logo=docker)](https://www.docker.com/)

## 📖 Sobre o Projeto

A **Game Store API** é o backend de uma plataforma de e-commerce de jogos digitais. O projeto está sendo desenvolvido inicialmente utilizando a abordagem **Monolith First** (Monolítico Primeiro), garantindo baixo acoplamento interno e fronteiras de domínio bem definidas. O objetivo futuro é evoluir esta arquitetura para **Microsserviços**.

O sistema consome dados de uma API externa de jogos para alimentar o catálogo e gerencia processos internos de usuários, autenticação e pedidos.

## 🚀 Tecnologias e Arquitetura

Este projeto foi construído utilizando as melhores práticas e ferramentas do ecossistema Java:

*   **Core:** Java 25 & Spring Boot 3
*   **Banco de Dados:** PostgreSQL
*   **Migrations:** Flyway (Controle de versão do banco de dados)
*   **Segurança:** Spring Security com JWT (Autenticação e Autorização)
*   **Integração:** Spring RestClient (Consumo da API externa de jogos)
*   **Infraestrutura Local:** Docker & Docker Compose
*   **CI/CD & Versionamento:** GitHub Actions (Integração Contínua) seguindo a cultura de *Trunk-Based Development*.

## ⚙️ Estrutura e Padrões Adotados

*   **Trunk-Based Development:** O código é integrado frequentemente na branch `main`, com validação automatizada de build e testes via GitHub Actions.
*   **Testes Automatizados:** Suíte de testes configurada com JUnit e Mockito para garantir a estabilidade das regras de negócio.
*   **Design Patterns:** Utilização de padrões como DTO (Data Transfer Object), Repository, Strategy e Builder para manter o código limpo, escalável e de fácil manutenção.

## 🛠️ Como executar o projeto localmente

### Pré-requisitos
*   [Java 25](https://adoptium.net/) instalado.
*   [Maven](https://maven.apache.org/) instalado.
*   [Docker](https://www.docker.com/) e Docker Compose instalados.

### Passos para rodar

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO.git](https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO.git)
   cd NOME_DO_REPOSITORIO