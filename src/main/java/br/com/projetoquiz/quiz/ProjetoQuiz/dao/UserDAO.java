package br.com.projetoquiz.quiz.ProjetoQuiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// 1. IMPORTAR A BIBLIOTECA BCRYPT
import org.mindrot.jbcrypt.BCrypt;

import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import br.com.projetoquiz.quiz.ProjetoQuiz.util.Database;

public class UserDAO {

    public UserDAO() {
        createTable();
    }

    public void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL," // Este campo agora armazenará o HASH
                + " email TEXT NOT NULL UNIQUE,"
                + " pin TEXT"
                + ");";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO users(username, password, email, pin) VALUES(?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public User findUserByUsernameAndEmail(String username, String email) {
        String sql = "SELECT * FROM users WHERE username = ? AND email = ?";
        User user = null;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("pin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("pin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            pstmt.setString(1, hashedPassword); // Salva o novo hash
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userOrEmailExists(String username, String email) {
        String sql = "SELECT id FROM users WHERE username = ? OR email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next(); 
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    public boolean checkPassword(String username, String plainPassword) {
        User user = findUserByUsername(username);
        if (user != null) {

            String storedHash = user.getPassword();

            return BCrypt.checkpw(plainPassword, storedHash);
        }

        return false;
    }
}