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
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        feedbackLabel.setText("Quiz Animado");

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
            feedbackLabel.setText("Usuário, e-mail ou senha inseridos estão incorretos.");
        }
    }

    private void switchScene(String fxmlFile, User user, String newTitle) {
        try {

            Stage currentStage = (Stage) usernameField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoquiz/quiz/ProjetoQuiz/" + fxmlFile));
            Parent root = loader.load();

            if (fxmlFile.equals("QuizView.fxml")) {
                QuizController controller = loader.getController();
                controller.initData(user);
            } else if (fxmlFile.equals("SecurityCheckView.fxml")) {
                SecurityCheckController controller = loader.getController();
                controller.initData(user);
            }

            root.setOpacity(0);

            Scene newScene = new Scene(root, 1280, 720);
            currentStage.setScene(newScene);
            currentStage.setTitle(newTitle);

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openQuizScreen(User user) {
        switchScene("QuizView.fxml", user, "Quiz Animado");
    }

    private void openSecurityCheckScreen(User user) {
        switchScene("SecurityCheckView.fxml", user, "Quiz Animado");
    }

    @FXML
    private void handleRegisterLinkAction() {
        switchScene("RegisterView.fxml", null, "Quiz Animado");
    }

    @FXML
    private void handleForgotPasswordLinkAction() {
        switchScene("ForgotPasswordView.fxml", null, "Quiz Animado");
    }
}