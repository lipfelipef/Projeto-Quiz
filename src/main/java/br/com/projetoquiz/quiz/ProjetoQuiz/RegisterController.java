package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
// ===== 1. IMPORTAÇÃO ADICIONADA =====
import java.util.regex.Pattern;
import br.com.projetoquiz.quiz.ProjetoQuiz.dao.UserDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    // ===== 2. REGRA DE VALIDAÇÃO ADICIONADA =====
    private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private PasswordField pinField;

    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String pin = pinField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            App.showAlert(AlertType.WARNING, "Quiz Animado - Versão Alpha 1.3", "Usuário, e-mail e senha são obrigatórios.");
            return;
        }

        // ===== 3. NOVA VALIDAÇÃO DE CARACTERES ESPECIAIS ADICIONADA =====
        if (!VALID_USERNAME_PATTERN.matcher(username).matches()) {
            App.showAlert(AlertType.WARNING, "Quiz Animado - Versão Alpha 1.3", "O nome de usuário pode conter apenas letras, números e underline (_).");
            return;
        }
        // ===================================================================

        // ----- NOVA VALIDAÇÃO DE TAMANHO DO USUÁRIO -----
        if (username.length() < 5) {
            App.showAlert(AlertType.WARNING, "Quiz Animado - Versão Alpha 1.3", "O nome de usuário deve conter no mínimo 5 caracteres.");
            return;
        }

        // ----- NOVA VALIDAÇÃO DE TAMANHO DA SENHA -----
        if (password.length() < 5) {
            App.showAlert(AlertType.WARNING, "Quiz Animado - Versão Alpha 1.3", "A senha deve conter no mínimo 5 caracteres.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            App.showAlert(AlertType.ERROR, "Quiz Animado - Versão Alpha 1.3", "As senhas digitadas não conferem.");
            return;
        }
        if (!email.contains("@")) {
            App.showAlert(AlertType.ERROR, "Quiz Animado - Versão Alpha 1.3", "Por favor, insira um e-mail válido.");
            return;
        }
        if (!pin.isEmpty() && (!pin.matches("\\d{4}"))) {
            App.showAlert(AlertType.ERROR, "Quiz Animado - Versão Alpha 1.3", "O PIN deve conter apenas 4 digitos.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.userOrEmailExists(username, email)) {
            App.showAlert(AlertType.ERROR, "Quiz Animado - Versão Alpha 1.3", "O nome de usuário ou o e-mail informado já estão em uso.");
            return;
        }

        try {
            User newUser = new User(username, password, email, pin);
            userDAO.saveUser(newUser);
            App.showAlert(AlertType.INFORMATION, "Quiz Animado - Versão Alpha 1.3", "Usuário cadastrado com sucesso. Seja bem-vindo!");
            handleBackButtonAction();
        } catch (Exception e) {
            App.showAlert(AlertType.ERROR, "Quiz Animado - Versão Alpha 1.3", "Ocorreu um erro inesperado ao salvar os dados, informe ao administrador responsável.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction() {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            Stage stage = new Stage();
            stage.setTitle("Quiz Animado - Versão Alpha 1.3");
            Scene scene = new Scene(App.loadFXML("primary"), 1280, 720);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}