-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS wear_app_db;

-- Usar o banco de dados
USE wear_app_db;

-- Criar a tabela para armazenar as mensagens
CREATE TABLE IF NOT EXISTS smartwatch_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    message TEXT NOT NULL,
    created_at DATETIME NOT NULL
);