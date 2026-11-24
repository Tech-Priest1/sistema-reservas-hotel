# Sistema de Reservas de Hotel (SRH) — Aplicação Demonstrativa

Repositório da aplicação demonstrativa e dos testes unitários referentes ao plano **TUT-RES-HOTEL-01** (Fase 02).

## Tecnologias
- **Linguagem:** Java 25 (LTS)
- **Gerenciador de Build:** Apache Maven
- **Framework de Testes:** JUnit 5 (Jupiter)

## Estrutura do Projeto
- `src/main/java/br/edu/hotel/model`: Entidades de domínio (`Quarto`, `Reserva`, `CategoriaQuarto`, `StatusQuarto`).
- `src/main/java/br/edu/hotel/service`: Regras de negócio e funções testáveis (`ReservaService`).
- `src/test/java/br/edu/hotel/service`: Suíte de testes unitários automatizados.

## Como Executar os Testes
```bash
mvn test
```
