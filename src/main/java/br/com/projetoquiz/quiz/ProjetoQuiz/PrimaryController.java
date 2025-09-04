package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
import br.com.projetoquiz.quiz.ProjetoQuiz.dao.UserDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class PrimaryController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label feedbackLabel;

    @FXML
    private void handleLoginButtonAction() {
        // ... (lógica de login continua a mesma) ...
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        feedbackLabel.setText("");

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findUserByUsernameAndEmail(username, email);

        if (user != null && userDAO.checkPassword(username, password)) {
            if (user.getPin() != null && !user.getPin().isEmpty()) {
                openSecurityCheckScreen(user);
            } else {
                openQuizScreen(user);
            }
        } else {
            feedbackLabel.setText("Usuário, e-mail ou senha incorretos.");
        }
    }
    
    // Método de transição genérico para reutilizar o código
    private void switchScene(String fxmlFile, User user, String newTitle) {
        try {
            // Pega a janela atual a partir de qualquer componente da tela
            Stage currentStage = (Stage) usernameField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoquiz/quiz/ProjetoQuiz/" + fxmlFile));
            Parent root = loader.load();

            // Se a tela de destino precisar de dados do usuário, nós os passamos
            if (fxmlFile.equals("QuizView.fxml")) {
                QuizController controller = loader.getController();
                controller.initData(user);
            } else if (fxmlFile.equals("SecurityCheckView.fxml")) {
                SecurityCheckController controller = loader.getController();
                controller.initData(user);
            }
            // Adicionar 'else if' para outros controllers se necessário

            // Animação de Fade-in
            root.setOpacity(0);
            
            // Cria a nova cena e a define na JANELA ATUAL
            Scene newScene = new Scene(root, 1280, 720);
            currentStage.setScene(newScene);
            currentStage.setTitle(newTitle);
            // Não precisamos mais de currentStage.show() pois a janela já está visível

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openQuizScreen(User user) {
        switchScene("QuizView.fxml", user, "Quiz de Desenhos Animados");
    }

    private void openSecurityCheckScreen(User user) {
        switchScene("SecurityCheckView.fxml", user, "Verificação de Segurança");
    }

    @FXML
    private void handleRegisterLinkAction() {
        switchScene("RegisterView.fxml", null, "Cadastro de Novo Usuário");
    }

    @FXML
    private void handleForgotPasswordLinkAction() {
        switchScene("ForgotPasswordView.fxml", null, "Recuperação de Senha");
    }
}