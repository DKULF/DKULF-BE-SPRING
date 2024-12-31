CREATE DATABASE IF NOT EXISTS DKULF_db CHARACTER SET utf8mb4;

USE DKULF_db;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(20) DEFAULT 'ROLE_USER',
    nickname VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO user (username, password, role,nickname,email) 
VALUES ('admin', '$2a$10$jVpYcQwzRgdxm3AWPfn.7uo7hY5B2hLaF3KA9fBMHlGGLOrb47VPW', 'ROLE_ADMIN','admin','admin@dankook.ac.kr'); 

INSERT INTO user (username, password,nickname,email) 
VALUES ('sample', '$2a$10$TmRcskiG9JXkCbKUa.qY1.PklAzIWJPxOMcukBItWg6trNmOHV66q','sample','sample@dankook.ac.kr'); 
