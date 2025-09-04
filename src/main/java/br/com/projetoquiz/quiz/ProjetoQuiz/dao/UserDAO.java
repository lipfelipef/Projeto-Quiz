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
        // A coluna 'password' deve ser grande o suficiente para o hash (VARCHAR(60) é seguro)
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

            // 2. GERAR O HASH DA SENHA ANTES DE SALVAR
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword); // Salva o hash, não a senha original
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
                    rs.getString("password"), // Retorna o HASH, o que é correto
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
                    rs.getString("password"), // Retorna o HASH, o que é correto
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
            
            // 3. GERAR O HASH DA NOVA SENHA ANTES DE ATUALIZAR
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

    // ----- NOVO MÉTODO PARA VERIFICAR A SENHA NO LOGIN -----
    /**
     * Verifica se a senha fornecida corresponde ao hash armazenado no banco de dados.
     * @param username O nome de usuário para verificar.
     * @param plainPassword A senha em texto puro digitada pelo usuário.
     * @return true se a senha for correta, false caso contrário.
     */
    public boolean checkPassword(String username, String plainPassword) {
        User user = findUserByUsername(username);
        if (user != null) {
            // Pega o hash que está salvo no banco
            String storedHash = user.getPassword();
            // BCrypt compara a senha digitada com o hash
            return BCrypt.checkpw(plainPassword, storedHash);
        }
        // Se o usuário não existe, a senha não pode ser válida
        return false;
    }
}