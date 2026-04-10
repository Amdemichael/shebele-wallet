# Shebele Wallet - Mobile Money Platform

[![Java](https://img.shields.io/badge/Java-25-blue.svg)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-brightgreen.svg)](https://spring.io)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://postgresql.org)

A **production-ready mobile money backend** inspired by M-Pesa and Telebirr. Implements double-entry accounting, idempotent transfers, and pessimistic locking for financial transaction integrity.

## 🚀 Features

- ✅ **Account Management** - Register, view details, check balances
- ✅ **Money Transfers** - P2P transfers with idempotency support
- ✅ **Idempotent APIs** - Prevent duplicate processing with `Idempotency-Key` headers
- ✅ **Pessimistic Locking** - Race condition prevention for balance updates
- ✅ **Transaction History** - Complete audit trail of all transfers
- ✅ **PostgreSQL** - Production-grade persistent storage
- ✅ **REST API** - Clean, documented endpoints

## 📋 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/accounts/register` | Create new account |
| GET | `/api/v1/accounts/{msisdn}` | Get account details |
| GET | `/api/v1/accounts/{msisdn}/balance` | Check balance |
| POST | `/api/v1/accounts/{msisdn}/deposit` | Deposit money |
| POST | `/api/v1/transfers` | Send money (idempotent) |
| GET | `/api/v1/transfers/history/{msisdn}` | Transaction history |

## 🛠️ Tech Stack

- **Java 25** - Modern Java features
- **Spring Boot 3.4** - Rapid application development
- **PostgreSQL 15** - Reliable data persistence
- **Hibernate/JPA** - Object-relational mapping
- **Maven** - Dependency management
- **Lombok** - Boilerplate reduction

## 🏃 Quick Start

### Prerequisites

- Java 25+
- PostgreSQL 15+
- Maven (or use included wrapper)

### Setup Database

```sql
CREATE DATABASE shebele_wallet;