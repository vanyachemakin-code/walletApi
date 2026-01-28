-- liquibase formatted sql

-- changeset author:vanyachemakin
-- comment: Создание таблицы Wallet для хранения баланса

CREATE TABLE Wallet (
    id UUID PRIMARY KEY,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00
);