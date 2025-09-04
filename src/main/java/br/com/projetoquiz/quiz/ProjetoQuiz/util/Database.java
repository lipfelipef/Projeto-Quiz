package br.com.projetoquiz.quiz.ProjetoQuiz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DATABASE_URL = "jdbc:sqlite:quiz.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados SQLite:");
            e.printStackTrace();
        }
        return conn;
    }
}