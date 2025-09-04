package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
import br.com.projetoquiz.quiz.ProjetoQuiz.dao.UserDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ForgotPasswordController {

    @FXML private VBox searchPane;
    @FXML private TextField usernameField;
    @FXML private VBox resetPane;
    @FXML private Label securityQuestionLabel; // Vamos reaproveitar este label para o PIN
    @FXML private TextField securityAnswerField;   // E este para a resposta do PIN
    @FXML private PasswordField newPasswordField;
    @FXML private Button resetPasswordButton;
    @FXML private Label statusLabel;
    
    private User foundUser;

    @FXML
    private void handleSearchUserAction() {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            statusLabel.setText("Por favor, digite um nome de usuário.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        this.foundUser = userDAO.findUserByUsername(username);

        // A lógica agora verifica se o usuário existe e se ele TEM um PIN cadastrado
        if (foundUser != null && foundUser.getPin() != null && !foundUser.getPin().isEmpty()) {
            securityQuestionLabel.setText("Por favor, insira seu PIN de 4 dígitos."); // Mudamos o texto
            
            searchPane.setVisible(false);
            searchPane.setManaged(false);
            resetPane.setVisible(true);
            resetPane.setManaged(true);
            statusLabel.setText("");
        } else {
            statusLabel.setText("Usuário não encontrado ou não possui PIN de segurança.");
            this.foundUser = null;
        }
    }

    @FXML
    private void handleResetPasswordAction() {
        String pinAttempt = securityAnswerField.getText(); // O usuário digita o PIN aqui
        String newPassword = newPasswordField.getText();

        if (pinAttempt.isEmpty() || newPassword.isEmpty()) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Por favor, preencha todos os campos.");
            return;
        }
        
        // A lógica agora compara o PIN
        if (foundUser != null && foundUser.getPin().equals(pinAttempt)) {
            UserDAO userDAO = new UserDAO();
            userDAO.updatePassword(foundUser.getUsername(), newPassword);
            
            statusLabel.setTextFill(Color.GREEN);
            statusLabel.setText("Senha redefinida com sucesso! Pode voltar e fazer o login.");

            resetPasswordButton.setDisable(true);
            newPasswordField.setDisable(true);
            securityAnswerField.setDisable(true);
        } else {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("PIN incorreto!");
        }
    }
    
    @FXML
    private void handleBackButtonAction() {
        try {
            Stage currentStage = (Stage) statusLabel.getScene().getWindow();
            currentStage.close();
            Stage stage = new Stage();
            stage.setTitle("Quiz de Desenhos Animados");
            Scene scene = new Scene(App.loadFXML("primary"), 1280, 720);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}