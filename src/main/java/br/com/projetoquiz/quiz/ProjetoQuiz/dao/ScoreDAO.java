package br.com.projetoquiz.quiz.ProjetoQuiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.projetoquiz.quiz.ProjetoQuiz.model.Score;
import br.com.projetoquiz.quiz.ProjetoQuiz.util.Database;

public class ScoreDAO {

    public ScoreDAO() {
        createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS scores ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " score INTEGER NOT NULL"
                + ");";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela de scores:");
            e.printStackTrace();
        }
    }

    public void saveOrUpdateRecord(Score newScore) {

        Score oldScore = findScoreByUsername(newScore.getUsername());

        if (oldScore != null) {

            if (newScore.getScore() > oldScore.getScore()) {

                updateScore(newScore);
                System.out.println("Novo recorde salvo para " + newScore.getUsername() + "!");
            } else {

                System.out.println("A pontuação de " + newScore.getScore() + " não é um novo recorde para " + newScore.getUsername() + ". O recorde é " + oldScore.getScore() + ".");
            }
        } else {

            insertScore(newScore);
            System.out.println("Primeira pontuação salva para " + newScore.getUsername() + "!");
        }
    }

    // Método que busca um score pelo nome do usuário
    private Score findScoreByUsername(String username) {
        String sql = "SELECT * FROM scores WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Score(rs.getString("username"), rs.getInt("score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void insertScore(Score score) {
        String sql = "INSERT INTO scores(username, score) VALUES(?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, score.getUsername());
            pstmt.setInt(2, score.getScore());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateScore(Score score) {
        String sql = "UPDATE scores SET score = ? WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, score.getScore());
            pstmt.setString(2, score.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Score> getTopScores() {
        String sql = "SELECT username, score FROM scores ORDER BY score DESC LIMIT 10";
        List<Score> topScores = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                topScores.add(new Score(rs.getString("username"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topScores;
    }
}